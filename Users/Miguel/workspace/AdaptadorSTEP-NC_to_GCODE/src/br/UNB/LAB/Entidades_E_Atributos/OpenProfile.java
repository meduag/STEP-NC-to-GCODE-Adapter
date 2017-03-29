package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.*;

public class OpenProfile{

	static int TempEnd = 0;
	static String EndCP="";
	public String EndAP3D="";
	public String data="";
	
	//valores del rectangulo
	public static String Largo="";
	public static String Ancho="";
	
	public static String SalidaOpP="";
	
	static int ctokens=0, cpar=1;
	
	//separadores e identificadores tipo char
	static char parD=')';
	static char vir=',';
	static char parI='(';

	public static String ExtrairDadosOpenPro(String data) {
		SalidaOpP="";
		ctokens=0;
		cpar=1;
		
		OpenProfile_SquareUProfile SquareUPro = new OpenProfile_SquareUProfile();
		OpenProfile_PartialCircularProfile PartCirc = new OpenProfile_PartialCircularProfile();
		OpenProfile_RoundedUProfile RounUPro = new OpenProfile_RoundedUProfile();
		OpenProfile_VeeProfile VeePro = new OpenProfile_VeeProfile();
		
	StringTokenizer st = new StringTokenizer(data, "=(",true);//buscador de tokens con separadores
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==3){
		    	SalidaOpP=valores;
		    	cpar=0;
			}
    
		}while(st.hasMoreTokens() & cpar==1);
		
		//SQUARE_U
		if(SalidaOpP.startsWith("SQUARE_U")==true){
			//System.out.println("Son Iguales" + SalidaOpP);
			SalidaOpP=SalidaOpP + ", "+SquareUPro.ExtrairDadosOP_SquareUP(data);
		}
		
		//PARTIAL_CIRCULAR
		if(SalidaOpP.startsWith("PARTIAL_CIRCULAR")==true){
			//System.out.println("Son Iguales" + SalidaOpP);
			SalidaOpP=SalidaOpP + ", RParcial="+PartCirc.ExtrairDadosOP_ParCirP(data)+";";
		}
		
		//
		if(SalidaOpP.startsWith("ROUNDED_U")==true){
			//System.out.println("Son Iguales" + SalidaOpP);
			SalidaOpP=SalidaOpP + ", Com_Radio="+RounUPro.ExtrairDadosOP_RoundedUP(data)+";";
		}
		
		if(SalidaOpP.startsWith("VEE")==true){
			//System.out.println("Son Iguales" + SalidaOpP);
			SalidaOpP=SalidaOpP + ", "+VeePro.ExtrairDadosOP_VeeP(data)+";";
		}
		

		return SalidaOpP;
	}

}
