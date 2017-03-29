package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;

import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;
import br.UNB.LAB.Integrador.Integrador;

//import FuncoesDePosicao.CalculoDosPuntosDoCirculo;

public class RectPocketComplexo {

	//variables necesarias
	public double X1=0, Y1=0, Inicio=1;
	public int ValorPasoAngulo=10;
	public double Largo=0, Ancho=0, DTool=0, Prof=0,LimXY=0,Acabamiento=0,OrtogonalRadius=0;
	
	public static double PlanSeg=0,FeedRate=100;
	
	static double Xmi=0, Ymi=0, Xmf=0, Ymf=0, Anc=0, AncT=0, Lar=0, X=0,Y=0, RTool=0,LimT=0;
	int inc=0;
	static int Npasos=0;
	static double Xf=0, Yf=0, Xp=0, Yp=0, Xi=0, Yi=0;

	public String Ori="Hor";
	static double Xt=0, Yt=0,restante =0, valLim=0;
	static String implin="",dadosFer;
	static int LineNo, salir=1;//contador de linea sucesiva
	
	static double LarguraTempRanura[] = new double[30];//array de pociciojnes para este Slot
	static double ProfTempRanura[] = new double[30];//array de pociciojnes para este Slot
	
	public boolean IniA = true, IniB = false, sal=false , restanOk=false, IniFer=true, IniC=false, ImpTrocaFer=true;
	static boolean Imp_G42=true;
	
