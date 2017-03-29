package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;

public class FuroSimples {

	static int LineNo;//contador de linea sucesiva
	static String implin="";//imrpimir linea
	 
	public double CenterX;//Pos inicial X
	public double CenterY;//Pos inicial Y
	public double BoreDepth;//Profudidad
	public static double FeedRate;//velocidad de avance
	public double OffsetProfZ;//paso para llegar a la prof
	//private static double SecPlan;//plano de seguranza
	public String Ferramenta="";//plano de seguranza
	public static double DiaTool;//Pos inicial X
	//public double offDrill = 0;
	public double ImpFinal=1;
	//public double OffSecPla=0;
	public static boolean pasante=false;
	private static double Npasadas=0; 
	
	
	
	private double CurX, CurY, ZOffset=0, NpasosDecimales=0;
	private int NpasosEnteros;  
	
	public void FSimplesNc(){
	
		DecimalFormat df = new DecimalFormat("0.000"); 
		LineNo=GeneradorCodidoG.LineNo;
		
		CurX = CenterX;
		CurY = CenterY;

		FeedRate=br.UNB.LAB.GerarCodG.GeneradorCodidoG.MaxVelcorteTool;//velocidad de avance
		
		if(OffsetProfZ>0){
			ZOffset=OffsetProfZ;
		}else{
			ZOffset=0;
		}
		
		
		//llamo a trocar la herramienta
		TrocaDeFerramenta.LineNo=LineNo;
		TrocaDeFerramenta.ImpriFer(Ferramenta);
		
		double temTlo=Double.parseDouble(br.UNB.LAB.GerarCodG.TrocaDeFerramenta.Tlo);//obtiene el Largo de corte de la Ferramenta el TLO e lo usa si es center drill
		
		if(BoreDepth>temTlo & br.UNB.LAB.GerarCodG.TrocaDeFerramenta.tipo.startsWith("CENTER_DRILL")==true){ //pregunta si es center drill e si la profundidad es mayor que el TLO de la herramienta
			//System.out.println("Solo una pasadas -----------------------------------");
			BoreDepth=temTlo; //actualizo la profundidade para el center drill
			}
		
		//imprimo la pocicion del hueco despues de estar en el plano de seguranza entrada inicial
		implin = "G00 Z" + br.UNB.LAB.InfBasicas.PlanoSeguranca.ComprimentoPlanoSegDouble;//VOY AL PLANO DE SEGURANZA
		WriteLineNo(implin);
		
		implin = "G01 F" + GeneradorCodidoG.MaxVelAvanceMaquina + " X" + CurX + " Y" + CurY;
		WriteLineNo(implin);

		//inicio secuencias de codigo G
		
		pasante=CodeG_Para__RoundHole_E_Operaciones.pasante;
		
		if(pasante==false){//el furo es pasante
			
			//calculo la cantidad de pasos para hacer el hueco
			double diametroFerra=br.UNB.LAB.GerarCodG.TrocaDeFerramenta.Diametro_Tool;
			Npasadas=br.UNB.LAB.FAcessorias.CalProfundidadeDrill.CalculoProfundidadeCorte(diametroFerra, BoreDepth);
			NpasosEnteros = (int)Npasadas;
			NpasosDecimales = (Npasadas - NpasosEnteros);
			
			double TempForINpasos = 0;
			
			if(NpasosEnteros ==1 & NpasosDecimales==0){
				implin = "G00 Z0.00";//voy al punto cero y espero el resto del codigo G para empezar la furacion
				WriteLineNo(implin);
				implin = "G01 F" + df.format(FeedRate*1.5) + " Z-" + df.format(BoreDepth);
				WriteLineNo(implin);
			}else {
				for (int i = 0; i < NpasosEnteros; i++) {
					implin = "G00 Z0.00";//voy al punto cero y espero el resto del codigo G para empezar la furacion
					WriteLineNo(implin);
					implin = "G01 F" + df.format(FeedRate*1.5) + " Z-" + df.format((i+1) * diametroFerra );
					WriteLineNo(implin);
					TempForINpasos = (i+1) * diametroFerra;
					}
	
				//hago la parte que falta si es que falta
				if(NpasosDecimales>0 & NpasosDecimales<1){
					implin = "G00 Z0.00";//voy al punto cero y espero el resto del codigo G para empezar la furacion
					WriteLineNo(implin);
					implin = "G01 F" + df.format(FeedRate*1.5) + " Z-" + df.format(NpasosDecimales * diametroFerra + TempForINpasos);
					WriteLineNo(implin);
					}			
			}
			
		}else{//el furo no es pasante
			temTlo=Double.parseDouble(TrocaDeFerramenta.Tlo);//obtiene el Largo de corte de la Ferramenta el TLO e lo usa si es center drill
			
			if(BoreDepth>temTlo & br.UNB.LAB.GerarCodG.TrocaDeFerramenta.tipo.startsWith("CENTER_DRILL")==true){ //pregunta si es center drill e si la profundidad es mayor que el TLO de la herramienta
				//System.out.println("Solo una pasadas -----------------------------------");
				BoreDepth=temTlo; //actualizo la profundidade para el center drill
				}

			//calculo la cantidad de pasos para hacer el hueco
			double diametroFerra=br.UNB.LAB.GerarCodG.TrocaDeFerramenta.Diametro_Tool;
			Npasadas=br.UNB.LAB.FAcessorias.CalProfundidadeDrill.CalculoProfundidadeCorte(diametroFerra, BoreDepth);
			NpasosEnteros = (int)Npasadas;
			NpasosDecimales = (Npasadas - NpasosEnteros);
			
			//double TempForINpasos = 0;
			for (int i = 0; i < NpasosEnteros; i++) {
				implin = "G00 Z" + df.format(OffsetProfZ);//voy al punto cero y espero el resto del codigo G para empezar la furacion
				WriteLineNo(implin);
				implin = "G91 G01 F" + df.format(FeedRate*1.5) + " Z-" + df.format(diametroFerra);
				WriteLineNo(implin);
				//TempForINpasos = (i+1) * diametroFerra;
				}

			//hago la parte que falta si es que falta
			if(NpasosDecimales>0 & NpasosDecimales<1){
				implin = "G00 Z0.00";//voy al punto cero y espero el resto del codigo G para empezar la furacion
				WriteLineNo(implin);
				implin = "G90 G01 F" + df.format(FeedRate*1.5) + " Z-" + df.format(BoreDepth+OffsetProfZ);
				WriteLineNo(implin);
				}
		}
		GeneradorCodidoG.LineNo=LineNo;
	}//fin
	
	public static void FSimplePassoFinal(double CentroX, double CentroY, double ProfundidadeAnterior, double ProfundidadeFinal) {
		DecimalFormat df = new DecimalFormat("0.000"); 
		FeedRate=br.UNB.LAB.GerarCodG.GeneradorCodidoG.MaxVelcorteTool;//velocidad de avance
		LineNo=GeneradorCodidoG.LineNo;
		
		implin = "G40 G00 X" + df.format(CentroX) + " Y" + df.format(CentroY) + " Z-" + df.format(ProfundidadeAnterior);
		WriteLineNo(implin);
		
		implin = "G01 F" + df.format(FeedRate*1.5) + " Z-" + df.format(ProfundidadeFinal);
		WriteLineNo(implin);
		
		GeneradorCodidoG.LineNo=LineNo;
		
	}
	

	public static void WriteLineNo(String lin) {
		
		System.out.println("N" + LineNo + " " + lin.replace(",", "."));
		LineNo += 1;
		
	}

}

