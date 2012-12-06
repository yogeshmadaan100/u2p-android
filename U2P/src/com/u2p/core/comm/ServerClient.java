package com.u2p.core.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

import com.u2p.core.db.DbDataSource;
import com.u2p.messages.ACK;
import com.u2p.messages.Authentication;
import com.u2p.messages.FileRequest;
import com.u2p.messages.ListRequest;
import com.u2p.messages.VoteFile;

public class ServerClient extends Thread{
	private InetAddress address;
	private Socket socket;
	private OutputStream out;
	private ObjectOutputStream oos;
	private InputStream in;
	private ObjectInputStream ois;
	private boolean end;
	private static final String TAG="ServerClient";
	private DbDataSource datasource;
	private Server parent;
	
	public ServerClient(Socket socket,InetAddress address, DbDataSource datasource, Server parent){
		this.socket=socket;
		this.address=address;
		this.datasource=datasource;
		this.parent=parent;
		end=false;
	}
	
	public InetAddress getAdress(){
		return this.address;
	}
	
	public void close() throws IOException{
		//Deberiamos enviar un ACK para cerrar la conexión con el otro extremo
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
						//Comparar los grupos que se reciben para ver si pertenecemos a alguno de esos grupos
						//Si pertenecemos a alguno lo guardamos como cliente
							//parent.addActiveClient(address,this);
							//Enviamos un message Authentication
							//Lanzar evento al activity principal de nuevo cliente
						
						//Si no enviamos ACK para acabar la comunicación
	
						Log.d(TAG,"Send Authentication message to "+address);
						
						//Si además de servidor también es el que oferta el servicio, cada vez que reciba un mensaje 
						//de estos tendrá que avisar a todos los clientes (menos al último) que le han contactado con los datos 
						//del último cliente que se haya conectado
						//if(parent.isService())
						continue;
					}
					if(aux instanceof FileRequest){
						Log.d(TAG,"Received FileRequest message from "+address);
						//Petición para el envio de un archivo
						FileRequest fileR=(FileRequest)aux;
						//Comprobamos que el archivo existe
						//Si da tiempo lo ciframos
						//Buscamos archivo
						//Lo enviamos con un FileAnswer
						Log.d(TAG,"Send FileAnswer message to "+address);
						continue;
					}
					if(aux instanceof ListRequest){
						Log.d(TAG,"Received ListRequest message from "+address);
						
						Log.d(TAG,"Send ListAnswer message to "+address);
						continue;
					}
					if(aux instanceof VoteFile){
						Log.d(TAG,"Received VoteFile message from "+address);
						//Comprobar grupo y fichero
						//votar
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
