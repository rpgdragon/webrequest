package jmcastellano.eu.utilidades.webrequest;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import jmcastellano.eu.utilidades.excepciones.WebRequestException;
import jmcastellano.eu.utilidades.modelo.Constantes;
import jmcastellano.eu.utilidades.modelo.Metodo;

public class PeticionNotGET extends PeticionHTTP {
	
	private Metodo metodo;
	
	public PeticionNotGET(String url, Metodo metodo) {
		super(url);
		this.metodo = metodo;
	}

	@Override
	protected void realizarAccion(URL url) throws WebRequestException {
		HttpURLConnection httpCon = null;
		try {
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setConnectTimeout(15000);
			httpCon.setRequestMethod(metodo.getNombre());
			httpCon.setDoInput(true);
	        httpCon.setDoOutput(!this.isEsQuery());
	        
	        if(!this.isEsQuery()) {
	        	OutputStream os = httpCon.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(
	                    new OutputStreamWriter(os, "UTF-8"));
	            writer.write(tramitarParametros());
	            writer.flush();
	            writer.close();
	            os.close();
	        }
	        else {
	        	httpCon.connect();
	        }
	        
	        int code = httpCon.getResponseCode();
 	        if(code!=HttpURLConnection.HTTP_OK) {
 	        	//todo lo que de distinto a 200 es que se ha producido un error
 	        	throw new WebRequestException("Petición de Borrado Fallido. Estatus recibido: " + code);
 	        }
	        
 	        //si llega aqui, es que hay una respuesta
	        if(this.isLeerRespuesta()) {
	        	Scanner s = null;
	    		s = new Scanner(httpCon.getInputStream(), "UTF-8");
	    		this.setRespuesta(tramitarRespuesta(s));
	    		if (this.getRespuesta() == null || this.getRespuesta().isEmpty()) {
	    			this.setRespuesta(Constantes.ERROR_NO_RESPUESTA);
	    			throw new WebRequestException(Constantes.ERROR_NO_RESPUESTA);
	    		}
	        }
	        
		}
		catch(Exception e) {
			throw new WebRequestException(Constantes.ERROR_PETICION);
		}
		finally {
			if(httpCon!=null) {
				httpCon.disconnect();
			}
		}
	}

	@Override
	public void tramitarParametrosQuery() throws Exception{
		if(this.isEsQuery()) {
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
	}
	
}
