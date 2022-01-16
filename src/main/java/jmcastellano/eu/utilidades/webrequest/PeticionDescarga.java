package jmcastellano.eu.utilidades.webrequest;

/**
 * 
 * @author José Manuel Castellano Domínguez
 *
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import jmcastellano.eu.utilidades.webrequest.excepciones.WebRequestException;

public final class PeticionDescarga extends PeticionGET {
	
	private File fichero;
	private boolean debeSobreescribir;

	/**
	 * Constructor que indica la URL de donde debe descargar el fichero y la ruta del fichero donde debe descargarlo
	 * @param url Cadena de texto que contiene la url desde donde se debe descargar el fichero
	 * @param rutafichero Ruta del fichero adonde se debe descargar el fichero
	 */
	public PeticionDescarga(String url, String rutafichero) {
		super(url);
		fichero = new File(rutafichero);
		this.debeSobreescribir = false;
	}
	
	/**
	 * Constructor que indica la URL de donde debe descargar el fichero, la ruta del fichero donde debe descargar y si se permite que se sobreescriba el fichero
	 * @param url Cadena de texto que contiene la url desde donde se debe descargar el fichero
	 * @param rutafichero Ruta del fichero adonde se debe descargar el fichero
	 * @param debeSobreescribir Booleano que indica si se acepta la sobreescritura del fichero o no
	 */
	public PeticionDescarga(String url, String rutafichero, boolean debeSobreescribir) {
		super(url);
		fichero = new File(rutafichero);
		this.debeSobreescribir = debeSobreescribir;
	}	
	
    /**
     * Método que se encarga de conectar con la URL 
     * @param url URL a la que se tiene que hacer la petición
     * @throws WebRequestException
     */
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
