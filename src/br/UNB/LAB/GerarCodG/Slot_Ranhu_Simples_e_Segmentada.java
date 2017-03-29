package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;

import br.UNB.LAB.Integrador.Integrador;


public class Slot_Ranhu_Simples_e_Segmentada {
	
	static int LineNo=0,Npasos=0;; 
	public static double PlanSeg=0,FeedRate=100,restante=0;
	static String implin="";
	static double X1p=0,Y1p=0,X2p=0,Y2p=0;
	static double Xmi=0,Ymi=0,Xmf=0,Ymf=0;
	static double Xi=0,Yi=0,Xf=0,Yf=0;
	static boolean correcionY=false, profileV=false;
	static boolean dirFinalX_G41true_G42false=true;
	static boolean CorrecionPerfil=true;
	

	

	//para imprimir la troca de herramienta ---------------------------------------------------------------------------------------------
	public static void SlotSimples(double X1, double Y1, double Ancho,double Largo,String Ori,boolean ImpTrocaFer,double DiaFer, double Profundidade, double OffsetProfundidade, String FerramentaDaLista,boolean ImpPlaSeg, boolean ImpPosIni_e_Z0){
		FeedRate=br.UNB.LAB.GerarCodG.Gcode.FeedRate;
		double RTool = DiaFer/2;
		
		//se imprime solamente una vez el inicio planode seguranza
		if(ImpTrocaFer==true){
			//imprimir antes de cualquier cosa
			LineNo=Gcode.LineNo;
			ImprimirPlaSeg();
			TrocaDeFerramenta.LineNo=LineNo;
			TrocaDeFerramenta.ImpriFer(FerramentaDaLista);
			//LineNo= FuroSimples.LineNo;
		}
		
		double inc=OffsetProfundidade + 1;
		
		
		if(Ori.startsWith("Hor")){/******** Feature Horizontal**********/
			/** Posciciones iniciales y finales **/
			//original
//			X1p = X1-RTool; 			Y1p = Y1+RTool;
//			X2p = X1+Largo+RTool; 			//Y2p = Y1p; //es el mismo valor
			
			//teste
			X1p = X1; 			Y1p = Y1+Ancho/2;
			X2p = X1+Largo; 			//Y2p = Y1p; //es el mismo valor
			
			
			implin = "G40 G00 F"+FeedRate + " X" + X1p + " Y" + Y1p;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			
			if(ImpPosIni_e_Z0==true){
				implin = "Z0.0";//imprimo la pocicion del hueco
				WriteLineNo(implin);
			}
			
			boolean sentido=false;
			do{
				implin = "G01 F"+FeedRate*0.75  +" Z-"+inc;//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);
				
				if(sentido==false){
					implin = "F"+FeedRate + " X" + X2p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=true;
				}else{
					implin = "F"+FeedRate + " X" + X1p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=false;
				}
			
				inc++;
			}while(inc<=Profundidade);
			/** Aqui va cuando la profundidad es decimal **/
			
			if((inc-1)==Profundidade){

			}else if(inc>Profundidade){
				implin = "F"+FeedRate*0.75  +" Z-"+Profundidade;//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);
				
				if(sentido==false){
					implin = "F"+FeedRate + " X" + X2p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=true;
				}else{
					implin = "F"+FeedRate + " X" + X1p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=false;
				}
			}
			//fin horizontal
			
			
			
		}else{        			/******** Feature Vertical**********/
			/** Posciciones iniciales y finales **/
			//original
//			X1p = X1+RTool; 		Y1p = Y1-RTool;
//			/**X2p = X1p;**/ 		Y2p = Y1p+Largo+RTool; //es el mismo valor
			
			//teste
			X1p = X1+RTool; 		Y1p = Y1;
			/**X2p = X1p;**/ 		Y2p = Y1p+Largo; //es el mismo valor
			
			if(ImpPosIni_e_Z0==true){
				implin = "G40 G00 F"+FeedRate + " X" + X1p + " Y" + Y1p;//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				implin = "Z0.0";//imprimo la pocicion del hueco
				WriteLineNo(implin);
			}
			
			boolean sentido=false;
			do{
				implin = "G01 F"+FeedRate*0.75  +" Z-"+inc;//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);
				
				if(sentido==false){
					implin = "G01 F"+FeedRate + " Y" + Y2p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=true;
				}else{
					implin = "G01 F"+FeedRate + " Y" + Y1p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=false;
				}
			
				inc++;
			}while(inc<=Profundidade);
			/** Aqui va cuando la profundidad es decimal **/
			
			if(inc==Profundidade){

			}else if(inc>Profundidade){
				implin = "F"+FeedRate*0.75  +" Z-"+Profundidade;//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);
				
				if(sentido==false){
					implin = "F"+FeedRate + " Y" + Y2p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=true;
				}else{
					implin = "F"+FeedRate + " Y" + Y1p;//imprimo la pocicion del hueco
					WriteLineNo(implin);
					sentido=false;
				}
			}
			
			//fin vertical
		}
		
		if(ImpPlaSeg==true){
			ImprimirPlaSeg();
		}
		
		
		br.UNB.LAB.GerarCodG.Gcode.LineNo=LineNo;
	}//fin pocketsimple para slot
	
	
	
	
	
	
	public static void Slot(double X1, double Y1, double Ancho, double Largo, double Profundidade, double ProfunOffset, double DiaFer, String Ori,boolean ImpTrocaFer,String FerramentaDaLista){
		FeedRate=br.UNB.LAB.GerarCodG.Gcode.FeedRate;
		//DecimalFormat L3 = new DecimalFormat("0.000"); 
		LineNo=br.UNB.LAB.GerarCodG.Gcode.LineNo;
		
		
		
		double RTool=DiaFer/2;
		
		if(Ori.startsWith("Hor")){/******** Feature Horizontal**********/
			//puntos de inicio en la mitad de la feature
			//inicio                                    //final
			Xmi=X1;                     	Xmf=X1+Largo;
			Ymi=Y1+(Ancho/2);               Ymf=Ymi;
			
			CaluloPasos(Y1,Ancho,Ymi,RTool);
			
			//se imprime solamente una vez el inicio planode seguranza
			if(ImpTrocaFer==true){
				//imprimir antes de cualquier cosa
				LineNo=Gcode.LineNo;
				ImprimirPlaSeg();
				TrocaDeFerramenta.LineNo=LineNo;
				TrocaDeFerramenta.ImpriFer(FerramentaDaLista);
				//LineNo= FuroSimples.LineNo;
			}
			
			implin = "G40 G00 F"+FeedRate + " X" + Xmi + " Y" + Ymi;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			
			if(ImpTrocaFer==true){
				implin = "Z0.0";//imprimo la pocicion del hueco
				WriteLineNo(implin);
			}
				
			
			double inc=ProfunOffset + 1;
			boolean IgualAncho=true;
			/** - Acabamiento 0 - **/
			
			if(Xmi+DiaFer-X1==Ancho){
				SlotSimples(X1, Y1, Ancho,Largo, Ori, false, DiaFer, Profundidade, 0, FerramentaDaLista,false,false);
				IgualAncho=false;
			}
			
			boolean Impg42=true;
			do{
				if(Integrador.ImpG40paraVericut==1){
					implin = "G40";
					WriteLineNo(implin);
				}
				
				implin = "G01 F"+FeedRate*0.75  +" Z-"+inc;//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);

				if(IgualAncho==true & Npasos!=0 & restante>0){
					Acabamiento0(DiaFer,false,IgualAncho);
					Impg42=true;
				}else{
					//colocar la conexion del valor inicial entre acabamento 0 y 1
					if(Impg42==true){
						implin = "X" + Xmf;//imprimo la pocicion del hueco
						WriteLineNo(implin);
					}//esta parte hace la conexion G40 a G42 cuando no se hace el acabamiento 0
				}
				
				if(Integrador.ImpG40paraVericut==1){
					Acabamiento1(X1-RTool, Y1, Ancho, Largo+DiaFer,DiaFer,true);
				}else{
					Acabamiento1(X1-RTool, Y1, Ancho, Largo+DiaFer,DiaFer,Impg42);
				}
				Impg42=false;
				
				inc++;
			}while(inc<=Profundidade);
			
			//para hacer lo que sobra
			if(Profundidade-(inc -1)>0){
				
				implin = "G01 F"+FeedRate*0.75  +" Z-"+Profundidade;//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);

				if(IgualAncho==true & Npasos!=0 & restante>0){
					Acabamiento0(DiaFer,false,IgualAncho);
					Impg42=true;
				}
				
				Acabamiento1(X1-RTool, Y1, Ancho, Largo+DiaFer,DiaFer,Impg42);
				Impg42=false;
			}
			
			
			
			

		}else{
			
			
		}
		br.UNB.LAB.GerarCodG.Gcode.LineNo=LineNo;
		
	}
	
