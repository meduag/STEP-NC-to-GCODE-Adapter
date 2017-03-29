package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;

import javax.vecmath.Point3d;

public class FuroComplexo {

	
	public double CentroX;
	public double CentroY;
	public double DiaTool;
	public double DiaFuro;
	public double FeedRate;
	public double ProfPorPaso;
	public double ProfTotalFuro;
	public double SecPlan;
	
	public static String Ferramenta="T1";
	
	
	public static int LineNo= 0;//contador de linea sucesiva
	static int Inicio=1, numeroD=1,Ini2=1,Ini3=1,ImpSegBlok,Ini4=1;//para imprimir la primera linea
	static String implin="";//imrpimir linea
	
	private static double NpasadasEsp=0;
	private static double NpasosEnterosEsp = 0;
	private static double NpasosDecimalesEsp = 0;
	
	private static double NpasadasPC=0;
	private static double NpasosEnterosPC = 0;
	private static double NpasosDecimalesPC = 0;
	
	
	public void FComplexoNc(){
		
		DecimalFormat df = new DecimalFormat("0.0000"); 
		
//		CentroX=30;
//		CentroY=30;
//		DiaTool=3;
//		DiaFuro=21;
		double proSig=1;
		FeedRate=br.UNB.LAB.GerarCodG.GeneradorCodidoG.FeedRate;
		
		if(CodeG_Para__RoundHole_E_Operaciones.pasante==false){
			ProfPorPaso=1;
			//proSig=ProfPorPaso;
		}else{
			ProfPorPaso=(CodeG_Para__RoundHole_E_Operaciones.OffsetProfZ)+1;
			//proSig=ProfPorPaso;
		}
		
//		ProfTotalFuro=5;
//		SecPlan=10;
		

	
	implin = "G40 G00 F" + FeedRate*1.2 +" Z" + SecPlan;
	WriteLineNo(implin);
	
	//para troca de ferramenta
//	implin = Ferramenta;
//	WriteLineNo(implin);
	TrocaDeFerramenta.LineNo=LineNo;
	TrocaDeFerramenta.ImpriFer(Ferramenta);
	
	//imprimir la pocicion del huco y colocar la velocidad maxima de la maquina
	implin = "G01 F"+ GeneradorCodidoG.MaxVelAvanceMaquina +" X" + CentroX + " Y" + CentroY;
	WriteLineNo(implin);
	
	double RadTool=0;
	RadTool=DiaTool/2;
	
	double Yn=0,YnMed1=0,YnMed2=0;
	double SigV=0;
	
	//double J=0;
	int salir=0;
	
	/** N130 G00 F150.0 X0 Y0 Z35.0
		G40 M6 T2
		
		N140 G01 X20.0 Y60.0
		N160 Z-0.5
		N150 G41 D3 Y66.0
		N180 G03 X20 Y54.0 R6.0
		N180 G03 X20 Y66.0 R6.0
		
		M30 	 * **/
		//salir=1;
	do{
		
		if(salir==0){
			implin = "G01 G40 X" + CentroX + " Y" + CentroY;
			WriteLineNo(implin);
			
			implin = "G01 Z-" + ProfPorPaso;
			WriteLineNo(implin);
			
			if(Inicio==1){
			implin = "G41 D" + TrocaDeFerramenta.numeroD;
			WriteLineNo(implin);
			}
			
			ImpSegBlok=1;//para imprimir la correcion de ferramenta en la secuencia
		}else{
			
			implin = "G01 Z-" + ProfPorPaso;
			WriteLineNo(implin);
			ImpSegBlok=0;//para imprimir la correcion de ferramenta en la secuencia
		}
		




		SigV=0;
		Ini2=1;
		salir=0;
		
		//warning por compensacion
		if(FuroSimples.DiaTool<DiaTool){
			FuroSimples.DiaTool = DiaTool;
		}
		
		//genero la secuencia para el llenado
		do{
			Yn=CentroY+(FuroSimples.DiaTool/2)+RadTool+SigV+1;
			
			if(Yn>(CentroY+DiaFuro/2)-(RadTool*0.25)){
				//System.out.println("mayor");
				salir=1;
			}else {
				//hay que dividir en dos la circunferencia
				if(Ini2==1){
					
					if(ImpSegBlok==1){
						implin = "G01 X" + CentroX + " Y" + Yn;
						WriteLineNo(implin);
					}else{
						implin = "G01 G40 X" + CentroX + " Y" + Yn;
						WriteLineNo(implin);
						
						implin = "G41 D" + TrocaDeFerramenta.numeroD;
						WriteLineNo(implin);
					}
					
					
					YnMed1=(Yn-CentroY);
					YnMed2=Yn - (YnMed1*2);
					implin = "G03 Y" + YnMed2 + " R" + YnMed1;
					WriteLineNo(implin);
					
					implin = "G03 Y" + Yn + " R" + YnMed1;
					WriteLineNo(implin);
					Ini2=0;
				}else{
					
					YnMed1=Yn-CentroY;
					double CentroX2=CentroX-YnMed1;
					implin = "G03 X"+ df.format(CentroX2) +" Y" + df.format(CentroY) + " R" + df.format(YnMed1);
					//implin = "G03 Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					YnMed2=CentroY - YnMed1;
					implin =  "G03 X"+ df.format(CentroX) +" Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					Yn=CentroY + YnMed1;
					implin = "G03 Y" + Yn + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
			}
			

			
			
			SigV += DiaTool*0.2;
			}
	
		}while(salir==0);
		
	
		if(salir==1){
			
			if(SigV==0){
					//hacer el recorrido del centro a la tangente del circulo solo la primera vez
					YnMed1=DiaFuro/2;
					
					if(YnMed1 <= DiaTool & Ini4==1 & DiaFuro>DiaTool){
						double addX = Math.abs(YnMed1-DiaTool);
						if(addX==0){
							addX = 1;
						}else{
							addX = addX+1;
						}
							
						implin = "G01 G40 X" + (CentroX+addX) + " Y" + (CentroY);
						WriteLineNo(implin);
						
						implin = "G41 D" + TrocaDeFerramenta.numeroD;
						WriteLineNo(implin);
						ImpSegBlok=1;//para imprimir la correcion de ferramenta en la secuencia
						Ini4=0;
					}
					
					double CentroX2=CentroX-YnMed1;
					if(Ini3==1){
					implin = "G03 X"+ df.format(CentroX2) +" Y" + df.format(CentroY) + " R" + df.format(YnMed1);
					//implin = "G03 Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					Ini3=0;
					}
					
					Yn=(CentroY+DiaFuro/2);
					YnMed2=Yn - DiaFuro;
					implin =  "G03 X"+ df.format(CentroX) +" Y" + YnMed2 + " R" + df.format(YnMed1);
					WriteLineNo(implin);
					
					implin = "G03 Y" + Yn + " R" + YnMed1;
					WriteLineNo(implin);
					
					implin = "G03 X"+ df.format(CentroX2) +" Y" + df.format(CentroY) + " R" + df.format(YnMed1);
					WriteLineNo(implin);
				
			}else{
				YnMed1=DiaFuro/2;
				double CentroX2=CentroX-YnMed1;
				implin = "G03 X"+ df.format(CentroX2) +" Y" + df.format(CentroY) + " R" + df.format(YnMed1);
				//implin = "G03 Y" + df.format(YnMed2) + " R" + df.format(YnMed1);
				WriteLineNo(implin);
				
				Yn=(CentroY+DiaFuro/2);
				YnMed2=Yn - DiaFuro;
				implin =  "G03 X"+ df.format(CentroX) +" Y" + YnMed2 + " R" + df.format(YnMed1);
				WriteLineNo(implin);
				
				implin = "G03 Y" + Yn + " R" + YnMed1;
				WriteLineNo(implin);
				
				
				implin = "G03 X"+ df.format(CentroX2) +" Y" + df.format(CentroY) + " R" + df.format(YnMed1);
				WriteLineNo(implin);
			}

			
		}
	
	ProfPorPaso+=proSig;
//	Ini2=1;
	
	}while(ProfPorPaso<=ProfTotalFuro);//para hacer la secuencia
	
	implin = "G40 G00 X" + CentroX + " Y" + CentroY;
	WriteLineNo(implin);

	
}//fin main

