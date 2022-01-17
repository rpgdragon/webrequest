package jmcastellano.eu.utilidades.webrequest;

/**
 * 
 * @author Jos� Manuel Castellano Dom�nguez
 *
 */

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jmcastellano.eu.utilidades.webrequest.excepciones.WebRequestException;

abstract class PeticionHTTP {
    
    private boolean enEjecucion = false;
    private String urlPeticion;
    private int maximo_intentos;
    private boolean exito = false;
    private String respuesta;
    private boolean esQuery = false;
    private boolean leerRespuesta = false;
    
    private static final int MAXIMO_INTENTOS = 10;
    
    private HashMap<String,Object> parametros;

    /**
     * Constructor que inicializa con la URL en la que hay que lanzar la peticion
     * @param urlPeticion URL donde se debe hacer la peticion (sin parametros)
     */
    public PeticionHTTP(String urlPeticion){
       this.urlPeticion = urlPeticion;
       this.maximo_intentos = MAXIMO_INTENTOS;
    }
    
    /**
     * Constructor que inicializa con la URL en la que hay que lanzar la peticion y permite indicar cuantos intentos se debe intentar
     * @param urlPeticion URL donde se debe hacer la peticion (sin parametros)
     * @param maximo_intentos N�mero m�ximo de reintentos que se intentara hacer
     */
    public PeticionHTTP(String urlPeticion, int maximo_intentos) {
    	this.urlPeticion = urlPeticion;
    	this.maximo_intentos = MAXIMO_INTENTOS;
    }
    
    /**
     * M�todo que se encarga de ejecutar la peticion, utilizando el estado en que se encuentra actualmente.
     * Este m�todo se lanza de forma asincrona, por lo que hay que esperar a que el campo enEjecucion se ponga
     * a false antes de intentar buscar la respuesta
     */
    public void realizarPeticion(){
        this.enEjecucion = true;
        Thread t = new Thread(() -> {
            int indice = 0;
            do{
                try{
                  tramitarParametrosQuery();
                  URL url = new URL(urlPeticion);
                  realizarAccion(url);
                  exito = true;
                  break;       
                } catch(Exception e){
                  indice++;
                }
            }while(indice < maximo_intentos);
            this.enEjecucion = false;
        });
        t.start();
    }

    /**
     * M�todo que se encarga de conectar con la URL 
     * @param url URL a la que se tiene que hacer la petici�n
     * @throws WebRequestException
     */
    protected abstract void realizarAccion(URL url) throws WebRequestException;
    
    /**
     * M�todo que se encarga de construir la URL a�adiendo los parametros query, si lo permite
     * el campo esQuery
     * @throws Exception Excepci�n que puede producirse durante la construcci�n del Query
     */
    protected abstract void tramitarParametrosQuery() throws Exception;
    
    /**
     * Este metodo se encarga de recuperar la respuesta devuelta por el servidor
     * @param s Objeto Escaner que permite leer los datos devueltos por el InputStream de la conei�n
     * @return  Cadena de texto con la respuesta del servidor
     * @throws Exception Excepci�n producida por la lectura de la respuesta
     */
	public String tramitarRespuesta(Scanner s) throws Exception {
		StringBuilder strb = new StringBuilder();
		while(s.hasNextLine()) {
			strb.append(s.nextLine());
		}
		return strb.toString();
	}
    
	/**
	 * Getter de los parametros, devuelve la colecci�n de parametros que se han ido a�adiendo al metodo
	 * @return Conjunto de parametros asociados a la petici�n
	 */
    public HashMap<String,Object> getParametros(){
    	return this.parametros;
    }
        
    /**
     * Este metodo a�ade un nuevo parametro al listado de parametros
     * @param nombre Nombre del parametro que se quiere a�adir
     * @param valor Valor del parametro que se quiere a�adir
     */
    public void addParametro(String nombre, Object valor) {
    	if(this.parametros==null) {
    		this.parametros = new HashMap<String,Object>();
    	}
    	this.parametros.put(nombre, valor);
    }
    
