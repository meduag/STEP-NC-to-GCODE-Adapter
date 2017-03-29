package br.UNB.LAB.Integrador;

import br.UNB.LAB.GerarCodG.GeneradorCodidoG;
import br.UNB.LAB.IndEnderecos.PasarArchivo;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.IndEnderecos.TirarEspacosEmBranco;
import br.UNB.LAB.InfBasicas.DadosDaPeca;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;


public class Integrador {
	
	//Linux
	//static String rutaLec = "/home/test1/mod/f.txt";
	//static String rutaEsc = "/home/test1/mod/f2.txt";
	//Windows
	public static String rutaLec = "";
	public static String rutaEsc = "";
	public static String rutaEscTBL = "";
	public static String rutaMod = ""; 
	//public static double MaxVelAvanceMaquina = 800; 
	
	/********************************************************/
	/***** Para colocar en el archivo de configuracion  *****/
	/********************************************************/
	
	//para imprimir todas las leyendas de entrada y salida de las clases obtencion de informaciones avançadas
	public static int ImpLedClass=0;
	//para imprimir todas las leyendas de entrada y salida de las features y ferramentas
	public static int ImpLedFer=0;
	//para imprimir el g40 para la simulacion en vericut 1 imprime 0 no imp
	public static int ImpG40paraVericut=0;
	public static int ImpG55=1;
	//para imprimir todas las leyendas de los acabamientos en la feature Pocket
	public static int ImpLedAcabamentoPocket=1;
	
	/********************************************************/
	/************  fin archivo de configuracion   ***********/
	/********************************************************/


	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		int so=1;//sistema operativo 1 windows ---- 0 linux
		
		if(so==1){
			/********  Furos  ******/
			/*01 base plana */
			//rutaLec = "C:/mod/Furos/01 FuroBasePlana/FuroBasePLanaT=Df";
			//rutaLec = "C:/mod/Furos/01 FuroBasePlana/FuroBasePLanaT-Df";
			//rutaLec = "C:/mod/Furos/01 FuroBasePlana/FuroBasePLanaT-Df3espiral";//el hueco es mucho mayor que dos veces el diametro la herramienta
			
			/*02 base conica */
			//rutaLec = "C:/mod/Furos/02 FuroBaseConica/FuroBasePLanaTool=DFuro";
			
			/*03 base conica */
			//rutaLec = "C:/mod/Furos/03 FuroBaseEsferica/FuroBaseArreT=Df";
			//rutaLec = "C:/mod/Furos/03 FuroBaseEsferica/FuroBaseArreT-Df";
			
			/*04 base conica */
			//rutaLec = "C:/mod/Furos/04 FuroBaseArredondada/FuroBaseArredondadaT=Df";
			//rutaLec = "C:/mod/Furos/04 FuroBaseArredondada/FuroBaseArredondadaT-Df";
			
			//ruto solo mod
			//rutaLec = "C:/mod/teste2";
			
			/*Teste furo */
			rutaLec = "C:/mod/furo.p21";
			
			
			rutaEsc = "C:/mod/temp.txt";
			rutaEscTBL = "C:/mod/tool.tbl";
			//rutaEscTBL = "C:/Users/Miguel/Dropbox/Disertacion/tool.tbl";
			rutaMod = "C:/Code_original.txt"; 
		}else{
			rutaLec=args[0];
			rutaEsc = "/home/mod/temp.txt";
			rutaEscTBL = "/home/mod/tool.tbl";//mudar en el sistema para que quede dentro del ejecutable del EMC2 xej "pasta /my mill" en el area de trabajo
			rutaMod = "/home/mod/GUI/Code_original.txt"; // ruta en linux
		}
		
		/***************** Leer archivo de configuraciones ******************/
		/*** freerate
		 * valares de avance en angulo y prof
		
		*/
		
		
		/************-Copio el archivo a la carpeta mod temporalmente-**************/
		PasarArchivo.Pasar(rutaLec,rutaMod);
		
		/************-Quito los espacios en blanco-**************/
		TirarEspacosEmBranco TSB = new TirarEspacosEmBranco();
		TSB.EspacosEmBranco();
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		SecEnder.SecEnderecosDoFormato();
		
		DadosDaPeca Bloco = new DadosDaPeca();
		Bloco.DadosDoBloko();
		
		/************-Ubico qual es el pano de seguranca-**************/
		PlanoSeguranca secPlan = new PlanoSeguranca();
		secPlan.PlanSegur();
		
		/************-Mapeo las features-**************/
		//System.out.println("");
		if(ImpLedClass==1){
			System.out.println("(Entro en Mapear Features)");
		}
		MapearMachining_workingstep Mfeat = new MapearMachining_workingstep();
		//Mfeat.rutaEsc = rutaEsc;
		Mfeat.MapFeatures();
		
		if(ImpLedFer==1){
			System.out.println("(Cantidad de features: "+Mfeat.cFeatures+")");
		}
		GeneradorCodidoG GenGcode = new GeneradorCodidoG();
		
		//////////////configuro el valor del Angulo para ser el paso de desvaste de la profundidad
		GenGcode.ValorPasoAngulo=5;
		GenGcode.GenCodG();
		
		//despues de esto eliminar el archivo temporal f2.txt
	}

}
