package br.UNB.LAB.Integrador;

/*import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
*/
import br.UNB.LAB.GerarCodG.Gcode;
import br.UNB.LAB.IndEnderecos.PasarArchivo;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.IndEnderecos.TirarEspacosEmBranco;
import br.UNB.LAB.InfAvancadas.MapearMachining_workingstep;
import br.UNB.LAB.InfBasicas.DadosDaPeca;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;


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
	public static int ImpVerG55=0;
	//para imprimir todas las leyendas de los acabamientos en la feature Pocket
	public static int ImpLedAcabamentoPocket=1;
	
	/********************************************************/
	/************  fin archivo de configuracion   ***********/
	/********************************************************/


	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		//int so=1;//sistema operativo 1 windows ---- 0 linux
		//activar lectura de dados do arquivo de configuraçao
		lerDadosArqConfg.LeerDadConfg();	
		ImpLedClass=Integer.parseInt(lerDadosArqConfg.DadosarqConfg.get(2));
		ImpLedFer=Integer.parseInt(lerDadosArqConfg.DadosarqConfg.get(3));
		ImpLedAcabamentoPocket=ImpLedFer;
		
		Gcode.ValorZ_PlanoDeSeguranza = Double.parseDouble(lerDadosArqConfg.DadosarqConfg.get(4));
		Gcode.MaxVelAvanceMaquina = Double.parseDouble(lerDadosArqConfg.DadosarqConfg.get(5));
		Gcode.MaxVelcorteTool =  Double.parseDouble(lerDadosArqConfg.DadosarqConfg.get(6));
		Gcode.ProfundidadeCorte = Double.parseDouble(lerDadosArqConfg.DadosarqConfg.get(7));
		Gcode.AvanceDoAguloSemiCircunferencia = Double.parseDouble(lerDadosArqConfg.DadosarqConfg.get(8));
		Gcode.PorcentageToolPassoEspiral = Double.parseDouble(lerDadosArqConfg.DadosarqConfg.get(9))/100;
		Gcode.AvanceDaPendienteDaLinha = Double.parseDouble(lerDadosArqConfg.DadosarqConfg.get(10));
		
		
		
		//asignacion de dados
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(0));//repositorio temp.txt
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(1));//copia code original
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(2));//comentarios informaçao adv y basica
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(3));//comentarios codigo g features y ferramentas
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(4));//plano seg
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(5));//vel max maquina
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(6));//vel max corte
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(7));//avan prof corte
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(8));//avan ang semi circunf
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(9));//porcentage fer espiral
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(10));// avance linha prfoun
//		System.out.println(lerDadosArqConfg.DadosarqConfg.get(11));// rota tool.tbl

		
		
		
		
		 // Determinar en qué SO estamos
        String so1 = System.getProperty("os.name");
        if (so1.equals("Linux")){// Comando para Linux
        	rutaLec=args[0];
			rutaEsc = lerDadosArqConfg.DadosarqConfg.get(0);
			rutaEscTBL = lerDadosArqConfg.DadosarqConfg.get(11);//mudar en el sistema para que quede dentro del ejecutable del EMC2 xej "pasta /my mill" en el area de trabajo
			rutaMod = lerDadosArqConfg.DadosarqConfg.get(1); // ruta en linux
        }else{// Comando para Windows
        	
        	/***********************************************************  Furos  *******************************************************/
			/*01 base plana */
			//rutaLec = "C:/mod/Furos/01 FuroBasePlana/FuroBasePLanaT=Df";
			//rutaLec = "C:/mod/Furos/01 FuroBasePlana/FuroBasePLanaT-Df";
			//rutaLec = "C:/mod/Furos/01 FuroBasePlana/FuroBasePLanaT-Df3espiral";//el hueco es mucho mayor que dos veces el diametro la herramienta
			
			/*02 base conica */
			//rutaLec = "C:/mod/Furos/02 FuroBaseConica/FuroBaseConica";
			//rutaLec = "C:/mod/Furos/02 FuroBaseConica/FuroBaseConicaProf5mm";
			//rutaLec = "C:/mod/Furos/02 FuroBaseConica/FuroBaseConicaProf15mm";
			
			/*03 base Esferica */
			//rutaLec = "C:/mod/Furos/03 FuroBaseEsferica/FuroBaseArreT=Df";
			//rutaLec = "C:/mod/Furos/03 FuroBaseEsferica/FuroBaseArreT-Df";
			//rutaLec = "C:/mod/Furos/03 FuroBaseEsferica/FuroBaseArreT-DfM";
			
			/*04 base Arredondada */
			//rutaLec = "C:/mod/Furos/04 FuroBaseArredondada/FuroBaseArredondadaT=Df";
			//rutaLec = "C:/mod/Furos/04 FuroBaseArredondada/FuroBaseArredondadaTr=DfMr";//Trariobase=Dfradiobase pero el furo es mayor que la herramienta
			//rutaLec = "C:/mod/Furos/04 FuroBaseArredondada/FuroBaseArredondadaT-Df";
			
			/*05 Furo Conico */
			//rutaLec = "C:/mod/Furos/05 FuroConico/FuroConico1";
			//rutaLec = "C:/mod/Furos/05 FuroConico/FuroConico2";
			
        	/***************************************************** Ranhuras  ****************************************************************/
        	/*01 RanhuraU paralela Baseplana*/
        	//rutaLec ="C:/mod/Ranhuras/01 RanhuraUBaseplanaT=Dr/RanhuraUBasePlanaT=Dr";
        	//rutaLec ="C:/mod/Ranhuras/01 RanhuraUBaseplanaT=Dr/RanhuraUBasePlanaT=2Dr";
        	//rutaLec ="C:/mod/Ranhuras/01 RanhuraUBaseplanaT=Dr/RanhuraUBasePlanaT=3Dr";
        	//rutaLec ="C:/mod/Ranhuras/01 RanhuraUBaseplanaT=Dr/RanhuraUBasePlanaT=31Dr";
        	//rutaLec ="C:/mod/Ranhuras/01 RanhuraUBaseplanaT=Dr/RanhuraUBasePlanaT=32Dr";
        	//rutaLec ="C:/mod/Ranhuras/01 RanhuraUBaseplanaT=Dr/RanhuraUBasePlanaT=33Dr";
        	//rutaLec ="C:/mod/Ranhuras/01 RanhuraUBaseplanaT=Dr/RanhuraUBasePlanaT=34Dr";
        	
        	/*02 RanhuraU paralela  e en V Base Arredondada*/
        	//rutaLec ="C:/mod/Ranhuras/02 RanhuraPerfilUquadradoBaseRedonda/RanhuraPerfilUquadradoBaseRedonda_ParalelaT=0DrBr"; //paralelo AngBase == Ferra
        	//rutaLec ="C:/mod/Ranhuras/02 RanhuraPerfilUquadradoBaseRedonda/RanhuraPerfilUquadradoBaseRedonda_ParalelaT=DrBr";//paralelo AngBase == Ferra
        	//rutaLec ="C:/mod/Ranhuras/02 RanhuraPerfilUquadradoBaseRedonda/RanhuraPerfilUquadradoBaseRedonda_ParalelaT-AnDrBr";//paralelo AngBase > Ferra
        	//rutaLec ="C:/mod/Ranhuras/02 RanhuraPerfilUquadradoBaseRedonda/RanhuraPerfilUquadradoBaseRedonda_ParalelaT-0AnDrBr";//paralelo AngBase > Ferra
        	//rutaLec ="C:/mod/Ranhuras/02 RanhuraPerfilUquadradoBaseRedonda/RanhuraPerfilUquadradoBaseRedonda_V70gT=DrBr";
        	//rutaLec ="C:/mod/Ranhuras/02 RanhuraPerfilUquadradoBaseRedonda/RanhuraPerfilUquadradoBaseRedonda_V70gT=0DrBr";//teste1
        	//rutaLec ="C:/mod/Ranhuras/02 RanhuraPerfilUquadradoBaseRedonda/RanhuraPerfilUquadradoBaseRedonda_V70gT=01rBr";//teste2
        	
        	/*03 RanhuraU perfil circular*/
        	//rutaLec ="C:/mod/Ranhuras/03 RanhuraPerfilCircular/RanhuraPerfilCircularT=DrBr";
        	//rutaLec ="C:/mod/Ranhuras/03 RanhuraPerfilCircular/RanhuraPerfilCircularT-DrBr";
        	//rutaLec ="C:/mod/Ranhuras/03 RanhuraPerfilCircular/RanhuraPerfilCircularT-01DrBr";
        	
        	/*04 RanhuraU perfil U  circular*/
        	//rutaLec ="C:/mod/Ranhuras/04 RanhuraPerfilUCircular/RanhuraPerfilUCircularT=DrBr";
        	//rutaLec ="C:/mod/Ranhuras/04 RanhuraPerfilUCircular/RanhuraPerfilUCircularT=01DrBr";
        	//rutaLec ="C:/mod/Ranhuras/04 RanhuraPerfilUCircular/RanhuraPerfilUCircularT=02DrBr";
        	//rutaLec ="C:/mod/Ranhuras/04 RanhuraPerfilUCircular/RanhuraPerfilUCircularT=03DrBr";
        	
        	/*05 RanhuraU perfil V*/
        	
        	/*01 Degraus*/
        	//rutaLec ="C:/mod/Degraus/degrauSuperioh";

        	
			//ruto solo mod
        	//rutaLec = "C:/mod/ran.p21"; //pieza de teste
			//rutaLec = "C:/mod/teste2"; //pieza de teste
        	//rutaLec = "C:/mod/teste3"; //pieza de teste
        	//rutaLec = "C:/mod/teste4"; //pieza de teste
        	//rutaLec = "C:/mod/teste5"; //pieza de teste
        	//rutaLec = "C:/mod/teste0"; //pieza de teste
        	//rutaLec = "C:/mod/teste01.p21"; //pieza de teste
			//rutaLec = "C:/mod/TesteFinal"; //pieza de teste
			//rutaLec = "C:/mod/TesteFinalUNBLAB.p21"; //pieza de teste
			//rutaLec = "C:/mod/peçateste.p21"; //pieza de teste
			rutaLec = "C:/mod/Peça_Defesa.p21"; //pieza de teste
			
			rutaEsc = lerDadosArqConfg.DadosarqConfg.get(0);
			rutaEscTBL = lerDadosArqConfg.DadosarqConfg.get(11);
			//rutaEscTBL = "C:/Users/Miguel/Dropbox/Disertacion/tool.tbl";
			rutaMod = lerDadosArqConfg.DadosarqConfg.get(1);//"C:/Code_original.txt";
        	
        }

		
		/***************** Leer archivo de configuraciones ******************/
		/*** freerate
		 * valares de avance en angulo y prof
		*/
		
		
		/************-Copio el archivo a la carpeta mod temporalmente-**************/
		PasarArchivo.Pasar(rutaLec,rutaMod);
		//Ventana1.barra(true);
		
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
		Gcode GenGcode = new Gcode();
		
		//////////////configuro el valor del Angulo para ser el paso de desvaste de la profundidad
		GenGcode.ValorPasoAngulo=5;
		//Ventana1.barra(false);
		GenGcode.GenCodG();
		
		
		
		//despues de esto eliminar el archivo temporal f2.txt
	}
	
	
	
	/*public static class Ventana1 extends JPanel {
	    JProgressBar barra = new JProgressBar();
	
	public Ventana1() {
        setLayout( null );
        barra.setBounds( 5,10,315,20 );
        add( barra );
        UIManager.put( "ProgressBar.repaintInterval",new Integer(20) );
        UIManager.put( "ProgressBar.cycleTime",new Integer(2000) );
        barra.setIndeterminate( true );
    }
	
	public static void barra( boolean inicio) {
		JFrame frame = new JFrame( "Convertendo STEP-NC a Codigo G" );
        frame.setDefaultCloseOperation( frame.EXIT_ON_CLOSE );
        frame.getContentPane().add( new Ventana1(),BorderLayout.CENTER );
        frame.setSize( 340,80 );
        //aqui midifico la pocicion de la ventana
        frame.setBounds(100, 100, 340,80);
        
    	if(inicio==true){
    		frame.setVisible( true );
    		try {
    			Thread.sleep(5000);
    			frame.setVisible( false );
    			
    		} catch (Exception e) {}
    	}else{
    		
    	}
	}
	}*/

}
