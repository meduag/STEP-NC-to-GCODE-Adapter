package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;

//import java.text.DecimalFormat;
//import GerarCodG.FuroSimples;

public class FuroSegmentado {
	
	public double CentroX;
	public double CentroY;
	public double DiaTool;
	public double DiaFuro;
	public double FeedRate;
	public double ProfPorPaso;
	public double ProfTotalFuro;
	public double SecPlan;
	public double XRadBase=0;
	
	public static String Ferramenta="T1";//plano de seguranza
	
	
	public static int LineNo= 10;//contador de linea sucesiva
	static int Inicio=1;//para imprimir la primera linea
	static String implin="";//imrpimir linea
	
	static int inicio=1,ini1=1,ini2=1,ini3=1;
	
	/*Nuevas variables*/
	private static double TempFor_i_Npasos = 0;
	
	public void FSegmentadoNc(){
		
		DecimalFormat df = new DecimalFormat("0.0000"); 

//		CentroX=30;
//		CentroY=30;
//		DiaTool=3;
//		DiaFuro=21;
		FeedRate=br.UNB.LAB.GerarCodG.GeneradorCodidoG.FeedRate;
//		ProfPorPaso=0.5;
//		ProfTotalFuro=5;
//		SecPlan=10;
		
		

	if(inicio==1){
		implin = "G00 G40 F" + FeedRate +" Z" + SecPlan;
		WriteLineNo(implin);
		
		//para troca de ferramenta
//		implin = Ferramenta;
//		WriteLineNo(implin);
		TrocaDeFerramenta.LineNo=LineNo;
		TrocaDeFerramenta.ImpriFer(Ferramenta);	
		
		implin = "G01 F"+ FeedRate +" X" + CentroX + " Y" + CentroY;
		WriteLineNo(implin);
		inicio=0;
	}
	
//	implin = "X" + CentroX + " Y" + CentroY;
//	WriteLineNo(implin);
	
	double RadTool=0;
	RadTool=DiaTool/2;
	
	double Yn=0,YnMed1=0,YnMed2=0,Xf=0,Yf=0;
	double SigV=0;

	//double J=0;
	int salir=0;
	
	
//	N760 G00 F150.0 Z55.0
//	N770 X30.0 Y30.0
//	N780 G01 Z-10.0000
//	N790 G01 Y32.5000
//	N800 G03 Y32.5000 I0 J-2.5000
	
	//profundidad 	
//	implin = "G01 Z-" + df.format(ProfPorPaso);
//	WriteLineNo(implin);
	
	implin = "G01 Z-" + df.format(ProfPorPaso);
	WriteLineNo(implin);
	
	if(ini2==1){
		/** G03 X60 Y23 R1.5 **/
		if((CentroY + DiaTool)>(DiaFuro/2)){
			implin = "G41 D" + TrocaDeFerramenta.numeroD;
			WriteLineNo(implin);
			ini2=0;
		}else{
		
		implin = "G03 X"+ CentroX + " Y"+ (CentroY + RadTool) + " R" + (RadTool/2);
		WriteLineNo(implin);	
			
		implin = "G41 D" + TrocaDeFerramenta.numeroD;
		WriteLineNo(implin);
		ini2=0;
		}
	}
	
	//warning por compensacion
	if(FuroSimples.DiaTool<DiaTool){
		FuroSimples.DiaTool = DiaTool;
	}
	

	//diametro de la herramienta es menot que la del furo
	if(DiaTool <=DiaFuro ){
		
		do{
			Yn=CentroY+(FuroSimples.DiaTool/2)+RadTool+SigV+1;
			
			if(Yn>((CentroY+DiaFuro/2)-(RadTool/2))){
				//System.out.println("mayor");
				salir=1;
			}else {
				
				implin = "G01 Y" + Yn;
				WriteLineNo(implin);
				
				YnMed1=(Yn-CentroY);
				YnMed2=Yn - (YnMed1*2);
				implin = "G03 Y" + YnMed2 + " R" + YnMed1;
				WriteLineNo(implin);
				
				implin = "G03 Y" + Yn + " R" + YnMed1;
				WriteLineNo(implin);
				
				
				SigV += DiaTool*0.75;
				}
		
			}while(salir==0);
		
	//System.out.println();
		if(salir==1){
			//System.out.println();
			if(SigV==0){
				
				if(ini3==1){
					
					/**
					N114 G03 X55 Y20.0000 R5.0000
					N114 G03 X60 Y15.0000 R5.0000
					N114 G03 X60 Y25.0000 R5.0000
					 **/
					
				//para el exterior
					YnMed1=DiaFuro/2;
					Xf = (YnMed1 * Math.cos(Math.toRadians(160)))+CentroX;
					Yf = (YnMed1 * Math.sin(Math.toRadians(160)))+CentroY;
					implin = "G03 X"+ df.format(Xf) +" Y" + df.format(Yf) + " R" + df.format(YnMed1);
					WriteLineNo(implin);	
					
					Xf = (YnMed1 * Math.cos(Math.toRadians(180)))+CentroX;
					Yf = (YnMed1 * Math.sin(Math.toRadians(180)))+CentroY;
					implin = "G03 X"+ df.format(Xf) +" Y" + df.format(Yf) + " R" + df.format(YnMed1);
					WriteLineNo(implin);	
					
					
//				Yn=(CentroY+DiaFuro/2);
//				implin = "G01 Y" + df.format(Yn);
//				WriteLineNo(implin);
				Yn=(CentroY+DiaFuro/2);
				YnMed1=DiaFuro/2;
				YnMed2=CentroY - YnMed1;
				implin = "G03 X"+CentroX +" Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
				WriteLineNo(implin);
				
				implin = "G03 Y" + df.format(Yn) + " R" + df.format(YnMed1);
				WriteLineNo(implin);
				
				Xf = (YnMed1 * Math.cos(Math.toRadians(135)))+CentroX;
				Yf = (YnMed1 * Math.sin(Math.toRadians(135)))+CentroY;
				
				implin = "G03 X"+ df.format(Xf) +" Y" + df.format(Yf) + " R" + df.format(YnMed1);
				WriteLineNo(implin);
				ini3=0;
				}else{
					//hace lo mismo pero con un deplazamiento diferente
					
					YnMed1=DiaFuro/2;
					double CentroX2=CentroX-YnMed1;
					implin = "G03 X"+ df.format(CentroX2) +" Y" + df.format(CentroY) + " R" + df.format(YnMed1);
					//implin = "G03 Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					Yn=(CentroY+DiaFuro/2);
					YnMed1=(Yn-CentroY);
					YnMed2=Yn - (YnMed1*2);
					implin = "G03 X"+ df.format(CentroX) +" Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					//implin = "G03 Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					Xf = (YnMed1 * Math.cos(Math.toRadians(65)))+CentroX;
					Yf = (YnMed1 * Math.sin(Math.toRadians(65)))+CentroY;
					
					implin = "G03 X"+ df.format(Xf) +" Y" + df.format(Yf) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
//					implin = "G03 Y" + df.format(Yn) + " R" + df.format(YnMed1);
//					WriteLineNo(implin);
					
					Xf = (YnMed1 * Math.cos(Math.toRadians(90)))+CentroX;
					Yf = (YnMed1 * Math.sin(Math.toRadians(90)))+CentroY;
					
					implin = "G03 X"+ df.format(Xf) +" Y" + df.format(Yf) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					
				}
				
				
				
			}else{
					//para el exterior
					YnMed1=DiaFuro/2;
					double CentroX2=CentroX-YnMed1;
					implin = "G03 X"+ df.format(CentroX2) +" Y" + df.format(CentroY) + " R" + df.format(YnMed1);
					//implin = "G03 Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					
					YnMed2=CentroY-(DiaFuro/2);
					implin = "G03 X"+ df.format(CentroX) +" Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					Yn=CentroY+(DiaFuro/2);
					implin = "G03 Y" + df.format(Yn) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					Xf = (YnMed1 * Math.cos(Math.toRadians(180)))+CentroX;
					Yf = (YnMed1 * Math.sin(Math.toRadians(180)))+CentroY;
					
					implin = "G03 X"+ df.format(Xf) +" Y" + df.format(Yf) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					implin = "G01 X"+ df.format(CentroX) +" Y" + df.format(CentroY);
					WriteLineNo(implin);
					
			}
		}

	}
	
}//fin main
	
