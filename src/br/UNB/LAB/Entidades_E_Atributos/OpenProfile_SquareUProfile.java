package br.UNB.LAB.Entidades_E_Atributos;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.Features.FeatureSlot;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class OpenProfile_SquareUProfile {


	static int TempEnd = 0;
	public String data="";
	
	//dados do Profile
	static String CompSup="0", CompInf="0", AngBase="0";
	static double calComSup=0,valAng;
	
	public static String SalidaOpSUP="";
	
	static int ctokens=0, cpar=1;
	
	@SuppressWarnings("static-access")
	public static String ExtrairDadosOP_SquareUP(String data) {
		CompSup="0";
		CompInf="0";
		AngBase="0";
		SalidaOpSUP="";
		ctokens=0;
		cpar=1;
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		
		DecimalFormat df = new DecimalFormat("0.00");

		
		StringTokenizer st = new StringTokenizer(data, ",)#",true);//buscador de tokens con separadores
		 int sigVal=0;
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==7) {
				TempEnd = Integer.parseInt(valores);
				CompInf=TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
				double temp = Double.parseDouble(CompInf);
				String compINftemp =  df.format(temp).replace(',', '.');
				CompInf=String.valueOf(compINftemp);
			}
		   
		    if (ctokens==10) {
		    	char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		    	if(aCaracteres[0]!=','){
				TempEnd = Integer.parseInt(valores);
				AngBase=TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
				sigVal=1;
		    	}
			}
		    
		    if (ctokens==12 & sigVal==1) {
		    	valAng = Double.parseDouble(valores);
		    	double ang = Double.parseDouble(valores);
		    	ang = 90-Math.toDegrees(ang);
		    	double Hipot = FeatureSlot.ValorProfundidad/Math.sin(Math.toRadians(ang));
		    	//System.out.println(ValAncX );
		    	double comprimentoSup = Math.sqrt((Hipot*Hipot)-(FeatureSlot.ValorProfundidad*FeatureSlot.ValorProfundidad));
		    	comprimentoSup = Math.ceil((comprimentoSup*2) + Double.parseDouble(CompInf));
		    	CompSup=String.valueOf(comprimentoSup);
			}

		}while(st.hasMoreTokens() & cpar==1);
		
		if(CompSup=="0"){
			CompSup=CompInf;
		}
		
		SalidaOpSUP="CoSup="+CompSup + ", CoInf=" + df.format(Double.parseDouble(CompInf )).replaceAll(",", ".") + ", AnBa=" + AngBase;
		
		return SalidaOpSUP;
	}

}
