package br.UNB.LAB.MachiningOperations_E_Ferramentas;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.MillingToolDimension;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;

public class Ferramenta{

	//static int TempEnd = 0;
	static String TipoFer="",InfoFer="";
	public String data="";
	

	
	public static String SalidaFerCeD="";
	
	static int ctokens=0, cpar=1;
	
	//separadores e identificadores tipo char
	//static char parD=')';
	//static char vir=',';
	//static char parI='(';

	@SuppressWarnings("static-access")
	public static String ExtrairDadosFerCD(String data) {
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		MillingToolDimension MillTD = new MillingToolDimension();
		
		if(data.indexOf("TWIST_DRILL")!=-1){
			MillTD.AdqPuntaTwDrill=1;//adquirir el valor del angulo offset de la herramienta twdrill
		}else{
			MillTD.AdqPuntaTwDrill=0;//No adquirir el valor del angulo offset de la herramienta twdrill
		}
		
	StringTokenizer st = new StringTokenizer(data, "=(#,",true);//buscador de tokens con separadores
		ctokens=0;
		cpar=1;
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    //System.out.println(ctokens+ " valor " + valores);
		    if (ctokens==4) {
		    	TipoFer=valores;
		    	}
		    
		    if (ctokens==7) {
		    	int TempEnd=Integer.parseInt(valores);
		    	InfoFer=MillTD.ExtrairDadosMillToDi(SecEnder.IndexacaoEnderecos.get(TempEnd));
		    	cpar=0;
			}
    
		}while(st.hasMoreTokens() & cpar==1);

		SalidaFerCeD = ",TipoFer=" + TipoFer + InfoFer+";";
		
		return SalidaFerCeD;
	}

}

