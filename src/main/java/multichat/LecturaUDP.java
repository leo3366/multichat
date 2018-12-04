package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class LecturaUDP implements Runnable{
	DatagramSocket sock;
	DatagramPacket pack;
	Ventana v;
	ArrayList<DatagramSocket> sockets_udp;
	
	public LecturaUDP(DatagramSocket ds, DatagramPacket ps, Ventana vent, ArrayList<DatagramSocket> sock_udp) {
		sock = ds;
		pack = ps;
		v= vent;
		sockets_udp = sock_udp;
		
	}
	
	
	public void run(){
		String cadena=null;
		while(!sock.isClosed()){
		
		try {
			sock.receive(pack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			break;
		}
		 v.textarea.append(pack.getAddress().getHostAddress()+"\n");
		  try {
			  cadena = new String(pack.getData(),0,pack.getLength(),"UTF-8");
			  v.textarea.append(cadena+"\n");
			  v.textarea.setCaretPosition(v.textarea.getDocument().getLength());			  
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  v.textarea.setCaretPosition(v.textarea.getDocument().getLength());		
	
		}		
		for(int i =0; i < sockets_udp.size(); i++)
			if(sockets_udp.get(i) == sock){
				sockets_udp.remove(i);
				v.model2.remove(i);
				
			}
		
		
		
	}

}
