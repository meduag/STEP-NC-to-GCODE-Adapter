package br.UNB.LAB.GerarCodG;

import br.UNB.LAB.GerarCodG.CodeG_Para__RoundHole_E_Operaciones;
import br.UNB.LAB.GerarCodG.CodeG_Para__Slot_E_Operaciones;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;



public class GeneradorCodidoG {
	
	public static int ValorPasoAngulo=10;
	public static int LineNo=1;
	public static double FeedRate=100*6;
	int salif=1;
	static int cDatList=0; 
	public static double MaxVelAvanceMaquina = 2000; //para colocar en el archivo de configuracion - Maxima velocidad de Avance de la Maquina en todos los ejes (Valor normal 800)
	public static double MaxVelcorteTool = 2000; //para colocar en el archivo de configuracion - Maxima velocidad de Avance de la Maquina en todos los ejes (Valor normal 150)
	public static double ProfundidadeCorte = 2; //para colocar en el archivo de configuracion - avance para la profundidad de corte no maximo 3mm (modificar velocidad de corte se precisar)
	public static double AvanceDoAguloSemiCircunferencia = 10; //para colocar en el archivo de configuracion - avance en grados para calcular los puntos XY, DiametroProf da semicircunferencia(Valor normal 800)
	public static double PorcentageToolPassoEspiral = 0.5; //para colocar en el archivo de configuracion - paso de avance en la circunferencia el valor tiene que ser mayor o igual al de 1/2(ejemplos 0.5, 0.75, 0.85) e menor igual a 1
	
	
	
	public static double ZM06 = 5; //para colocar en el archivo de configuracion - es el valor de offset del exe Z cuando troca la herramienta con G55

	
	public void GenCodG(){
	
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		

		MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
		CodeG_Para__RoundHole_E_Operaciones FurosNC = new CodeG_Para__RoundHole_E_Operaciones();
		CodeG_Para__Slot_E_Operaciones RanurasNc = new CodeG_Para__Slot_E_Operaciones();
		CodeG_Para__Step_E_Operaciones DegrauNC = new CodeG_Para__Step_E_Operaciones();
		CodeG_Para__Pocket_E_Operaciones PocketNC = new CodeG_Para__Pocket_E_Operaciones();
		
		int TamList= MapearMachining_workingstep.cFeatures;//aqui va es la cantidad de features no la lista de los datos
		
		
		//llamar la funcion de ordenar ferramentas esta es la primera llamada
		OrdenarFerramentas.OrdenFer();
			
		//Codigo Inicial del programa NC

			System.out.println("(Header)");
			System.out.println("(Codigo G gerado pelo Adaptador de STEP-NC a Codigo G v1.0 R0.001)");
			
			System.out.println("N" + LineNo + " " + "G17 G21 G54 G40 G90");
			LineNo += 1;
			//System.out.println(" ");
			
		
			cDatList=0;
			do{
				@SuppressWarnings("static-access")
				String data=ExtrairDatos.InformacoesAvancadas.get(0);
				
				if(data.startsWith("Feature - RoundHole")==true){
					if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
						System.out.println("(Generar codido G para Furos)");
					}
					FurosNC.GenCodG_RoundHoleOper();
					
				}
				
				if(data.startsWith("Feature - Slot")==true){
					if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
						System.out.println("(Generar codido G para Ranuras)");
					}
					RanurasNc.GenCodG_SloteOper();
									
				}
				
				if(data.startsWith("Feature - Step")==true){
					if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
						System.out.println("(Generar codido G para Degraus)");
					}
					DegrauNC.GenCodG_StepeOper();

									
				}
				
				if(data.startsWith("Feature - Pocket")==true){
					if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
						System.out.println("(Generar codido G para Pockets)");
					}
					PocketNC.GenCodG_PocketeOper();
			
				}
				
				MapearMachining_workingstep.InformacoesAvancadas.remove(0);
				int valorf = MapearMachining_workingstep.InformacoesAvancadas.size();
				//System.out.println(valorf);
				for (int i = 0; i < valorf; i++) {
					
					String eliminar=MapearMachining_workingstep.InformacoesAvancadas.get(0);
					
					if(eliminar.startsWith("Feature")==true){
						i = valorf;
					}else{
						MapearMachining_workingstep.InformacoesAvancadas.remove(0);
					}
					
				}
					
					
				
				
				cDatList++;
				
			}while(cDatList<TamList);
			

			System.out.println("N" + LineNo + " G00 Z" + br.UNB.LAB.InfBasicas.PlanoSeguranca.ComprimentoPlanoSegDouble);
			LineNo += 1;
			System.out.println("N" + LineNo + " M30");
	}

}
