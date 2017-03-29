package br.UNB.LAB.Entidades_E_Atributos;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class OpenProfile_VeeProfile {


	static int TempEnd = 0;
	static String EndCP="";
	public String EndAP3D="";
	public String data="";
	
	//dados do Profile
	static String CompSup, CompInf, AngBase;
	static double calComSup=0,valAng;
	
	public static String SalidaOpVP="";
	
	static int ctokens=0, cpar=1;
	
	public static String ExtrairDadosOP_VeeP(String data) {
		SalidaOpVP="";
		ctokens=0;
		cpar=1;
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		
		DecimalFormat df = new DecimalFormat("0.000");

		
		StringTokenizer st = new StringTokenizer(data, ",)#",true);//buscador de tokens con separadores
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==7) {
				TempEnd = Integer.parseInt(valores);
				SalidaOpVP="RadBase="+TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
				
			}
		    
		    if (ctokens==9) {
		    	//valor del angulo entre paredes
		    	SalidaOpVP = SalidaOpVP + ", AngV="+(Double.parseDouble(valores)*180)/Math.PI;//usar df fi fuera necesario
		    	}
		    
		    if (ctokens==11) {
		    	//valor del angulo de inclinacion
		    	SalidaOpVP = SalidaOpVP + ", AngInc="+(Double.parseDouble(valores)*180)/Math.PI;
			}

		}while(st.hasMoreTokens() & cpar==1);
		
		
		return SalidaOpVP;
	}

}