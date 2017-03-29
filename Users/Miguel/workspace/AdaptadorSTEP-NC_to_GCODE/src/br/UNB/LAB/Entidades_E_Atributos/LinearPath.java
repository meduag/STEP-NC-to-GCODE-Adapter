package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.Direction;
import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class LinearPath {

	static int TempEnd = 0;
	static String EndCP="";
	public String EndAP3D="";
	public String data="";
	static String TrvPath="";
	
	public static String SalidaLinP="";
	
	static int ctokens=0, cpar=1;
	
	@SuppressWarnings("static-access")
	public static String ExtrairDadosLinPath(String data) {
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		Direction Dir = new Direction();
		
		StringTokenizer st2 = new StringTokenizer(data, "=(",true);//buscador de tokens con separadores
		ctokens=0;
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores2 = st2.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==3) {
		    	TrvPath=valores2;
		    	cpar=0;
			}


		}while(st2.hasMoreTokens() & cpar==1);
		
		
		
		StringTokenizer st = new StringTokenizer(data, ",)#",true);//buscador de tokens con separadores
		
		ctokens=0;
		cpar=1;
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==7) {
				TempEnd = Integer.parseInt(valores);
				SalidaLinP=TooLM.ExtrairDadosTLM((SecEnder.IndexacaoEnderecos.get(TempEnd)));
			}
		    
		    if (ctokens==10) {
				TempEnd = Integer.parseInt(valores);
				String Dird=""; 
				if(Dir.ExtrairDadosDirHV(SecEnder.IndexacaoEnderecos.get(TempEnd)).startsWith("1")==true){
					Dird="Hor";
				}else{
					Dird="Ver";
				}
				
				SalidaLinP="CompFea="+SalidaLinP + ",Dir="+Dird+";";
				cpar=0;
			}

		}while(st.hasMoreTokens() & cpar==1);
		
		SalidaLinP="Tipo="+TrvPath +"," +SalidaLinP;

		return SalidaLinP;
	}

}
