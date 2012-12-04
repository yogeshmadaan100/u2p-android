package com.u2p.core.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

import com.u2p.messages.Hello;

public class ServerClient extends Thread{
	private Socket socket;
	private OutputStream out;
	private ObjectOutputStream oos;
	private InputStream in;
	private ObjectInputStream ois;
	
	public ServerClient(Socket socket){
		this.socket=socket;
	}
	
	public void run(){
		try{
			out=socket.getOutputStream();
			oos=new ObjectOutputStream(out);
			
			in=socket.getInputStream();
			ois=new ObjectInputStream(in);
			try{
				Object aux=null;
				aux=ois.readObject();
				if(aux instanceof Hello){
					Hello message=(Hello)aux;
					Log.i("ServerClient", "Received hello message: "+message);
				
					Hello messageACK=new Hello("Hello ACK");
					oos.writeObject(messageACK);
				}
				
				ois.close();
				in.close();
				oos.close();
				out.close();
				socket.close();
			}catch(ClassNotFoundException e){
				Log.e("ServerClient","ClassNotFoundException");
			}
		}catch(IOException ew){
			Log.e("ServerClient","IOException");
		}
	}
}
