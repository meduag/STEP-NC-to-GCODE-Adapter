package br.UNB.LAB.FAcessorias;

import java.util.StringTokenizer;

public class ExtrairToken3_Profundidad_DiaInicial_DiaFinal {

	public static double ExtrairToken3(String data, String Selector) {
		// TODO Auto-generated method stub
		double valor=0;
		String tok="";
		
		if(Selector=="P"){
			tok="=;";
		}else{
			tok=":;";
		}
		
		StringTokenizer st = new StringTokenizer(data, tok,true);//buscador de tokens con separadores activados
		
		int ctokens = 0;
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = st.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);
			if(ctokens==3){//copia el primer token con la primera ferramenta
				valor=Double.parseDouble(valores);	
			}
		}while(st.hasMoreTokens());

		return valor;
		
	}


}