    /**
     * Este metodo borra el parametro si lo encuentro en el lsitado de parametros
     * @param nombre Nombre del parametro a borrar
     */
    public void deleteParametro(String nombre) {
    	if(this.parametros==null) {
    		return;
    	}
    	this.parametros.remove(nombre);
    }

    /**
     * Getter de la variable que indica si esta en ejecuci�n
     * @return Boolean que indica si la petici�n sigue en ejecuci�n
     */
    public boolean isEnEjecucion() {
        return enEjecucion;
    }

    /**
     * Getter de la variable que indica si la petici�n se ha realizado con exito
     * @return Boolean que indica si la petici�n se ha realizado exitosamente
     */
    public boolean isExito(){
        return exito;
    }

    /**
     * Getter de la variable que contiene la respuesta recibida de la petici�n
     * @return String que indica la cadena de texto con la respuesta recibida
     */
	public String getRespuesta() {
		return respuesta;
	}
	
	/**
	 * Setter que permite introducir la respuesta. S�lo utilizable por las clases hija 
	 * @param respuesta Respuesta que ha proporcionado el servicio web
	 */
	protected void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	/**
	 * Getter que permite obtener la URL donde se hace la petici�n
	 * @return String que indica la URL donde se hace la peticion
	 */
	public String getUrlPeticion() {
		return urlPeticion;
	}
	
	/**
	 * Setter que permite introducir la URL donde se hace la petici�n. S�lo utilizable por las clases hija. Normalmente utilizado para actualizar la url con queries
	 * @param url Cadena de texto que contiene la URL adonde se hace la petici�n
	 */
	protected void setUrlPeticion(String url) {
		this.urlPeticion = url;
	}

	/**
	 * Getter que obtiene el n�mero m�ximo de reintentos que se har�n
	 * @return Entero que indica el n�mero de reintentos que se har�n como m�ximo
	 */
	public int getMaximo_intentos() {
		return maximo_intentos;
	}

	/**
	 * Setter que indica el n�mero m�ximo de reintentos que se har�n
	 * @param maximo_intentos Entero que indica el n�mero de reintentos que se har�n como m�ximo
	 */
	public void setMaximo_intentos(int maximo_intentos) {
		this.maximo_intentos = maximo_intentos;
	}

	/**
	 * Getter que indica si los parametros se pasan via Query o via Form
	 * @return Booleano que indica si los parametros se pasan via query (true) o Form (false) 
	 */
	public boolean isEsQuery() {
		return esQuery;
	}

	/**
	 * Setter que indica si los parametros se pasan via Query o via Form
	 * @param esQuery Booleano que indica si se debe pasar los parametros via query (true) o Form (false)
	 */
	public void setEsQuery(boolean esQuery) {
		this.esQuery = esQuery;
	}
	
	/**
	 * Getter que indica si debe leerse la respuesta que provenga del servidor
	 * @return Booleano que indica si debe leerse la respuesta que venga del servidor
	 */
	public boolean isLeerRespuesta() {
		return leerRespuesta;
	}
	
	/**
	 * Setter que indica si debe leerse la respuesta que provenga del servidor
	 * @param leerRespuesta Booleano que indica si debe leerse la respuesta que venga del servidor
	 */
	public void setLeerRespuesta(boolean leerRespuesta) {
		this.leerRespuesta = leerRespuesta;
	}

	/**
	 * Metodo que se encarga de formatear los parametros ya sea para enviarlos por Query o por POST
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	String tramitarParametros() throws UnsupportedEncodingException{
		if(this.getParametros()==null || this.getParametros().isEmpty()) {
			return null;
		}
		//los parametros en un GET se definen via la URL
		StringBuilder strb = new StringBuilder();
	    boolean primero = true;
	    for(Map.Entry<String, Object> entry : this.getParametros().entrySet()){
	    	//primero validamos que el Object que viene sea valido
	    	StringBuilder st = new StringBuilder();
	    	st.append(entry.getValue());
	    	if (primero) {
	    		primero = false;
	    	}
	        else {
	            strb.append("&");
	        }

	        strb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	        strb.append("=");    
	        strb.append(URLEncoder.encode(st.toString(), "UTF-8"));
	    }

	    return strb.toString();
	}

}
