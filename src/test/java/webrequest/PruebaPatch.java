package webrequest;

import jmcastellano.eu.utilidades.modelo.Metodo;
import jmcastellano.eu.utilidades.webrequest.PeticionNotGET;

public class PruebaPatch {
	
	public static final String url="https://httpbin.org/put";
	
	public static void main(String[] args) {
		PeticionNotGET p = new PeticionNotGET(url,Metodo.PATCH);
		p.setEsQuery(false);
		p.setLeerRespuesta(true);
		p.addParametro("parametro1", "ValorParametro");
		p.realizarPeticion();
		while(p.isEnEjecucion()) {
			try { Thread.sleep(100);} catch(Exception e) {}
		}
		if(p.isExito()) {
			System.out.println(p.getRespuesta());
		}
		else {
			System.out.println("Se ha producido un error: " + p.getRespuesta());
		}
		
	}

}
