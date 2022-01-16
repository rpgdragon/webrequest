package jmcastellano.eu.utilidades.webrequest;

/**
 * 
 * @author José Manuel Castellano Domínguez
 *
 */


import java.net.URL;
import java.util.Scanner;

import jmcastellano.eu.utilidades.webrequest.excepciones.WebRequestException;
import jmcastellano.eu.utilidades.webrequest.modelo.Constantes;

public class PeticionGET extends PeticionHTTP {

	public PeticionGET(String url){
		super(url);
		super.setLeerRespuesta(true);
	}
	
	public PeticionGET(String url, int maximo_intentos) {
		super(url,maximo_intentos);
		super.setLeerRespuesta(true);
	}
	
	/**
	 * Metodo que permite convertir los parametros en la URL definitva. Solo se pueden apilar al final por lo que no se tiene en cuenta la variable esQuery
	 */
	@Override
	public void tramitarParametrosQuery() throws Exception {
		//los GET siempre van a ser de tipoQuery
		StringBuilder strb = new StringBuilder();
		String url = this.getUrlPeticion();
		strb.append(url);
		String query = tramitarParametros();
		if(query!=null && !query.isEmpty()) {
			strb.append("?");
			strb.append(query);
			this.setUrlPeticion(strb.toString());
		}
	}
	
	@Override
	protected void realizarAccion(URL url) throws WebRequestException{
		Scanner s = null;
		try {
			s = new Scanner(url.openStream(), "UTF-8");
			this.setRespuesta(tramitarRespuesta(s));
			if (this.getRespuesta() == null || this.getRespuesta().isEmpty()) {
				this.setRespuesta(Constantes.ERROR_NO_RESPUESTA);
				throw new WebRequestException(Constantes.ERROR_NO_RESPUESTA);
			}
		}
		catch(Exception e) {
			this.setRespuesta(Constantes.ERROR_PETICION);
			throw new WebRequestException(Constantes.ERROR_PETICION);
		}
	}
	
	@Override
	public void setLeerRespuesta(boolean leerRespuesta) {
		//en un metodo GET nunca se debe modificar si se puede leer la respuesta
	}
	
}
