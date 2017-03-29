package br.UNB.LAB.Entidades_E_Atributos;

import java.util.StringTokenizer;

import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.MachiningOperations_E_Ferramentas.OperationsMill;



public class Axis2Placement3D {
	
	private static int TempEnd = 0;
	private static int plane=1;
	private static String SalidaAP3d="";
	private static double nValOpDrilling=0;
	private static int ctokens=0, cpar=1;
	public static double RestarCompBloco=0;
	public static double ProfPasante=0;
	public static int PlanSeg=0;
	public static double offCeterDrill=0;
	public static boolean pasante=false;
	

	@SuppressWarnings("static-access")
	public static String ExtrairDadosAP3D(String data) {
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		OperationsMill nValOp = new OperationsMill();

		//PlanSeg=0;
		nValOpDrilling=0;
		plane=1;
		if(data.contains("PLANE(")){
			cpar=1;
			ctokens=0;
			
			StringTokenizer st3 = new StringTokenizer(data, "#)",true);//buscador de tokens con separadores
			do{//recorro el chararray, en busca de los parentesis
				ctokens++;//contador de tokens para encontrar el parentesis
			    String valores = st3.nextToken();//leo el proximo token
			    if (ctokens==4) {
					//System.out.println(ctokens+ " valor " + valores);
					TempEnd = Integer.parseInt(valores);
					//
					//data = SecEnder.Enderecos[TempEnd];
					//System.out.println(data);
					data=SecEnder.IndexacaoEnderecos.get(TempEnd);
					cpar=0;
				}
			}while(st3.hasMoreTokens() & cpar==1);
				
			plane=0;
		}
		
		StringTokenizer st = new StringTokenizer(data, ",#",true);//buscador de tokens con separadores
		
		cpar=1;
		ctokens=0;
		
		do{//recorro el chararray, en busca de los parentesis
			ctokens++;//contador de tokens para encontrar el parentesis
		    String valores = st.nextToken();//leo el proximo token
		    if (ctokens==5) {
				//System.out.println(ctokens+ " valor " + valores);
				TempEnd = Integer.parseInt(valores);
				data=SecEnder.IndexacaoEnderecos.get(TempEnd);
				//data = SecEnder.Enderecos[TempEnd];
				cpar=0;
				SalidaAP3d ="";
			}
		}while(st.hasMoreTokens() & cpar==1);
		
		
		StringTokenizer st2 = new StringTokenizer(data, "(,)",true);//buscador de tokens con separadores
		cpar=1;
		ctokens=0;
  
		do{//recorro el chararray, en busca de los parentesis
  		   ctokens++;//contador de tokens para encontrar el parentesis
  		   
		   String valores = st2.nextToken();//leo el proximo token
		   char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		   
		   if(aCaracteres[0]==')'){//encuentro el token ')' parD
			   cpar=0;//salgo del wile
		   }
		   
		  if (plane==1){ 
		   if(cpar==1){
			  if(ctokens>=6){//solo copia numeros
				 if(aCaracteres[0]==',' | aCaracteres[0]=='('){//solo copia numeros
				  }else{
					//System.out.println(valores);
					switch (ctokens) {
						case 6:
							//System.out.println("X"+valores);
							SalidaAP3d = SalidaAP3d + "X=" + valores+",";
							break;
						case 8:
							//System.out.println("Y"+valores);
							SalidaAP3d = SalidaAP3d + "Y=" + valores +",";
							break;
						case 10:
							//System.out.println("Z"+valores);
							if(Math.abs(RestarCompBloco-Double.parseDouble(valores))>0){
								//System.out.println("(furo na mesma pociçao)");
								SalidaAP3d = SalidaAP3d + "Z=" + Math.abs(RestarCompBloco-Double.parseDouble(valores)) + ";";
								//copia el valor para dejarlo en pasante
								ProfPasante=RestarCompBloco;
								pasante=true;
							}else{
								//System.out.println("Menor que cero");
								SalidaAP3d = SalidaAP3d + "Z=" + Math.abs(RestarCompBloco-Double.parseDouble(valores)) + ";";	
							}
								
							//SalidaAP3d = SalidaAP3d + "Z=" + Math.abs(RestarCompBloco-Double.parseDouble(valores)) + ";";	
							break;
					}
				  }	 
			  }
		   }
		  }else{
			  if(cpar==1){
				  if(ctokens>=6){//solo copia numeros
					 if(aCaracteres[0]==',' | aCaracteres[0]=='('){//solo copia numeros
					  }else{
						//System.out.println(valores);
						switch (ctokens) {
							case 10:
								//System.out.println("Z"+valores);
								if(PlanSeg==2){
									nValOpDrilling = nValOp.ProfOpe;
									nValOpDrilling = (nValOpDrilling-Math.abs(Double.parseDouble(valores)));
									//System.out.println("Valor a sumar"+nValOpDrilling);
									
									if(nValOpDrilling<0){
										nValOpDrilling=0;
									}	
									if(pasante==false){
									offCeterDrill = nValOpDrilling;
									SalidaAP3d = "Z=" + (Math.abs(RestarCompBloco-Double.parseDouble(valores))+nValOpDrilling) + ";";
									}else{
										SalidaAP3d = "Z=" + ProfPasante;
									}
								}
								
								if(PlanSeg==1){
									SalidaAP3d = "Z=" + (Math.abs(RestarCompBloco-Double.parseDouble(valores))) + ";";
									PlanSeg=2;
								}
									
								break;
						}
					  }	 
				  }
			   }
		  }
		   
		}while(st2.hasMoreTokens() & cpar==1);
		
		

		return SalidaAP3d;
	}

}
