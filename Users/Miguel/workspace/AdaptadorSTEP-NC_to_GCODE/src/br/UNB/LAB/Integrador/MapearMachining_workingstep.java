package br.UNB.LAB.Integrador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import br.UNB.LAB.Features.FeaturePoket;
import br.UNB.LAB.Features.FeatureRoundHole;
import br.UNB.LAB.Features.FeatureSlot;
import br.UNB.LAB.Features.FeatureStep;
//import java.util.StringTokenizer;
//import java.util.Vector;


public class MapearMachining_workingstep {

	//public String DatosRH[] = new String[50];// array de pociciojnes para este cambiar a matrriz
												// Slot
	public static int cFeatures = 0; // contador de features
	//public String rutaEsc = "";
	private int cSt=1, cRh=1, cSp=1, cCp=1;
	
	public static ArrayList<String> InformacoesAvancadas = new ArrayList<String>();


	public void MapFeatures(){
		
		File archivo=new File(Integrador.rutaEsc);
		String ln= System .getProperty("line.separator");
		//String fin="";//fin del round hole
		
		//Vector<String> vc=new Vector<String>();
		
		
		try{
			
			BufferedReader in=new BufferedReader(new FileReader(archivo));//crea el bufer para leer el archivo
			String data="";//variable data vacia
			data=in.readLine();//lee el valor de la linea y se lo pasa a data
			//int ctokens=0;//contador de tokens
			int slir=1;//salir del round hole
			//int salir=1;//salir del centerdriil

			while(data!=null){//while para recorrer el archivo
			
				
				slir=1;
			
				if(data.indexOf("ROUND_HOLE(")!=-1){
					if(Integrador.ImpLedClass==1){
						System.out.println("(Entro en ROUND_HOLE - Furos)");
					}
					FeatureRoundHole RoundHole = new FeatureRoundHole();//creo el roudhole
					do{//recorro el RH hasta el final de los parametros
						RoundHole.data = data;
						RoundHole.cFeaRHole=cRh++;
						RoundHole.FeaRoundHole();
						data=in.readLine(); 
						slir=RoundHole.salirRH;
						}while(slir==1);
					//System.out.println("");
					RoundHole.Imprimir();
					cFeatures++;
					}
				
				
				slir=1;
		
				if(data.indexOf("=SLOT(")!=-1){
					if(Integrador.ImpLedClass==1){
						System.out.println("(Entro en SLOT - Ranuras)");
					}
					FeatureSlot Slot = new FeatureSlot();//creo el slot
					do{//recorro el SLOT hasta el final de los parametros
						Slot.actAsig=1;
						Slot.cFeaSlot=cSt++;
						Slot.data = data;
						Slot.FeaSlot();
						data=in.readLine(); 
						slir=Slot.salirST;
						}while(slir==1);
					//System.out.println("");
					Slot.Imprimir();
					cFeatures++;	
				}

				
				slir=1;

				if(data.indexOf("=STEP(")!=-1){
					//System.out.println();
					if(Integrador.ImpLedClass==1){
						System.out.println("(Entro en STEP - DegaU)");
					}
					FeatureStep Step = new FeatureStep();//creo el slot
					do{//recorro el SLOT hasta el final de los parametros
						Step.data = data;
						Step.cFeaStep = cSp++;
						Step.FeaStep();
						data=in.readLine(); 
						slir=Step.salirSP;
						}while(slir==1);
					//System.out.println("");
					Step.Imprimir();
					cFeatures++;	
				}
				
				
				slir=1;

				if(data.indexOf("=CLOSED_POCKET(")!=-1){
					if(Integrador.ImpLedClass==1){
						System.out.println("(Entro en Poket - ClosedPocket)");
					}
					FeaturePoket CPocket = new FeaturePoket();//creo el slot
					slir=1;
					do{//recorro la feature hasta el final de los parametros
						CPocket.data = data;
						CPocket.cPoket = cCp++;
						CPocket.FeaClosedPocket();
						data=in.readLine(); 
						slir=CPocket.salirCP;
					}while(slir==1);
					
					//System.out.println("");
					CPocket.Imprimir();
					cFeatures++;
				}
				
			data=in.readLine(); 
			
			}//fin while lectura de las lineas del archivo
			in.close();
			
			if(Integrador.ImpLedClass==1){
				System.out.println(ln + "(Fin================= Salio de MapearFeatures)");
			}
	}//fin try
	catch(IOException e){
		if(Integrador.ImpLedClass==1){
			System.out.println("(no se encontro el Archivo!)");
		}
	}
	}
}
