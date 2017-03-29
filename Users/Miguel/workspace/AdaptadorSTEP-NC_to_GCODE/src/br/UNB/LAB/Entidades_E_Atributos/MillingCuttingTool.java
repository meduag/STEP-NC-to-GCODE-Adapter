package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.MachiningOperations_E_Ferramentas.Ferramenta;


public class MillingCuttingTool{

	static int TempEnd = 0;
	public String data="";
	public static String SalidaFCP="";
	static int ctokens=0, cpar=1;
	static String NomFer="";
	

	@SuppressWarnings("static-access")
	public static String ExtrairDadosMCT(String data) {
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		Ferramenta FerCD = new Ferramenta();
		
	StringTokenizer st = new StringTokenizer(data, ",#'",true);//buscador de tokens con separadores
	
		ctokens=0;
		cpar=1;
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    
		    if (ctokens==4) {
		    	NomFer=valores;
			}
		    
		    if (ctokens==8) {
		    	int TempEnd=Integer.parseInt(valores);
		    	SalidaFCP=FerCD.ExtrairDadosFerCD(SecEnder.IndexacaoEnderecos.get(TempEnd));
		    	//NomFer=valores;
		    	cpar=0;
			}
    
		}while(st.hasMoreTokens() & cpar==1);

		SalidaFCP= "NomeFer=" + NomFer + SalidaFCP;
		
		return SalidaFCP;
	}

}