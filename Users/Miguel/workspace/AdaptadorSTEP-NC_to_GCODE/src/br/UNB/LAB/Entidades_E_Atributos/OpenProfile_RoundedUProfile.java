package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class OpenProfile_RoundedUProfile {


	static int TempEnd = 0;
	public String data="";
	
	public static String SalidaOpRUP="";
	
	static int ctokens=0, cpar=1;
	
	public static String ExtrairDadosOP_RoundedUP(String data) {
		SalidaOpRUP="";
		ctokens=0;
		cpar=1;
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		
		StringTokenizer st = new StringTokenizer(data, ",)#",true);//buscador de tokens con separadores
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==7) {
				TempEnd = Integer.parseInt(valores);
				SalidaOpRUP=TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
			}
		    
		}while(st.hasMoreTokens() & cpar==1);
		
		
		return SalidaOpRUP;
	}

}