		public void RanPocketComplexoNc() {
			FeedRate=Gcode.MaxVelcorteTool;
			
			/** esta feature tiene esta dividida en cuafrantes, asi:
				
				   P1 ******************************* P2
				   *                                  *
				   *								  *
				   *								  *
				   *								  *
				   P3 ******************************* P4 **/
			
			DecimalFormat L3 = new DecimalFormat("0.000"); 
			
			FeedRate=br.UNB.LAB.GerarCodG.Gcode.FeedRate;
			
			//inicializo X e Y
			X=0;
			Y=0;
			Anc=0;
			Lar=0;
			
			//radio ferramenta
			RTool=DTool / 2;
			
			//identifico el mayor lado para saber si esta en vertical o Horizointal
			//se imprime solamente una vez el inicio planode seguranza
			if(ImpTrocaFer==true){
				//imprimir antes de cualquier cosa
				LineNo=Gcode.LineNo;
				ImprimirPlaSeg();
				TrocaDeFerramenta.LineNo=LineNo;
				TrocaDeFerramenta.ImpriFer(dadosFer);
			}
			
			if(Largo >= Ancho){
				Ori="Hor";
			}else{
				Ori="Ver";
			}
				
			
			
			
			if(Ori.startsWith("Hor")==true){//////////////////////// horizontal
				//puntos de inicio en la mitad de la feature
				//inicio                                    //final
				Xmi=X1+(Ancho/2);                     Xmf=(Largo-(Ancho/2))+X1;
				Ymi=Y1+(Ancho/2);                           Ymf=Ymi;
				
				//System.out.println("Xmi:"+Xmi+"  Ymi:"+Ymi+"       Xmf:"+Xmf+"  Ymf:"+Ymf);
				

			if(Acabamiento==0){//para indicar que no hay radio de base 0
				if(Integrador.ImpLedAcabamentoPocket==1){
					System.out.println("(Acabamiento=0)");
				}
				if(IniA==true){
					//System.out.println("no tiene radio inferior");
					CaluloLimitesE_Pasos();
				}
				
				//imprimir solo una vez tambien
				implin = "G00 G40 X" + L3.format(Xmi) + " Y" + L3.format(Ymi);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				if(IniA==true){
					implin = "Z0.0";//imprimo la pocicion del hueco
					WriteLineNo(implin);
					IniA=false;
					ImpTrocaFer=false;//para imprimir solo una vez
				}
				
				/******** para el acabamiento 0 esta dividido en 2 partes
				 * 1ro se imprime para usinar la linea media de la feature
				 * 2do se recorre la feature por cuadrantes P1,P2,P3,P4 hasta llegar al Npasos y el valor restante *******/
				implin = "G01 F"+ FeedRate*.6 +" Z-" + Prof;//imprimo la pocicion del hueco con el 60% del feedrate
				WriteLineNo(implin);
					
				implin = "F"+ FeedRate;//cambio la velocidad feedrate al 100%
				WriteLineNo(implin);
				
				implin = "X" + L3.format(Xmf) + " Y" + L3.format(Ymf);//imprimo la pocicion final media
				WriteLineNo(implin);
					
				//uso de una vez el valor restante y evito un recorrido de cuadrantes ahorrando tiempo 
				if(restante<=RTool & restanOk==false){
					if(Npasos!=0 & Npasos>0 ){
						restanOk=true;
						Imprimir_Cuadrantes(restanOk);
						//imprimo el resto de los passos
						restanOk=false;
						Imprimir_Cuadrantes(restanOk);
					}else{
						if(Npasos<0){
							System.out.println("(Continua com acabamento 1)");
						}else{
							//imprimir cuando el radio de la herramienta es igual al ortogonal de la esquina
							restanOk=false;//para que no haga con el valor restante
							IniB=true;//para que entre en la segunda parte
							Imprimir_Cuadrantes(restanOk);
						}
					}
				}else{//para seguir con los ciclos ///colocar el resto de los ciclos
					restanOk=false;
					Imprimir_Cuadrantes(restanOk);
				}
		}//fin acabamento 0
			
		if(Acabamiento==1){//para indicar que no hay radio de base 0
		
			if(Integrador.ImpLedAcabamentoPocket==1){
				System.out.println("(Acabamiento=1)");
			}
			
			implin = "G01 F"+ FeedRate*.6 +" Z-" + Prof;//imprimo la pocicion del hueco con el 60% del feedrate
			WriteLineNo(implin);
				
			implin = "F"+ FeedRate;//cambio la velocidad feedrate al 100%
			WriteLineNo(implin);
			
			Cuadrantes_Finales_Acabamento1(Imp_G42);
			Imp_G42=false;
			IniA=false;
			ImpTrocaFer=false;//para imprimir solo una vez
		}//fin acabamento 1 ------ Radio de la esquina MAYOR que la herramienta
		
		
		
		
		
		if(Acabamiento==2){//para indicar que no hay radio de base 0
			if(Integrador.ImpLedAcabamentoPocket==1){
				System.out.println("(Acabamiento=2)");
			}
			
			implin = "G01 F"+ FeedRate*.6 +" Z-" + Prof;//imprimo la pocicion del hueco con el 60% del feedrate
			WriteLineNo(implin);
				
			implin = "F"+ FeedRate;//cambio la velocidad feedrate al 100%
			WriteLineNo(implin);
			
			Cuadrantes_Finales_Acabamento2(Imp_G42);
			Imp_G42=false;
			IniA=false;
			ImpTrocaFer=false;//para imprimir solo una vez
		}//fin acabamento 2 ----- Radio de la esquina IGUAL que la herramienta
					
				
				
				
				
				
				
				
				
				
				
				
				
			}else{//////////////////////// vertical
				//inicio                                    //final
				Xmi=X1+(Largo/2);                           Xmf=Xmi;
				Ymi=Y1+(Largo/2);                           Ymf=Y1+Ancho;
				
				
				if(Acabamiento==0){//para indicar que no hay radio de base 0
					if(Integrador.ImpLedAcabamentoPocket==1){
						System.out.println("(Acabamiento=0)");
					}
					
					double enteros = Largo/(RTool*2);
					double pasoEnt = (int)enteros;
					double pasoDec = enteros-pasoEnt;
					Npasos = (int)pasoEnt;
					restante = pasoDec;
					
					//imprimir solo una vez tambien
					Double ValYMIn = Y1+RTool;
					
					implin = "G00 G40 X" + L3.format(Xmi) + " Y" + L3.format(ValYMIn);//imprimo la pocicion del hueco
					WriteLineNo(implin);
					
					/******** para el acabamiento 0 esta dividido en 2 partes
					 * 1ro se imprime para usinar la linea media de la feature
					 * 2do se recorre la feature por cuadrantes P1,P2,P3,P4 hasta llegar al Npasos y el valor restante *******/
					implin = "G01 F"+ FeedRate*.6 +" Z-" + Prof;//imprimo la pocicion del hueco con el 60% del feedrate
					WriteLineNo(implin);
						
					implin = "F"+ FeedRate;//cambio la velocidad feedrate al 100%
					WriteLineNo(implin);
					
					implin = "X" + L3.format(Xmf) + " Y" + L3.format(Ymf-RTool);//imprimo la pocicion final media
					WriteLineNo(implin);
					
					Double ValYMAx = Ymf-RTool;
					
					 //Gcode.LineNo = LineNo;
					int Sig=-1;
					
					for (int i = 1; i < Npasos; i++) {
						//ImprimirSecuenciaLineaNo.EscribirLinea("Hola");
						ImprimirSecuenciaLineaNo.EscribirLinea("X" + L3.format((Xmf + (RTool*i*Sig))));
						Sig = Sig*-1;
						ImprimirSecuenciaLineaNo.EscribirLinea("Y" + L3.format(ValYMIn));
						//i=i+1;
						ImprimirSecuenciaLineaNo.EscribirLinea("X" + L3.format((Xmf + (RTool*i*Sig))));
						Sig = Sig*-1;
						ImprimirSecuenciaLineaNo.EscribirLinea("Y" + L3.format(ValYMAx));
					}
					
					//acabamento final
					ImprimirSecuenciaLineaNo.EscribirLinea("X" + L3.format(X1+RTool));
					ImprimirSecuenciaLineaNo.EscribirLinea("Y" + L3.format(ValYMIn));
					ImprimirSecuenciaLineaNo.EscribirLinea("X" + L3.format(X1-RTool+Largo));
					ImprimirSecuenciaLineaNo.EscribirLinea("Y" + L3.format(ValYMAx));
					ImprimirSecuenciaLineaNo.EscribirLinea("X" + L3.format(Xmi));
					
					//System.out.println();
				}                         Ymf=(Ancho-(Largo/2))+Y1;
				
				
				
				
			}
			

	}//fin funcion principal
		

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		public void CaluloLimitesE_Pasos() {
			
			DecimalFormat L3 = new DecimalFormat("0.000"); 
			
			if(OrtogonalRadius>=RTool){
				/***************************************************************************** calculos de los limites ************************************************************************************************************/
				//este es el limite para hacer el acabamiento 0
				double xO, Xi, rf;
				xO = OrtogonalRadius * Math.cos(Math.toRadians(45));
				Xi = xO - (1 + RTool);
				rf = Xi / Math.cos(Math.toRadians(45));
				//aqui el problema//////////////////
				//rf = Math.abs(rf);
					
				//pontos finales reales osea con los valore x y y adicionados al radio del limite
				double Xrf, Yrf;
				Xrf = (X1 + OrtogonalRadius) - rf;
				Yrf = (Y1 + Ancho - OrtogonalRadius) + rf;
				valLim = Xrf - X1;
				valLim = Double.parseDouble(L3.format(valLim).replace(",", "."));
				
				if(valLim>OrtogonalRadius){
					//System.out.println();
					valLim=OrtogonalRadius;
				}
				
				//encuentro el paso
				double pasoFer = Yrf -Ymf;
				double canPasos = pasoFer / RTool;
				Npasos = (int) Math.floor(canPasos);
				restante = canPasos - Npasos;
				//este valor hay que multiplicarlo por el radio de la herramienta para dar el final
				restante = Double.parseDouble(L3.format(restante).replace(",", "."));
				
//					System.out.println(xO);
//					System.out.println(Xi);
//					System.out.println(rf);
//					//imprimo todos los valores pero el mas importante es el valor del Limite = valLim
//					System.out.println("X:"+Xrf + " Y:"+Yrf + "Valor Limite:" + valLim);
//					System.out.println("paso:" + pasoFer);
//					System.out.println("Cantidad de pasos:" + canPasos);
//					System.out.println("Cantidad de pasos rondeado al minimo:" + Npasos);
//					System.out.println("faccion faltante:" + restante);
			}
			else{
				Npasos=0;
			}
				//System.out.println();
				/**********************************************************************************************************************************************************************************************************/
		}//fin CaluloLimitesE_Pasos
		
		
		
		
		public void Imprimir_Cuadrantes(boolean resOk) {
			DecimalFormat L3 = new DecimalFormat("0.000"); 
			FeedRate=Gcode.MaxVelcorteTool;
			
			if(resOk==true){
				//solo para esta primera vez incluyendo el valor restante
				implin = "F"+ FeedRate*.85;//cambio la velocidad feedrate al 85%
				WriteLineNo(implin);
				
				Yi=Double.parseDouble(L3.format(Ymf - RTool - (restante*RTool)).replace(",", "."));
				implin = "Y" + Yi;//imprimo la pocicion P1
				WriteLineNo(implin);
				
				Xi=Double.parseDouble(L3.format(Xmi - RTool - (restante*RTool)).replace(",", "."));
				implin = "X" + Xi;//imprimo la pocicion P2
				WriteLineNo(implin);
				
				Yf=Double.parseDouble(L3.format(Ymi + RTool + (restante*RTool)).replace(",", "."));
				implin = "Y" + Yf;//imprimo la pocicion P3
				WriteLineNo(implin);
				
				Xf=Double.parseDouble(L3.format(Xmf + RTool + (restante*RTool)).replace(",", "."));
				implin = "X" + Xf;//imprimo la pocicion P4
				WriteLineNo(implin);
				IniB=true;
			}else{
				//para continuar haciendo el recorrido de los cuadrantes
				if(IniB==true){
					IniC = false;
					
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////problema
					if(Npasos!=0){
						int incPaso=2;//inicia en Npasos = 1 por que ya se tenia este valor con el restante
						if(Npasos==2){//si solo hay dos pasos solo se hace la conexion de G40 A G42 y listo
							Cuadrantes(true,false);
						}else{						
							do{
								
								/*************************************************************************************************** Problema **/
								
								if((incPaso+1)>=Npasos){
									IniC = true;
								}
								Cuadrantes(IniC,false);
								incPaso++;
							}while(incPaso<=Npasos);
						}
						
					}else{
						//imprir como si fuera el acabamiento 2
						Cuadrantes(IniC,true);
					}
					
					
				}else{
					int incPaso=0;//inicia en Npasos = 0 por que no hay valor restante al inicio
					Xi=Xmi;
					Yi=Ymf;
					Xf=Xmf;
					Yf=Ymi;
					IniC = false;
					do{
						Cuadrantes(IniC,false);
						if((incPaso+1)==Npasos){
							IniC = true;
						}
						incPaso++;
					}while(incPaso<=Npasos);
					
					
				}
				
				System.out.println();

			}//fin restante o no
			
			//reinicia la siguiente etapa
			Imp_G42=true;
			
		}//fin imprimir cuadrantes para espiral
		
		
		public void Cuadrantes(boolean finP4,boolean ImpSoP1) {
			DecimalFormat L3 = new DecimalFormat("0.000");
			FeedRate=Gcode.MaxVelcorteTool;
			
			if(ImpSoP1==false){
				Yi=Double.parseDouble(L3.format(Yi - RTool).replace(",", "."));
				implin = "Y" + Yi;//imprimo la pocicion P1
				WriteLineNo(implin);
				
				Xi=Double.parseDouble(L3.format(Xi - RTool).replace(",", "."));
				implin = "X" + Xi;//imprimo la pocicion P2
				WriteLineNo(implin);
				
				Yf=Double.parseDouble(L3.format(Yf + RTool).replace(",", "."));
				implin = "Y" + Yf;//imprimo la pocicion P3
				WriteLineNo(implin);
				
				if(finP4==true){
					Xf=Double.parseDouble(L3.format(Xf + RTool).replace(",", "."));
					implin = "X" + Xf;//imprimo la pocicion P4
					WriteLineNo(implin);
					
					Yi=Double.parseDouble(L3.format(Yi).replace(",", "."));
					implin = "Y" + Yi;//imprimo la pocicion P1
					WriteLineNo(implin);
				}else{
					Xf=Double.parseDouble(L3.format(Xf + RTool).replace(",", "."));
					implin = "X" + Xf;//imprimo la pocicion P4
					WriteLineNo(implin);
				}
			}else{//imprime como si fuera acabamiento 2
				double Por30Fer1=RTool + (CodeG_Para__Pocket_E_Operaciones.DiaFertas[1]/2)*0.75;
				
				Xi=Double.parseDouble(L3.format(X1 + Largo - Por30Fer1).replace(",", "."));//Radio ferramenta 1 * 30% para aproximar ao limite
				implin = "X" + Xi ;//imprimo la pocicion P2/2
				WriteLineNo(implin);
				
				Yi=Double.parseDouble(L3.format(Y1 + Por30Fer1).replace(",", "."));
				implin = "Y" + Yi;//imprimo la pocicion P1
				WriteLineNo(implin);
				
				Xi=Double.parseDouble(L3.format(X1 + Por30Fer1).replace(",", "."));
				implin = "X" + Xi;//imprimo la pocicion P2
				WriteLineNo(implin);
				
				Yi=Double.parseDouble(L3.format(Y1 + Ancho - Por30Fer1).replace(",", "."));
				implin = "Y" + Yi;//imprimo la pocicion P1
				WriteLineNo(implin);
				
				Xi=Double.parseDouble(L3.format(X1 + Largo - Por30Fer1).replace(",", "."));
				implin = "X" + Xi;//imprimo la pocicion P2
				WriteLineNo(implin);
			
				Yi=Double.parseDouble(L3.format(Y1 + Por30Fer1).replace(",", "."));
				implin = "Y" + Yi;//imprimo la pocicion P1
				WriteLineNo(implin);
			
			}
		}//fin cuadrantes
		
		
		public void Cuadrantes_Finales_Acabamento1(boolean Imp_G42) {
			DecimalFormat L3 = new DecimalFormat("0.000");
			FeedRate=Gcode.MaxVelcorteTool;
			
			if(Imp_G42==true){
			implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la correcion segun la herramienta
			WriteLineNo(implin);
			
			//hace la trancicion de G40 a G42
			//original
			//Xi=Double.parseDouble(L3.format(X1 + Largo - (OrtogonalRadius + RTool)).replace(",", "."));
			//mudanzav1
			Xi=Double.parseDouble(L3.format(X1 + (Largo/2)).replace(",", "."));
			Yi=Double.parseDouble(L3.format(Y1).replace(",", "."));
			implin = "X" + Xi + " Y" + Yi;//imprimo la pocicion P2
			WriteLineNo(implin);
			}
			
			Xi=Double.parseDouble(L3.format(X1 + OrtogonalRadius).replace(",", "."));
			implin = "X" + Xi;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			//aqui calcularia el valor del radio con un g02 sentido horario
			
			
			//Xi=Double.parseDouble(L3.format(X1 + OrtogonalRadius).replace(",", "."));
			implin = "G02 X" + X1 + " Y" + (Y1 + OrtogonalRadius) + " R" + OrtogonalRadius;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			Yi=Double.parseDouble(L3.format(Y1 + Ancho - OrtogonalRadius).replace(",", "."));
			implin = "G01 Y" + Yi;//imprimo la pocicion P1
			WriteLineNo(implin);
			
			implin = "G02 X" + (X1 + OrtogonalRadius) + " Y" + (Y1 + Ancho) + " R" + OrtogonalRadius;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			Xi=Double.parseDouble(L3.format(X1 + Largo - OrtogonalRadius).replace(",", "."));
			implin = "G01 X" + Xi;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			implin = "G02 X" + (X1 + Largo) + " Y" + (Y1 + Ancho - OrtogonalRadius) + " R" + OrtogonalRadius;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			Yi=Double.parseDouble(L3.format(Y1 + OrtogonalRadius).replace(",", "."));
			implin = "G01 Y" + Yi;//imprimo la pocicion P1
			WriteLineNo(implin);
			
			implin = "G02 X" + (X1 + Largo - OrtogonalRadius ) + " Y" + Y1 + " R" + OrtogonalRadius;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			//original
			//Xi=Double.parseDouble(L3.format(X1 + Largo - (OrtogonalRadius + RTool) - DTool).replace(",", "."));
			//mudanzaV1
			Xi=Double.parseDouble(L3.format((X1 + (Largo/2)) - DTool).replace(",", "."));
			implin = "G01 X" + Xi;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			
		}//fin cuadrantes de acabamiento 1
		
