package br.UNB.LAB.GerarCodG;

import br.UNB.LAB.FAcessorias.CalularNoPasosEspiral;
import br.UNB.LAB.FAcessorias.CalularNoPasosProfundidadeCorte;
import br.UNB.LAB.FAcessorias.ImprimirInfo;
import br.UNB.LAB.FAcessorias.ImprimirPlanoSeg;
import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;

public class Gcode_02ST {
	
	//private static boolean imprimirBase=false;
	public static double DiametroAnterior=0; 
	public static boolean imprimirlinhaAdd=false;
	public static double AnguloBase=0;
	private static double ValorY_AnteriorYborde=0;
	private static double ValorY_AnteriorYEspiral=0;
	

	public static void GCode_RanhuraSimple(double InicioX, double InicioY, double Profundidade,double LarguraRanhura,String ToolData, double ProfundidadeOffset,boolean imprimirInicio){
		
		ImprimirInfo.ImprimirInformacion("(Dados da Nova Ferramenta: "+ ToolData);
		
		if(imprimirInicio==true){
			TrocaDeFerramenta.LineNo=Gcode.LineNo;
			TrocaDeFerramenta.ImpriFer(ToolData);
		}
		
		if(imprimirlinhaAdd==true){
			ImprimirSecuenciaLineaNo.EscribirLinea("G40");
		}
		//System.out.println("(parte simple)");
		/**calcular la cantidad de pasadas dependiendo del diametro de la Tool **/
		CalularNoPasosProfundidadeCorte.PassadasPC(Profundidade, Gcode.ProfundidadeCorte);
		//CalularNoPasosProfundidadeCorte.NpasosEnterosPC = 1;//para testar cuando es 1
		/*** Imprimir centro del Furo ***/
		ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(InicioX) + " Y" + Gcode.df.format(InicioY));
		if(imprimirInicio==true){
			/*** Imprimir Plano de Seguranza ***/
			ImprimirPlanoSeg.ImpPlanoSeguranza();
		}
		//ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));//Original funcionando
		ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(ProfundidadeOffset));
/*		if(ProfundidadeOffset>0){
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 Z-" + Gcode.df.format(ProfundidadeOffset));
		}else{
			ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(ProfundidadeOffset));
		}*/
		/*** Operacion de Furacion ***/
		if(CalularNoPasosProfundidadeCorte.NpasosEnterosPC ==1 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC==0){//aqui se cuando la profundidade es igual al diametro dela Herramienta
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 F" + Gcode.df.format(Gcode.MaxVelcorteTool) + " Z-" + Gcode.df.format(Profundidade));
			ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(LarguraRanhura));
		}else{
			double signo = LarguraRanhura;//doy el primer paso
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 Z-" + Gcode.df.format(Gcode.ProfundidadeCorte));
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format(signo));
			for (int i = 1; i < CalularNoPasosProfundidadeCorte.NpasosEnterosPC; i++) {
				signo = signo *-1;
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(Gcode.ProfundidadeCorte));
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 X" + Gcode.df.format(signo));
				}
			//hago la parte decimal
			if(CalularNoPasosProfundidadeCorte.NpasosDecimalesPC>0 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC<1){
				signo = signo *-1;
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(CalularNoPasosProfundidadeCorte.NpasosDecimalesPC*Gcode.ProfundidadeCorte));
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 X" + Gcode.df.format(signo));
				}
			}
		//DiametroToolAnterior  = DiametroTool;
		}
	
	public static void GCode_RanhuraRecorrerBordeSegmentada(double XPartida, double YPartida, double Profundidade, String ToolData,double LarguraRanhura,double AnchuraRanhura,boolean imprimirInicio,boolean imprimirFinal, double ProfundidadeOffset){
		
		if(imprimirInicio==true){
			TrocaDeFerramenta.LineNo=Gcode.LineNo;
			TrocaDeFerramenta.ImpriFer(ToolData);
			//inicio
			ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(0) + " Y" + Gcode.df.format(0));
			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(Profundidade));//estaba rpofundidad
			ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
			//YPartida =YPartida -3;//teste
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format(XPartida + (LarguraRanhura/2)) + " Y" + Gcode.df.format(YPartida));
			if(ProfundidadeOffset>0){
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 Z-" + Gcode.df.format(ProfundidadeOffset));
				ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 Z-" + Gcode.df.format(Profundidade));
			}else{
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(Profundidade));
			}
			
		}else{
			if(imprimirlinhaAdd==true){
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format(XPartida + (LarguraRanhura/2)) + " Y" + Gcode.df.format(YPartida - AnchuraRanhura/2));
			}
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 F" + Gcode.MaxVelcorteTool +" Z-" + Gcode.df.format(Profundidade));//continua con la secuencia
		}
		///secuencia
		double RadioTool=TrocaDeFerramenta.Diametro_Tool/2;
		ImprimirSecuenciaLineaNo.EscribirLinea("G90 Y" + Gcode.df.format(YPartida+(AnchuraRanhura/2)));//esquina sup der
		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
		ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida -(AnchuraRanhura/2)));//esquina inf izq
		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//medio prepara la proxima
		
