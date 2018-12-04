package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class LecturaMulticast implements Runnable{
	MulticastSocket sock;
	DatagramPacket pack;
	Ventana ven;
	
	public LecturaMulticast(MulticastSocket s, DatagramPacket p, Ventana v){
		sock = s;
		pack = p;
		ven = v;
	}
	
	
	@Override
	public void run() {
		
		String cadena=null;
		while(!sock.isClosed()){
		
		try {
			sock.receive(pack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			break;
		}
		 ven.textarea.append(pack.getAddress().getHostAddress()+"\n");
		  try {
			  cadena = new String(pack.getData(),0,pack.getLength(),"UTF-8");
			  ven.textarea.append(cadena+"\n");
			  ven.textarea.setCaretPosition(ven.textarea.getDocument().getLength());			  
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  ven.textarea.setCaretPosition(ven.textarea.getDocument().getLength());	
		 
		}
		
	}

}
