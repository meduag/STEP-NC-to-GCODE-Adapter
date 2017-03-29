package br.UNB.LAB.FAcessorias;

import br.UNB.LAB.GerarCodG.Gcode;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;

public class ImprimirPlanoSeg {

	public static void ImpPlanoSeguranza(){
		//double PlanoSeg = PlanoSeguranca.ComprimentoPlanoSegDouble;
		ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G00 Z" +  Gcode.df.format(PlanoSeguranca.ComprimentoPlanoSegDouble));
	}
}
