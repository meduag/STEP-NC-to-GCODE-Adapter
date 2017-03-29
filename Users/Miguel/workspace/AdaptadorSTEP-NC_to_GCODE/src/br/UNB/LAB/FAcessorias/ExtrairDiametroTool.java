package br.UNB.LAB.FAcessorias;

import java.util.StringTokenizer;

public class ExtrairDiametroTool {

	public static double ExtrairDiametroFerrameta(String DadosFerramenta) {
		double DiaTool=0;
		
		//extraer diametro de la toll
		StringTokenizer st = new StringTokenizer(DadosFerramenta, ":,=;",true);//buscador de tokens con separadores activados
		int ctokens = 0;
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = st.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);

			if(ctokens==15){//copia el primer token con la primera ferramenta
				DiaTool=Double.parseDouble(valores);
			}
		}while(st.hasMoreTokens());

		return DiaTool;
	}

}
