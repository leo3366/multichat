package main;
import java.awt.Checkbox;
import java.awt.Color;
import java.net.Inet4Address;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class Ventana extends JFrame{

	private static final long serialVersionUID = 1L;
	
	String nombre;
	
	JTextArea textarea;
	JTextPane textpane;	
	
	JTextField textfield;
	JButton boton_enviar;
	
	JButton escuchar;
	JButton conectar;
	JLabel JLpuerto;
	JLabel JLdir_ip;
	JLabel JLmulticast;
	JTextField JTpuerto;
	JTextField JTdir_ip;
	JTextField JTmulticast;	
	JScrollPane scroll;
	JScrollPane scrollpane;
	JScrollPane panel_lista;
	JScrollPane panel_lista2;
	JScrollPane panel_lista3;
	
	JButton desconectar;
	JButton Broadcast;
	JButton registrar;	
	
	JList list;
	JList list2;
	JList list3;
	DefaultListModel model;
	DefaultListModel model2;
	DefaultListModel model3;	
	
	Checkbox cb_udp;
	Checkbox cb_multicast;
	
	Ventana(String nombre){		
		this.nombre = nombre;
		this.setTitle(this.nombre);
		this.setLayout(null);
		this.setBounds(100, 0, 1000, 600);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.darkGray);
		
		//Componentes:	
		textarea = new JTextArea();
		textarea.setBounds(310, 10, 300, 300);		
		textarea.setEditable(false);
		getContentPane().add(textarea);
		textarea.setAutoscrolls(true);
		textarea.setBackground(Color.black);
		textarea.setForeground(Color.white);
	
		
		textpane = new JTextPane();
		textpane.setBounds(textarea.getX()+textarea.getWidth()+10, textarea.getY(), textarea.getWidth(), textarea.getHeight());
		textpane.setEditable(false);
		getContentPane().add(textpane);		
		
		textfield = new JTextField();
		textfield.setBounds(textarea.getX()+(textarea.getX()*1/2), 320, 300, 20);
		getContentPane().add(textfield);
		
		boton_enviar = new JButton("Enviar");
		boton_enviar.setBounds(textfield.getX()+textfield.getWidth()+10, textfield.getY(), 100, 20);
		getContentPane().add(boton_enviar);
		
		escuchar = new JButton("TCP");
		conectar = new JButton("Conectar");
		JLpuerto = new JLabel("Puerto de escucha");
		JLdir_ip = new JLabel("IP del Servidor");
		JLmulticast = new JLabel("Direccion multicast");
		JTpuerto = new JTextField();
		JTdir_ip = new JTextField();
		JTmulticast = new JTextField();
		desconectar = new JButton("Desconectar");
		registrar = new JButton("Registrar");
		
		JLpuerto.setBounds(300, textfield.getY()+textfield.getHeight()+140, 200, 20);
		JLpuerto.setBackground(Color.LIGHT_GRAY);
		JLpuerto.setOpaque(true);	
		
		JLdir_ip.setBounds(300, JLpuerto.getY()+JLpuerto.getHeight()+10, 200, 20);
		JLdir_ip.setBackground(Color.LIGHT_GRAY);
		JLdir_ip.setOpaque(true);
		
		JLmulticast.setBounds(300, JLpuerto.getY()-JLpuerto.getHeight()-10, 200, 20);
		JLmulticast.setBackground(Color.LIGHT_GRAY);
		JLmulticast.setOpaque(true);
		
		
		JTpuerto.setBounds(JLpuerto.getWidth()+JLdir_ip.getX()+10, JLpuerto.getY(), 200, 20);
		JTdir_ip.setBounds(JLdir_ip.getWidth()+JLdir_ip.getX()+10, JLdir_ip.getY(), 200, 20);
		JTmulticast.setBounds(JLmulticast.getWidth()+JLmulticast.getX()+10, JLmulticast.getY(), 200, 20);
		
		escuchar.setBounds(JTpuerto.getX()+JLpuerto.getWidth()+10, JLpuerto.getY(), 100, 20);
		conectar.setBounds(JTdir_ip.getX()+JLdir_ip.getWidth()+10, JLdir_ip.getY(), 100, 20);
		desconectar.setBounds(conectar.getX()+conectar.getWidth()+10, JLdir_ip.getY(), 100, 20);
		registrar.setBounds(JTmulticast.getX()+JTmulticast.getWidth()+10, JTmulticast.getY(),100,20);
				
		Broadcast = new JButton("UDP");
		Broadcast.setBounds(desconectar.getX(),desconectar.getY()-desconectar.getHeight()-10, desconectar.getWidth(), desconectar.getHeight());
		
		scroll = new JScrollPane(textarea);	
		scroll.setBounds(textarea.getX(), textarea.getY(), textarea.getWidth(), textarea.getHeight());
		getContentPane().add(scroll);
		scroll.setEnabled(true);
		
		scrollpane = new JScrollPane(textpane);
		scrollpane.setBounds(textpane.getBounds());
		getContentPane().add(scrollpane);
		scrollpane.setEnabled(true);
		//scrollpane.setAutoscrolls(true);
		
		
		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);			
		
		
		model = new DefaultListModel();
		list = new JList(model);
		panel_lista = new JScrollPane(list);
		panel_lista.setBounds(10, 10, 260, 150);
		panel_lista.setVisible(true);
		getContentPane().add(panel_lista);
		
		model2 = new DefaultListModel();
		list2 = new JList(model2);
		panel_lista2 = new JScrollPane(list2);
		panel_lista2.setBounds(panel_lista.getX(),panel_lista.getY()+panel_lista.getHeight()+10, 260, 150);
		panel_lista2.setVisible(true);
		getContentPane().add(panel_lista2);
		
		model3 = new DefaultListModel();
		list3 = new JList(model3);		
		panel_lista3 = new JScrollPane(list3);
		panel_lista3.setBounds(panel_lista2.getX(),panel_lista2.getY()+panel_lista2.getHeight()+10, 260, 150);
		panel_lista3.setVisible(true);
		getContentPane().add(panel_lista3);
		
		cb_udp = new Checkbox("Envio UDP");
		cb_multicast = new Checkbox("Envio Multicast");
		cb_udp.setBounds(textfield.getX(), textfield.getY()+textfield.getHeight()+10, 150, 20);
		cb_multicast.setBounds(textfield.getX(), cb_udp.getY()+cb_udp.getHeight()+10, 150, 20);
		cb_udp.setBackground(Color.white);
		cb_multicast.setBackground(Color.white);	
		
		getContentPane().add(JLpuerto);
		getContentPane().add(JLdir_ip);
		getContentPane().add(JTpuerto);
		getContentPane().add(JTdir_ip);
		getContentPane().add(escuchar);
		getContentPane().add(conectar);
		getContentPane().add(desconectar);
		getContentPane().add(Broadcast);
		getContentPane().add(JLmulticast);
		getContentPane().add(JTmulticast);
		getContentPane().add(registrar);	
		getContentPane().add(cb_udp);
		getContentPane().add(cb_multicast);
		
	
		
	}
	
	public void Appendtext(String str,Color c){
		StyledDocument doc = textpane.getStyledDocument();

        javax.swing.text.Style style = textpane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, c);

        try { doc.insertString(doc.getLength(), str,style); }
        catch (BadLocationException e){}
        textpane.setCaretPosition(textpane.getDocument().getLength());
//--------- La linea anterior hace autoscroll
		
	}
	

}
