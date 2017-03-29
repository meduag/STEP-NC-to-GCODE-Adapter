package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;

//import java.text.DecimalFormat;

public class RectPocketSimples {

	//variables necesarias
	public double X1=0, Y1=0;
	public double Largo=0, Ancho=0, DTool=0, Prof=0;
	
	public static double PlanSeg=0,FeedRate=100;
	
	double X2=0, Y2=0, Xi, Xip, Yip, Yi, Xf, Xfp, Yf, Yfp, Xp=0, Yp=0, RTool=0;

	public String Ori="Hor",dadosFer;
	
	static String implin="";
	static int LineNo; 
	static public boolean IniA = true, IniB = true, sal=false , bor=true, IniFer=true;
	

	//public static void main(String[] args) {
		public void RanPocketSimplesNc() {
			
			DecimalFormat L3 = new DecimalFormat("0.0000"); 
		
			FeedRate=br.UNB.LAB.GerarCodG.GeneradorCodidoG.FeedRate;
			RTool=DTool/2;
			
			//se imprime solamente una vez el inicio planode seguranza
			if(IniA==true){
			//imprimir antes de cualquier cosa
			LineNo=GeneradorCodidoG.LineNo;
			ImprimirPlaSeg();
			TrocaDeFerramenta.LineNo=LineNo;
			TrocaDeFerramenta.ImpriFer(dadosFer);
			}
			
			//reinicia valores iniciales
			Xp=0;
			Yp=0;
			sal=false;
			
			if(Ori.startsWith("Hor")){
				
				/************************************************************************** Horizontal ***************************************************************************************/
				
				//System.out.println("(Ranhura Horizontal)");
				//limites superiores de la ranura
				X2=X1+Largo;
				Y2=Y1+Ancho;
				
				//Inicio en el punto medio
				Xi=(Ancho/2) + X1;
				Yi=(Ancho/2) + Y1;
				
				Xip=Xi;//punto medio inicial de referencia en X
				Yip=Yi;//punto medio inicial de referencia en Y
				
				//Final del punto medio tomando en cuenta que no hay limite visible en el X
				Xf=Largo-Xi+RTool;
				Yf=Yi;
				
				Xfp=Xf;//punto temp medio final de referencia en X
				Yfp=Yf;//punto temp medio final de referencia en X
				
				if(IniA==true){
					//imprimir solo una vez tambien
					implin = "G00 G40 F"+ FeedRate + " X" + L3.format(Xi) + " Y" + L3.format(Yi);//imprimo la pocicion del hueco
					WriteLineNo(implin);
					
					implin = "Z0.0";//imprimo la pocicion del hueco
					WriteLineNo(implin);
				}

				if(Ancho<DTool){
//					double temmostrar = (Y1+Ancho)-Y1;
//					System.out.println(temmostrar);
//					System.out.println("Es menor");
					CodeG_Para__Slot_E_Operaciones.SalExt=true;
				}else{
				implin = "G01 F"+(FeedRate*.75)+" Z-" + L3.format(Prof);//imprimo la pocicion del hueco
				WriteLineNo(implin);	
				}
				



				
				/*** aqui primero pregunto si el doble de la herramienta ES MAYOR que el valor del ancho de la ranhura 
				 este es un caso perticular en horizontal ***/
				if((DTool*2)>Ancho){
					/********************************************************************************************** - Caso 1 Hor -************************************************************************************************************/
					if(IniA==true){
						implin = "X" + L3.format(Xf) + " Y" + L3.format(Yf);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						Yp=Yf;
						}
					
					do{
						if(IniB==true){
							//para la primera compensacion
							implin = "X" + L3.format(X1+Largo+RTool);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la correcion segun la herramienta
							WriteLineNo(implin);
							IniB=false;
						}
						
						if(Ancho<DTool){
//							double temmostrar = (Y1+Ancho)-Y1;
//							System.out.println(temmostrar);
//							System.out.println("Es menor");
							CodeG_Para__Slot_E_Operaciones.SalExt=true;
						}else{
							implin = "F"+FeedRate;//imprimo la pocicion del hueco
							WriteLineNo(implin);
						
							implin = "Y" + L3.format(Y1);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin = "X" + L3.format(X1-RTool);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin = "Y" + L3.format(Y1+Ancho);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin = "X" + L3.format(X1+Largo+RTool);//imprimo la pocicion del hueco
							WriteLineNo(implin);
						
						}

						//Para salir e imprimir el resto
						sal=true;
						IniA=false;
						
						}while(sal==false);//fin del do/while ranura hor

					}else{
						
						/********************************************************************************************** - Caso 2 Hor -************************************************************************************************************/
						/**aqui van los puntos p1,p2,p3,p4 e p5 
						se colocan todos los puntos del recorrido de la herramienta en forma de espiral cuadrada */
						
						implin = "X" + L3.format(Xf) + " Y" + L3.format(Yf);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						Yp=Yf;
						
						
						do{
							//puntos temporales de las pociciones finales para usarlos despues
							Xp=Xf;
							Yp=Yp+RTool;
							
							//para saber los limite
							if(Yp>=Y2){//del limite sup max en Y
								//System.out.println("(Limite ultrapsado pela ferramenta)");
								//se sale del ciclo
								sal=true;
								}else{
									/** ahora si los puntos de la espiral cuadrada para horizontal */
									//punto 1
									Yf=Yi - RTool;
									if(Yf > Y1){//comprubo el limite inferior "seguridad 2"
										implin ="F"+(FeedRate*2)+"Y"+L3.format(Yf);//imprimo P2
										WriteLineNo(implin);
										Yi=Yf;
										}else{
											sal=true;
											}
									
									Xf=Xi - RTool;
									implin ="X"+L3.format(Xf);//imprimo P3
									WriteLineNo(implin);
									Xi=Xf;
									
									Yf=Yp;
									implin ="Y"+L3.format(Yf);//imprimo P4 seg-1
									WriteLineNo(implin);
									Yp=Yf;
																
									Xf=Xp + RTool;
									implin ="X"+L3.format(Xf);//imprimo la pocicion del hueco
									WriteLineNo(implin);
									Xp=Xf;
						
									if((Yi-RTool)<(Y1+RTool)){//para comprobar los limites "seguridad 3"
										if((X1+Largo)!=Xf){
											implin ="X"+L3.format(X1+Largo);//imprimo la pocicion del hueco
											WriteLineNo(implin);
										}
										sal=true;
										bor=true;//para hacer el borde
										}
									}//fin de los puntos
							IniA=false;

				}while(sal==false);//fin del do/while ranura hor
						
					/*** termino con el recorrido del bordeborde ****/
						implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la pocicion del hueco
						WriteLineNo(implin);
						IniFer=false;

						
						implin ="X"+L3.format(X1+Largo+RTool+1)+"  Y"+L3.format(Y1);//imprimo la pocicion del hueco
						WriteLineNo(implin);

						//esq inf izq
						implin ="X"+(X1-RTool);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						
						//esq sup izq
						implin ="Y"+L3.format(Y1+Ancho);//imprimo la pocicion del hueco
						WriteLineNo(implin);

						//esq sup der
						implin ="X"+L3.format(X1+Largo);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						
						implin = "G00 G40 X" + L3.format(Xip) + " Y" + L3.format(Yip);//imprimo la pocicion del hueco
						WriteLineNo(implin);
					
				
			}//fin cuando el doble de la herramienta ES MENOR al ancho de la ranura
				
				
				
				
				
				
				
			}else{
				
				/************************************************************************** Vertical ***************************************************************************************/
			
				//System.out.println("Vertical");
				//limites superiores de la ranura
				X2=X1+Ancho;
				Y2=Y1+Largo;
				
				//Inicio en el punto medio
				Xi=Ancho/2 + X1;
				Yi=Ancho/2 + Y1;
				
				Xip=Xi;//punto medio inicial de referencia en X
				Yip=Yi;//punto medio inicial de referencia en Y
				
				//Final del punto medio tomando en cuenta que no hay limite visible en el Y
				Xf=Xi;
				Yf=Largo-Yi+RTool;
				
				Xfp=Xf;//punto temp medio final de referencia en X
				Yfp=Yf;//punto temp medio final de referencia en Y
				
				if(IniA==true){
					//imprimir solo una vez tambien
					implin = "G01 G40 F"+ FeedRate + " X" + L3.format(Xi) + " Y" + L3.format(Yi);//imprimo la pocicion del hueco
					WriteLineNo(implin);
					
					implin = "Z0.0";//imprimo la pocicion del hueco
					WriteLineNo(implin);
				}

				if(Ancho<DTool){
//					double temmostrar = (Y1+Ancho)-Y1;
//					System.out.println(temmostrar);
//					System.out.println("Es menor");
					CodeG_Para__Slot_E_Operaciones.SalExt=true;
				}else{
					implin = "G01 F"+(FeedRate*.75)+" Z-" + L3.format(Prof);//imprimo la pocicion del hueco
					WriteLineNo(implin);	
				}
				
				


				
				/*** aqui primero pregunto si el doble de la herramienta ES MAYOR que el valor del ancho de la ranhura 
				 este es un caso perticular en horizontal ***/
				if((DTool*2)>Ancho){
					/********************************************************************************************** - Caso 1 Ver -************************************************************************************************************/
					if(IniA==true){
						implin = "X" + L3.format(Xf) + " Y" + L3.format(Yf);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						Yp=Yf;
						}
					
					do{
						if(IniB==true){
							//para la primera compensacion
							implin = "Y" + L3.format(Y1+Largo+RTool);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la correcion segun la herramienta
							WriteLineNo(implin);
							IniB=false;
						}

						if(Ancho<DTool){
//							double temmostrar = (Y1+Ancho)-Y1;
//							System.out.println(temmostrar);
//							System.out.println("Es menor");
							CodeG_Para__Slot_E_Operaciones.SalExt=true;
						}else{
							implin = "F"+FeedRate;//imprimo la pocicion del hueco
							WriteLineNo(implin);
						
							implin = "X" + L3.format(X1+Ancho);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin = "Y" + L3.format(Y1-RTool);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin = "X" + L3.format(X1);//imprimo la pocicion del hueco
							WriteLineNo(implin);
							
							implin = "Y" + L3.format(Y1+Largo+RTool);//imprimo la pocicion del hueco
							WriteLineNo(implin);
						}

						//Para salir e imprimir el resto
						sal=true;
						IniA=false;
						
						}while(sal==false);//fin del do/while ranura hor

					}else{
						
						/********************************************************************************************** - Caso 2 Ver -************************************************************************************************************/
						/**aqui van los puntos p1,p2,p3,p4 e p5 
						se colocan todos los puntos del recorrido de la herramienta en forma de espiral cuadrada */
						
						implin = "X" + L3.format(Xf) + " Y" + L3.format(Yf);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						Xp=Xf;
						
						
						do{
							//puntos temporales de las pociciones finales para usarlos despues
							Xp=Xp-RTool;
							Yp=Yf;
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// arreglar
							//para saber los limite
							if(Xp<=X1){//del limite sup mim en X
								//System.out.println("(Limite ultrapsado pela ferramenta)");
								//se sale del ciclo
								sal=true;
								}else{
									/** ahora si los puntos de la espiral cuadrada para horizontal */
									//punto 1
									Xf=Xi + RTool;
									if(Xf < (X1+Ancho)){//comprubo el limite inferior "seguridad 2"
										implin ="X"+L3.format(Xf);//imprimo P2
										WriteLineNo(implin);
										Xi=Xf;
										}else{
											sal=true;
											}
									
									Yf=Yi - RTool;
									implin ="Y"+L3.format(Yf);//imprimo P3
									WriteLineNo(implin);
									Yi=Yf;
									
									Xf=Xp;
									implin ="X"+L3.format(Xf);//imprimo P4 seg-1
									WriteLineNo(implin);
																									
									Yf=Yp + RTool;
									implin ="Y"+L3.format(Yf);//imprimo la pocicion del hueco
									WriteLineNo(implin);
									Yp=Yf;
						
									if((Xi+RTool)>(X1+Ancho-RTool)){//para comprobar los limites "seguridad 3"
										if((Y1+Largo)!=Yf){
											implin ="Y"+L3.format(Y1+Largo);//imprimo la pocicion del hueco
											WriteLineNo(implin);
										}
										sal=true;
										bor=true;//para hacer el borde
										}
									}//fin de los puntos
							IniA=false;

				}while(sal==false);//fin del do/while ranura hor
						
					/*** termino con el recorrido del bordeborde ****/
						implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la pocicion del hueco
						WriteLineNo(implin);
						IniFer=false;
						
						implin ="X"+L3.format(X1+Ancho)+"  Y"+L3.format(Y1+Largo+RTool+1);//imprimo la pocicion del hueco
						WriteLineNo(implin);

						//esq inf izq
						implin ="Y"+(Y1-RTool);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						
						//esq sup izq
						implin ="X"+L3.format(X1);//imprimo la pocicion del hueco
						WriteLineNo(implin);

						//esq sup der
						implin ="Y"+L3.format(Y1+Largo);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						
						implin = "G00 G40 X" + L3.format(Xip) + " Y" + L3.format(Yip);//imprimo la pocicion del hueco
						WriteLineNo(implin);
					
				
			}//fin cuando el doble de la herramienta ES MENOR al ancho de la ranura
				
				
				
				
			}//fin vertical
			
			//secuencia de la linea de conteo de comandos
			GeneradorCodidoG.LineNo=LineNo;
	
	}
		
		
		public static void WriteLineNo(String lin) {
			
			System.out.println("N" + LineNo + " " + lin.replaceAll(",", "."));
			LineNo += 1;
			
		}
		
