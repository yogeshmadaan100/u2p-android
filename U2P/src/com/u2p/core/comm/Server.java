package com.u2p.core.comm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.u2p.core.db.DbDataSource;
import com.u2p.events.ChangesClientEvents;
import com.u2p.events.ServerEventsGenerator;
import com.u2p.ui.MainActivity;

public class Server extends Thread{
	private ServerSocket ssocket;
	private ServerEventsGenerator eventsGenerator;
	private HashMap<InetAddress,Thread> activeClients;
	private int port;
	private static final String TAG="Server";
	private DbDataSource datasource;
	private boolean isService;
	
	public static final int ACK_END=-1;
	
	public static final int EVENT_CHANGE_CLIENTS=1;
	
	public Server(int port,DbDataSource datasource,MainActivity activity){
		this.port=port;
		this.datasource=datasource;
		activeClients=new HashMap<InetAddress,Thread>();
		this.eventsGenerator=new ServerEventsGenerator();
		eventsGenerator.addListener(activity);
	}
	public void setService(boolean service){
		this.isService=service;
	}
	
	public boolean isService(){
		return this.isService;
	}
	
	public void launchEventToActivity(EventObject event,int type){
		if(type==this.EVENT_CHANGE_CLIENTS){
			ChangesClientEvents change=new ChangesClientEvents(eventsGenerator,this.activeClients);
			eventsGenerator.addEvent(change);
		}
		//eventsGenerator.addEvent(event);
		
	}
	
	public synchronized void addActiveClient(InetAddress address,Thread client){
		if(!activeClients.containsKey(address)){
			activeClients.put(address,client);
			Log.d(TAG, "Add new active client "+address);
		}
	}
	
	public void deleteActiveClient(InetAddress address){
		if(!activeClients.containsKey(address)){
			ServerClient client=(ServerClient)activeClients.get(address);
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "Exception close client "+address);
			}
			activeClients.remove(address);
			Log.d(TAG, "Delete active client "+address);
		}
	}
	
	public void deleteAllActiveClients(){
		List<Thread> clients=new ArrayList<Thread>(activeClients.values());
		ServerClient sClient=null;
		if(clients!=null){
			for(Thread client:clients){
				sClient=(ServerClient)client;
				this.deleteActiveClient(sClient.getAdress());
			}
		}
	}
	
	public HashMap<InetAddress,Thread> getAllActiveClient(){
		return this.activeClients;
	}
	
	public Thread getActiveClient(InetAddress client){
		if(!activeClients.containsKey(client)){
			return activeClients.get(client);
		}
		return null;
	}
	
	public void close(){
		try{
			this.deleteAllActiveClients();
			this.ssocket.close();
		Log.d(TAG,"Close server");
		}catch(IOException e){
			Log.d(TAG,"Close server exception");
		}
	}
	public void run(){
		try{
			ssocket=new ServerSocket(port);
			Log.d(TAG,"Server listening on port "+port);
			while(true){
				Socket socket=ssocket.accept();
				Log.d(TAG,"New client connected: "+socket.getInetAddress());
				ServerClient sClient=new ServerClient(socket,socket.getInetAddress(),datasource,this);
				
				sClient.start();
			}
		}catch(IOException e){
			Log.e(TAG,"IOException");
		}
	}
}
