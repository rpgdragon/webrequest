package webrequest;

import jmcastellano.eu.utilidades.webrequest.PeticionGET;

public class PruebaGET {
	
	private static final String URL = "http://www.jmcastellano.eu/creatorOST/index.php";

	public static void main(String[] args) {
		PeticionGET p = new PeticionGET(URL);
		//pasar token por args
		String token=args[0];
		p.addParametro("token", token);
		p.setEsQuery(true);
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
