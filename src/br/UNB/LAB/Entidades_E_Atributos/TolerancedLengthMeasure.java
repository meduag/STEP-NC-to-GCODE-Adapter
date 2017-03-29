package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

public class TolerancedLengthMeasure {

	public String data="";
	public static String SalidaTLM="";
	static int ctokens=0, cpar=1;



	public static String ExtrairDadosTLM(String data) {

		StringTokenizer st = new StringTokenizer(data, "(,",true);//buscador de tokens con separadores
		//System.out.println("Valor recivido" + data);
			cpar=1;
			ctokens=0;
			
			do{//recorro el chararray, en busca de los parentesis
				ctokens++;//contador de tokens para encontrar el parentesis
			    String valores = st.nextToken();//leo el proximo token
			    if (ctokens==3) {
					//System.out.println(ctokens+ " valor " + valores);
			    	SalidaTLM=valores;
					cpar=0;
				}
			}while(st.hasMoreTokens() & cpar==1);
		
		
			return SalidaTLM;
		
	}

}