	/**************************************************************************************************************************************************************************************************************************************/
	/**************************************************************************************************************************************************************************************************************************************/
	/**************************************************************************************************************************************************************************************************************************************/
	/**************************************************************************************************************************************************************************************************************************************/
	
	//metodo 2 para la creacion del furo
	public static void ImpGcodeFuroComplexo_F2(double CentroX, double CentroY, double DiaIniFuro, double DiaFinFuro, double ProfTotalDoFuro, String ToolData) {
		//para darle el formato decimal
		LineNo=GeneradorCodidoG.LineNo;
		DecimalFormat df = new DecimalFormat("0.000"); 
		
		/*** 1ro ir al plano de segurança  **/
		implin = "G40 G00 Z" + br.UNB.LAB.InfBasicas.PlanoSeguranca.ComprimentoPlanoSegDouble;
		WriteLineNo(implin);
		
		/*** 2do imprimir troca de Tool  **/
		TrocaDeFerramenta.LineNo=LineNo;
		TrocaDeFerramenta.ImpriFer(ToolData);
		
		/** obtener **/
		//double DiaTool = TrocaDeFerramenta.Diametro_Tool;
		
		double FazerEspiral = (DiaIniFuro-FuroSimples.DiaTool);//encuentro la cantidad de material que todavia falta por usinar
		FazerEspiral = (FazerEspiral/2)/TrocaDeFerramenta.Diametro_Tool;//verifico que la herramienta pode fazer o resto da usinagem sim fazer o espiral
		
		/****************************** si es menor que 1 quiere decir que solo se hace el barrido del borde para retirar el material que falta ****************************************************/
		if(FazerEspiral<=1){//
			//*** 2do imprimir compensacion de ferramenta, plano de segurança e primer corte profCorte **//*
			LineNo = br.UNB.LAB.FAcessorias.EncabezadoGcodeCompenTool.ImprimirGcodeHeaderCON_G41(CentroX + 0.01, CentroY, LineNo);
			BarridoDelBorde(CentroX, CentroY, DiaIniFuro, ProfTotalDoFuro);
			GeneradorCodidoG.LineNo=LineNo;
		/************************** Si Es mayor que 1 quiere decir que debe hacerse el barrido en espiral hasta llegar al borde para retirar el material que falta *********************************/
		}else {//
			LineNo = br.UNB.LAB.FAcessorias.EncabezadoGcodeCompenTool.ImprimirGcodeHeaderSIN_G41(CentroX + 0.01, CentroY, LineNo);
			BarridoEnSpiral(CentroX, CentroY, DiaIniFuro, ProfTotalDoFuro);
			GeneradorCodidoG.LineNo=LineNo;
			
	
		}
		//System.out.println();
		GeneradorCodidoG.LineNo=LineNo;
	}
	
