package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Servidor implements ActionListener, KeyListener, ListSelectionListener{

	
	public ObjectOutputStream objos;
	public Envio objenvio;
	public Ventana vent;
	public int puerto;
	public Lectura lectura;
	public Thread t1;
	public ObjectInputStream objis;
	public InetAddress group; // Direccion de Multicast
	
	public MulticastSocket mcsock_ref;
	public ServerSocket ss;
	public Socket sock;
	
	public ArrayList<Socket> sockets_conectados;
	public ArrayList<DatagramSocket> sockets_udp;
	public ArrayList<MulticastSocket> sockets_multicast;
	public ArrayList<InetAddress> grupos_multicast;

	public InetAddress ip_servidor;
	public DatagramSocket sockudp;		

	
	public Servidor(){
		vent = new Ventana("Servidor");
		vent.setVisible(true);
		vent.escuchar.addActionListener(this);
		vent.conectar.addActionListener(this);
		vent.desconectar.addActionListener(this);
		vent.boton_enviar.addActionListener(this);
		vent.textfield.addKeyListener(this);
		vent.Broadcast.addActionListener(this);
		//vent.Enviar_MC.addActionListener(this);
		vent.registrar.addActionListener(this);
		vent.list.addListSelectionListener(this);
		vent.list2.addListSelectionListener(this);
		vent.list3.addListSelectionListener(this);
		
	}
	public void Enviarudp(){	
	
				
		if (!vent.JTpuerto.getText().isEmpty())
	     	puerto = Integer.parseInt(vent.JTpuerto.getText());
			else
			puerto=3366;
		
		if (!vent.JTdir_ip.getText().isEmpty())
			try {
				ip_servidor = InetAddress.getByName(vent.JTdir_ip.getText());
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else
			try {
				ip_servidor = InetAddress.getByName("255.255.255.255");
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	
			try {
				sockudp = new DatagramSocket();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sockudp.setBroadcast(true);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
			String cadena;			
						
			if(!vent.textfield.getText().isEmpty()){			
				cadena = new String ("-broadcast:\n"+vent.textfield.getText());
				vent.textfield.setText("");
			}
			
			else
				cadena = "-broadcast:\n"+"mensaje UDP";
			
			byte[] sendData = cadena.getBytes();
			DatagramPacket udpenvio = new DatagramPacket(sendData, sendData.length, ip_servidor, puerto);
			try {
				sockudp.send(udpenvio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			sockudp.close();		
}
	public void EscucharUDP(){
		if (!vent.JTpuerto.getText().isEmpty())
	     	puerto = Integer.parseInt(vent.JTpuerto.getText());
			else
			puerto=3366; 
		
			DatagramSocket ssudp;
			try {
				ssudp = new DatagramSocket(puerto);
				ssudp.setBroadcast(true);
				
				if (sockets_udp == null)
					sockets_udp = new ArrayList<DatagramSocket>();
				
				sockets_udp.add(ssudp);
				vent.model2.addElement("puerto udp: "+ ssudp.getLocalPort());
				
				
				byte[] recvBuf = new byte[15000];
				DatagramPacket udppack = new DatagramPacket(recvBuf, recvBuf.length);					
			    Thread Lectudp = new Thread(new LecturaUDP(ssudp,udppack,vent,sockets_udp));
			    Lectudp.start();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
	
	}
	public void Enviar_Multicast(){  // clase D 224.0.0.0 239.255.255.255		
		if(!vent.list3.isSelectionEmpty()){
			String cadena;		
			group = grupos_multicast.get(vent.list3.getSelectedIndex());
			mcsock_ref = sockets_multicast.get(vent.list3.getSelectedIndex());
		
			if(!vent.textfield.getText().isEmpty()){			
				cadena = new String ("-multicast "+group+":\n"+vent.textfield.getText());
				vent.textfield.setText("");
			}
		
			else
				cadena = "-multicast "+group+":\n"+"mensaje multicast";
		
			byte[] sendData = cadena.getBytes();
			DatagramPacket udpenvio = new DatagramPacket(sendData, sendData.length, group, puerto);
			try {
				mcsock_ref.send(udpenvio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			
			}			
		}
	}
	public void Escuchar_multicast(){		
		
		if (!vent.JTpuerto.getText().isEmpty())
	     	puerto = Integer.parseInt(vent.JTpuerto.getText());
			else
			puerto=3366; 
		
			try {
				if(!vent.JTmulticast.getText().isEmpty())
					group = InetAddress.getByName(vent.JTmulticast.getText());				
				else			
					group = InetAddress.getByName("228.5.6.7");
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		
			MulticastSocket mcsock;
			try {
				mcsock = new MulticastSocket(puerto);
				mcsock.joinGroup(group);
				mcsock_ref = mcsock;
				
				if (sockets_multicast == null){
					sockets_multicast = new ArrayList<MulticastSocket>();
					grupos_multicast = new ArrayList<InetAddress>();
				}
					sockets_multicast.add(mcsock);
					grupos_multicast.add(group);
					vent.model3.addElement("Multicast: "+group+"/ "+mcsock.getLocalPort());
				
				
				byte[] recvBuf = new byte[15000];
				DatagramPacket udppack = new DatagramPacket(recvBuf, recvBuf.length);					
			    Thread Lectmulticast = new Thread(new LecturaMulticast(mcsock,udppack,vent));
			    Lectmulticast.start();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		
		
	}
	public void Conectar(){		
	Thread t1;
	InetAddress ip_servidor = null;
	int puerto;
		
		if (!vent.JTpuerto.getText().isEmpty())
	     	puerto = Integer.parseInt(vent.JTpuerto.getText());
			else
			puerto=3366;
		
		if (!vent.JTdir_ip.getText().isEmpty())
			try {
				ip_servidor = InetAddress.getByName(vent.JTdir_ip.getText());
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else
			try {
				ip_servidor = InetAddress.getByName("localhost");
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			vent.textarea.append("intentando conectar al puerto: "+puerto+" \n");
	        vent.textarea.setCaretPosition(vent.textarea.getDocument().getLength());
			//vent.update(vent.getGraphics());
try {
			
			// valida que no este en uso el puerto en esa dir ip
			int dir_disponible =1;
			
			if (sockets_conectados != null)
				for(int i =0; i< sockets_conectados.size(); i++)
					if(sockets_conectados.get(i).getInetAddress() == ip_servidor && sockets_conectados.get(i).getPort() == puerto){
						dir_disponible = 0;
					}
	
			if (sockets_conectados == null || dir_disponible == 1){
			sock = new Socket(ip_servidor,puerto);		
			
			if (sockets_conectados == null)
				sockets_conectados = new ArrayList<Socket>();
			
				sockets_conectados.add(sock);
						
			objis = new ObjectInputStream(sock.getInputStream());	
			
			
				objenvio =(Envio) objis.readObject();				
				vent.textarea.append(objenvio.mensaje+" a: "+sock.getInetAddress().getHostAddress()+"\n");
		        vent.textarea.setCaretPosition(vent.textarea.getDocument().getLength());

				
				lectura = new Lectura(ss,sock,objenvio,vent,objis, sockets_conectados);					
				vent.model.addElement(sock.getInetAddress().getHostAddress()+"/ "+sock.getPort()+" local:"+sock.getLocalPort());
				t1 = new Thread(lectura);
				t1.start();			
				
			}
			else{
				vent.textarea.append("La dirección ya está en uso! \n");
			}
			 
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
			vent.textarea.append("No se ha podido establecer la conexion...\n");
	        vent.textarea.setCaretPosition(vent.textarea.getDocument().getLength());

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
	}
	// Funcion escuchar: espera conexión en puerto especificado (3366 por defecto) durante 5s ----
	public void Esperar(){
		int puerto;
		Thread hilo;
			
		if(!vent.JTpuerto.getText().isEmpty())
			puerto = Integer.parseInt(vent.JTpuerto.getText());
		else
			puerto = 3366;	
		
		hilo = new Thread(new TCP_Listen(puerto,this));
		
		vent.textarea.append("Esperando conexion en el puerto:"+puerto+"\n");
        vent.textarea.setCaretPosition(vent.textarea.getDocument().getLength());		
		
		hilo.start();// Espera de conexion		
	}
	public void Escuchar(Socket sock, ServerSocket ss){
				
		
		if (sockets_conectados == null)
			sockets_conectados = new ArrayList<Socket>();
		
		sockets_conectados.add(sock);
		vent.model.addElement(sock.getInetAddress().getHostAddress()+"/ "+sock.getPort()+" local:"+sock.getLocalPort());		
		
		
		
				try {
					objos = new ObjectOutputStream(sock.getOutputStream());				
					objenvio = new Envio();	
					objos.writeObject(objenvio);
				

					lectura = new Lectura(ss,sock,objenvio,vent,objis,sockets_conectados); // hilo de lectura TCP
				
					if (sock.isConnected()){
						t1 = new Thread(lectura);
						t1.start();
					}
				
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	//Función enviar para TCP: envía un objeto de tipo envio. 
	public void Enviar(){		
		if(!vent.list.isSelectionEmpty())
		try {														// añadir excepcion si no esta conectadoa la direccion y mejorar las funciones para cerrar el socket y los stream
			sock = sockets_conectados.get(vent.list.getSelectedIndex());
			objos = new ObjectOutputStream(sock.getOutputStream());
			objenvio = new Envio();
			objenvio.mensaje = vent.textfield.getText();
			vent.Appendtext("Yo: "+vent.textfield.getText()+"\n", Color.blue);
			objos.writeObject(objenvio);
			vent.textfield.setText("");		
		
			if(objenvio.mensaje.compareTo("Salir")== 0){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i=0; i< sockets_conectados.size(); i++)
					if(sock == sockets_conectados.get(i)){
						sockets_conectados.remove(i);
						vent.model.remove(i);
						break;
					}				
				sock.close();
				ss.close();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//Funcion desconectar
	public void Desconectar(){
		
		if(!vent.list.isSelectionEmpty()){
				sock = sockets_conectados.get(vent.list.getSelectedIndex());
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			if(!vent.list2.isSelectionEmpty()){					
					sockets_udp.get(vent.list2.getSelectedIndex()).close();
					sockets_udp.remove(vent.list2.getSelectedIndex());
					vent.model2.remove(vent.list2.getSelectedIndex());
			}
			
			if(!vent.list3.isSelectionEmpty()){
				try {
					sockets_multicast.get(vent.list3.getSelectedIndex()).leaveGroup(grupos_multicast.get(vent.list3.getSelectedIndex()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sockets_multicast.get(vent.list3.getSelectedIndex()).close();
				sockets_multicast.remove(vent.list3.getSelectedIndex());
				grupos_multicast.remove(vent.list3.getSelectedIndex());
				vent.model3.remove(vent.list3.getSelectedIndex());
			}
			
	}
	
//-------------------------------------- MAIN -----------------------------
	public static void main(String[] args) {	
		
	    new Servidor();  //Inicia la ventana del servidor 	    

	}	
//-----------------------------------------------------
	
//										EVENTOS
//Eventos del ratón
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vent.Broadcast)
			EscucharUDP();
		if (e.getSource() == vent.registrar)
			Escuchar_multicast();
		//if (e.getSource() == vent.Enviar_MC)
			//Enviar_Multicast();
		if (e.getSource() == vent.escuchar)
			Esperar();
		if (e.getSource() == vent.conectar)
			Conectar();		
		if(e.getSource()== vent.desconectar)
			Desconectar();			
		if(e.getSource() == vent.boton_enviar)
			if(!vent.textfield.getText().isEmpty()){	// Se llama enviar si hay algo escrito.	
				if(vent.cb_udp.getState() == false && vent.cb_udp.getState() == false)
					Enviar();		
				if(vent.cb_multicast.getState() == false && vent.cb_udp.getState() == true)
					Enviarudp();
				if(vent.cb_multicast.getState() == true && vent.cb_udp.getState() == false)
					if(!vent.list3.isSelectionEmpty())
					Enviar_Multicast();
			}
				
	}
//Eventos del teclado
	@Override
	public void keyPressed(KeyEvent e) {	
		if(e.getKeyCode() == 10){ //Si se presiona Enter se llama a la funcion Enviar (envío para conexión TCP).
			if(!vent.textfield.getText().isEmpty()){	// Se llama enviar si hay algo escrito.
				if(vent.cb_udp.getState() == false && vent.cb_udp.getState() == false)
					Enviar();		
				if(vent.cb_multicast.getState() == false && vent.cb_udp.getState() == true)
					Enviarudp();
				if(vent.cb_multicast.getState() == true && vent.cb_udp.getState() == false)
					Enviar_Multicast();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent evento) {			
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == vent.list){
			vent.list2.clearSelection();
			vent.list3.clearSelection();
			vent.list.setSelectedIndex(vent.list.getSelectedIndex());
		}
		if(e.getSource() == vent.list2){
			vent.list.clearSelection();
			vent.list3.clearSelection();
			vent.list2.setSelectedIndex(vent.list2.getSelectedIndex());
		}
		if(e.getSource() == vent.list3){
			vent.list.clearSelection();
			vent.list2.clearSelection();
			vent.list3.setSelectedIndex(vent.list3.getSelectedIndex());
		}
		
	}	
}