/*		ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(LarguraRanhura+(RadioTool*2)+1));//esquina inf der
		ImprimirSecuenciaLineaNo.EscribirLinea("G91 Y" + Gcode.df.format(AnchuraRanhura));//esquina sup der
		ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(-RadioTool));//esquina sup izq
		ImprimirSecuenciaLineaNo.EscribirLinea("G91 Y-" + Gcode.df.format(AnchuraRanhura));//esquina inf izq
		ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(XPartida + (LarguraRanhura/2)) + " Y" + Gcode.df.format(YPartida));//medio prepara la proxima
*/		
		if(imprimirFinal==true & imprimirlinhaAdd==false){
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(LarguraRanhura+(RadioTool*2)+1));//finalizar en la esq inf der
			ImprimirSecuenciaLineaNo.EscribirLinea("G40");
			ImprimirPlanoSeg.ImpPlanoSeguranza();
		}
	}
		
	
	public static void GCode_RanhuraEspiralSegmentada(double XPartida, double YPartida, double Profundidade, String ToolData,double LarguraRanhura,double AnchuraRanhura,boolean imprimirInicio,boolean imprimirFinal, double ProfundidadeOffset){
		//AnchuraRanhura = AnchuraRanhura -7;
		//System.out.println("(Dados da Nova Ferramenta: "+ ToolData +")");
		
		double RadioTool=0;
		if(imprimirInicio==true){
			TrocaDeFerramenta.LineNo=Gcode.LineNo;
			TrocaDeFerramenta.ImpriFer(ToolData);
			RadioTool=TrocaDeFerramenta.Diametro_Tool/2;
			//inicio en el medio de la feature
			ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(0) + " Y" + Gcode.df.format(0));
			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(Profundidade));
			ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
			if(ProfundidadeOffset>0){
				//***************************Verificar Mod en el valor de Y (RadioTool/2) o solamente el radio *******************************//*
				double YinicialRadio=0;
				if(RadioTool<=AnguloBase){
					YinicialRadio=YPartida-(RadioTool/2);
				}else{
					YinicialRadio=(YPartida+(AnchuraRanhura/2))-RadioTool;
				}
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format((AnchuraRanhura/2)) + " Y" + Gcode.df.format(YinicialRadio));//Verificar Mod
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 Z-" + Gcode.df.format(ProfundidadeOffset));
				ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 Z-" + Gcode.df.format(Profundidade));
			}else{
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format((AnchuraRanhura/2)) + " Y" + Gcode.df.format(YPartida-RadioTool));
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(Profundidade));
			}
		}else{
			RadioTool=TrocaDeFerramenta.DatoDiametroTool(ToolData)/2;
			double valY = YPartida-(RadioTool/2);
			//System.out.println("("+valY+")");
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format((AnchuraRanhura/2)) + " Y" + Gcode.df.format(valY-DiametroAnterior));//Verificar Mod
			//ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format((AnchuraRanhura/2)) + " Y" + Gcode.df.format(YPartida-(RadioTool/2)));//Verificar Mod
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 Z-" + Gcode.df.format(Profundidade));//continua con la secuencia
		}
		///secuencia
		CalularNoPasosEspiral.PassadasEspiral(AnchuraRanhura, RadioTool);
		//hacer espiral -- inicio 
		ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + (Gcode.MaxVelcorteTool/2) + " X" + Gcode.df.format(LarguraRanhura+(RadioTool*2)-AnchuraRanhura/2));//centro mas radio tool
		
		double offsetXinicial = LarguraRanhura - AnchuraRanhura + TrocaDeFerramenta.Diametro_Tool;
		double Xespiral = 0;//para compensar la salida de la tool
		double Yespiral = 0;

		ImprimirSecuenciaLineaNo.EscribirLinea("G91 F" + Gcode.MaxVelcorteTool);//esquina inf der
		boolean signo = true;
		for (int i = 2; i < (CalularNoPasosEspiral.NpasosEnterosEsp+1); i++) {
			if(signo == true){
				Xespiral = (offsetXinicial+(RadioTool*i))*-1;
				Yespiral = (RadioTool *i);
				signo = false;
			}else{
				Xespiral = (offsetXinicial+(RadioTool*i));
				Yespiral = (RadioTool *i)*-1;
				signo = true;
			}
			ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(Yespiral));//esquina sup der
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(Xespiral));//esquina sup izq

		}
		if(Xespiral<0 & Yespiral>0){
			ImprimirInfo.ImprimirInformacion("Esquina sup izq");
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 F" + Gcode.MaxVelAvanceMaquina*0.75 + " Y" + Gcode.df.format(-AnchuraRanhura));//esquina sup der
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(Xespiral*-1));//esquina sup izq
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 Y" + Gcode.df.format(AnchuraRanhura));//esquina inf izq
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(-RadioTool));//esquina inf der
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(XPartida + (0)) + " Y" + Gcode.df.format(YPartida));//medio prepara la proxima
		}
		
		if(Xespiral>0 & Yespiral<0){
			ImprimirInfo.ImprimirInformacion("Esquina inf der");
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 F" + Gcode.MaxVelAvanceMaquina*0.75 + " Y" + Gcode.df.format(AnchuraRanhura));//esquina sup der
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(-RadioTool));//esquina sup izq
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 Y" + Gcode.df.format(-(AnchuraRanhura)));//esquina inf izq
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(TrocaDeFerramenta.Diametro_Tool*2));//esquina inf der
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(XPartida + (-RadioTool/2)) + " Y" + Gcode.df.format(YPartida));//medio prepara la proxima
		}

		//System.out.println();
		if(imprimirFinal==true){
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(LarguraRanhura+TrocaDeFerramenta.Diametro_Tool));//finalizar en la esq inf der
			ImprimirSecuenciaLineaNo.EscribirLinea("G40");
			ImprimirPlanoSeg.ImpPlanoSeguranza();
		}
	}
	
	
	
	
	public static void GCode_RanhuraEspiralSegmentada2(double XPartida, double YPartida, double Profundidade, String ToolData,double LarguraRanhura,double AnchuraRanhura,boolean imprimirInicio,boolean imprimirFinal, double ProfundidadeOffset){
		
		double diatool=TrocaDeFerramenta.DatoDiametroTool(ToolData);
		//LarguraRanhura = 50;
		double RadioTool = diatool/2;
		if(imprimirInicio==true){/*********************************************************************div2**/
			Gcode_02ST.GCode_RanhuraRecorrerBorde2(XPartida, YPartida, Profundidade, ToolData, LarguraRanhura, diatool*2,true,false,ProfundidadeOffset);//intermedio
			//ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(LarguraRanhura+(RadioTool*2)+1));//finalizar en la esq inf der
		}else{
			ImprimirInfo.ImprimirInformacion("------------------ sep");
			ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 X"+ Gcode.df.format(0) +" Y" + Gcode.df.format(YPartida));//finalizar en la esq inf der
			Gcode_02ST.GCode_RanhuraRecorrerBorde2(XPartida, YPartida, Profundidade, ToolData, LarguraRanhura, diatool*2,false,false,ProfundidadeOffset);//intermedio
		}
		
		//AnchuraRanhura=21;
		CalularNoPasosEspiral.PassadasEspiral(AnchuraRanhura, RadioTool);
		
		//calcular valor de Y
		//double IniYespiral = (YPartida-(AnchuraRanhura/2)+RadioTool/2)+diatool;
		
		if((CalularNoPasosEspiral.NpasosEnterosEsp-2)==1){
			ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
			ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida+(AnchuraRanhura/2)));//esquina sup der
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
			ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida -(AnchuraRanhura/2)));//esquina inf izq
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//medio prepara la proxima
			
		}else{
			ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);ImprimirSecuenciaLineaNo.G41Impreso=true;
			for (int i = 2; i < (CalularNoPasosEspiral.NpasosEnterosEsp); i++) {
				if((YPartida+(diatool*i))>(((CalularNoPasosEspiral.NpasosEnterosEsp*RadioTool)/2))+YPartida){
					ImprimirInfo.ImprimirInformacion("se paso ------------");
					ImprimirInfo.ImprimirInformacion("Inicio: " + (YPartida+(diatool*i)));
					ImprimirInfo.ImprimirInformacion("Valor limite: "+ ((((CalularNoPasosEspiral.NpasosEnterosEsp*RadioTool)/2))+YPartida));
					if(ValorY_AnteriorYborde-(YPartida-(AnchuraRanhura/2))<AnchuraRanhura){//para encontrar el valor que todavia no pasodel limite
						CalularNoPasosEspiral.PassadasEspiral(AnchuraRanhura, diatool);
					}
					i = (int)CalularNoPasosEspiral.NpasosEnterosEsp;
				}else{
				
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 Y" + Gcode.df.format(YPartida+(diatool*i)));//esquina sup der
				ValorY_AnteriorYEspiral = (YPartida+(diatool*i));
				ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
				
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 Y" + Gcode.df.format(YPartida-(diatool*i)));//esquina sup der
				ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//esquina sup izq
				}
			}
		}

		
		//hacer el final
		if(CalularNoPasosEspiral.NpasosDecimalesEsp>0 ){
			if(ImprimirSecuenciaLineaNo.G41Impreso==false){
				ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
			}
			ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida+(AnchuraRanhura/2)));//esquina sup der
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
			ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida -(AnchuraRanhura/2)));//esquina inf izq
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//medio prepara la proxima
			ImprimirSecuenciaLineaNo.G41Impreso=false;//retorno el valor original
		}else if(ValorY_AnteriorYEspiral-(YPartida-(AnchuraRanhura/2))<AnchuraRanhura){
			//System.out.println("--------------------- falta");
			ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
			ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida+(AnchuraRanhura/2)));//esquina sup der
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
			ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida -(AnchuraRanhura/2)));//esquina inf izq
			ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//medio prepara la proxima
		}
		
		if(imprimirFinal==true){
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 X" + Gcode.df.format(LarguraRanhura+TrocaDeFerramenta.Diametro_Tool));//finalizar en la esq inf der
			ImprimirPlanoSeg.ImpPlanoSeguranza();
		}
		
	}
	
    public static void GCode_RanhuraRecorrerBorde2(double XPartida, double YPartida, double Profundidade, String ToolData,double LarguraRanhura,double AnchuraRanhura,boolean imprimirInicio,boolean imprimirFinal, double ProfundidadeOffset){
		
		if(imprimirInicio==true){
			TrocaDeFerramenta.LineNo=Gcode.LineNo;
			TrocaDeFerramenta.ImpriFer(ToolData);
			//inicio
			ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(0) + " Y" + Gcode.df.format(0));
			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " Y" + Gcode.df.format(YPartida));
			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));//estaba rpofundidad
			if(ProfundidadeOffset>0){
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G00 Z-" + Gcode.df.format(ProfundidadeOffset));
				ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 Z-" + Gcode.df.format(Profundidade));
			}else{
				ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(Profundidade));
			}
			
		}else{
			if(imprimirlinhaAdd==true){
				ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format(0) + " Y" + Gcode.df.format(YPartida));
			}
			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 F" + Gcode.MaxVelcorteTool +" Z-" + Gcode.df.format(Profundidade));//continua con la secuencia
		}
		///secuencia
		double RadioTool=TrocaDeFerramenta.Diametro_Tool/2;
		ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 X" + Gcode.df.format(LarguraRanhura+RadioTool));//esquina inf der
		ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida + RadioTool));//esquina sup der
		ValorY_AnteriorYborde = (YPartida + RadioTool);
		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
		ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida - RadioTool));//esquina inf izq
		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//medio prepara la proxima
	}
    
    public static void GCode_RanhuraRecorrerBorde3ConFinal(double XPartida, double YPartida, double Profundidade, String ToolData,double LarguraRanhura,double AnchuraRanhura,boolean imprimirInicio,boolean ImprimirFinal, double ProfundidadeOffset){
    	double RadioTool=TrocaDeFerramenta.Diametro_Tool/2;
    	
    	if((TrocaDeFerramenta.DatoDiametroTool(ToolData)*2)<AnchuraRanhura){//estaba  el diametro*2
    		Gcode_02ST.GCode_RanhuraRecorrerBorde2(XPartida, YPartida, Profundidade, ToolData, LarguraRanhura, AnchuraRanhura,imprimirInicio,ImprimirFinal,ProfundidadeOffset);//inicial
    		
    		ImprimirSecuenciaLineaNo.EscribirLinea("G41 D" + TrocaDeFerramenta.numeroD);
    		ImprimirSecuenciaLineaNo.EscribirLinea("G90 Y" + Gcode.df.format(YPartida+(AnchuraRanhura/2)));//esquina sup der
    		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
    		ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format(YPartida -(AnchuraRanhura/2)));//esquina inf izq
    		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//medio prepara la proxima
			ImprimirSecuenciaLineaNo.EscribirLinea("G40");
    	}else{
    		
    		if(ImprimirSecuenciaLineaNo.G41Impreso==false){
    			ImprimirSecuenciaLineaNo.EscribirLinea("G40");
    			ImprimirSecuenciaLineaNo.G41Impreso=true;
    		}
    		if(ToolData.indexOf("BALL_ENDMILL")!=-1 & TrocaDeFerramenta.tipo.indexOf("FACEMILL")!=-1){
    			RadioTool=TrocaDeFerramenta.DatoDiametroTool(ToolData)/2;
    			ImprimirInfo.ImprimirInformacion("Falta troca de ferramenta");
    			//System.out.println("(Falta trroca de ferramenta)");
    			TrocaDeFerramenta.LineNo=Gcode.LineNo;
    			TrocaDeFerramenta.ImpriFer(ToolData);
    			
    			ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(0) + " Y" + Gcode.df.format(0));
    			ImprimirSecuenciaLineaNo.EscribirLinea("G90 G01 F" + Gcode.MaxVelcorteTool + " X" + Gcode.df.format(LarguraRanhura+RadioTool) + " Y" + Gcode.df.format(YPartida));
    			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));//estaba rpofundidad
    			ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z-" + Gcode.df.format(ProfundidadeOffset));//estaba rpofundidad
    			ImprimirSecuenciaLineaNo.EscribirLinea("G01 Z-" + Gcode.df.format(ProfundidadeOffset+Profundidade));//estaba rpofundidad
    		}else{
    			ImprimirSecuenciaLineaNo.EscribirLinea("G91 G01 F" + (Gcode.MaxVelcorteTool/2) +" Z-" + Gcode.df.format(Profundidade));
    		}

    		

    		ImprimirSecuenciaLineaNo.EscribirLinea("G90 F" + Gcode.MaxVelcorteTool +" Y" + Gcode.df.format((YPartida+(AnchuraRanhura/2))-RadioTool));//esquina sup der
    		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(-RadioTool));//esquina sup izq
    		ImprimirSecuenciaLineaNo.EscribirLinea("Y" + Gcode.df.format((YPartida -(AnchuraRanhura/2))+RadioTool));//esquina inf izq
    		ImprimirSecuenciaLineaNo.EscribirLinea("X" + Gcode.df.format(LarguraRanhura+RadioTool));//medio prepara la proxima

    	}
	}
}