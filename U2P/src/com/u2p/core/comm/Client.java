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
import com.u2p.messages.ACK;
import com.u2p.messages.Authentication;

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
			Log.i(TAG,"Send Authentication message to "+socket.getInetAddress());
			//Esperamos ack
			
			while(!end){
				Object aux=ois.readObject();
				
				if(aux instanceof ACK){
					ACK ack=(ACK)aux;
					Log.d(TAG,"Received ACK type: "+ack.getACKType());
				}
				if(aux instanceof Authentication){
					
				}
			}
			
			ois.close();
			in.close();
			oos.close();
			out.close();
			socket.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleActivityEventsListener(EventObject e) {
		// TODO Auto-generated method stub
		ActivityEvents event=(ActivityEvents)e;
		
		if(event.getAddress().toString().equals(this.address.toString())){
			//Manejamos el evento
			
		}
	}

}
