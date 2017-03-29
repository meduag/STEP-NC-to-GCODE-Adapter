package br.UNB.LAB.FAcessorias;

import br.UNB.LAB.Integrador.Integrador;

public class ImprimirInfo {

	public static void ImprimirInformacion(String DatoImprimir){
		if(Integrador.ImpLedFer==1){
			System.out.println("("+ DatoImprimir + ")");
		}

	}

}
