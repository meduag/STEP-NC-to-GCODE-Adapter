package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;


public class RectangularClosedProfile {

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

	public static String ExtrairDadosRecgCP(String data) {
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		
		
		
		
		
		
		//System.out.println("Valor recivido" + data);
		StringTokenizer st = new StringTokenizer(data, "(,)#",true);//buscador de tokens con separadores
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    
		    if(ctokens==4){
		    	char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		    	if(aCaracteres[0]=='$'){//solo copia numeros
		    		ctokens++;	
				}
		    	
		    }
		    
		    if (ctokens==8) {
				TempEnd = Integer.parseInt(valores);
				Ancho = TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
				//System.out.println("Ancho: " + Ancho );
				//data = SecEnder.Enderecos[TempEnd];
				//cpar=0;
			}
		    
		    if (ctokens==11) {
				TempEnd = Integer.parseInt(valores);
				Largo = TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd));
				//System.out.println("Largo: " + Largo);
				//data = SecEnder.Enderecos[TempEnd];
				cpar=0;
			}
		    
		    
		    
		    
		}while(st.hasMoreTokens() & cpar==1);
		
		data = "Largo=" + Largo + ",Ancho="+Ancho;
		return data;
	}

}
