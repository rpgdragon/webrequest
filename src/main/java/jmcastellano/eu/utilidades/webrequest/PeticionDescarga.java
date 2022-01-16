package jmcastellano.eu.utilidades.webrequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import jmcastellano.eu.utilidades.excepciones.WebRequestException;

public final class PeticionDescarga extends PeticionGET {
	
	private File fichero;
	private boolean debeSobreescribir;

	public PeticionDescarga(String url, String rutafichero) {
		super(url);
		fichero = new File(rutafichero);
		this.debeSobreescribir = false;
	}
	
	public PeticionDescarga(String url, String rutafichero, boolean debeSobreescribir) {
		super(url);
		fichero = new File(rutafichero);
		this.debeSobreescribir = debeSobreescribir;
	}	
	
	@Override
	protected void realizarAccion(URL url) throws WebRequestException{
		if(fichero!=null && fichero.exists() && !debeSobreescribir) {
			//no se hace nada, ya que no se puede sobreescribir
			return;
		}
		try {
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			try (OutputStream outstream = new FileOutputStream(fichero)) {
				byte[] buffer = new byte[4096];
				int len;
				while ((len = is.read(buffer)) > 0) {
					outstream.write(buffer, 0, len);
				}
			}
			catch(Exception e){
				throw new WebRequestException("No se ha podido descargar el fichero");
			}
		}
		catch(IOException e) {
			throw new WebRequestException("No se ha podido descargar el fichero");
		}
		
	}
}
