package br.UNB.LAB.GerarCodG;

import br.UNB.LAB.FAcessorias.CalularNoPasosEspiral;
import br.UNB.LAB.FAcessorias.CalularNoPasosProfundidadeCorte;
import br.UNB.LAB.FAcessorias.ImprimirPlanoSeg;
import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;


public class Gcode_01RH {
		
	private static double DiametroToolAnterior=0;
	private static boolean imprimirBase=false;
	public static boolean imprimirBaseUnaPasada=false;
	
	public static void GCode_Furos(double CentroX, double CentroY, double Profundidade, double DiatroInicialFuro,double DiatroFinalFuro, double ProfundidadeOffset, String ToolData,int TipoFuro_1Simple_2Segmentado){
			//Profundidade = Profundidade+1;
		double DiametroTool = TrocaDeFerramenta.DatoDiametroTool(ToolData);
		
		TrocaDeFerramenta.LineNo=Gcode.LineNo;
		TrocaDeFerramenta.ImpriFer(ToolData);
		
		switch (TipoFuro_1Simple_2Segmentado) {
		case 1://furo simple
			GCode_FuroSimple(CentroX, CentroY, Profundidade, DiametroTool,TrocaDeFerramenta.TloNum,ProfundidadeOffset);
			break;
		case 2://furo segmentado recto
			imprimirBase=false;
			GCode_FuroSegmentado(CentroX, CentroY, Profundidade, DiatroInicialFuro, DiametroTool, TrocaDeFerramenta.TloNum, ProfundidadeOffset);
			break;
		}
	}

