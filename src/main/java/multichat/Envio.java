package multichat;

import java.io.Serializable;


public class Envio implements Serializable{
	public String mensaje;
		Envio(){
			mensaje= "CONECTADO\n";
		}
	private static final long serialVersionUID = 1L;

}