	//metodo 2 para la creacion del furo
		public static void ImpGcodeFuroSegmentado_F2(double CentroX, double CentroY, double DiaIniFuro, double ProfTotalDoFuro, String ToolData, String ImprimirHeader, double ProfPasso) {
			//para darle el formato decimal
			LineNo=GeneradorCodidoG.LineNo;
			DecimalFormat df = new DecimalFormat("0.000"); 
			
			if(ImprimirHeader.indexOf("Sim_Imprimir_cabecera")!=-1){
				/*** 1ro ir al plano de segurança  **/
				implin = "G40 G00 Z" + br.UNB.LAB.InfBasicas.PlanoSeguranca.ComprimentoPlanoSegDouble;
				WriteLineNo(implin);
				
				/*** 2do imprimir troca de Tool  **/
				TrocaDeFerramenta.LineNo=LineNo;
				TrocaDeFerramenta.ImpriFer(ToolData);
				
				/** obtener **/
				//double DiaTool = TrocaDeFerramenta.Diametro_Tool;
				
				//*** 2do imprimir compensacion de ferramenta, plano de segurança e primer corte profCorte **//*
				LineNo = br.UNB.LAB.FAcessorias.EncabezadoGcodeCompenTool.ImprimirGcodeHeaderCON_G41(CentroX + 0.1, CentroY, LineNo);	
				//double ProfCorte = GeneradorCodidoG.ProfundidadeCorte;
				
				//acelero porque el valor de la prof de corte es pequenho, portanto la velocidad de corte aumenta en relacion de la mitad de la velocidad mavima de avance de la maquina
				implin = "F" + df.format(GeneradorCodidoG.MaxVelAvanceMaquina/2);
				WriteLineNo(implin);
				}
			
			// cuadrante 1 (x- , y)
			implin = "G03 X"+ df.format(CentroX-(DiaIniFuro/2)) +" Y" + df.format(CentroY) + " Z-" + df.format(ProfPasso) +  " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			// cuadrante 1 (x , y-)
			implin = "G03 X"+ df.format(CentroX) +" Y" + df.format(CentroY-(DiaIniFuro/2)) + " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			// cuadrante 1 (x+ , y)
			implin = "G03 X"+ df.format(CentroX+(DiaIniFuro/2)) +" Y" + df.format(CentroY) + " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			//el punto de conexion
			double TempCentroX = (DiaIniFuro/2)*Math.cos(Math.toRadians(45));
			CentroX = CentroX - TempCentroX;
			double TempCentroY = (DiaIniFuro/2)*Math.sin(Math.toRadians(45));
			CentroY = CentroY + TempCentroY;
			//System.out.println("X= "+ df.format(CentroX) +" Y= "+ df.format(CentroY));

			implin = "G03 X"+ df.format(CentroX) +" Y" + df.format(CentroY) + " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			GeneradorCodidoG.LineNo=LineNo;
		}





public static void WriteLineNo(String lin) {
		
		System.out.println("N" + LineNo + " " + lin.replace(",", "."));
		LineNo += 1;
		
	}
	
}
