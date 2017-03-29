package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;

import br.UNB.LAB.FAcessorias.ImprimirPlanoSeg;
import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;
import br.UNB.LAB.GerarCodG.A01_GerarGcode_RoundHole;
import br.UNB.LAB.GerarCodG.A02_GerarGcode_Slot;
import br.UNB.LAB.InfAvancadas.MapearMachining_workingstep;
import br.UNB.LAB.InfBasicas.DadosDaPeca;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;
import br.UNB.LAB.Integrador.Integrador;



public class Gcode {
	
	public static int ValorPasoAngulo=20;
	public static int LineNo=1;
	public static double FeedRate=100*6;
	int salif=1;
	static int cDatList=0; 
	public static double ValorZ_PlanoDeSeguranza = 55; //se puede configurar, mas solo funciona en caso de que sea mayor a este valor
	
	public static double MaxVelAvanceMaquina = 800; //para colocar en el archivo de configuracion - Maxima velocidad de Avance de la Maquina en todos los ejes (Valor normal 800)
	public static double MaxVelcorteTool = 400; //para colocar en el archivo de configuracion - Maxima velocidad de Avance de corte de la maquina (Valor normal 150)
	public static double ProfundidadeCorte = 2; //para colocar en el archivo de configuracion - avance para la profundidad de corte no maximo 3mm (modificar velocidad de corte se precisar)
	public static double AvanceDoAguloSemiCircunferencia = 10; //para colocar en el archivo de configuracion - avance en grados para calcular los puntos XY, DiametroProf da semicircunferencia(Valor normal 800)
	public static double PorcentageToolPassoEspiral = 0.5; //para colocar en el archivo de configuracion - paso de avance en la circunferencia el valor tiene que ser mayor o igual al de 1/2(ejemplos 0.5, 0.75, 0.85) e menor igual a 1
	public static double AvanceDaPendienteDaLinha = 1; //para colocar en el archivo de configuracion - avance en grados para calcular los puntos XY de la linea. avance en milimetros (0.5, 1, 2) 
	
	public static DecimalFormat df = new DecimalFormat("0.0000");//formato dos decimales
	
	public static double ZM06 = 5; //para colocar en el archivo de configuracion - es el valor de offset del exe Z cuando troca la herramienta con G55

	
	public void GenCodG(){
	
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		/****arreglar velocidad de avance del cuadrado puede ser 400****/
		

		MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
		A01_GerarGcode_RoundHole FurosNC = new A01_GerarGcode_RoundHole();
		A02_GerarGcode_Slot RanurasNc = new A02_GerarGcode_Slot();
		//CodeG_Para__Step_E_Operaciones DegrauNC = new CodeG_Para__Step_E_Operaciones();//activar para hacerlo solo
		CodeG_Para__Pocket_E_Operaciones PocketNC = new CodeG_Para__Pocket_E_Operaciones();
		
		int TamList= MapearMachining_workingstep.cFeatures;//aqui va es la cantidad de features no la lista de los datos
		
		
		//llamar la funcion de ordenar ferramentas esta es la primera llamada
		OrdenarFerramentas.OrdenFer();
			
		//Codigo Inicial del programa NC
		if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
			System.out.println("(..:::  Header  :::..)");
			System.out.println("(..:::  Codigo G gerado pelo Adaptador de STEP-NC a Codigo G v1.0 R0.002  :::..)");
			System.out.println("(Arquivo: " + Integrador.rutaLec + ")");
			System.out.println("(Dados da peça: ComX:"+DadosDaPeca.ComprimentoX_DoBloco + " ComY:"+ DadosDaPeca.ComprimentoY_DoBloco + " CompZ:"+DadosDaPeca.ComprimentoZ_DoBloco+")");
			System.out.println();
			ImprimirSecuenciaLineaNo.EscribirLinea("G17 G21 G54 G40 G90");
			
		}else{
			System.out.println("(..:::  Header  :::..)");
			System.out.println("(..:::  Codigo G gerado pelo Adaptador de STEP-NC a Codigo G v1.0 R57  :::..)");
			System.out.println("(Arquivo: " + Integrador.rutaLec + ")");
			System.out.println();
			ImprimirSecuenciaLineaNo.EscribirLinea("G17 G21 G54 G40 G90");
		}
		
		
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
				
				/*if(data.startsWith("Feature - Step")==true){//problema na seleçao de ferramenta
					if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
						System.out.println("(Generar codido G para Degraus)");
					}
					DegrauNC.GenCodG_StepeOper();

									
				}*/
				
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
				ProfundidadeCorte = 2;//reinicio la profundidad
				
			}while(cDatList<TamList);
			
			
			String ver ="G40 G90 G00 Z" +  Gcode.df.format(PlanoSeguranca.ComprimentoPlanoSegDouble);
			if(ImprimirSecuenciaLineaNo.VerificarUltimaImpresion.startsWith(ver)==false){
				ImprimirPlanoSeg.ImpPlanoSeguranza();
			}
			
			ImprimirSecuenciaLineaNo.EscribirLinea("M05");
			ImprimirSecuenciaLineaNo.EscribirLinea("M30");
			

	}

}
