package br.UNB.LAB.GerarCodG;

//import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import br.UNB.LAB.FAcessorias.ImprimirInfo;
import br.UNB.LAB.InfAvancadas.MapearMachining_workingstep;
import br.UNB.LAB.InfBasicas.*;


public class CodeG_Para__Step_E_Operaciones {
	
	int ctokens=0;//contador de tokens
	int salif=1;
	
	static double Xp=0,Yp=0,Zp=0,ProfTem=0,CompFea=0,lagura=0,CompSup=0,CompInf=0,AngBase=0,ValorX0=0;
	static String Ori="",OpenProfileTipo="";
	
	//datos Ferramentas
	static String Ferramentas="",TipoFer="",DataFer="",NomeFer="";
	static double DiaFer=0,ContadorNumFer=0;
	static double diaFerFaceMill[] = new double[5];//array de pociciojnes para este Step
	static double DiaFertas[] = new double[5];//array de pociciojnes para este Step
	
	public static ArrayList<String> listDadosFerramentas = new ArrayList<String>();
	public static ArrayList<String> listFer = new ArrayList<String>();
	
	static boolean passou=false;
	static int F1=0, F2=0;
	static String StrpasF1F2="";

		@SuppressWarnings("static-access")
		public void GenCodG_StepeOper(){
			//System.out.println("Entro a pasar datos");
			
			//ferramenta 1 y 2
			passou=false;
			F1=0;
			F2=1;
			StrpasF1F2="";
			listDadosFerramentas.clear();
			
			
			PlanoSeguranca SecPlan = new PlanoSeguranca();
			MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
			RectPocketSimples RanhuraSimp = new RectPocketSimples();
			DadosDaPeca ValPeca = new DadosDaPeca();
			
			
			
			//DecimalFormat L4 = new DecimalFormat("0.0000"); 
			
			
			///////////////Plano de seguranca
			String data2=SecPlan.ComprimentoPlanoSeg;
			StringTokenizer stp = new StringTokenizer(data2, "=;",true);//buscador de tokens con separadores activados
			ctokens=0;
			do{//asignacion de direcciones
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = stp.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				double menosPS=0;
				if(ctokens==3){//copia el primer token con la primera ferramenta
					//System.out.println("Imprimiendo Secplan            "+ (Double.parseDouble(valores)-menosPS));
					RanhuraSimp.PlanSeg=Double.parseDouble(valores)-menosPS;

				}
			}while(stp.hasMoreTokens());
			
			
			
			int TamList=ExtrairDatos.InformacoesAvancadas.size();
			int cDatList=0;
				
				do{
					String data=ExtrairDatos.InformacoesAvancadas.get(cDatList);
					
					
					if(data.indexOf("TipoFer=")!=-1){
						ContadorNumFer++;
					}
					
					//System.out.println("ContadorNumFer " + ContadorNumFer);
					
					if(data.startsWith("Ubicacion:")==true){
						
						StringTokenizer st = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
						ctokens=0;
						do{//asignacion de direcciones
							ctokens++;//contador de tokens para encontrar el parentesis
							String valores = st.nextToken();//leo el proximo token
							//System.out.println(ctokens + " "+valores);
						if(ctokens==5){//para X
							Xp=Double.parseDouble(valores);
//							System.out.println(Xp);
							}
						if(ctokens==9){//para Y
							Yp=Double.parseDouble(valores);
//							System.out.println(Yp);
							}
						if(ctokens==13){//para Y
							Zp=Double.parseDouble(valores);
//							System.out.println(Yp);
							}
						
						}while(st.hasMoreTokens());
					}
				
					ctokens=0;
					
					if(data.startsWith("Profundidad:")==true){
						
						StringTokenizer st = new StringTokenizer(data, "=;",true);//buscador de tokens con separadores activados
						
						do{//asignacion de direcciones
							ctokens++;//contador de tokens para encontrar el parentesis
							String valores = st.nextToken();//leo el proximo token
							//System.out.println(ctokens + " "+valores);
							if(ctokens==3){//copia el primer token con la primera ferramenta
								ProfTem = Double.parseDouble(valores);//temporal para furos segmentados
//								System.out.println(ProfTem);
							}
						}while(st.hasMoreTokens());
					}
				
			if(data.startsWith("TravelPath:")==true){
				StringTokenizer st = new StringTokenizer(data, "=,;",true);//buscador de tokens con separadores activados
				do{//asignacion de direcciones
					ctokens++;//contador de tokens para encontrar el parentesis
					String valores = st.nextToken();//leo el proximo token
					//System.out.println(ctokens + " "+valores);
					if(ctokens==7){//copia el primer token con la primera ferramenta
						CompFea = Double.parseDouble(valores);//temporal para furos segmentados
						}
					if(ctokens==11){//copia el primer token con la primera ferramenta
						if(Xp==CompFea | Xp==0){
							Ori = "Hor";
							data=data.replaceAll("Ver", "Hor");
						}else{
							Ori = "Ver";
							data=data.replaceAll("Hor", "Ver");
						}
						
						//Ori=valores;
						}
					}while(st.hasMoreTokens());
				
//				System.out.println(Ori);
//				System.out.println(Comprimento);
				salif=0;
				}
				
			if(data.indexOf("TipoFer=")!=-1){
				StringTokenizer stIni = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
				String valoresDataIni = stIni.nextToken();//leo el proximo token osea el TX del inicio del data
				
				//adquiero los valores arrumados de esta herramienta
				String dataFeatureNova=OrganizarFerraEntreFeatures.ExtracDados(data);
				//System.out.println(dataFeatureNova);
				String NewTool = "";
				//hago las comparaciones y adiciono los valores de f1 Y f2
				for (int i = 0; i < OrdenarFerramentas.listDadosTrocaFerra.size(); i++) {
					//System.out.println(listDadosFerramentas.get(i));
					String dataferExis = OrdenarFerramentas.listDadosTrocaFerra.get(i);
					if(dataferExis.indexOf(dataFeatureNova)!=-1){
						
						
						//obtengo el valor de la herramienta osea el index
						StringTokenizer st = new StringTokenizer(OrdenarFerramentas.listDadosTrocaFerra.get(i), ":=,;",true);//buscador de tokens con separadores activados
						String valores = st.nextToken();//leo el proximo token osea el TX del inicio del data
						String valores2 = valores.replaceAll("T", "");
						int vTool=Integer.valueOf(valores2);
						
							if(F1==0 & F2==1){
								F1=vTool;
								NewTool = "T" + F1;
								//cambio la herramienta
								ImprimirInfo.ImprimirInformacion(data);
								data=data.replaceAll(valoresDataIni, NewTool);
								listDadosFerramentas.add(data);
								i = OrdenarFerramentas.listDadosTrocaFerra.size();
							}
							if(F2==0){
								F2=vTool;
								NewTool = "T" + F2;
								//cambio la herramienta
								ImprimirInfo.ImprimirInformacion(data);
								data=data.replaceAll(valoresDataIni, NewTool);
								listDadosFerramentas.add(data);
								passou=true;
								i = OrdenarFerramentas.listDadosTrocaFerra.size();
							}
							if(passou==false){
								//para ejecutar solo una vez
								F2=0;
							}
					}
				
					//pregunto por las herramientas que ya existen

					
				}//fin for
			
				
				
			}
				
			ImprimirInfo.ImprimirInformacion(data);
			
			
			
				if(salif==0){
					ImprimirInfo.ImprimirInformacion("Fin feature");
				salif=0;
				}
			
			

			
			cDatList++;
			
		}while(cDatList<TamList & salif==1);
				
				
				
				
				
				/********************** extraer informacion de las herramienras******************************/
				
			cDatList=0;
			//int LineNo=0;
			salif=1;
			ctokens=0;
			int cont=1,cFerSt=0;  
			//cantFerFacemill=0;
			do{
				String data=ExtrairDatos.InformacoesAvancadas.get(cDatList);
				
				if(data.indexOf("FACEMILL")!=-1){
					//System.out.println("("+data+")");

					StringTokenizer st = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
					
					do{//asignacion de direcciones
						ctokens++;//contador de tokens para encontrar el parentesis
						String valores = st.nextToken();//leo el proximo token
						//System.out.println(ctokens + " "+valores);
						
						if(ctokens==1){//copia el primer token con la primera ferramenta
							Ferramentas=valores;
							valores = String.valueOf(cFerSt);						
							cFerSt++;
							listFer.add(valores);
							//diaFerFaceMill[cont++]=Double.parseDouble(valores);
							}
						if(ctokens==7){
							NomeFer=valores;
							}
						if(ctokens==11){
							TipoFer=valores;
							}
						if(ctokens==15){
							DiaFer = Double.parseDouble(valores);
							diaFerFaceMill[cont++]=Double.parseDouble(valores);
							}
						
						}while(st.hasMoreTokens());
					
					
				}
				
				//int iPosicion=0;
				double iNumeroMayor=25;
				int x=1,posx=0;
				for (x=1;x<diaFerFaceMill.length;x++){
			           
		             if (diaFerFaceMill[x]<iNumeroMayor & diaFerFaceMill[x]!=0){
		               iNumeroMayor = diaFerFaceMill[x];
		               //iPosicion = x;
		               Ferramentas="T"+x;
		               posx=x;
		             } 
		           } 
				DiaFertas[0]=diaFerFaceMill[posx];   
			
				cDatList++;
				
			}while(cDatList<TamList);
				
		////////////fin area de herramientas
			ImprimirInfo.ImprimirInformacion("");	
		
		
		
		
		
		/******************* Generar codigo G *********************/
		int inc=0;
		double ancho=0;
		String valTem="";
		if(Ori.startsWith("Hor")==true){
		if(Xp==CompFea){
			Xp=0;
			valTem=ValPeca.ComprimentoY_DoBloco.replaceAll(",", ".");
			ancho=Double.parseDouble(valTem) - Yp;
		}else if(Xp==0){
			ancho=Yp;
			Yp=0;
		}
		}else{
		if(Yp==CompFea){
			Yp=0;
			ancho=Xp;
			Xp=0;
		}else if(Yp==0){
			valTem=ValPeca.ComprimentoX_DoBloco.replaceAll(",", ".");
			ancho=Double.parseDouble(valTem)-Xp;
		}
		}
		
		
		
		
		
		
		
		
		//RanhuraSimp.ImprimirPlaSeg();
		RanhuraSimp.IniFer=true;
		RanhuraSimp.IniB=true;
		RanhuraSimp.IniA=true;

			do{
				if(Ori.startsWith("Hor")==true){
					
				RanhuraSimp.X1=Xp;
				RanhuraSimp.Y1=Yp;
				}else{
					RanhuraSimp.X1=Xp;
					RanhuraSimp.Y1=Yp;	
				}
				//System.out.println("Valor real feature  2         "+RanhuraSimp.Y1);
				RanhuraSimp.Ori=Ori;
				RanhuraSimp.Largo=CompFea;
				RanhuraSimp.Ancho=ancho;
				RanhuraSimp.DTool=DiaFertas[0];
				RanhuraSimp.dadosFer=listDadosFerramentas.get(0);
				RanhuraSimp.Prof=inc;
				RanhuraSimp.RanPocketSimplesNc();
				inc++;
				}while(inc<=ProfTem);
			
			RanhuraSimp.ImprimirPlaSeg();
			
			

	}//FIN GERAR NC

}
