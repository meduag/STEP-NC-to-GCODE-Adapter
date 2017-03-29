package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

public class Direction{

	static int TempEnd = 0;
	static String EndCP="";
	public String EndAP3D="";
	public String data="";
	
	//valores del rectangulo
	public static String Largo="";
	public static String Ancho="";
	
	public static String SalidaRCP="";
	
	static int ctokens=0, cpar=1;
	
	//separadores e identificadores tipo char
	static char parD=')';
	static char vir=',';
	static char parI='(';

	public static String ExtrairDadosDirHV(String data) {
		
	StringTokenizer st = new StringTokenizer(data, "(,",true);//buscador de tokens con separadores
		cpar=1;
		ctokens=0;
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==6) {
		    	SalidaRCP=valores;
		    	cpar=0;
			}
    
		}while(st.hasMoreTokens() & cpar==1);

		return SalidaRCP;
	}

}

