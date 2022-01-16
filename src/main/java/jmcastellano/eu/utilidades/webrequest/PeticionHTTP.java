package jmcastellano.eu.utilidades.webrequest;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jmcastellano.eu.utilidades.excepciones.WebRequestException;

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

    public PeticionHTTP(String urlPeticion){
       this.urlPeticion = urlPeticion;
       this.maximo_intentos = MAXIMO_INTENTOS;
    }
    
    public PeticionHTTP(String urlPeticion, int maximo_intentos) {
    	this.urlPeticion = urlPeticion;
    	this.maximo_intentos = MAXIMO_INTENTOS;
    }
      
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

    protected abstract void realizarAccion(URL url) throws WebRequestException;
    protected abstract void tramitarParametrosQuery() throws Exception;
    
    /**
     * Este metodo se encarga de recuperar la respuesta
     * @param s
     * @return
     * @throws Exception
     */
	public String tramitarRespuesta(Scanner s) throws Exception {
		StringBuilder strb = new StringBuilder();
		while(s.hasNextLine()) {
			strb.append(s.nextLine());
		}
		return strb.toString();
	}
    
    public HashMap<String,Object> getParametros(){
    	return this.parametros;
    }
        
    /**
     * Este metodo añade un nuevo parametro al listado de parametros
     * @param nombre
     * @param valor
     */
    public void addParametro(String nombre, Object valor) {
    	if(this.parametros==null) {
    		this.parametros = new HashMap<String,Object>();
    	}
    	this.parametros.put(nombre, valor);
    }
    
    /**
     * Este metodo borra el parametro si lo encuentro en el lsitado de parametros
     * @param nombre
     */
    public void deleteParametro(String nombre) {
    	if(this.parametros==null) {
    		return;
    	}
    	this.parametros.remove(nombre);
    }

    public boolean isEnEjecucion() {
        return enEjecucion;
    }

    public boolean isExito(){
        return exito;
    }

	public String getRespuesta() {
		return respuesta;
	}
	
	void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getUrlPeticion() {
		return urlPeticion;
	}
	
	protected void setUrlPeticion(String url) {
		this.urlPeticion = url;
	}

	public int getMaximo_intentos() {
		return maximo_intentos;
	}

	public void setMaximo_intentos(int maximo_intentos) {
		this.maximo_intentos = maximo_intentos;
	}

	public boolean isEsQuery() {
		return esQuery;
	}

	public void setEsQuery(boolean esQuery) {
		this.esQuery = esQuery;
	}
	
	
	public boolean isLeerRespuesta() {
		return leerRespuesta;
	}

	public void setLeerRespuesta(boolean leerRespuesta) {
		this.leerRespuesta = leerRespuesta;
	}

	/**
	 * Metodo que se encarga de formatear los parametros ya sea para enviarlos por Query o por POST
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	String tramitarParametros() throws UnsupportedEncodingException{
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
