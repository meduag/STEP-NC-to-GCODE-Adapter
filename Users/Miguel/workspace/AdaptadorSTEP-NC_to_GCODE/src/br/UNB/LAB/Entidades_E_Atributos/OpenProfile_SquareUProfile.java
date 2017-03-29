package br.UNB.LAB.Entidades_E_Atributos;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.FuncoesDePosicao.CalculoDosPontosDaReta;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class OpenProfile_SquareUProfile {


	static int TempEnd = 0;
	public String data="";
	
	//dados do Profile
	static String CompSup="0", CompInf="0", AngBase="0";
	static double calComSup=0,valAng;
	
	public static String SalidaOpSUP="";
	
	static int ctokens=0, cpar=1;
	
	public static String ExtrairDadosOP_SquareUP(String data) {
		SalidaOpSUP="";
		ctokens=0;
		cpar=1;
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		
		DecimalFormat df = new DecimalFormat("0.000");

		
		StringTokenizer st = new StringTokenizer(data, ",)#",true);//buscador de tokens con separadores
		 int sigVal=0;
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==7) {
				TempEnd = Integer.parseInt(valores);
				CompInf=TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
			}
		   
		    if (ctokens==10) {
		    	char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		    	if(aCaracteres[0]!=','){
				TempEnd = Integer.parseInt(valores);
				AngBase=TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));;
				sigVal=1;
		    	}
			}
		    
		    if (ctokens==12 & sigVal==1) {
		    	valAng=Double.parseDouble(valores);
		    	calComSup=Math.ceil(((valAng/0.0174532925199432)*0.245)+(Double.parseDouble(CompInf)));
		    	CompSup=String.valueOf(calComSup);
			}

		}while(st.hasMoreTokens() & cpar==1);
		
		if(CompSup=="0"){
			CompSup=CompInf;
		}
		
		SalidaOpSUP="CoSup="+CompSup + ", CoInf=" + df.format(Double.parseDouble(CompInf )).replaceAll(",", ".") + ", AnBa=" + AngBase;
		
		return SalidaOpSUP;
	}

}
