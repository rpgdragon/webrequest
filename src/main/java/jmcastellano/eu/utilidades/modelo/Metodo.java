package jmcastellano.eu.utilidades.modelo;

/**
 * 
 * @author José Manuel Castellano Domínguez
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
