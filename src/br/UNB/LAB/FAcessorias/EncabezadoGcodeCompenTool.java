package br.UNB.LAB.FAcessorias;

import br.UNB.LAB.GerarCodG.Gcode;
import br.UNB.LAB.GerarCodG.TrocaDeFerramenta;

public class EncabezadoGcodeCompenTool {

	public static int LineNo = 0;
	public static String LinG41D="";

	public static int ImprimirGcodeHeaderCON_G41(double CentroX, double CentroY, int LineNoSeq) {
		LineNo = LineNoSeq;
		
		/*** 2do imprimir compensacion de ferramenta, plano de segurança e primer corte profCorte **/
		String implin = "G40 G00 Z" + br.UNB.LAB.InfBasicas.PlanoSeguranca.ComprimentoPlanoSegDouble;//vuelvo al planoSec en G54
		WriteLineNo(implin);
		
		implin = "G01 F"+ Gcode.MaxVelAvanceMaquina +" X" + (CentroX) + " Y" + (CentroY);
		WriteLineNo(implin);
		
		implin = "G00 Z0.0";
		WriteLineNo(implin);
	
		//valor temporal de la profundidade de corte para hacer las camadas
		double ProfCorte = Gcode.ProfundidadeCorte;
		
		/*implin = "G01 F"+ GeneradorCodidoG.MaxVelcorteTool +" Z-" + ProfCorte;
		WriteLineNo(implin);*/
		
		implin = "G01 F"+ Gcode.MaxVelcorteTool*1.25;
		WriteLineNo(implin);
		
		implin = "G41 D" + TrocaDeFerramenta.numeroD;
		LinG41D=implin;
		WriteLineNo(implin);
		/**Fin encabezado  compenzacion de Tool*/
		return LineNo;
	}
	
	public static int ImprimirGcodeHeaderSIN_G41(double CentroX, double CentroY, int LineNoSeq) {
		LineNo = LineNoSeq;
		
		/*** 2do imprimir compensacion de ferramenta, plano de segurança e primer corte profCorte **/
		String implin = "G40 G00 Z" + br.UNB.LAB.InfBasicas.PlanoSeguranca.ComprimentoPlanoSegDouble;//vuelvo al planoSec en G54
		WriteLineNo(implin);
		
		implin = "G01 F"+ Gcode.MaxVelAvanceMaquina +" X" + (CentroX) + " Y" + (CentroY);
		WriteLineNo(implin);
		
		implin = "G00 Z0.0";
		WriteLineNo(implin);
		
		implin = "G01 F"+ Gcode.MaxVelcorteTool;
		WriteLineNo(implin);
	
		//valor temporal de la profundidade de corte para hacer las camadas
		double ProfCorte = Gcode.ProfundidadeCorte;
		
		/*implin = "G01 F"+ GeneradorCodidoG.MaxVelcorteTool +" Z-" + ProfCorte;
		WriteLineNo(implin);*/
		/**Fin encabezado  compenzacion de Tool*/
		return LineNo;
	}
	
	public static void WriteLineNo(String lin) {
		System.out.println("N" + LineNo + " " + lin.replace(",", "."));
		LineNo += 1;
	}
}