		public static void ImprimirPlaSeg() {
		
			implin = "G00 F"+ FeedRate +" Z" + PlanSeg;//imprimo la pocicion del hueco
			WriteLineNo(implin);	
			
		}
		
		//para hacer el contorno de la feature con la ferramenta 2
		public void ContornoRanPocketSimplesNc() {
			DecimalFormat L3 = new DecimalFormat("0.0000");
			
			FeedRate=br.UNB.LAB.GerarCodG.GeneradorCodidoG.FeedRate;
			RTool=DTool/2;
			
			//se imprime solamente una vez el inicio planode seguranza
			if(IniA==true){
			//imprimir antes de cualquier cosa
			LineNo=GeneradorCodidoG.LineNo;
			ImprimirPlaSeg();
			TrocaDeFerramenta.LineNo=LineNo;
			TrocaDeFerramenta.ImpriFer(dadosFer);
			}
			
			//reinicia valores iniciales
			Xp=0;
			Yp=0;
			sal=false;
			
			if(Ori.startsWith("Hor")){
				/************************************************************************** Horizontal ***************************************************************************************/
				
				//System.out.println("(Ranhura Horizontal)");
				//limites superiores de la ranura
				X2=X1+Largo;
				Y2=Y1+Ancho;
				
				//Inicio en el punto medio
				Xi=(Ancho/2) + X1;
				Yi=(Ancho/2) + Y1;
				
				Xip=Xi;//punto medio inicial de referencia en X
				Yip=Yi;//punto medio inicial de referencia en Y
				
				//Final del punto medio tomando en cuenta que no hay limite visible en el X
				Xf=Largo-Xi+RTool;
				Yf=Yi;
				
				Xfp=Xf;//punto temp medio final de referencia en X
				Yfp=Yf;//punto temp medio final de referencia en X
				
				if(IniA==true){
					//imprimir solo una vez tambien
					implin = "G00 G40 X" + L3.format(Xi) + " Y" + L3.format(Yi);//imprimo la pocicion del hueco
					WriteLineNo(implin);
					
					implin = "Z-"+(CodeG_Para__Slot_E_Operaciones.ProfTem-CodeG_Para__Slot_E_Operaciones.AngBase);//imprimo la pocicion del hueco
					WriteLineNo(implin);
					
				}

				implin = "G01 F"+(FeedRate*.75)+" Z-" + L3.format(Prof);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				if(IniA==true){
					//imprimir solo una vez tambien
					implin = "G01 F"+ FeedRate + " X" + L3.format(Xf) + " Y" + L3.format(Yf);//imprimo la pocicion del hueco
					WriteLineNo(implin);
				
					IniA=false;
				}
				
				implin ="X"+L3.format(X1+Largo+RTool+1);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				implin ="Y"+L3.format(Y1);//imprimo la pocicion del hueco
				WriteLineNo(implin);

				//esq inf izq
				implin ="X"+(X1-RTool);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				//esq sup izq
				implin ="Y"+L3.format(Y1+Ancho);//imprimo la pocicion del hueco
				WriteLineNo(implin);

				//esq sup der
				implin ="X"+L3.format(X1+Largo);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				implin = "G00 G40 X" + L3.format(Xip) + " Y" + L3.format(Yip);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				
			}else{
				/************************************************************************** Vertical ***************************************************************************************/
				//System.out.println("Vertical");
				//limites superiores de la ranura
				X2=X1+Ancho;
				Y2=Y1+Largo;
				
				//Inicio en el punto medio
				Xi=Ancho/2 + X1;
				Yi=Ancho/2 + Y1;
				
				Xip=Xi;//punto medio inicial de referencia en X
				Yip=Yi;//punto medio inicial de referencia en Y
				
				//Final del punto medio tomando en cuenta que no hay limite visible en el Y
				Xf=Xi;
				Yf=Largo-Yi+RTool;
				
				Xfp=Xf;//punto temp medio final de referencia en X
				Yfp=Yf;//punto temp medio final de referencia en Y
				
				if(IniA==true){
					//imprimir solo una vez tambien
					implin = "G01 G40 F"+ FeedRate + " X" + L3.format(Xi) + " Y" + L3.format(Yi);//imprimo la pocicion del hueco
					WriteLineNo(implin);
					
					implin = "Z0.0";//imprimo la pocicion del hueco
					WriteLineNo(implin);
				}

				implin = "G01 F"+(FeedRate*.75)+" Z-" + L3.format(Prof);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				if(IniA==true){
					//imprimir solo una vez tambien
					implin = "G01 F"+ FeedRate + " X" + L3.format(Xf) + " Y" + L3.format(Yf);//imprimo la pocicion del hueco
					WriteLineNo(implin);
				
					IniA=false;
				}
				
				implin ="Y"+L3.format(Y1+Largo+RTool+1);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				implin ="G42 D" + TrocaDeFerramenta.numeroD;//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				implin ="X"+L3.format(X1+Ancho);//imprimo la pocicion del hueco
				WriteLineNo(implin);

				//esq inf izq
				implin ="Y"+(Y1-RTool);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				//esq sup izq
				implin ="X"+L3.format(X1);//imprimo la pocicion del hueco
				WriteLineNo(implin);

				//esq sup der
				implin ="Y"+L3.format(Y1+Largo);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
				implin = "G00 G40 X" + L3.format(Xip) + " Y" + L3.format(Yip);//imprimo la pocicion del hueco
				WriteLineNo(implin);
				
			}
			
			
			
			
		}

}