	public static void BarridoDelBorde(double CentroX, double CentroY, double DiaIniFuro, double ProfTotalDoFuro) {
		LineNo=GeneradorCodidoG.LineNo;
		DecimalFormat df = new DecimalFormat("0.000"); 
		
		CalularNumeroPassadasProfundidade(ProfTotalDoFuro,GeneradorCodidoG.ProfundidadeCorte);
		
/*		double Npasadas=ProfTotalDoFuro/GeneradorCodidoG.ProfundidadeCorte;
		double NpasosEnteros = (int)Npasadas;
		double NpasosDecimales = (Npasadas - NpasosEnteros);*/
		double TempFor_i_Npasos = 0;
		
		//hago la cantidad de pasos enteros
		for (int i = 0; i < NpasosEnterosPC; i++) {
			// cuadrante 1 (x- , y)
			implin = "G03 X"+ df.format(CentroX-(DiaIniFuro/2)) +" Y" + df.format(CentroY) + " Z-" + df.format((i+1)* GeneradorCodidoG.ProfundidadeCorte) +  " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			// cuadrante 1 (x , y-)
			implin = "G03 X"+ df.format(CentroX) +" Y" + df.format(CentroY-(DiaIniFuro/2)) + " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			// cuadrante 1 (x+ , y)
			implin = "G03 X"+ df.format(CentroX+(DiaIniFuro/2)) +" Y" + df.format(CentroY) + " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);

			TempFor_i_Npasos = (i+1)* GeneradorCodidoG.ProfundidadeCorte;//guarda temporalmente el valor maximo de la profundidad de corte en el ultimo paso
			}

		//hago la cantidad decimal del paso que falta, si es que falta
		if(NpasosDecimalesPC>0){
			// cuadrante 1 (x- , y)
			implin = "G03 X"+ df.format(CentroX-(DiaIniFuro/2)) +" Y" + df.format(CentroY) + " Z-" + df.format((NpasosDecimalesPC * GeneradorCodidoG.ProfundidadeCorte) + TempFor_i_Npasos) +  " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			// cuadrante 1 (x , y-)
			implin = "G03 X"+ df.format(CentroX) +" Y" + df.format(CentroY-(DiaIniFuro/2)) + " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
			
			// cuadrante 1 (x+ , y)
			implin = "G03 X"+ df.format(CentroX+(DiaIniFuro/2)) +" Y" + df.format(CentroY) + " R" + df.format(DiaIniFuro/2);
			WriteLineNo(implin);
		
			}
		
		//impreme el regreso al punto final
		implin = "G03 X"+ df.format(CentroX-(DiaIniFuro/2)) +" Y" + df.format(CentroY) + " R" + df.format(DiaIniFuro/2);
		WriteLineNo(implin);
		
		//regresa al centro del furo
		implin = "G40 G00 X" + (CentroX) + " Y" + (CentroY);
		WriteLineNo(implin);
		
		implin = "G90";
		WriteLineNo(implin);
		
	}
	
