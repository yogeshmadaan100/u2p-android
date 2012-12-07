package com.u2p.core.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.util.Log;

import com.u2p.core.db.DbDataSource;
import com.u2p.core.db.DbFile;
import com.u2p.events.NewClientEvent;
import com.u2p.messages.ACK;
import com.u2p.messages.Authentication;
import com.u2p.messages.FileAnswer;
import com.u2p.messages.FileRequest;
import com.u2p.messages.ListAnswer;
import com.u2p.messages.ListRequest;
import com.u2p.messages.VoteFile;
import com.u2p.ui.component.ItemFile;

public class ServerClient extends Thread{
	private InetAddress address;
	private Socket socket;
	private OutputStream out;
	private ObjectOutputStream oos;
	private InputStream in;
	private ObjectInputStream ois;
	private boolean end;
	private static final String TAG="ServerClient";
	private String userName;
	private DbDataSource datasource;
	private Server parent;
	
	public ServerClient(Socket socket,InetAddress address, DbDataSource datasource, Server parent){
		this.socket=socket;
		this.address=address;
		this.datasource=datasource;
		this.parent=parent;
		end=false;
		this.userName=datasource.getUser(1).getUser();
	}
	
	public InetAddress getAdress(){
		return this.address;
	}
	
	public void close() throws IOException{
		//Deberiamos enviar un ACK para cerrar la conexión con el otro extremo
		if(!end){
			ACK ack=new ACK(Server.ACK_END);
			oos.writeObject(ack);
			Log.d(TAG,"Sending ACK END to "+address);
		}
		Log.d(TAG, "Close comunication");
		ois.close();
		in.close();
		oos.close();
		out.close();
		socket.close();
	}
	
	public List<String> compareGroups(HashMap<String,String> socketGroups,HashMap<String,String> userGroups){
	    List<String> commons=new ArrayList<String>();
		for (Entry<String, String> entry : socketGroups.entrySet()) {
	        String key = entry.getKey();
	        String hash=entry.getValue();
	        if(userGroups.containsKey(key) && hash.equals(userGroups.get(key))){
	        	commons.add(key);
	        }
	    }
		return commons;
	}
	
	public void run(){
		try{
			out=socket.getOutputStream();
			oos=new ObjectOutputStream(out);
			
			in=socket.getInputStream();
			ois=new ObjectInputStream(in);
			try{
				while(!end){
					Object aux=null;
					aux=ois.readObject();
					if(aux instanceof ACK){
						ACK ack=(ACK)aux;
						Log.d(TAG,"Received ACK, type:"+ack.getACKType()+" from "+address);
						
						switch(ack.getACKType()){
							case Server.ACK_END:
								end=true;
								Log.d(TAG,"End comunication with "+address);
								break;
							default:
								break;
						}
						continue;
					}
					if(aux instanceof Authentication){
						Log.d(TAG,"Received Authentication message from "+address);
						Authentication aut=(Authentication)aux;
						HashMap<String,String> socketGroups=aut.getGroups();
						List<String> userGroups=datasource.getAllGroups();
						HashMap<String,String> serverGroups=new HashMap<String,String>();
						
						String hashtmp;
						for(String group:userGroups){
							hashtmp=datasource.getHashGroup(group);
							if(hashtmp!=null){
								serverGroups.put(group, hashtmp);
							}
						}
						//Comparar los grupos que se reciben para ver si pertenecemos a alguno de esos grupos
						List<String> commons=this.compareGroups(socketGroups,serverGroups);
						for(String str:commons){
							Log.d(TAG,str);
						}
						if(commons.size()>0){
							//Si tenemos algún grupo en común lo guardamos como cliente
							parent.addActiveClient(address, this);
							//Enviamos un message Authentication
							Authentication at=new Authentication(userName);
							at.setCommons((ArrayList)commons);
							oos.writeObject(at);
							Log.d(TAG, "Send Authentication message with commons groups to "+address);
							NewClientEvent event=new NewClientEvent(parent.getGenerator(),address,at.getUser(),commons);
							parent.launchEventToActivity(event, Server.EVENT_NEW_CLIENT);
							
						}else{
							//Si no enviamos ACK para acabar la comunicación
							ACK ack=new ACK(Server.ACK_END);
							oos.writeObject(ack);
							Log.d(TAG, "No commons group, cancel communication to "+address);
						}
	
						//Lanzar evento al activity principal de nuevo cliente
						//Si además de servidor también es el que oferta el servicio, cada vez que reciba un mensaje 
						//de estos tendrá que avisar a todos los clientes (menos al último) que le han contactado con los datos 
						//del último cliente que se haya conectado
						if(parent.isService()){
							
						}
						continue;
					}
					if(aux instanceof FileRequest){
						Log.d(TAG,"Received FileRequest message from "+address);
						//Petición para el envio de un archivo
						FileRequest fileR=(FileRequest)aux;
						//Comprobamos que el archivo existe
						if(datasource.existsFile(fileR.getGroup(),fileR.getName())){
							//Si da tiempo lo ciframos
							FileAnswer file=new FileAnswer(fileR.getName(),fileR.getGroup());
							String uri=datasource.getFile(fileR.getGroup(),fileR.getName()).getUri();
							file.read(uri);
							oos.writeObject(file);
							Log.d(TAG,"Send FileAnswer message to "+address);
							continue;
						}else{
							ACK ack=new ACK(Server.ACK_ERROR_FILE);
							oos.writeObject(ack);
							Log.e(TAG, "File "+fileR.getName()+" not found in group "+fileR.getGroup());
						}
						continue;
					}
					if(aux instanceof ListRequest){
						Log.d(TAG,"Received ListRequest message from "+address);
						ListRequest list=(ListRequest)aux;
						String group=list.getGroup();
						
						if(datasource.groupExist(group)){
							List<DbFile> files=datasource.getFilesGroup(group);
							List<ItemFile> items=new ArrayList<ItemFile>();
							
							for(DbFile file:files){
								items.add(new ItemFile(file,this.userName,datasource.getFileType(file.getType())));
							}
							ListAnswer listA=new ListAnswer((ArrayList<ItemFile>)items,group);
							oos.writeObject(listA);
							Log.d(TAG, "Send ListAnswer message to "+address);
						}else{
							ACK ack=new ACK(Server.LIST_REQUEST_ERROR);
							oos.writeObject(ack);
							Log.d(TAG,"Group not found");
						}
						continue;
					}
					if(aux instanceof VoteFile){
						Log.d(TAG,"Received VoteFile message from "+address);
						VoteFile vote=(VoteFile)aux;
						datasource.voteFile(vote.getGroup(),vote.getFile(),vote.getVote());
						continue;
					}
					
				}
				this.close();
			}catch(ClassNotFoundException e){
				Log.e("ServerClient","ClassNotFoundException");
			}
		}catch(IOException ew){
			Log.e("ServerClient","IOException");
		}
	}
}
