package com.u2p.core.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.EventObject;
import java.util.List;

import android.util.Log;

import com.u2p.core.db.DbDataSource;
import com.u2p.core.db.DbUser;
import com.u2p.events.ActivityEvents;
import com.u2p.events.ActivityEventsListener;
import com.u2p.events.FileEvent;
import com.u2p.events.ListEvent;
import com.u2p.events.VoteEvent;
import com.u2p.messages.ACK;
import com.u2p.messages.Authentication;
import com.u2p.messages.FileAnswer;
import com.u2p.messages.ListAnswer;
import com.u2p.messages.VoteFile;

public class Client extends Thread implements ActivityEventsListener{
	private Socket socket;
	private InetAddress address;
	private int port;
	private OutputStream out;
	private ObjectOutputStream oos;
	private InputStream in;
	private ObjectInputStream ois;
	private DbDataSource datasource;
	private static final String TAG="Client";
	private boolean end;
	
	public Client(InetAddress address, int port, DbDataSource datasource){
		this.address=address;
		this.port=port;
		this.datasource=datasource;
		this.end=false;
	}
	
	public void close() throws IOException{
		if(!end){
			ACK ack=new ACK(Server.ACK_END);
			oos.writeObject(ack);
		}
		Log.d(TAG, "Close comunication");
		ois.close();
		in.close();
		oos.close();
		out.close();
		socket.close();	
	}
	
	public void run(){
		socket=new Socket();
		try {
			socket.connect(new InetSocketAddress(address,port),5000);
			//Para escribir
			out=socket.getOutputStream();
			oos=new ObjectOutputStream(out);
			//Para recibir
			in=socket.getInputStream();
			ois=new ObjectInputStream(in);
			
			//Mandamos primer mensaje
			DbUser user=datasource.getUser(1);
			Authentication autms=new Authentication(user.getUser());
			List<String> userGroups=datasource.getAllGroups();
			
			String hashtmp;
			for(String group:userGroups){
				hashtmp=datasource.getHashGroup(group);
				if(hashtmp!=null){
					autms.addGroup(group, hashtmp);
				}
			}
			
			oos.writeObject(autms);
			Log.i(TAG,"Send Authentication message to "+address);
			//Esperamos ack
			
			while(!end){
				Object aux=ois.readObject();
				
				if(aux instanceof ACK){
					ACK ack=(ACK)aux;
					Log.d(TAG,"Received ACK type: "+ack.getACKType()+" from "+address);
						
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
					continue;
				}
				if(aux instanceof FileAnswer){
					Log.d(TAG,"Received FileAnswer message from "+address);
					//Recibimos un archivo
					FileAnswer fileA=(FileAnswer)aux;
					//Si da tiempo lo desciframos
					//Escribimos archivo
					//Enviamos ACK
					Log.d(TAG,"Send FileAnswer ACK message to "+address);
					continue;
				}
				if(aux instanceof ListAnswer){
					Log.d(TAG,"Received ListAnswer message from "+address);
					
					Log.d(TAG,"Send ListAnswer ACK message to "+address);
					continue;
				}
			}
			
			this.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"IOException in thread "+this.address);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"ClassNotFoundException in thread"+this.address);
		}
	}
	
	private void requestFile(String group,String file){
		Log.d(TAG,"Sending FileRequest message to "+this.address);
	}
	
	private void requestList(String group){
		Log.d(TAG,"Sending ListRequest message to "+this.address);
	}
	
	private void voteFile(String group,String file, int vote) throws IOException{
		VoteFile votem=new VoteFile(group,file,vote);
		oos.writeObject(votem);
		Log.d(TAG,"Sending VoteFile message to "+this.address);
	}
	
	public void handleActivityEventsListener(EventObject e) {
		// TODO Auto-generated method stub
		ActivityEvents event=(ActivityEvents)e;
		
		if(event.getAddress().toString().equals(this.address.toString())){
			//El evento es para este cliente, por tanto manejamos el evento
			try{
				if(event instanceof FileEvent){
					FileEvent file=(FileEvent)event;
					this.requestFile(file.getGroup(), file.getFile());
				}
				if(event instanceof ListEvent){
					ListEvent list=(ListEvent)event;
					this.requestList(list.getGroup());
				}
				if(event instanceof VoteEvent){
					VoteEvent vote=(VoteEvent)event;
					this.voteFile(vote.getGroup(),vote.getFile(),vote.getVote());
				}
			}catch(IOException e1){
				Log.e(TAG,"IOException handle activity events");
			}
		}
	}

}
