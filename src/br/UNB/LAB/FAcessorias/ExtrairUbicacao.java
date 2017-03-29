package br.UNB.LAB.FAcessorias;

import java.util.StringTokenizer;

import javax.vecmath.Point3d;

public class ExtrairUbicacao {

	//private static String ExtrairPocisao="";
	//private static Point3d [] ExtrairPocisaoXYZ;
	private static Point3d ExtrairPocisaoXYZ;
	
	public static Point3d ExtPocisaoXYZ(String data) {
		// TODO Auto-generated method stub
		double x = 0, y = 0, zOffset = 0;
		
		
		StringTokenizer st = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
		int ctokens = 0;
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = st.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);
		if(ctokens==5){//para X
			x=Double.parseDouble(valores);	
			}
		if(ctokens==9){//para Y
			y=Double.parseDouble(valores);	
			}
		if(ctokens==13){//para Z
			zOffset=Double.parseDouble(valores);	
			}
		}while(st.hasMoreTokens());
		
		ExtrairPocisaoXYZ = new Point3d(x, y, zOffset);

		return ExtrairPocisaoXYZ;
		
	}



}
