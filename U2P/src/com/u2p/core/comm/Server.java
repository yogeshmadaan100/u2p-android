package com.u2p.core.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class Server extends Thread{
	private ServerSocket ssocket;
	private int port;
	
	public Server(int port){
		this.port=port;
	}
	
	public void close(){
		try{
			this.ssocket.close();
			Log.d("Server","Close server");
		}catch(IOException e){
			Log.d("Server","Close server exception");
		}
	}
	public void run(){
		try{
			ssocket=new ServerSocket(port);
			Log.d("Server","Server listening on port "+port);
			while(true){
				Socket socket=ssocket.accept();
				Log.d("Server","New client connected: "+socket.getInetAddress());
				ServerClient sClient=new ServerClient(socket);
				sClient.start();
			}
		}catch(IOException e){
			Log.e("Server","IOException");
		}
	}
}
