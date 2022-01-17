package webrequest;

import jmcastellano.eu.utilidades.webrequest.PeticionDescarga;

public class PruebaDescarga {
  public static void main(String[]args) {
	  PeticionDescarga kh = new PeticionDescarga("https://raw.githubusercontent.com/obskyr/khinsider/master/khinsider.py","khinsider.py",false);
	  kh.realizarPeticion();
  }
}
