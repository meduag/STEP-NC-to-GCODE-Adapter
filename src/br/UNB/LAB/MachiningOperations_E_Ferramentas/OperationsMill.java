package br.UNB.LAB.MachiningOperations_E_Ferramentas;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.MillingCuttingTool;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;


public class OperationsMill {

	public String data="";
	static String Salida="";
	public static double ProfOpe=0;

	
	static int ctokens=0, cpar=1;
	
	@SuppressWarnings("static-access")
	public static String ExtrairDadosCD(String data) {
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		MillingCuttingTool MillCT = new MillingCuttingTool();
		
		StringTokenizer st = new StringTokenizer(data, ",#",true);//buscador de tokens con separadores
		ctokens=0;
		cpar=1;
		
	    //System.out.println("Operacion   "+data);
	    
	    
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token

		    //System.out.println(ctokens+ " valor " + valores);
		    
		    if(ctokens==10){
		    	char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		    	if(aCaracteres[0]=='$'){//solo copia numeros
		    		ctokens++;	
				}
		    	
		    }
		    	
		    if (ctokens==14) {
		    	int TempEnd=Integer.parseInt(valores);
		    	Salida=MillCT.ExtrairDadosMCT(SecEnder.IndexacaoEnderecos.get(TempEnd));
		    	//cpar=0;
			}
		    
		    if (ctokens==24 & ProfOpe==1) {
		    	char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		    	if(aCaracteres[0]=='$'){//solo copia numeros
		    		cpar=0;
		    		ProfOpe=0;
				}else{
		    	ProfOpe=Double.parseDouble(valores);
		    	cpar=0;
				}
			}
		    
		    //if
    
		}while(st.hasMoreTokens() & cpar==1);
		
		return Salida;
	}

}
