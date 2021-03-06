package br.UNB.LAB.GerarCodG;

import java.util.ArrayList;
import java.util.StringTokenizer;

import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;
import br.UNB.LAB.Integrador.Integrador;



public class TrocaDeFerramenta {

	//public static String data="Aqui va la info de la herramienta";
	public static ArrayList<String> listDadosFerramentas = new ArrayList<String>();
	public static int numeroD=1, contFer=1,LineNo ;
	static String dataAnt="", ImpFerChamada="";
	
	static String tipo= "", diametro= "", RadioBase= "", AngOffset= "",Tlo = "";
	public static double Diametro_Tool=0, RadioBaseBulEM=0,TloNum=0;
	
		public static void ImpriFer(String data) {
			

				ImprimirSecuenciaLineaNo.EscribirLinea("M05");
				
				//if(Integrador.ImpG55==1){
					if(Integrador.ImpG55==1){
						ImprimirSecuenciaLineaNo.EscribirLinea("G90 G40 G55 G00 Z" + Gcode.df.format(Gcode.ZM06));
					}else{
						ImprimirSecuenciaLineaNo.EscribirLinea("G90 G40 G00 Z" + Gcode.df.format(Gcode.ZM06));
					}
					ImprimirSecuenciaLineaNo.EscribirLinea("G01 F" + Gcode.df.format(Gcode.MaxVelAvanceMaquina) +" X"+Gcode.df.format(0) + " Y"+ Gcode.df.format(0));
					ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
				//}
				
				


			int ctokens=0;
			int ini=1;
			int salir=1;
			//System.out.println("Troca de ferramenta:  " + data);
			StringTokenizer stf = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
			do{
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = stf.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				if(ctokens==11){//copia el primer token con la primera ferramenta
					tipo=valores;
					}
				if(ctokens==15){//copia el primer token con la primera ferramenta
					diametro=valores;
					//ini=0;
					}
				if(ctokens==19){//copia el primer token con la primera ferramenta
					Tlo=valores;
					TloNum = Double.parseDouble(valores);
					//ini=0;
					}
				if(ctokens==23){//copia el primer token con la primera ferramenta
					RadioBase=valores;
					//ini=0;
					}
				if(ctokens==27){//copia el primer token con la primera ferramenta
					AngOffset=valores;
					//ini=0;
					}
				}while(stf.hasMoreTokens() & ini==1);
			
			
			if(tipo.startsWith("CENTER_DRILL")==true){
				ImpFerChamada = tipo + "," + diametro;
				Diametro_Tool=Double.parseDouble(diametro);
				}
			if(tipo.startsWith("TWIST_DRILL")==true){
				ImpFerChamada =tipo +"," + diametro+","+RadioBase+","+AngOffset;
				Diametro_Tool=Double.parseDouble(diametro);
				}
			if(tipo.startsWith("FACEMILL")==true){
				ImpFerChamada =tipo +"," + diametro+","+RadioBase;
				Diametro_Tool=Double.parseDouble(diametro);
				}
			if(tipo.startsWith("BALL_ENDMILL")==true){
				ImpFerChamada =tipo +"," + diametro+","+RadioBase;
				Diametro_Tool=Double.parseDouble(diametro);
				}
			if(tipo.startsWith("BULLNOSE_ENDMILL")==true){
				ImpFerChamada =tipo +"," + diametro+","+RadioBase;
				Diametro_Tool=Double.parseDouble(diametro);
				}
			
			
			
			//Asigno la ferramenta dependendo si esta en el orden
			if(tipo.startsWith("CENTER_DRILL")==true & salir==1){
				int tamListt = OrdenarFerramentas.listDadosTrocaFerra.size();
				for (int i = 0; i < tamListt; i++) {
					String Data = OrdenarFerramentas.listDadosTrocaFerra.get(i);
					StringTokenizer st = new StringTokenizer(Data, ";");//buscador de tokens con separadores activados
					String valores = st.nextToken();
					//System.out.println("Valor="+(i+1) + "   token=" + valores);
					if(valores.indexOf(ImpFerChamada)!=-1){
						String ValoresData = valores;
						StringTokenizer stV = new StringTokenizer(ValoresData, ",");//buscador de tokens con separadores activados
						String valoresData = stV.nextToken();
						//System.out.println(valoresData);
						
						//pasar el valor a numeroD
						char[] aCaracteres = valoresData.toCharArray();//convierto la cadena a un chararray
						String NumDTem = String.valueOf(aCaracteres[1]);
						numeroD = Integer.parseInt(NumDTem);
						
						if(Integrador.ImpLedFer==1){
							System.out.println("(Troca de Ferramenta)");
						}
						/*FuroSimples.LineNo= LineNo;
						String troca="G40";
						FuroSimples.WriteLineNo(troca);*/
						ImprimirSecuenciaLineaNo.EscribirLinea("M06 T" + numeroD);
					/*	String troca="M06 T" + numeroD;
						FuroSimples.WriteLineNo(troca);*/
						
						i = tamListt;
					}
					
				}
				salir=0;
			}//fin de la iumpresion
			
			if(tipo.startsWith("TWIST_DRILL")==true & salir==1){
				int tamListt = OrdenarFerramentas.listDadosTrocaFerra.size();
				for (int i = 0; i < tamListt; i++) {
					String Data = OrdenarFerramentas.listDadosTrocaFerra.get(i);
					StringTokenizer st = new StringTokenizer(Data, ";");//buscador de tokens con separadores activados
					String valores = st.nextToken();
					//System.out.println("Valor="+(i+1) + "   token=" + valores);
					if(valores.indexOf(ImpFerChamada)!=-1){
						String ValoresData = valores;
						StringTokenizer stV = new StringTokenizer(ValoresData, ",");//buscador de tokens con separadores activados
						String valoresData = stV.nextToken();
//						System.out.println(valoresData);
						
						//pasar el valor a numeroD
						char[] aCaracteres = valoresData.toCharArray();//convierto la cadena a un chararray
						String NumDTem = String.valueOf(aCaracteres[1]);
						numeroD = Integer.parseInt(NumDTem);
						
						if(Integrador.ImpLedFer==1){
							System.out.println("(Troca de Ferramenta)");
						}
						/*FuroSimples.LineNo= LineNo;
						String troca="G40";
						FuroSimples.WriteLineNo(troca);*/
						ImprimirSecuenciaLineaNo.EscribirLinea("M06 T" + numeroD);
						/*String troca="M06 T" + numeroD;
						FuroSimples.WriteLineNo(troca);*/
						i = tamListt;
					}
					
				}
				salir=0;
			}//fin de la iumpresion
			
			
			
			if(tipo.startsWith("FACEMILL")==true & salir==1){
				int tamListt = OrdenarFerramentas.listDadosTrocaFerra.size();
				for (int i = 0; i < tamListt; i++) {
					String Data = OrdenarFerramentas.listDadosTrocaFerra.get(i);
					StringTokenizer st = new StringTokenizer(Data, ";");//buscador de tokens con separadores activados
					String valores = st.nextToken();
					//System.out.println("Valor="+(i+1) + "   token=" + valores);
					if(valores.indexOf(ImpFerChamada)!=-1){
						String ValoresData = valores;
						StringTokenizer stV = new StringTokenizer(ValoresData, ",");//buscador de tokens con separadores activados
						String valoresData = stV.nextToken();
//						System.out.println(valoresData);
						
						//pasar el valor a numeroD
						char[] aCaracteres = valoresData.toCharArray();//convierto la cadena a un chararray
						String NumDTem = String.valueOf(aCaracteres[1]);
						numeroD = Integer.parseInt(NumDTem);
						
						if(Integrador.ImpLedFer==1){
							System.out.println("(Troca de Ferramenta)");
						}
						/*FuroSimples.LineNo= LineNo;
						String troca="G40";
						FuroSimples.WriteLineNo(troca);*/
						ImprimirSecuenciaLineaNo.EscribirLinea("M06 T" + numeroD);
						/*String troca="M06 T" + numeroD;
						FuroSimples.WriteLineNo(troca);*/
						i = tamListt;
					}
					
				}
				salir=0;
			}//fin de la iumpresion
			
			
			
			if(tipo.startsWith("BALL_ENDMILL")==true & salir==1){
				int tamListt = OrdenarFerramentas.listDadosTrocaFerra.size();
				for (int i = 0; i < tamListt; i++) {
					String Data = OrdenarFerramentas.listDadosTrocaFerra.get(i);
					StringTokenizer st = new StringTokenizer(Data, ";");//buscador de tokens con separadores activados
					String valores = st.nextToken();
					//System.out.println("Valor="+(i+1) + "   token=" + valores);
					if(valores.indexOf(ImpFerChamada)!=-1){
						String ValoresData = valores;
						StringTokenizer stV = new StringTokenizer(ValoresData, ",");//buscador de tokens con separadores activados
						String valoresData = stV.nextToken();
//						System.out.println(valoresData);
						
						//pasar el valor a numeroD
						char[] aCaracteres = valoresData.toCharArray();//convierto la cadena a un chararray
						String NumDTem = String.valueOf(aCaracteres[1]);
						numeroD = Integer.parseInt(NumDTem);
						
						if(Integrador.ImpLedFer==1){
							System.out.println("(Troca de Ferramenta)");
						}
						/*FuroSimples.LineNo= LineNo;
						String troca="G40";
						FuroSimples.WriteLineNo(troca);*/
						ImprimirSecuenciaLineaNo.EscribirLinea("M06 T" + numeroD);
						/*String troca="M06 T" + numeroD;
						FuroSimples.WriteLineNo(troca);*/
						i = tamListt;
					}
					
				}
				salir=0;
			}//fin de la iumpresion
			
			
			
			if(tipo.startsWith("BULLNOSE_ENDMILL")==true & salir==1){
				int tamListt = OrdenarFerramentas.listDadosTrocaFerra.size();
				for (int i = 0; i < tamListt; i++) {
					String Data = OrdenarFerramentas.listDadosTrocaFerra.get(i);
					StringTokenizer st = new StringTokenizer(Data, ";");//buscador de tokens con separadores activados
					String valores = st.nextToken();
					//System.out.println("Valor="+(i+1) + "   token=" + valores);
					if(valores.indexOf(ImpFerChamada)!=-1){
						String ValoresData = valores;
						StringTokenizer stV = new StringTokenizer(ValoresData, ",");//buscador de tokens con separadores activados
						String valoresData = stV.nextToken();
//						System.out.println(valoresData);
						
						//pasar el valor a numeroD
						char[] aCaracteres = valoresData.toCharArray();//convierto la cadena a un chararray
						String NumDTem = String.valueOf(aCaracteres[1]);
						numeroD = Integer.parseInt(NumDTem);
						
						if(Integrador.ImpLedFer==1){
							System.out.println("(Troca de Ferramenta)");
						}
						/*FuroSimples.LineNo= LineNo;
						String troca="G40";
						FuroSimples.WriteLineNo(troca);*/
						ImprimirSecuenciaLineaNo.EscribirLinea("M06 T" + numeroD);
						/*String troca="M06 T" + numeroD;
						FuroSimples.WriteLineNo(troca);*/
						i = tamListt;
					}
					
				}
				salir=0;
			}//fin de la iumpresion

			if(Integrador.ImpG55==1){
				ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(Gcode.ZM06));
				ImprimirSecuenciaLineaNo.EscribirLinea("M03");
				ImprimirSecuenciaLineaNo.EscribirLinea("G54");
			}
			
		}
		
		
		
		
		
		public static double DatoDiametroTool(String dataFerramenta) {
			double DadoToolAtual=0;
			int ctokens=0;
			int ini=1;
			//System.out.println("Troca de ferramenta:  " + data);
			StringTokenizer stf = new StringTokenizer(dataFerramenta, ":=,;",true);//buscador de tokens con separadores activados
			do{
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = stf.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				if(ctokens==15){//copia el primer token con la primera ferramenta
					diametro=valores;
					DadoToolAtual=Double.parseDouble(diametro);
					ini=0;
					}
				}while(stf.hasMoreTokens() & ini==1);
			
			return DadoToolAtual;
			}
		
		
		
		
		public static double DatoAngBAseTool(String dataFerramenta) {
			double DadoRadioBaseToolAtual=0;
			int ctokens=0;
			int ini=1;
			//System.out.println("Troca de ferramenta:  " + data);
			StringTokenizer stf = new StringTokenizer(dataFerramenta, ":=,;",true);//buscador de tokens con separadores activados
			do{
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = stf.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				if(ctokens==23){//copia el primer token con la primera ferramenta
					RadioBaseBulEM=Double.parseDouble(valores);
					DadoRadioBaseToolAtual=RadioBaseBulEM;
					ini=0;
					}
				}while(stf.hasMoreTokens() & ini==1);
			
			return DadoRadioBaseToolAtual;
			}
}