	//falta adicionar el offset de profundidad
	public static void GCode_FuroSimple(double CentroX, double CentroY, double Profundidade, double DiametroTool,double Tlo,double ProfundidadeOffset){
		
		/** Compenso la Profundidad si hubiera Necesidad**/
		if(Profundidade>Tlo & TrocaDeFerramenta.tipo.startsWith("CENTER_DRILL")==true){ //pregunta si es center drill e si la profundidad es mayor que el TLO de la herramienta
			Profundidade=Tlo; //actualizo la profundidade para el center drill
			}
		
		/**calcular la cantidad de pasadas dependiendo del diametro de la Tool **/
		CalularNoPasosProfundidadeCorte.PassadasPC(Profundidade, DiametroTool);
		
		/*** Imprimir centro del Furo ***/
		ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));
		
		/*** Imprimir Plano de Seguranza ***/
		ImprimirPlanoSeg.ImpPlanoSeguranza();
		
		/*** Operacion de Furacion ***/
		if(CalularNoPasosProfundidadeCorte.NpasosEnterosPC ==1 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC==0){//aqui se cuando la profundidade es igual al diametro dela Herramienta
			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(ProfundidadeOffset));
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 F" + Gcode.df.format(Gcode.MaxVelcorteTool*1.5) + " Z-" + Gcode.df.format(Profundidade));
		}else{
			for (int i = 0; i < CalularNoPasosProfundidadeCorte.NpasosEnterosPC; i++) {
				ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(DiametroTool * (i+1)));
				}
			if(CalularNoPasosProfundidadeCorte.NpasosDecimalesPC>0 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC<1){
				ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(Profundidade));
				}
			}
		DiametroToolAnterior=DiametroTool;
		}
	
	public static void GCode_FuroSimplePasadaFinal(double CentroX, double CentroY, double Profundidade){
		ImprimirSecuenciaLineaNo.EscribirLinea("G90 G40 G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));
		ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(Profundidade));
		ImprimirPlanoSeg.ImpPlanoSeguranza();
		}
	
	public static void GCode_FuroSegmentado(double CentroX, double CentroY, double Profundidade, double DiatroInicialFuro,double DiametroTool,double ProfundidadeOffset, double FeedRate){
		
		CalularNoPasosProfundidadeCorte.PassadasPC(Profundidade, Gcode.ProfundidadeCorte);
		
		double FazerEspiral = (DiatroInicialFuro-DiametroToolAnterior);//encuentro la cantidad de material que todavia falta por usinar
		FazerEspiral = (FazerEspiral/2)/DiametroTool;//verifico que la herramienta pode fazer o resto da usinagem sim fazer o espiral
		
		/****************************** si es menor que 1 quiere decir que solo se hace el barrido del borde para retirar el material que falta ****************************************************/
		if(FazerEspiral<=1){//
			if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
				System.out.println("(furo segmentado Recorrer borde)");
			}
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX-(DiatroInicialFuro/2)) + " Y" + Gcode.df.format(CentroY-(DiatroInicialFuro/2)));
			ImprimirPlanoSeg.ImpPlanoSeguranza();
			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
			ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.df.format(Gcode.MaxVelcorteTool));
			imprimirBase=true;
			GCode_FS_RecorrerBorde(CentroX, CentroY, DiatroInicialFuro, DiametroTool,Profundidade);
		}else{
			if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
				System.out.println("(furo segmentado espiral)");
			}
			CalularNoPasosEspiral.PassadasEspiral(DiatroInicialFuro/2, DiametroTool*Gcode.PorcentageToolPassoEspiral);
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY-(DiametroToolAnterior/2)));
			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
			ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.df.format(Gcode.MaxVelcorteTool));
			
			//CalularNoPasosProfundidadeCorte.NpasosDecimalesPC =0.3;
			
			double addPasoPC =0;
			if(CalularNoPasosProfundidadeCorte.NpasosDecimalesPC>0){
				addPasoPC =1;
			}
			
			for (int i = 0; i < (CalularNoPasosProfundidadeCorte.NpasosEnterosPC + addPasoPC); i++) {
				ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
				double Radio1raPasada=(DiametroToolAnterior/2)+(DiametroTool/2);
				ImprimirSecuenciaLineaNo.EscribirLinea("G91  X" + Gcode.df.format(Radio1raPasada) + " Y" + Gcode.df.format(DiametroToolAnterior/2));
				
				if (addPasoPC==1 & i == (CalularNoPasosProfundidadeCorte.NpasosEnterosPC) ) {
					GCode_FS_HacerEspiral(CentroX, CentroY, DiatroInicialFuro, DiametroTool,Gcode.ProfundidadeCorte * CalularNoPasosProfundidadeCorte.NpasosDecimalesPC);
					ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));

				}else{
					GCode_FS_HacerEspiral(CentroX, CentroY, DiatroInicialFuro, DiametroTool,Gcode.ProfundidadeCorte );
					
					if (i==(CalularNoPasosProfundidadeCorte.NpasosEnterosPC - 1) & addPasoPC==0 ) {
						ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));
					}else{
						ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY-(DiametroToolAnterior/2)));
					}

				}
			//ImprimirPlanoSeg.ImpPlanoSeguranza();
			}//fin for
		}

		DiametroToolAnterior=DiametroTool;		
	}
	
	public static void GCode_FS_RecorrerBorde(double CentroX, double CentroY, double DiatroInicialFuro, double DiametroTool,double Profundidade){
		ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
		ImprimirSecuenciaLineaNo.EscribirLinea("G91  X" + Gcode.df.format(DiatroInicialFuro) + " Y" + Gcode.df.format(DiatroInicialFuro/2));
		//ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 Z-" + GeneradorCodidoG.df.format(GeneradorCodidoG.ProfundidadeCorte));
		double pasosEspiralBorde=0;
		if(CalularNoPasosProfundidadeCorte.NpasosDecimalesPC>0){
			pasosEspiralBorde = CalularNoPasosProfundidadeCorte.NpasosEnterosPC +1;
		}else{
			pasosEspiralBorde = CalularNoPasosProfundidadeCorte.NpasosEnterosPC;
			CalularNoPasosProfundidadeCorte.NpasosDecimalesPC=1;
		}

		//hago la cantidad de pasos enteros
		for (int i = 0; i < pasosEspiralBorde; i++) {
			// cuadrante 1 (x- , y)
			if(i<pasosEspiralBorde-1){
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " Z-" + Gcode.df.format(Gcode.ProfundidadeCorte) +  " R" + Gcode.df.format(DiatroInicialFuro/2));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
				}else{
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " Z-" + Gcode.df.format(Gcode.ProfundidadeCorte * CalularNoPasosProfundidadeCorte.NpasosDecimalesPC) +  " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
				}
			}
		//DEJO REDONDO EL CIRCULO	
		ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
		if(imprimirBase==true){
			double valCirBase = 0;
			if((DiatroInicialFuro/2)<DiametroTool){
				valCirBase = (DiatroInicialFuro-DiametroTool)/2;
			}else{
				valCirBase = DiametroTool/2;
			}
			ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(CentroX + valCirBase));
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX - valCirBase) + " R" + Gcode.df.format(valCirBase));
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX + valCirBase) + " R" + Gcode.df.format(valCirBase));
			}
		ImprimirSecuenciaLineaNo.EscribirLinea("G01 X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));
		ImprimirPlanoSeg.ImpPlanoSeguranza();
		DiametroToolAnterior=DiametroTool;
	}
		
	public static void GCode_FS_HacerEspiral(double CentroX, double CentroY, double DiatroInicialFuro, double DiametroTool, double ProfundidadeCorte){
		double Radio1raPasada=(DiametroToolAnterior/2)+(DiametroTool/2);
		double pasosEspiralBorde=0;
		if(CalularNoPasosEspiral.NpasosDecimalesEsp>0){
			pasosEspiralBorde = CalularNoPasosEspiral.NpasosEnterosEsp+1;
		}else{
			pasosEspiralBorde = CalularNoPasosEspiral.NpasosEnterosEsp;
		}

		//hago la cantidad de pasos enteros
		for (int i = 1; i < (pasosEspiralBorde+1); i++) {
			if(i==1){//tiene que haber un primero G03
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(Radio1raPasada) +" Y" + Gcode.df.format(Radio1raPasada) + " Z-" + Gcode.df.format(ProfundidadeCorte) +  " R" + Gcode.df.format(Radio1raPasada));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(Radio1raPasada) +" Y-" + Gcode.df.format(Radio1raPasada) + " R" + Gcode.df.format(Radio1raPasada));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(Radio1raPasada) +" Y-" + Gcode.df.format(Radio1raPasada) + " R" + Gcode.df.format(Radio1raPasada));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(Radio1raPasada+(DiametroTool/2)) +" Y" + Gcode.df.format(Radio1raPasada) + " R" + Gcode.df.format(Radio1raPasada+(DiametroTool/2)));
			}else{
			if(i<=(pasosEspiralBorde-1)){
				double XYRadio=(i*DiametroTool/2)+(DiametroToolAnterior/2);
				if(XYRadio<(DiatroInicialFuro/2)){
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(XYRadio) +" Y" + Gcode.df.format(XYRadio) + " R" + Gcode.df.format(XYRadio));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(XYRadio) +" Y-" + Gcode.df.format(XYRadio) + " R" + Gcode.df.format(XYRadio));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(XYRadio) +" Y-" + Gcode.df.format(XYRadio) + " R" + Gcode.df.format(XYRadio));
				}
				double rad1 = (XYRadio + (DiametroTool/2));
				double rad2 = DiatroInicialFuro/2;
				if(rad1<rad2){
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(XYRadio + (DiametroTool/2)) +" Y" + Gcode.df.format(XYRadio) + " R" + Gcode.df.format(XYRadio + (DiametroTool/2)));
				}else{
					if(XYRadio<(DiatroInicialFuro/2)){
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(rad2) +" Y" + Gcode.df.format(XYRadio) + " R" + Gcode.df.format(rad2));
					}
				}
				
				}else{
					//llmar a hacer el borde/--
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
				}//fin if passos restantes
			}//finif_i=1

			
			
		}//finfor
		
		//ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 X" + GeneradorCodidoG.df.format(CentroX) + " Y" + GeneradorCodidoG.df.format(CentroY));
		
	}
	
	
	
	
	public static void GCode_FuroSegmentadoUnaPasada(double CentroX, double CentroY, double Profundidade, double DiatroFuroPasada,double ProfundidadeOffset, String ToolData,boolean ImpInicio,double RadioMenos, boolean SumarAngTool){
		if(ImpInicio==true){
			ImprimirPlanoSeg.ImpPlanoSeguranza();
			TrocaDeFerramenta.LineNo=Gcode.LineNo;
			TrocaDeFerramenta.ImpriFer(ToolData);}
		
		double DiametroTool = TrocaDeFerramenta.DatoDiametroTool(ToolData);
		
		double FazerEspiral = DiatroFuroPasada/DiametroTool;
		
		if(FazerEspiral<=2 & DiatroFuroPasada>DiametroTool){//VERIFICO QUE DEBO HACER SI RECORRER EL BORDE O HACER SOLO UNA PASADA O HACER EL ESPIRAL
			if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
				System.out.println("(furo segmentado Recorrer borde)");
			}
			
			ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
			
			
			if(ImpInicio==true){
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX-(DiatroFuroPasada/2)) + " Y" + Gcode.df.format(CentroY-(DiatroFuroPasada/2)));
				ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.df.format(Gcode.MaxVelcorteTool));
			}
			
			
			GCode_FS_RecorrerBordeUnaPasada(CentroX, CentroY, DiatroFuroPasada, DiametroTool,Profundidade,ProfundidadeOffset,ToolData,ImpInicio,RadioMenos,SumarAngTool);
			
		}else{//hay dos opciones 1) el furo es menor que el radio de
			if(DiatroFuroPasada<=DiametroTool){
				GCode_FuroSimplePasadaFinal(CentroX, CentroY, Profundidade + DiatroFuroPasada);
			}
			if(FazerEspiral>2){
				//System.out.println("Diaaaaaaaaaaaaaaaaaaaaaaaaaaaa FURO: mior que ferramenta" + DiatroFuroPasada);
				CalularNoPasosEspiral.PassadasEspiral(DiatroFuroPasada/2, DiametroTool*Gcode.PorcentageToolPassoEspiral);
				GCode_FS_HacerEspiralUnaPasada(CentroX, CentroY, DiatroFuroPasada, DiametroTool, Profundidade, ProfundidadeOffset, SumarAngTool);
			}
		}
		DiametroToolAnterior=DiametroTool;	
		
		ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));
	}
	
	public static void GCode_FS_RecorrerBordeUnaPasada(double CentroX, double CentroY, double DiatroInicialFuro, double DiametroTool,double Profundidade, double ProfundidadeOffset,String ToolData, boolean ImpInicio,double RadioMenos, boolean SumAngBase){
		
		if(ImpInicio==true){
			ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
			ImprimirSecuenciaLineaNo.EscribirLinea("G91  X" + Gcode.df.format(DiatroInicialFuro) + " Y" + Gcode.df.format(DiatroInicialFuro/2));

			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 Z-" + Gcode.df.format(Profundidade +ProfundidadeOffset ));
			double SumarAnguloBaseProf=0;///////////////verificar puede dar error
			if(SumAngBase==true){
				SumarAnguloBaseProf=DiametroTool/2;
			}
			
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " Z-" + Gcode.df.format(SumarAnguloBaseProf) +  " R" + Gcode.df.format(DiatroInicialFuro/2));
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y-" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format(DiatroInicialFuro/2) + " R" + Gcode.df.format(DiatroInicialFuro/2));
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X-"+ Gcode.df.format(DiatroInicialFuro/2) +" Y" + Gcode.df.format((DiatroInicialFuro/2)-(RadioMenos/2)) + " R" + Gcode.df.format((DiatroInicialFuro/2)-(RadioMenos/2)));
		}else{
			double AngXY = (DiatroInicialFuro/2) * Math.cos(Math.toRadians(45));//angulo de entrada del eje Z
			double SumarAnguloBaseProf=0;///////////////verificar puede dar error
			if(SumAngBase==true){
				SumarAnguloBaseProf=DiametroTool/2;
			}else{
				SumarAnguloBaseProf=TrocaDeFerramenta.DatoAngBAseTool(ToolData);
			}
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G03 X"+ Gcode.df.format(CentroX-AngXY) +" Y" + Gcode.df.format(CentroY + AngXY) + " Z-" + Gcode.df.format(Profundidade + ProfundidadeOffset + SumarAnguloBaseProf) +" R" + Gcode.df.format(DiatroInicialFuro/2));
			ImprimirSecuenciaLineaNo.EscribirLinea("X"+ Gcode.df.format(CentroX-(DiatroInicialFuro/2)) +" Y" + Gcode.df.format(CentroY) + " R" + Gcode.df.format(DiatroInicialFuro/2));
			ImprimirSecuenciaLineaNo.EscribirLinea("X"+ Gcode.df.format(CentroX + (DiatroInicialFuro/2)) +" Y" + Gcode.df.format(CentroY) + " R" + Gcode.df.format(DiatroInicialFuro/2));
			
			ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y" + Gcode.df.format(CentroY + ((DiatroInicialFuro/2)-(RadioMenos/2))) + " R" + Gcode.df.format((DiatroInicialFuro/2)));
			//System.out.println();
			}

	}
	
	public static void GCode_FS_HacerEspiralUnaPasada(double CentroX, double CentroY, double DiatroInicialFuro, double DiametroTool, double ProfundidadeCorte,double ProfundidadeOffset,boolean SumAngBase){
		
		//double Radio1raPasada=(DiametroToolAnterior/2)+(DiametroTool/2);
		double pasosEspiralBorde=0;
		if(CalularNoPasosEspiral.NpasosDecimalesEsp>0){
			pasosEspiralBorde = CalularNoPasosEspiral.NpasosEnterosEsp+1;
		}else{
			pasosEspiralBorde = CalularNoPasosEspiral.NpasosEnterosEsp;
		}
		
		ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) + " X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY));
		if(imprimirBaseUnaPasada==true){
			ImprimirPlanoSeg.ImpPlanoSeguranza();
			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
			ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.df.format(Gcode.MaxVelcorteTool));
		}

		//hago la cantidad de pasos enteros
		//int quadrante = 0;
		double valtem = 0;
		double RadFuro = (DiatroInicialFuro/2);
		double SumarAnguloBaseProf=0;
		if(SumAngBase==true){
			SumarAnguloBaseProf=DiametroTool/2;
		}
		
		for (int i = 1; i < (pasosEspiralBorde+1); i++) {
			if(i==1){//tiene que haber un primero G03
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(ProfundidadeOffset + SumarAnguloBaseProf+ProfundidadeCorte));
				ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 X"+ Gcode.df.format(CentroX+DiametroTool) + " Y"+ Gcode.df.format(CentroY));
				
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX-DiametroTool) + " R" + Gcode.df.format(DiametroTool));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y"+ Gcode.df.format(CentroY-DiametroTool) + " R" + Gcode.df.format(DiametroTool));
				double BaseIniX = DiametroTool*1.25;
				double BaseIniY = DiametroTool*1.5;
				if(BaseIniX<=RadFuro){
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX+BaseIniX) +" Y"+ Gcode.df.format(CentroY) + " R" + Gcode.df.format(DiametroTool*1.25));
					if(BaseIniY<=RadFuro){
						ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y"+ Gcode.df.format(CentroY+BaseIniY) + " R" + Gcode.df.format(DiametroTool*1.5));	
					}
				}
			}else if(i<=(pasosEspiralBorde-1)){

				valtem = ((i+1) * (DiametroTool/2));
				//double RadFuro = (DiatroInicialFuro/2);
				if(valtem<=RadFuro){
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX-valtem) + " Y"+ Gcode.df.format(CentroX)+" R" + Gcode.df.format(valtem));
				ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y"+ Gcode.df.format(CentroY-valtem) + " R" + Gcode.df.format(valtem));
				//quadrante=2;
				}
				double valtt = (valtem+(DiametroTool*0.25));
				
				if(valtt<RadFuro){//para que no sobre pase el valor final
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX+(valtem+(DiametroTool*0.25))) +" Y"+ Gcode.df.format(CentroY) + " R" + Gcode.df.format((valtem+(DiametroTool*0.25))));
					//quadrante=3;
					double valtt2 = (valtem+(DiametroTool*0.5));
					if(valtt2<=RadFuro){//para que no sobre pase el valor final
						if(valtt2==RadFuro){
							ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y" + Gcode.df.format(CentroY + (DiatroInicialFuro/2)) + " R" + Gcode.df.format(DiatroInicialFuro/2));
						}else{
						ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y"+ Gcode.df.format(CentroY + (valtem+(DiametroTool*0.5))) + " R" + Gcode.df.format((valtem+(DiametroTool*0.5))));
						//quadrante=4;
						}
					}else{
						i=i++;//salgo del for
					}
				}else{
					i=i++;//salgo del for
				}
				
				
				}else{
					//hacer el borde/--
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y" + Gcode.df.format(CentroY + (DiatroInicialFuro/2)) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y" + Gcode.df.format(CentroY - (DiatroInicialFuro/2)) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					ImprimirSecuenciaLineaNo.EscribirLinea("G03 X"+ Gcode.df.format(CentroX) +" Y" + Gcode.df.format(CentroY + (DiatroInicialFuro/2)) + " R" + Gcode.df.format(DiatroInicialFuro/2));
					
				}//fin if passos restantes
			
		}//finfor
		
		//ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 X" + GeneradorCodidoG.df.format(CentroX) + " Y" + GeneradorCodidoG.df.format(CentroY));
		
	}
	
	
	
	//para hacer el paso final de un furo
	public static void FuroSimplePassoFinal(double CentroX, double CentroY, double ProfundidadeAnterior, double ProfundidadeFinal, double FeedRate) {
		ImprimirSecuenciaLineaNo.EscribirLinea("G40 G00 X" + Gcode.df.format(CentroX) + " Y" + Gcode.df.format(CentroY) + " Z-" + Gcode.df.format(ProfundidadeAnterior));
		ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.df.format(FeedRate*1.5) + " Z-" + Gcode.df.format(ProfundidadeFinal));
		}
	
	
	/**** Funciones accesorias ***/
	

	

}