		public void Cuadrantes_Finales_Acabamento2(boolean Imp_G42) {
			DecimalFormat L3 = new DecimalFormat("0.000");
			
			if(Imp_G42==true){
			implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la correcion segun la herramienta
			WriteLineNo(implin);
			
			//hace la trancicion de G40 a G42
			Xi=Double.parseDouble(L3.format(X1 + Largo - (OrtogonalRadius + RTool)).replace(",", "."));
			Yi=Double.parseDouble(L3.format(Y1).replace(",", "."));
			implin = "X" + Xi + "Y" + Yi;//imprimo la pocicion P2
			WriteLineNo(implin);
			}
			
			Xi=Double.parseDouble(L3.format(X1).replace(",", "."));
			implin = "X" + Xi;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			Yi=Double.parseDouble(L3.format(Y1 + Ancho).replace(",", "."));
			implin = "G01 Y" + Yi;//imprimo la pocicion P1
			WriteLineNo(implin);
			
			Xi=Double.parseDouble(L3.format(X1 + Largo).replace(",", "."));
			implin = "G01 X" + Xi;//imprimo la pocicion P2
			WriteLineNo(implin);
		
			Yi=Double.parseDouble(L3.format(Y1).replace(",", "."));
			implin = "G01 Y" + Yi;//imprimo la pocicion P1
			WriteLineNo(implin);
			
			Xi=Double.parseDouble(L3.format(X1 + Largo - (OrtogonalRadius + RTool) - DTool).replace(",", "."));
			implin = "G01 X" + Xi;//imprimo la pocicion P2
			WriteLineNo(implin);
			
			
		}//fin cuadrantes de acabamiento 2
		
		
		
		public static void WriteLineNo(String lin) {
			
			System.out.println("N" + LineNo + " " + lin.replaceAll(",", "."));
			LineNo += 1;
			
		}
		
		public void ImprimirPlaSeg() {
		
			implin = "G00 F150 Z" + PlanSeg;//imprimo la pocicion del hueco
			WriteLineNo(implin);	
			
		}
		
		static public void CorrecionDeParidad(double Xini, double Yini) {
			implin = "G00 X" + Xini + " Y" + Yini;//imprimo la pocicion P2
			WriteLineNo(implin);
		}

}
