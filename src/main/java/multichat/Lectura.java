package main;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Lectura implements Runnable{
	public Socket sock;
	public Envio objenvio;
	public Ventana vent;
	public ObjectInputStream objis;
	public ArrayList<Socket> sockets_conectados;
	public ServerSocket ss;
	
	public Lectura(ServerSocket servsock, Socket s, Envio o, Ventana v, ObjectInputStream ob, ArrayList<Socket> sock_con){
		sock = s;
		objenvio = o;
		vent = v;
		objis = ob;
		sockets_conectados = sock_con;
		ss = servsock;
	}
	
	

	@Override
	public void run() {	
	
		while(objenvio.mensaje.compareTo("Salir") != 0 && !sock.isClosed() && !sock.isOutputShutdown() ){			
			try {			
				if(sock.getInputStream() != null)
				objis = new ObjectInputStream(sock.getInputStream());
				else
					break;
				objenvio =(Envio) objis.readObject();			
				
				vent.Appendtext("IP: "+sock.getInetAddress().getHostAddress()+" PUERTO: "+sock.getLocalPort()+"\n"+objenvio.mensaje+"\n",Color.red);			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("se ha finalizado la conexión");
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i=0; i<sockets_conectados.size();i++){
			if (sockets_conectados.get(i) == sock){
				sockets_conectados.remove(i);
				vent.model.remove(i);
				break;
			}
		}
		try {
			sock.close();
			if(ss!=null)  // si es un servidor se ciera si es un cliente solo utiliza el socket
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		vent.textarea.append("Conexion finalizada...\n");
        vent.textarea.setCaretPosition(vent.textarea.getDocument().getLength());	
		
	}

}
