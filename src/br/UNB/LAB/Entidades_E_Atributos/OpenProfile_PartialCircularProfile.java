package br.UNB.LAB.Entidades_E_Atributos;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class OpenProfile_PartialCircularProfile {


	static int TempEnd = 0;
	public String data="";
	
	//dados do Profile
	static String RadioParcial;
	
	static int ctokens=0, cpar=1;
	
	public static String ExtrairDadosOP_ParCirP(String data) {
		RadioParcial="";
		ctokens=0;
		cpar=1;
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		
		//DecimalFormat df = new DecimalFormat("0.000");

		
		StringTokenizer st = new StringTokenizer(data, ",)#",true);//buscador de tokens con separadores
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==7) {
				TempEnd = Integer.parseInt(valores);
				RadioParcial=TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
				cpar=0;
			}
		}while(st.hasMoreTokens() & cpar==1);
		
		return RadioParcial;
	}

}
