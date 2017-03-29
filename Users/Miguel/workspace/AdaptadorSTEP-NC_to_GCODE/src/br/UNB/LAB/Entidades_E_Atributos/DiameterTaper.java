package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;


public class DiameterTaper{

	static int TempEnd = 0;
	public String data="";
	
	public static String SalidaDTap="";
	
	static int ctokens=0, cpar=1;


	@SuppressWarnings("static-access")
	public static String ExtrairDadosDiaTaper(String data) {
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		
	StringTokenizer st = new StringTokenizer(data, "#)",true);//buscador de tokens con separadores
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==4) {
		    	TempEnd = Integer.parseInt(valores);
		    	SalidaDTap=TooLM.ExtrairDadosTLM((SecEnder.IndexacaoEnderecos.get(TempEnd)));
			}
    
		}while(st.hasMoreTokens() & cpar==1);

		return SalidaDTap;
	}

}