	public static void Slot_Segmentado(double X1, double Y1, double Ancho, double Largo, double Profundidade, double DiaFer, String Ori,boolean ImpTrocaFer,String FerramentaDaLista,boolean ImpInicio, int OpAngBase0__OpPerfilV1_ImpZ00_con1,boolean NoImpSlotSimples){
		FeedRate=br.UNB.LAB.GerarCodG.Gcode.FeedRate;
		//DecimalFormat L3 = new DecimalFormat("0.000"); 
		LineNo=br.UNB.LAB.GerarCodG.Gcode.LineNo;
		
		
		
		double RTool=DiaFer/2;
		
		if(Ori.startsWith("Hor")){/******** Feature Horizontal**********/
			//puntos de inicio en la mitad de la feature
			//inicio                                    //final
			//Xmi=X1+(Ancho/2);                     	Xmf=X1+(Largo-(Ancho/2));
			Xmi=X1;                    		 		Xmf=X1+Largo;
			Ymi=Y1+(Ancho/2);                       Ymf=Ymi;
			
			CaluloPasos(Y1,Ancho,Ymi,RTool);
			
			//se imprime solamente una vez el inicio planode seguranza
			if(ImpTrocaFer==true){
				//imprimir antes de cualquier cosa
				LineNo=Gcode.LineNo;
				ImprimirPlaSeg();
				TrocaDeFerramenta.LineNo=LineNo;
				TrocaDeFerramenta.ImpriFer(FerramentaDaLista);
				//LineNo= FuroSimples.LineNo;
			}
			
			if(ImpInicio==true){
				implin = "G40 G00 F"+FeedRate + " X" + Xmi + " Y" + Ymi;//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				if(Xmi==0 | Xmi<=DiaFer){
					dirFinalX_G41true_G42false=true;					
				}else{
					dirFinalX_G41true_G42false=false;			
				}
				
				if(OpAngBase0__OpPerfilV1_ImpZ00_con1==1){					
					implin = "Z0.0";//imprimo la pocicion del hueco
					WriteLineNo(implin);
				}
			}
			
			boolean IgualAncho=true;
			/** - Acabamiento 0 - **/
			
			if(Npasos==0 | Npasos==1 & restante==0){
				IgualAncho=false;
			}
			
			
			
			
			if(Xmi+DiaFer-X1>=Ancho & NoImpSlotSimples==true){
				SlotSimples(X1, Ymi-RTool, Ancho,Largo, Ori, false, DiaFer, Profundidade, 0, FerramentaDaLista,false,false);
				IgualAncho=false;
			}
			
			boolean Impg42=true;
			
			if(correcionY==true){
				
				//colacar aqui el nuevo valor
				if(dirFinalX_G41true_G42false==true & CorrecionPerfil==true){
					implin = "G41";//imprimo la pocicion del hueco
					WriteLineNo(implin);
				}else if(CorrecionPerfil==true){
					implin = "G42";//imprimo la pocicion del hueco
					WriteLineNo(implin);
				}
				
				implin = "G01 "+ "X"+ (X1 +(Largo/2)-RTool-DiaFer)+" Y" + Y1;//imprimo la pocicion del hueco
				WriteLineNo(implin);
			}
				
			if(Integrador.ImpG40paraVericut==1){
				implin = "G40";
				WriteLineNo(implin);
			}
			
				implin = "G01 F"+FeedRate*0.75  +" Z-"+Profundidade;//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);
				correcionY=true;

				if(IgualAncho==true){
					Acabamiento0(DiaFer,false,IgualAncho);
					Impg42=true;
				}else{
					implin = "G40";//imprimo la pocicion del hueco
					WriteLineNo(implin);
					
					implin = "G01 F"+FeedRate + " X" + Xmf + " Y" + Ymf;//imprimo la pocicion del hueco
					WriteLineNo(implin);
				}
				
				
				Acabamiento1(X1-RTool, Y1, Ancho, Largo+DiaFer,DiaFer,Impg42);
				Impg42=false;
	
			
			
			

		}else{//PARA EL CODIGO EN VERTICAL
			
			
		}
		br.UNB.LAB.GerarCodG.Gcode.LineNo=LineNo;
		
	}
	
	
	public static void Acabamiento0(double DiaFer,boolean ImpTrayecFinal,boolean ImpIgualAnchoG40){
		
		int inc=1;
		double RTool = DiaFer/2;
		

			Xi = Xmf+RTool;
			Yi = Ymf-RTool;
			
			Xf=Xmi-RTool;
			Yf=Ymi+RTool;

		
		
		
		if(ImpIgualAnchoG40==true){
			implin = "G40";//imprimo la pocicion del hueco
			WriteLineNo(implin);
		}
		
		implin = "G01 F"+FeedRate + " X" + Xmf + " Y" + Ymf;//imprimo la pocicion del hueco
		WriteLineNo(implin);
		
		do{
			
			implin = "Y" + Yi;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			Yi=Yi-RTool;
			
			implin = "X" + Xmi;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			//Xf=Xf-RTool;
			
			implin = "Y" + Yf;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			Yf=Yf+RTool;
			
			//Xi=Xi+RTool;
			implin = "X" + Xmf;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			
			inc++;
//			if(profileV=true & inc==Npasos){
//				
//				
//			}
			
		}while(inc<=Npasos);
		
		if(ImpTrayecFinal==true){
			Yi=Yi+RTool;
			implin = "Y" + Yi;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			
			implin = "G00 F"+FeedRate + " X" + Xmi + " Y" + Ymi;//imprimo la pocicion del hueco
			WriteLineNo(implin);
		}else{
			implin = "Y" + Ymi;//imprimo la pocicion del hueco
			WriteLineNo(implin);
		}
		
	
		
		
	}
	
	
	public static void Acabamiento1(double X1, double Y1, double Ancho, double Largo,double DiaFer,boolean ImpG42){
		DecimalFormat L3 = new DecimalFormat("0.000"); 
		
		//int inc=1;
		double RTool = DiaFer/2;
		Xi = Xmf;
		Yi = Ymf-RTool;
		
		Xf=Xmi-RTool;
		Yf=Ymi+RTool;
		
		if(ImpG42==true){
			implin = "G42 D" + TrocaDeFerramenta.numeroD;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			
			//conector de G40 a G42
			implin = "G01 X"+ (X1 +(Largo/2)) +" Y" + L3.format(Y1);//imprimo la pocicion del hueco
			WriteLineNo(implin);
		}
		

		
		implin = "X" + X1;//imprimo la pocicion del hueco
		WriteLineNo(implin);
			
		implin = "Y" + L3.format(Y1+Ancho);//imprimo la pocicion del hueco
		WriteLineNo(implin);
			
		implin = "X" + (X1+Largo);//imprimo la pocicion del hueco
		WriteLineNo(implin);

		implin = "Y" + L3.format(Y1);//imprimo la pocicion del hueco
		WriteLineNo(implin);
			
		implin = "X"+ (X1 +(Largo/2)-RTool);//imprimo la pocicion del hueco
		WriteLineNo(implin);
	
		
	}
	
	
	
	/********************************************************************************** Funciones Independientes ***********************************************************************************************/
	public static void CaluloPasos(double Val00, double Ancho,double ValMedio,double RTool) {
		DecimalFormat L3 = new DecimalFormat("0.000"); 
		//X va con Largo
		//Y va con Ancho
		
		double temp = Val00 + Ancho; //posInicial + valor de la feature en ancho
		temp = temp - RTool;
		temp = temp - ValMedio;
		temp = temp / RTool;
		
		Npasos = (int) Math.floor(temp);
		restante = temp - Npasos;
		//este valor hay que multiplicarlo por el radio de la herramienta para dar el final
		restante = Double.parseDouble(L3.format(restante).replace(",", "."));
		
		//System.out.println("(Numero de pasos: "+Npasos+", paso restante: "+restante+")");
	}
	
	
	public static void WriteLineNo(String lin) {
		
		System.out.println("N" + LineNo + " " + lin.replaceAll(",", "."));
		LineNo += 1;
		
	}
	
	public static void ImprimirPlaSeg() {
		implin = "G00 Z" + PlanSeg;//imprimo la pocicion del hueco
		WriteLineNo(implin);	
	}
}
