package com.u2p.core.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

import com.u2p.messages.Hello;

public class Client extends Thread{
	private Socket socket;
	private InetAddress address;
	private int port;
	private OutputStream out;
	private ObjectOutputStream oos;
	private InputStream in;
	private ObjectInputStream ois;
	
	public Client(InetAddress address, int port){
		this.address=address;
		this.port=port;
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
			Hello message=new Hello("Hello Service");
			oos.writeObject(message);
			Log.i("Client","Send message hello to "+socket.getInetAddress());
			//Esperamos ack
			
			Object aux=ois.readObject();
			
			if(aux instanceof Hello){
				Hello messageACK=(Hello)aux;
				Log.i("Client","Received messageACK from "+socket.getInetAddress());
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

}
