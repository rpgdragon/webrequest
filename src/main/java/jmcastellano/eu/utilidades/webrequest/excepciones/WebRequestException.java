package jmcastellano.eu.utilidades.webrequest.excepciones;

/**
 * 
 * @author José Manuel Castellano Domínguez
 *
 */

public class WebRequestException extends Exception {
	
	private static final long serialVersionUID = 7695060011880432363L;

	public WebRequestException(String mensaje) {
		super(mensaje);
	}

}