	public static void BarridoEnSpiral(double CentroX, double CentroY, double DiaIniFuro, double ProfTotalDoFuro) {
		LineNo=GeneradorCodidoG.LineNo;
		DecimalFormat df = new DecimalFormat("0.000"); 
		
		//double diametroToolTemp = TrocaDeFerramenta.Diametro_Tool*GeneradorCodidoG.PorcentageToolPassoEspiral;
		double diametroToolAtual = TrocaDeFerramenta.Diametro_Tool*GeneradorCodidoG.PorcentageToolPassoEspiral;
		double diametroToolPasada = FuroSimples.DiaTool;
		System.out.println();
		//System.out.println("(Percentual de avanco do espiral -plano XY-: "+ (GeneradorCodidoG.PorcentageToolPassoEspiral*100) +"%)");
		CalularNumeroPassadasEspiral(DiaIniFuro,diametroToolAtual);
		
		CalularNumeroPassadasProfundidade(ProfTotalDoFuro,GeneradorCodidoG.ProfundidadeCorte);
		
		double TempFor_i_Npasos = 0;
		
		for (int i = 0; i < NpasosEnterosPC; i++) {
			
			implin = "G40 G90 G01 F"+ df.format(GeneradorCodidoG.MaxVelcorteTool)+" X"+ df.format(CentroX)+ " Y"+ df.format(CentroY) +" Z-"+ df.format((i+1)* GeneradorCodidoG.ProfundidadeCorte);
			WriteLineNo(implin);
			
			//YATIENE LA COMPENSACION DE FERRAMENTA
			implin = "G91 G01 F"+ df.format(GeneradorCodidoG.MaxVelcorteTool) +" X"+ df.format(diametroToolAtual/2) +" Y" + df.format(0);
			WriteLineNo(implin);
			
			implin = "G41 D" + TrocaDeFerramenta.numeroD;
			WriteLineNo(implin);
			
			Imprimir_Espiral(CentroX, CentroY, DiaIniFuro, ProfTotalDoFuro, diametroToolAtual, diametroToolPasada);
			
			TempFor_i_Npasos = (i+1)* GeneradorCodidoG.ProfundidadeCorte;//guarda temporalmente el valor maximo de la profundidad de corte en el ultimo paso
			GeneradorCodidoG.LineNo=LineNo;
		}
		
		//hago la cantidad decimal del paso que falta, si es que falta
		if(NpasosDecimalesPC>0){
			implin = "G90 G01 F"+ df.format(GeneradorCodidoG.MaxVelcorteTool) +" Z-"+ df.format((NpasosDecimalesPC * GeneradorCodidoG.ProfundidadeCorte) + TempFor_i_Npasos);
			WriteLineNo(implin);
			
			//YATIENE LA COMPENSACION DE FERRAMENTA
			implin = "G91 G01 F"+ df.format(GeneradorCodidoG.MaxVelcorteTool) +" X"+ df.format(diametroToolAtual) +" Y" + df.format(0);
			WriteLineNo(implin);
			
			implin = "G41 D" + TrocaDeFerramenta.numeroD;
			WriteLineNo(implin);
			
			Imprimir_Espiral(CentroX, CentroY, DiaIniFuro, ProfTotalDoFuro, diametroToolAtual, diametroToolPasada);
		}
	
		implin = "G90";
		WriteLineNo(implin);
		GeneradorCodidoG.LineNo=LineNo;

	}
	
	
	public static void Imprimir_Espiral(double CentroX, double CentroY, double DiaIniFuro, double ProfTotalDoFuro, double diametroToolAtual, double diametroToolPasada) {
		LineNo=GeneradorCodidoG.LineNo;
		DecimalFormat df = new DecimalFormat("0.000"); 

		
		double TempFor_i_Npasos=diametroToolAtual;
		double NpasosSigno=0, signo =0, salir05=0;
		
		for (int i = 1; i < NpasosEnterosEsp; i++) {
			TempFor_i_Npasos += diametroToolAtual; 
			NpasosSigno = TempFor_i_Npasos;

			//cambio el signo
			if(signo == 0){
				NpasosSigno = NpasosSigno *-1;
				signo =1;
			}else{
				signo=0;
			}
					
			if(salir05==0 & i == 1){//imprime adecuacion
				implin = "G03 X"+ df.format(NpasosSigno) + " R" + df.format((TempFor_i_Npasos/2)+0.001);
				WriteLineNo(implin);
				salir05=1;
			}else{//imprime normal
				implin = "G03 X"+ df.format(NpasosSigno) + " R" + df.format(TempFor_i_Npasos/2);
				WriteLineNo(implin);
			}
		}
		
		//hago la cantidad decimal del paso que falta, si es que falta
		if(NpasosDecimalesEsp>0){
			//double Temp2=NpasosSigno;
			
			TempFor_i_Npasos += (diametroToolAtual)*NpasosDecimalesEsp; 
			NpasosSigno = TempFor_i_Npasos;

			//cambio el signo
			if(signo == 0){
				NpasosSigno = NpasosSigno *-1;
				signo =1;
			}else{
				signo=0;
			}
				implin = "G03 X"+ df.format(NpasosSigno) + " R" + df.format(TempFor_i_Npasos/2);
				WriteLineNo(implin);
		}
		
		//fechar el circulo
		if(NpasosSigno<0){
			implin = "G03 X"+ df.format(NpasosSigno*-1) + " R" + df.format(TempFor_i_Npasos/2);
			WriteLineNo(implin);
		}else{
			implin = "G03 X"+ df.format(NpasosSigno*-1) + " R" + df.format(TempFor_i_Npasos/2);
			WriteLineNo(implin);
		}
		
	}
	
	
	
	
	public static void CalularNumeroPassadasEspiral(double ValorMayor, double ValorMenor) {
		
		NpasadasEsp=ValorMayor/ValorMenor;
		NpasosEnterosEsp = (int)NpasadasEsp;
		NpasosDecimalesEsp = (NpasadasEsp - NpasosEnterosEsp);
		
	}
	
	public static void CalularNumeroPassadasProfundidade(double ValorMayor, double ValorMenor) {
		
		NpasadasPC=ValorMayor/ValorMenor;
		NpasosEnterosPC = (int)NpasadasPC;
		NpasosDecimalesPC = (NpasadasPC - NpasosEnterosPC);
		
	}
	
	


public static void WriteLineNo(String lin) {
		
		System.out.println("N" + LineNo + " " + lin.replace(",", "."));
		LineNo += 1;
		
	}
	
}
