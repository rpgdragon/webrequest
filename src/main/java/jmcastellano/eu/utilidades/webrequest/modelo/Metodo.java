package jmcastellano.eu.utilidades.webrequest.modelo;

/**
 * 
 * @author Jos� Manuel Castellano Dom�nguez
 *
 */

public enum Metodo {
	DELETE("DELETE"),PUT("PUT"),POST("POST"),PATCH("PATCH");
	
	private String nombre;
	
	private Metodo(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
}
