package br.UNB.LAB.FAcessorias;

import br.UNB.LAB.GerarCodG.Gcode;

public class ImprimirSecuenciaLineaNo {
	
	static int LineNo=0;
	public static String VerificarUltimaImpresion ="";
	public static boolean G41Impreso=false;

	public static void EscribirLinea(String Comando) {
		LineNo = Gcode.LineNo;
		System.out.println("N" + LineNo + " " + Comando.replace(",", "."));
		VerificarUltimaImpresion = Comando;
		LineNo += 1;
		Gcode.LineNo = LineNo;
	}

}
