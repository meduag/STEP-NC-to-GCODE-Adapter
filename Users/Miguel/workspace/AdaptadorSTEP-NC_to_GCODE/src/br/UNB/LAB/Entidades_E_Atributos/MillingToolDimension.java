package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

public class MillingToolDimension{

	public String data="";
	
	public static String SalidaRCP="";
	
	static String Dia="";
	static String CutEdgeLeng="";
	static String EdgeRad="";
	static String AngOffTwD="";
	//static String EdgeCentHor="";
	
	static int ctokens=0, cpar=1;
	public static int AdqPuntaTwDrill=0;


	public static String ExtrairDadosMillToDi(String data) {
		
	StringTokenizer st = new StringTokenizer(data, "(,)",true);//buscador de tokens con separadores
		ctokens=0;
		cpar=1;
		

		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==3) {
		    	Dia=valores;
		    	//cpar=0;
			}
		    
		    if (ctokens==5 & AdqPuntaTwDrill==1) {
		    	AngOffTwD=valores;
		    	//cpar=0;
			}
		    
		    
		    if (ctokens==9) {
		    	CutEdgeLeng=valores;
		    	//cpar=0;
			}
		    
		    if (ctokens==11) {
		    	EdgeRad=valores;
		    	//cpar=0;
			}
		    
//		    if (ctokens==15) {
//		    	EdgeCentHor=valores;
//		    	cpar=0;
//			}
    
		}while(st.hasMoreTokens() & cpar==1);
		

		if(AdqPuntaTwDrill==1){
			SalidaRCP= ",DiaFer="+Dia +",LargoCorteFer=" + CutEdgeLeng + ",RadioBase=" + EdgeRad + ",AngOffset TwD=" + AngOffTwD;
		}else{
			SalidaRCP= ",DiaFer="+Dia +",LargoCorteFer=" + CutEdgeLeng + ",RadioBase=" + EdgeRad;
		}
		//SalidaRCP= ",DiaFer="+Dia +",LargoCorteFer=" + CutEdgeLeng + ",LargoCentroBase=" + EdgeCentHor + ",RadioBase=" + EdgeRad;
		
		
		return SalidaRCP;
	}

}


