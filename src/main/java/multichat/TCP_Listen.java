package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCP_Listen implements Runnable{
	
	int puerto;
	Servidor serv_ref;
	
	public TCP_Listen(int port, Servidor serv){
		puerto = port;
		serv_ref = serv;
	}
		
	

	@Override
	public void run() {
		
		ServerSocket ss = null;
		Socket sock = null;	
			
			try {
				ss = new ServerSocket(puerto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ss.setSoTimeout(5000);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sock = ss.accept();
				serv_ref.vent.textarea.append("Conectado a: "+sock.getInetAddress()+"\n");
		        serv_ref.vent.textarea.setCaretPosition(serv_ref.vent.textarea.getDocument().getLength());			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Pasamos la referencia a las variables globales para que sean manipuladas por otras funciones
			if(ss.isBound())
				serv_ref.ss = ss;
			
			if (sock.isConnected())
				serv_ref.sock = sock;			
			serv_ref.Escuchar(sock,ss);
			
	}
	

}
