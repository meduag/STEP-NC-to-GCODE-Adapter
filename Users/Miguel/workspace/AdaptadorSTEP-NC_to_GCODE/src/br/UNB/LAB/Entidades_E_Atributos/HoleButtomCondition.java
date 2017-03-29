package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class HoleButtomCondition {

	public String data="";
	public static String SalidaHBC="";
	static int ctokens=0, cpar=1;
	public static double PforSphe=0, RadiusSphe=0;
	
	@SuppressWarnings("static-access")
	public static String ExtrairDadosHBC(String data) {
		
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();

		StringTokenizer st = new StringTokenizer(data, "=(#)",true);//buscador de tokens con separadores
		//System.out.println("Valor recivido" + data);
		//// #46=FLAT_HOLE_BOTTOM();
			cpar=1;
			ctokens=0;
			PforSphe=0;
			
			do{//recorro el chararray, en busca de los parentesis
				ctokens++;//contador de tokens para encontrar el parentesis
			    String valores = st.nextToken();//leo el proximo token
			    //System.out.println(ctokens+ " valor " + valores);
			    if (ctokens==4) {
			    	SalidaHBC=valores;
			    	if(valores.startsWith("SPHERICAL")==true | valores.startsWith("FLAT_WITH_RADIUS")==true){
			    		PforSphe=1;
			    	}
					
				}
			    
			    if (ctokens==7 & PforSphe==1) {
			    	int TempEnd = Integer.parseInt(valores);
			    	RadiusSphe=Double.parseDouble(TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd)));
			    	//System.out.println("Valor de sphere radius"+RadiusSphe);
			    	SalidaHBC = SalidaHBC + ",Radius=" + RadiusSphe+";";
			    	cpar=0;
				}
			    
			}while(st.hasMoreTokens() & cpar==1);
		
		
			return SalidaHBC ;
		
	}
}
