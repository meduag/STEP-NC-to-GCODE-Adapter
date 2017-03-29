package br.UNB.LAB.GerarCodG;

//import java.text.DecimalFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import br.UNB.LAB.FuncoesDePosicao.CalculoDosPontosDaReta;
import br.UNB.LAB.FuncoesDePosicao.CalculoDosPuntosDoCirculo;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;
import br.UNB.LAB.Integrador.Integrador;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;


public class CodeG_Para__Slot_E_Operaciones {

	//private static double T1 = 0, T2 = 0;
	static int T3=0;

	int ctokens=0;//contador de tokens
	int salif=1;
	static int ant=0,conJexis=0;
	
	static double Xp=0,Yp=0,Zp=0,ProfTem=0,CompFea=0,lagura=0,CompSup=0,CompInf=0,AngBase=0,ValorX0=0,Xint=0,Yint=0,m=0,b=0;
	static String Ori="",OpenProfileTipo="";
	//private int cTool=0;
	//valores das pociciones de las herramientas en la lista F1yF2    
	static int F1=0, F2=0;
	//addFerAOlist para saber si agrego mas herramientas o no
	static String addFerAOlist="",StrpasF1F2="";
	static String VerficarExistenciaFer="",Ferjaexiste="";
	static int contFer=0; 
	
	
	
	
	//datos Ferramentas
	static String Ferramentas="",TipoFer="",DataFer="",NomeFer="";
	static double DiaFer=0,ContadorNumFer=0;
	static double t1,t2,t3,t4,t5,t6,Tep_t6=0; 
	
	static double LarguraTempRanura[] = new double[30];//array de pociciojnes para este Slot
	static double ProfTempRanura[] = new double[30];//array de pociciojnes para este Slot
	
	public static int ValorPasoAngulo=br.UNB.LAB.GerarCodG.GeneradorCodidoG.ValorPasoAngulo;
	
	public static ArrayList<String> listDadosFerramentas = new ArrayList<String>();
	public static ArrayList<String> listFer = new ArrayList<String>();
	
	public static ArrayList<Double> X_CalPontosDaRecta = new ArrayList<Double>();
	public static ArrayList<Double> Y_CalPontosDaRecta = new ArrayList<Double>();
	
	static double diaFerFaceMill[] = new double[5];//array de pociciojnes para este Slot
	
	static double DiaFertas[] = new double[5];//array de pociciojnes para este Slot
	static boolean SalExt=false,copListaOrdFer=false, passou=false;
	
	
	@SuppressWarnings({ "static-access"})
	public void GenCodG_SloteOper(){
		
		//ferramenta 1 y 2
		passou=false;
		F1=0;
		F2=1;
		StrpasF1F2="";
		listDadosFerramentas.clear();
		
		
		//adicionar ferramentas en falso
		addFerAOlist="";
	
		PlanoSeguranca SecPlan = new PlanoSeguranca();
		MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
		RectPocketSimples RanhuraSimp = new RectPocketSimples();
		//RectPocketSimples ContornoRanSimp = new RectPocketSimples();
		//RectSimples RanLinSimp = new RectSimples();
		//CalculoDosPontosDaReta CalPonRect = new CalculoDosPontosDaReta();
		CalculoDosPuntosDoCirculo CalPonCir = new CalculoDosPuntosDoCirculo();
		
		//DecimalFormat L3 = new DecimalFormat("0.0000"); 
		SalExt=false;
		
		
		///////////////Plano de seguranca
		String data2=SecPlan.ComprimentoPlanoSeg;
		
		//asignacion del valor que trabajara el angulo
		ValorPasoAngulo=GeneradorCodidoG.ValorPasoAngulo;
		
		StringTokenizer stp = new StringTokenizer(data2, "=;",true);//buscador de tokens con separadores activados
		ctokens=0;
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = stp.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);
			double menosPS=0;///////////////////////////////////////////////////////////////vALOR A MODIFICAR DEL PLANO DE SEGURIDAD
			if(ctokens==3){//copia el primer token con la primera ferramenta
				//System.out.println("Imprimiendo Secplan            "+ (Double.parseDouble(valores)-menosPS));
				RanhuraSimp.PlanSeg=Double.parseDouble(valores)-menosPS;
				Slot_Ranhu_Simples_e_Segmentada.PlanSeg=Double.parseDouble(valores)-menosPS;

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
//						System.out.println(Xp);
						}
					if(ctokens==9){//para Y
						Yp=Double.parseDouble(valores);
//						System.out.println(Yp);
						}
					if(ctokens==13){//para Z
						Zp=Double.parseDouble(valores);
//						System.out.println(Yp);
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
//							System.out.println(ProfTem);
						}
					}while(st.hasMoreTokens());
				}
			
				ctokens=0;
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
					Ori = valores;
					}
				}while(st.hasMoreTokens());
			
//			System.out.println(Ori);
//			System.out.println(Comprimento);
			}
		
		ctokens=0;	
		if(data.startsWith("OpenProfile Tipo:")==true){
			StringTokenizer st = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
			do{//asignacion de direcciones
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = st.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				
				if(ctokens==3){//copia el primer token con la primera ferramenta
					OpenProfileTipo = valores.replaceAll(" ", "");
					}
				if(ctokens==7){
					CompSup = Double.parseDouble(valores);//sirve tambien como radParcial para - PARTIAL_CIRCULAR_PROFILE //VeeProf - Radio de la base
					}
				if(ctokens==11){
					//correcion para open profice en V
					if(valores.startsWith("=")){
						valores = st.nextToken();
					}
					CompInf = Double.parseDouble(valores);//VeeProf - AngV
					
					}
				if(ctokens==15){
					AngBase = Double.parseDouble(valores);//VeeProf - AngInclinacion
					}
				
				}while(st.hasMoreTokens());
//			System.out.println(OpenProfileTipo);
//			System.out.println(CompSup);
//			System.out.println(CompInf);
//			System.out.println(AngBase);
			
			salif=0;
			}
			

		
		
		if(data.indexOf("TipoFer=")!=-1){
			contFer++;
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
							//System.out.println("(Troca:"+data+")");
							data=data.replaceAll(valoresDataIni, NewTool);
							listDadosFerramentas.add(data);
							VerficarExistenciaFer = VerficarExistenciaFer + OrganizarFerraEntreFeatures.ExtracDados(data);
							i = OrdenarFerramentas.listDadosTrocaFerra.size();
						}
						if(F2==0){
							F2=vTool;
							NewTool = "T" + F2;
							//cambio la herramienta
							//System.out.println("(Troca:"+data+")");
							data=data.replaceAll(valoresDataIni, NewTool);
							listDadosFerramentas.add(data);
							VerficarExistenciaFer = VerficarExistenciaFer + OrganizarFerraEntreFeatures.ExtracDados(data);
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
			
			if(F2!=0 & passou==true & OrdenarFerramentas.listDadosTrocaFerra.size()==3){
				//System.out.println("("+data+")");
				//cDatList++;
				data=ExtrairDatos.InformacoesAvancadas.get(cDatList);
				String ProxFer = data;
				dataFeatureNova=OrganizarFerraEntreFeatures.ExtracDados(ProxFer);
				
				if(VerficarExistenciaFer.indexOf(dataFeatureNova)!=-1){
					//System.out.println("(A Ferra já Existe " + dataFeatureNova +")");
					
					if(Ferjaexiste=="" & conJexis==0){
						conJexis++;
					}else{
						Ferjaexiste = "A Ferra já Existe";
						conJexis=0;
					}
				}else{
					StringTokenizer st = new StringTokenizer(OrdenarFerramentas.listDadosTrocaFerra.get(2), ":=,;",true);//buscador de tokens con separadores activados
					String valores = st.nextToken();//leo el proximo token osea el TX del inicio del data
					String valores2 = valores.replaceAll("T", "");
					int vTool=Integer.valueOf(valores2);
					int F3 = vTool;
					
					NewTool = "T" + F3;
					
					StringTokenizer stIniF3 = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
					String valoresDataIniF3 = stIniF3.nextToken();//leo el proximo token osea el TX del inicio del data
					
					//System.out.println("(Troca: "+data+")");
					data=data.replaceAll(valoresDataIniF3, NewTool);
					listDadosFerramentas.add(data);
					Ferjaexiste="";
					
				}
		
			}	
			
		}//fin tipo fer
		
			if(Integrador.ImpLedFer==1){
				System.out.println("("+data+")");
			}
		
		
		
			if(salif==0){
				if(Integrador.ImpLedFer==1){
					System.out.println("(Fin feature)");
				}
			salif=0;
			}
		
		

		
		cDatList++;
		
	}while(cDatList<TamList & salif==1);
			
			
			
			
			
			/******************************************************** arreglar herramientas no adicionadas *****************************************************************/
			
			
			
			
			
			
			
			
			
			/********************** extraer informacion de las herramienras******************************/
			
		cDatList=0;
		//int LineNo=0;
		
		
		int cFerSt=0; 
		
		//cantFerFacemill=0;
		
		do{
			salif=1;
			ctokens=0;
			String data=OrdenarFerramentas.listDadosTrocaFerra.get(cDatList);
			StringTokenizer st = new StringTokenizer(data, ",",true);//buscador de tokens con separadores activados
			
			do{
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = st.nextToken();//leo el proximo token
				if(ctokens==5){//copia el primer token con la primera ferramenta
					//Ferramentas=valores;
					DiaFertas[cFerSt]= Double.parseDouble(valores);						
					cFerSt++;
					salif=0;
				}
				
			}while(salif==1);
			

			
			cDatList++;
			
		}while(cDatList<=OrdenarFerramentas.listDadosTrocaFerra.size()-1);
			
	////////////fin area de herramientas
		
		
		
			
		/****************************************************************************  Tipo de OpenProfile  - generar cod G***********************************************************/	
		
		 
	/***********************       SQUARE_U_PROFILE        *******************************/
		int inc=0;
		CalPonCir.graAvan=ValorPasoAngulo;//avance por grados
		
	if(OpenProfileTipo.startsWith("SQUARE_U_PROFILE")==true){
		InicioG();
		double RToolt = DiaFertas[1]/2;
		//Correcion de la ubicacion en verticalllllllllllllllllllllllllllllllllllllllllllllllllllllllllll 
		if(Ori.startsWith("Hor")==false){
			Xp=Xp-CompSup/2;
		}
		
		
		
		/****************************************************************************************************************************************************************************************************/
		
		//comprimento sup == comprimento inferior pero SIN angulo en la base
		if(CompInf==CompSup & AngBase==0){
			////////////////////////////////////////////////////////////////////////////***************************************** Nuevo codigo
			if(DiaFertas[0]==CompSup){
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[0], ProfTem, 0, listDadosFerramentas.get(0),true,true);
			}
			
			if(DiaFertas[0]<CompSup){
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem, 0, DiaFertas[0], Ori, true, listDadosFerramentas.get(0));
			}

		}
		
		
		//int fin2=0;
		//	double LagTemR=0, ProfTemR=0;
		
		
		/****************************************************************************************************************************************************************************************************/
		
		//comprimento sup == comprimento inferior pero CON angulo en la base
		if(CompInf==CompSup & AngBase!=0){
			
			//ws0 -- hasta la prof antes de ang de la base
			if(DiaFertas[0]==CompSup){
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[0], ProfTem-AngBase, 0, listDadosFerramentas.get(0),true,true);
			}
			
			if(DiaFertas[0]<CompSup){
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem-AngBase, 0, DiaFertas[0], Ori, true, listDadosFerramentas.get(0));
			}
			
			
			if(DiaFertas[1]/2==AngBase){//el radio de la herramienta 1 es igual al del angulo de la base
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem, ProfTem-AngBase, DiaFertas[1], Ori, true, listDadosFerramentas.get(1));
				
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[1], ProfTem, ProfTem-AngBase, listDadosFerramentas.get(1),true,false);
				
			}else{//el radio de la herramienta 1 es menor al del angulo de la base
				
				//ws parte 1
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem-AngBase+RToolt, ProfTem-AngBase, DiaFertas[1], Ori, true, listDadosFerramentas.get(1));
				
				
				//ws parte 2
				//calculo los valores de la curva en la base
				/**
				 * A tener en cuenta que se usa el metodo de calcular angulo y las variables que contienen los resultados de la clase:   CodeG_Para__Pocket_E_Operaciones
				 * ---   X_AngBase_PlanarRadius         -avance por el angulo
				 * ---   Y_AngBase_PlanarRadius     -profundidad
				 * */
				CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(AngBase);
				inc=1;
				double res=0;
				//boolean ImpTroFer = true;
				Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imporma la correcion del eje Y hasta que sea necesario
				
				do{
					t1=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(inc);
					t2=AngBase-t1;
					t3=t2/2;
					t4=(Yp-CompSup/2);
					t5=t3+t4;
					
					t6=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc);

					//Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,CompSup-t2,CompFea, (ProfTem-AngBase)+t6+RToolt, DiaFertas[1], Ori, false, listDadosFerramentas.get(1),false,1,true);
					//ImpTroFer=false;
					res = t6+ProfTem-AngBase+RToolt;
					if(res>=ProfTem){//es para saber si supera el valor de la profundidad menos el radio de la herramienta  -- comp de fer en eje Z
						inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size();
					}else{
						Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,CompSup-t2,CompFea, (ProfTem-AngBase)+t6+RToolt, DiaFertas[1], Ori, false, listDadosFerramentas.get(1),false,1,true);

					}

					inc++;
				}while(inc<=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1);
				
				//coloco el final 
				t1=(CompSup/2) - AngBase;
				t2=AngBase-t1;
				t3=t2/2;
				t4=(Yp-CompSup/2);
				t5=t3+t4;
				
				t6=ProfTem;

				Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,CompSup-t2,CompFea, t6, DiaFertas[1], Ori, false, listDadosFerramentas.get(1),false,1,true);
				
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[1], ProfTem, ProfTem-AngBase, listDadosFerramentas.get(1),true,false);
				
			}
			
			
			
			
	
		}//fin cuando hay angulo de base		
			
		/****************************************************************************************************************************************************************************************************/
		
		
		if(CompInf<CompSup){//cuando hay angulo de base y hay perfil en v
			Slot_Ranhu_Simples_e_Segmentada.CorrecionPerfil=true;//para hacer la correcion del g41e42
			
			double proWS0=ProfTem-AngBase;
			
			//CalPendenteXY(CompSup, CompInf, proWS0);
			CalPendenteXY(CompSup, CompInf, ProfTem,0);
			
			boolean ImpTroFer = true;
			Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imporma la correcion del eje Y hasta que sea necesario
			inc=0;
			//para ws0
			do{
				t1=X_CalPontosDaRecta.get(inc);
				t2=CompSup-t1*2;
				t3=t2/2;
				t4=(Yp-CompSup/2);//valor original de inicio de la feature
				t5=t3+t4;
				t6=Y_CalPontosDaRecta.get(inc);
				if(t6==0){
					t6=Y_CalPontosDaRecta.get(inc+1)/3;
				}
				
				Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,CompSup-t2,CompFea, t6, DiaFertas[0], Ori, ImpTroFer, listDadosFerramentas.get(0),true,2,true);
				ImpTroFer=false;
				inc++;
			}while(t6<proWS0);
			//System.out.println(t1);
			//System.out.println(inc);
			
			Tep_t6=CompSup-t2;//para hacer la ws1 y poder colocar la ferramenta 1 para usinar una ranhura para dejar solo la parte del angulo base
			
			
			//calculo el punto de intercepcion y le doy el valor al offset para hacer el angulo de base
			CalAngDeIntercepcion(t2/2, t6,AngBase);
			
			t6 = t6 + Yint;
			t1 = (t6 - b)/m;
			t2=CompSup-t1*2;
			t3=t2/2;
			t4=(Yp-CompSup/2);//valor original de inicio de la feature
			t5=t3+t4;
			
			if(Yint<=1){//hay MENOS de un milimetro para ranurar
				Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,CompSup-t2,CompFea, t6, DiaFertas[0], Ori, ImpTroFer, listDadosFerramentas.get(0),true,2,true);
				ImpTroFer=false;
				Tep_t6=CompSup-t2;//para hacer la ws1 y poder colocar la ferramenta 1 para usinar una ranhura para dejar solo la parte del angulo base
			}else{//hay MAS de un milimetro para ranurar
				proWS0 = proWS0 + Math.floor(Yint);

				double restanteProf = Yint - Math.floor(Yint);
				Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imporma la correcion del eje Y hasta que sea necesario
							
				do{
					t1=X_CalPontosDaRecta.get(inc);
					t2=CompSup-t1*2;
					t3=t2/2;
					t4=(Yp-CompSup/2);//valor original de inicio de la feature
					t5=t3+t4;
					t6=Y_CalPontosDaRecta.get(inc);
					if(t6==0){
						t6=Y_CalPontosDaRecta.get(inc+1)/3;
					}
					
					Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,CompSup-t2,CompFea, t6, DiaFertas[0], Ori, ImpTroFer, listDadosFerramentas.get(0),true,2,true);
					ImpTroFer=false;
					inc++;
					
				}while(t6<proWS0);
				
				//el resto de lo que falta para hacer el final de la intecepcion del angulo debase con el final de la recta inch=linada todo en el WS0
				Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imporma la correcion del eje Y hasta que sea necesario
				if(restanteProf>0 & restanteProf<1){
					//Yint=restanteProf;
					t6 = t6 + restanteProf;
					t1 = (t6 - b)/m;
					t2=CompSup-t1*2;
					t3=t2/2;
					t4=(Yp-CompSup/2);//valor original de inicio de la feature
					t5=t3+t4;
					
					Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,CompSup-t2,CompFea, t6, DiaFertas[0], Ori, ImpTroFer, listDadosFerramentas.get(0),true,2,true);
					ImpTroFer=false;
						
				}//fin del resto de la profundidad
				
			
				Tep_t6=CompSup-t2;//para hacer la ws1 y poder colocar la ferramenta 1 para usinar una ranhura para dejar solo la parte del angulo base
			}////////fin ws0
			
			
			//para ws1
			if(contFer==3){
				if((Tep_t6-(AngBase*2)) <= DiaFertas[1]){
					Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[1], ProfTem, ProfTem-AngBase, listDadosFerramentas.get(1),false,true);
				}else{
					//para hacer el recorrido hasta terminar la profundidad teniendo en cuenta que el diametro es menor segun el Angulo de la base AnBase*2.
					//Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompInf,CompFea, ProfTem, ProfTem-AngBase, DiaFertas[1], Ori, true, listDadosFerramentas.get(1));
					Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-(Tep_t6/2)+AngBase ,Tep_t6 - AngBase*2 ,CompFea, ProfTem, ProfTem-AngBase, DiaFertas[1], Ori, false, listDadosFerramentas.get(1));
				}
				
				//para ws3
				//como tiene 3 herramientas, la herramienta a usar para el AngBase es la 2
				ImpWSAnguloBase(0,true,CompInf,2);
				

			}else if(contFer==2){
				if((Tep_t6-(AngBase*2)) <= DiaFertas[0]){
					Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[0], ProfTem, ProfTem-AngBase, listDadosFerramentas.get(0),false,false);
				}else{
					//para hacer el recorrido hasta terminar la profundidad teniendo en cuenta que el diametro es menor segun el Angulo de la base AnBase*2.
					Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-(Tep_t6/2)+AngBase ,Tep_t6 - AngBase*2 ,CompFea, ProfTem, ProfTem-AngBase, DiaFertas[0], Ori, false, listDadosFerramentas.get(0));
				}
				
				//para ws3
				//como tiene 2 herramientas, la herramienta a usar para el AngBase es la 1
				ImpWSAnguloBase(0,true,CompInf,1);
			}
			

			}//fin del la igualdad
	
		}//fin SQUARE_U_PROFILE

	
	
	
	
	
	
	/***********************       PARTIAL_CIRCULAR_PROFILE        *******************************/
	inc=0;
	//int conVal=0;
	if(OpenProfileTipo.startsWith("PARTIAL_CIRCULAR_PROFILE")==true){
		
		InicioG();
		
		//primero hacemos la WS0 que una ranhura con la fer facemill F0 en caso de que la Fer0 sea igual que el Comp Sup entonces no se hace la WS0
		if(DiaFertas[0]==CompSup*2 & DiaFertas[1]==CompSup*2){//condicion para saber sison iguales el diamtro del furo con el diametro de la herramienta, teniendo en cuenta el offset
			
			Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup, CompSup*2,CompFea, Ori, true, DiaFertas[1], ProfTem, Zp, listDadosFerramentas.get(1),true,true);
		}
		
		//primero hacemos la WS0 que una ranhura con la fer facemill F0  hasta cierta profundidad
		if(DiaFertas[0]<CompSup*2 & DiaFertas[1]==CompSup*2){//condicion para saber sison iguales el diamtro del furo con el diametro de la herramienta, teniendo en cuenta el offset
			//calculo los valores de la curva en la base
			/*** A tener en cuenta que se usa el metodo de calcular angulo y las variables que contienen los resultados de la clase:   CodeG_Para__Pocket_E_Operaciones
			 * ---   X_AngBase_PlanarRadius         -avance por el angulo
			 * ---   Y_AngBase_PlanarRadius     -profundidad  * */
			CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(CompSup);
			
			//encuentro el valor maximo en que la herramienta 0 puede entrar en el perfil circular parcial
			int temp_Val_i=0;
			for (int i = 0; i < CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1 ; i++) {
				
				if(CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(i)*2<=CompSup){
					temp_Val_i=i-1;
					i = CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1;
				}
			}
			
			double ProfTemWS0 = CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(temp_Val_i);
			//para ws0
			Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup, CompSup*2,CompFea, Ori, true, DiaFertas[0], ProfTemWS0, Zp, listDadosFerramentas.get(0),true,false);
			
			
			Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup, CompSup*2,CompFea, Ori, true, DiaFertas[1], ProfTem, Zp, listDadosFerramentas.get(1),true,true);
		}
		
		//primero hacemos la WS0 que una ranhura con la fer facemill F0  hasta cierta profundidad en caso de que la Fer0 sea igual que el Comp Sup entonces no se hace la WS0
		if(DiaFertas[0]==CompSup*2 & DiaFertas[1]<CompSup*2){
			Slot_Ranhu_Simples_e_Segmentada.CorrecionPerfil=false;//para hacer la correcion del g41e42
			
			Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup,CompSup*2,CompFea, ProfTem-CompSup/2, 0 , DiaFertas[1], Ori, true, listDadosFerramentas.get(1));
			
			CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(CompSup);
			
			//double tempPosY=0, tempProf=0;
			inc = 0 + (int)Zp;
			double res=0;
			double RToolt = DiaFertas[1]/2;
			boolean ImpTroFer=true;
			Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imporma la correcion del eje Y hasta que sea necesario
			
			do{
				t1=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(inc);
				t2=CompSup-t1;
				t3=(Yp-CompSup);
				t4=t2+t3;//ancho
				t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc);
				if(t5==0){
					t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc+1)/3;
				}
				
				res = t5+RToolt/2;
				//res = t5;
				if(res>=ProfTem){//es para saber si supera el valor de la profundidad menos el radio de la herramienta  -- comp de fer en eje Z
					Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup, CompSup*2,CompFea, Ori, false, DiaFertas[1], ProfTem, t5-1, listDadosFerramentas.get(1),true,false);
					inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1;
				}else{
					Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(2*CompSup)-(t2*2),CompFea, t5+RToolt/2, DiaFertas[1], Ori, ImpTroFer, listDadosFerramentas.get(1),ImpTroFer,0,false);
					//Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(2*CompSup)-(t2*2),CompFea, t5, DiaFertas[1], Ori, ImpTroFer, listDadosFerramentas.get(1),ImpTroFer,0,false);
					ImpTroFer=false;
				}
				inc++;
			}while(inc<=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1);

					                  
		}
		//cuando el valor del radio parcial del perfil es diferente del de la herramienta 
		if(DiaFertas[0]<CompSup*2 & DiaFertas[1]<CompSup*2){
			
			CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(CompSup);
			//encuentro el valor maximo en que la herramienta 0 puede entrar en el perfil circular parcial
			int temp_Val_i=0;
			for (int i = 0; i < CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1 ; i++) {
				
				if(CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(i)*2<=CompSup){
					temp_Val_i=i-1;
					i = CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1;
				}
			}
			
			double ProfTemWS0 = CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(temp_Val_i-1);
			//para ws0
			Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup, CompSup*2,CompFea, Ori, true, DiaFertas[0], ProfTemWS0, Zp, listDadosFerramentas.get(0),true,false);
			
			//para ws1
			inc = 0 + (int)Zp;
			boolean ImpTroFer=true;
			Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imporma la correcion del eje Y hasta que sea necesario
			double res=0;
			double RToolt = DiaFertas[1]/2;
			
			Slot_Ranhu_Simples_e_Segmentada.CorrecionPerfil=true;//para hacer la correcion del g41e42
			
			do{
				t1=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(inc);
				t2=CompSup-t1;
				t3=(Yp-CompSup);
				t4=t2+t3;//ancho
				t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc);
				if(t5==0){
					t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc+1)/3;
				}
				
				res = t5+RToolt/2;
				//res = t5;
				if(res>=ProfTem){//es para saber si supera el valor de la profundidad menos el radio de la herramienta  -- comp de fer en eje Z
					Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup, CompSup*2,CompFea, Ori, false, DiaFertas[1], ProfTem, t5-1, listDadosFerramentas.get(1),true,false);
					inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1;
				}else{
					Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(2*CompSup)-(t2*2),CompFea, t5+RToolt/2, DiaFertas[1], Ori, ImpTroFer, listDadosFerramentas.get(1),ImpTroFer,0,false);
					//Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(2*CompSup)-(t2*2),CompFea, t5, DiaFertas[1], Ori, ImpTroFer, listDadosFerramentas.get(1),ImpTroFer,0,false);
					ImpTroFer=false;
				}
				inc++;
			}while(inc<=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1);
			
		}

		
	}//fin PARTIAL_CIRCULAR_PROFILE
	
	
	
	

	
	/***********************       ROUNDED_U_PROFILE        *******************************/
	inc=0;
	RanhuraSimp.IniA=true;
	//conVal=0;
	
	if(OpenProfileTipo.startsWith("ROUNDED_U_PROFILE")==true){
		InicioG();
		
		int F1=1;//asignacion del numero de fer
		if(contFer==3){//son 2 herramientas osea cuatro posibles convnaciones
			F1=2;
		}
			//caso 1
			if(DiaFertas[0]==CompSup & DiaFertas[F1]==CompSup){
				//ws0
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[0], ProfTem-CompSup/2, 0, listDadosFerramentas.get(0),false,true);
				//ws1
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[F1], ProfTem, ProfTem-CompSup/2, listDadosFerramentas.get(F1),false,true);
			}
			
			//caso 2
			if(DiaFertas[0]<CompSup & DiaFertas[F1]==CompSup){
				//ws0
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem-CompSup/2, 0 , DiaFertas[0], Ori, true, listDadosFerramentas.get(0));
				//ws1
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[F1], ProfTem, ProfTem-CompSup/2, listDadosFerramentas.get(F1),false,true);
			}
			
			//caso 3
			if(DiaFertas[0]==CompSup & DiaFertas[F1]<CompSup){//tener en cuenta el offse de la herramienta 2
				double RToolt = (DiaFertas[F1]/2);
				//ws0
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[0], ProfTem-CompSup/2, 0, listDadosFerramentas.get(0),false,true);
				
				//ws1 parte 1(baja hasta conpensar el radio de la Fer2)
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem-(CompSup/2) +RToolt, (ProfTem-CompSup/2) ,  DiaFertas[F1], Ori, true, listDadosFerramentas.get(F1));
				
				//ws1 parte 2
				inc = 0 ;
				//boolean ImpTroFer=true;
				CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(CompSup/2);
				Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imporma la correcion del eje Y hasta que sea necesario (inicializado en false)
				double tpro2=0;
				
				
				do{
					t1=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(inc);
					t2=CompSup-t1*2;
					t3=(Yp-CompSup/2);
					t4=t2+t3;//pos y inicial
					t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc);
					
					if(t5==0){
						t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc+1)/3;
					}
					
					if(t5+(ProfTem-CompSup/2)>=ProfTem-RToolt){//es para saber si supera el valor de la profundidad menos el radio de la herramienta  -- comp de fer en eje Z
						inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size();
					}else{
						if(Yp-t4>RToolt){
							double tpro = RToolt +(ProfTem-CompSup/2);
							tpro2= tpro+t5;
							Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(CompSup)-(t2*2),CompFea, tpro2, DiaFertas[F1], Ori, false, listDadosFerramentas.get(F1),false,0,false);
						}else{
							//Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[2], ProfTem, (ProfTem-CompSup/2)+t5-1, listDadosFerramentas.get(2),true,false);
							inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1;
						}
					}//fin supero el valor final menos el radio de la fer
			inc++;
			}while(inc<=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1);
			
			
			//ws1 parte 3 profundidade final
			/** al final lo que sobra se divide en dos parte 
			 * una que es la mitad de lo que sobra y se hace con slot segmentado
			 * dos la prof restante con slot simples**/

			//parte uno	 - segmentada
			tpro2=((ProfTem-(tpro2 - RToolt))/2);
		
			double tpro = (CompSup/2)- RToolt - tpro2;
			tpro = tpro/2;
			tpro2=tpro2+tpro;
			
			
			double Ang= Math.asin(tpro2/(CompSup/2));//caculo el angulo para despues calcular el valor de x
			//Ang=Math.toDegrees(Ang);//paso a grados
			//System.out.println(Ang);
			
			double DiaFinalX= (CompSup/2)*Math.cos(Ang);
			//System.out.println(DiaFinalX);
			
			t1=DiaFinalX;
			t2=CompSup - t1*2;
			t3=(Yp-CompSup/2);
			t4=t2+t3;//pos y inicial
			t5=RToolt +(ProfTem-CompSup/2) + tpro2;

			Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(CompSup)-(t2*2),CompFea, t5, DiaFertas[F1], Ori, false, listDadosFerramentas.get(F1),false,0,false);
			
			//System.out.println();
			//parte dos - simples
			Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[F1], ProfTem, ProfTem-1, listDadosFerramentas.get(F1),false,false);
			
			//System.out.println();
				
				
			}
			
			//caso 4
			if(DiaFertas[0]<CompSup & DiaFertas[F1]<CompSup){//tener en cuenta el offse de la herramienta 2
				double RToolt = (DiaFertas[F1]/2);
				
				//ws0
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem-CompSup/2, 0 , DiaFertas[0], Ori, true, listDadosFerramentas.get(0));
				
				//ws1 parte 1(baja hasta conpensar el radio de la Fer2)
				Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem-(CompSup/2) +RToolt, (ProfTem-CompSup/2) ,  DiaFertas[F1], Ori, true, listDadosFerramentas.get(F1));

				//Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, true, DiaFertas[F1], ProfTem-CompSup/2, ((ProfTem-CompSup/2) - (DiaFertas[F1]/2)), listDadosFerramentas.get(F1),false,true);
				
				//ws1 parte 2
				inc = 0 ;
				//boolean ImpTroFer=true;
				CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(CompSup/2);
				Slot_Ranhu_Simples_e_Segmentada.correcionY=false;//para que no imprima la correcion del eje Y hasta que sea necesario (inicializado en false)
				double tpro2=0;
				
				
				do{
					t1=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(inc);
					t2=CompSup-t1*2;
					t3=(Yp-CompSup/2);
					t4=t2+t3;//pos y inicial
					t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc);
					
					if(t5==0){
						t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc+1)/3;
					}
					
					if(t5+(ProfTem-CompSup/2)>=ProfTem-RToolt){//es para saber si supera el valor de la profundidad menos el radio de la herramienta  -- comp de fer en eje Z
						inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size();
					}else{
						if(Yp-t4>RToolt){
							double tpro = RToolt +(ProfTem-CompSup/2);
							tpro2= tpro+t5;
							Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(CompSup)-(t2*2),CompFea, tpro2, DiaFertas[F1], Ori, false, listDadosFerramentas.get(F1),false,0,false);
						}else{
							//Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[2], ProfTem, (ProfTem-CompSup/2)+t5-1, listDadosFerramentas.get(2),true,false);
							inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1;
						}
					}//fin supero el valor final menos el radio de la fer
			inc++;
			}while(inc<=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1);
			
			
			//ws1 parte 3 profundidade final
			/** al final lo que sobra se divide en dos parte 
			 * una que es la mitad de lo que sobra y se hace con slot segmentado
			 * dos la prof restante con slot simples**/

			//parte uno	 - segmentada
			tpro2=((ProfTem-(tpro2 - RToolt))/2);
		
			double tpro = (CompSup/2)- RToolt - tpro2;
			tpro = tpro/2;
			tpro2=tpro2+tpro;
			
			
			double Ang= Math.asin(tpro2/(CompSup/2));//caculo el angulo para despues calcular el valor de x
			//Ang=Math.toDegrees(Ang);//paso a grados
			//System.out.println(Ang);
			
			double DiaFinalX= (CompSup/2)*Math.cos(Ang);
			//System.out.println(DiaFinalX);
			
			t1=DiaFinalX;
			t2=CompSup - t1*2;
			t3=(Yp-CompSup/2);
			t4=t2+t3;//pos y inicial
			t5=RToolt +(ProfTem-CompSup/2) + tpro2;

			Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(CompSup)-(t2*2),CompFea, t5, DiaFertas[F1], Ori, false, listDadosFerramentas.get(F1),false,0,false);
			
			//parte dos - simples
			Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[F1], ProfTem, ProfTem-1, listDadosFerramentas.get(F1),false,false);
	
			}
			
	}//fin ROUNDED_U_PROFILE
	
	
	
	
	//System.out.println();
	/***************************       VEE_PROFILE        ***********************************/
	inc=0;
	
	//conVal=0;
	ant=0;
	if(OpenProfileTipo.startsWith("VEE_PROFILE")==true){
		InicioG();
		
		//calcular la distancia ou diametro teniendo en cuenta el angulo de la base
		double largura = 2 * ProfTem / Math.tan(Math.toRadians(90 - CompInf/2)) - 2 * CompSup * (1 - 1 / Math.cos(Math.toRadians(90 - CompInf/2)) / Math.tan(Math.toRadians(90 - CompInf/2)));
		
		double catOp_a=0,catAd_b=0,hipotenusa=0,cosAng=0;
		double Xf=0, Yf=0;
		
		catOp_a=ProfTem;
		catAd_b=largura/2;
		
		//calcular la hipotenusa
		hipotenusa = Math.hypot(catOp_a, catAd_b);
		
		//encuentro el angulo de intecepcion
		cosAng=catAd_b/hipotenusa;
		cosAng=Math.acos(cosAng);
		cosAng=90-Math.toDegrees(cosAng);
		
		//calculo del pinto final de la rexta y ecuacion de la recta
		Xf=CompSup*Math.cos(Math.toRadians(cosAng));
		Yf=CompSup*Math.sin(Math.toRadians(cosAng));
		Yf=Yf+(ProfTem-CompSup);//se convierte en el offset de la profundidad para el circulo
		
		//System.out.println();
		
		CalPendenteXY(largura, Xf, Yf,0);
		
		/** la salida esta en x y
		X_CalPontosDaRecta.get(inc);
		Y_CalPontosDaRecta.get(inc);
		
									**/
		inc = 0;
		boolean ImpTroFer=true;
		
		//WS0
		do{
			t1=X_CalPontosDaRecta.get(inc);
			t2 = (Math.round(largura*100)/100.0);
			t3=t2-t1*2;
			t4=(Yp-t2/2);
			t5=t4+t3/2;//pos y inicial
			t6=Y_CalPontosDaRecta.get(inc);
			
			if(t6==0){
				t6=Y_CalPontosDaRecta.get(inc+1)/3;
			}
			
//			if(t6==8.5){
//				System.out.println();
//			}
			
			if(t2-t3>=DiaFertas[0]){
				Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,t2-t3,CompFea, t6, DiaFertas[0], Ori, ImpTroFer, listDadosFerramentas.get(0),ImpTroFer,0,false);
				ImpTroFer=false;
			}else{
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-t2/2, t2,CompFea, Ori, false, DiaFertas[0], Yf, Y_CalPontosDaRecta.get(inc-1)-CalculoDosPontosDaReta.Avance, listDadosFerramentas.get(0),false,false);
			}
			
			
			inc++;
		}while(inc <=Y_CalPontosDaRecta.size()-1);
		
		//WS1
		if(DiaFertas[1]==CompSup){
			if(Yf-ProfTem<DiaFertas[1]/2){
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-largura/2, CompSup,CompFea, Ori, true, DiaFertas[1], ProfTem, Yf, listDadosFerramentas.get(1),false,true);
			}else{
				Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-largura/2, CompSup,CompFea, Ori, true, DiaFertas[1], ProfTem, ProfTem-DiaFertas[1]/2, listDadosFerramentas.get(1),false,true);
				
			}
			
		}else{//a ferramenta es menor
			//double RToolt = (DiaFer/2);

			//ws1 parte 2
			inc = 0 ;
			//boolean ImpTroFer=true;
			CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(CompSup);

		
	}
	
		
		
	}
	
	System.out.println();
	
	//PARA LA CURVA BEZIER
	if(OpenProfileTipo.startsWith("GENERAL_PROFILE")==true){
		
		
		
	}
		
		
		

	}//fin de los profiles
	
	
	
	//public static String ExtrairDadosAP3D(String data) {
	public static void calEcuaRec(double X1, double Y1, double X2, double Y2) {
		double m,b;
		
		m=(Y2-Y1)/(X2-X1);
		b=Y1-(m*X1);
		
//		if(b<0){
//			System.out.println("y="+m+"x" + b);
//		}else{
//			System.out.println("y="+m+"x+" + b);
//		}
		ValorX0=b;
		
	}
	
	
	//public static String ExtrairDadosAP3D(String data) {
		public static void CalAngDeIntercepcion(double X, double Y,double AngBaseC) {
			DecimalFormat df = new DecimalFormat("0.000");
			double Hip=0;
			
			Hip = Math.sqrt((X*X) + (Y*Y));
			//System.out.println("Hipotenusa: "+ Hip);
			
			double Angulo_Intercepcion = Math.acos(X/Hip);
			Angulo_Intercepcion = Double.valueOf(df.format(Math.toDegrees(Angulo_Intercepcion)).replaceAll(",", "."));
			Angulo_Intercepcion = (90-Angulo_Intercepcion);
			
			//System.out.println("(Angulo: "+ Angulo_Intercepcion + ")");
			
			
			X = AngBaseC * Math.cos(Math.toRadians(Angulo_Intercepcion));
			X =  Double.valueOf(df.format(X).replaceAll(",", "."));
			//System.out.println("(Val X: "+X+ ")");
			
			Y = AngBaseC * Math.sin(Math.toRadians(Angulo_Intercepcion));
			Y =  Double.valueOf(df.format(Y).replaceAll(",", "."));
			//System.out.println("(Val Y: "+Y+ ")");
			
			Xint=X;
			
			Yint=Y;
			
		}
		
		
		
		public static void ImpWSAnguloBase(int inc, boolean ImpTroFer,double CompEntreAngBase,int NumeroFer) {
			double radTool=DiaFertas[NumeroFer]/2;
			if(AngBase==radTool){
				//calculo el offset de la poccion en x para encontrar el diametro exacto 
				double h = Math.hypot((CompSup-CompInf)/2, ProfTem);
				//System.out.println(h); visualizo la hipotenusa
				double CalAngOffset = Math.asin((((CompSup-CompInf)/2)/h));
				//CalAngOffset = Math.toDegrees(CalAngOffset);
				//System.out.println(CalAngOffset);//visualizo el angulo de intercepcion
				double X_Offset = radTool * Math.cos(CalAngOffset);
				//X_Offset = Math.toDegrees(X_Offset);
				//System.out.println(radTool - X_Offset);
				double OffsetDiametro = radTool - X_Offset;
				
				do{
					Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, Yp-Tep_t6/2 + OffsetDiametro ,Tep_t6-OffsetDiametro*2,CompFea, ProfTem, DiaFertas[NumeroFer], Ori, ImpTroFer, listDadosFerramentas.get(NumeroFer),ImpTroFer,1,true);
				}while(inc==2);
				
				
				//Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompInf,CompFea, ProfTem, ProfTem-AngBase, DiaFertas[NumeroFer], Ori, true, listDadosFerramentas.get(NumeroFer));
			}else{
				
				
			Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-Tep_t6/2,Tep_t6,CompFea, ProfTem-AngBase+DiaFertas[1]/2, ProfTem-AngBase, DiaFertas[1], Ori, true, listDadosFerramentas.get(1));	
				
			ArrayList<Double> NumMasCercano = new ArrayList<Double>();
			//double NumMasCercano2[] = new double[11];//array de pociciojnes para este Slot
			//para ws3
			//calculo los valores de la curva en la base
			/**
			 * A tener en cuenta que se usa el metodo de calcular angulo y las variables que contienen los resultados de la clase:   CodeG_Para__Pocket_E_Operaciones
			 * ---   X_AngBase_PlanarRadius         -avance por el angulo
			 * ---   Y_AngBase_PlanarRadius     -profundidad
			 * */
			CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(AngBase);
			
			double datos[] = new double[CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.size()-1];//array de pociciojnes para este Slot
			
			if(CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.contains(Yint)==true){
				inc = CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.indexOf(Yint);
				//System.out.println("esta en:" + inc);
			}else{
				//System.out.println("(el valor no esta en la lista, buscando el mas proximo)");
				//double Nprox=0;
				for (int i = 0; i < CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.size()-1; i++) {
					double Resta= Math.abs(Yint-CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(i));
					NumMasCercano.add(Resta);
					datos[i] = Resta;
				}
				
				int n=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.size()-1;
				
				//organizo de menor a menor para tener el valor menor osea el valor mas proximo al Yintercepcion
				for(int i=0;i<n-1;i++){
	              int min=i;
                   for(int j=i+1;j<n;j++){
                       	if(datos[j]<datos[min]){
                    	   min=j;}
		               	}
		               
                   		if(i!=min){
		                  double aux =datos[i];
		                  datos[i]=datos[min];
		                  datos[min] = aux;
                   		}
				}//fin ordenar menor a mayor
				
				if(NumMasCercano.contains(datos[0])==true){
					inc = NumMasCercano.indexOf(datos[0]);
					//System.out.println("esta en:" + inc);
				}
				
				//System.out.println("(el mas proximo es: "+ CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc) +")");
			}

			//double tempPosY=0, tempProf=0;
			do{
				t1=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(inc);
				t2=AngBase-t1;
				t3=t2/2;
				
				if(CompEntreAngBase==CompInf){
					t4=(Yp-CompSup/2)+((CompSup-Tep_t6)/2);
				}else if(CompEntreAngBase==CompSup){
					t4=(Yp-CompSup/2);
				}
				
				
				
				t5=t3+t4;
				
				t6=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc);
				Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t5,Tep_t6-t2,CompFea, (ProfTem-AngBase)+t6, DiaFertas[NumeroFer], Ori, ImpTroFer, listDadosFerramentas.get(NumeroFer),ImpTroFer,1,true);
				//Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, Yp-Tep_t6/2,Tep_t6,CompFea, ProfTem, DiaFertas[NumeroFer], Ori, ImpTroFer, listDadosFerramentas.get(NumeroFer),ImpTroFer,1,true);
				ImpTroFer=false;

				inc++;
			}while(inc<=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1);
			
			}
			
		}//fin ImpWSAnguloBase
	
		public static void WorkingStep_Final_BaseRedonda(double DiaFer, boolean ProfileV_false_ProfileU_true, double Comp_CalculoAnguloBase, boolean ImpCorrecionY, double AdicionalAvance) {
			
			double RToolt = (DiaFer/2);
			
			if(ProfileV_false_ProfileU_true==true){
			//ws1 parte 1(baja hasta conpensar el radio de la Fer2)
			Slot_Ranhu_Simples_e_Segmentada.Slot(Xp, Yp-CompSup/2,CompSup,CompFea, ProfTem-(CompSup/2) +RToolt, (ProfTem-CompSup/2) ,  DiaFertas[F1], Ori, true, listDadosFerramentas.get(F1));
			}
			
			//ws1 parte 2
			int inc = 0 ;
			//boolean ImpTroFer=true;
			CodeG_Para__Pocket_E_Operaciones.CalculoAnguloBase(Comp_CalculoAnguloBase/2);
			
			Slot_Ranhu_Simples_e_Segmentada.correcionY=ImpCorrecionY;//para que no imporma la correcion del eje Y hasta que sea necesario (inicializado en false)
			
			double tpro2=0;
			
			if(ProfileV_false_ProfileU_true==true){
				do{
					t1=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.get(inc);
					t2=CompSup-t1*2;
					t3=(Yp-CompSup/2);
					t4=t2+t3;//pos y inicial
					t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc);
					
					if(t5==0){
						t5=CodeG_Para__Pocket_E_Operaciones.Y_AngBase_PlanarRadius.get(inc+1)/3;
					}
					
					if(t5+(ProfTem-CompSup/2)>=ProfTem-RToolt){//es para saber si supera el valor de la profundidad menos el radio de la herramienta  -- comp de fer en eje Z
						inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size();
					}else{
						if(Yp-t4>RToolt){
							double tpro = RToolt +(ProfTem-CompSup/2);
							tpro2= tpro+t5;
							Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(CompSup)-(t2*2),CompFea, tpro2, DiaFertas[F1], Ori, false, listDadosFerramentas.get(F1),false,0,false);
						}else{
							//Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[2], ProfTem, (ProfTem-CompSup/2)+t5-1, listDadosFerramentas.get(2),true,false);
							inc=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1;
						}
					}//fin supero el valor final menos el radio de la fer
			inc++;
			}while(inc<=CodeG_Para__Pocket_E_Operaciones.X_AngBase_PlanarRadius.size()-1);
			
			
			//ws1 parte 3 profundidade final
			/** al final lo que sobra se divide en dos parte 
			 * una que es la mitad de lo que sobra y se hace con slot segmentado
			 * dos la prof restante con slot simples**/
	
			//parte uno	 - segmentada
			tpro2=((ProfTem-(tpro2 - RToolt))/2);
		
			double tpro = (CompSup/2)- RToolt - tpro2;
			tpro = tpro/2;
			tpro2=tpro2+tpro;
			
			
			double Ang= Math.asin(tpro2/(CompSup/2));//caculo el angulo para despues calcular el valor de x
			//Ang=Math.toDegrees(Ang);//paso a grados
			//System.out.println(Ang);
			
			double DiaFinalX= (CompSup/2)*Math.cos(Ang);
			//System.out.println(DiaFinalX);
			
			t1=DiaFinalX;
			t2=CompSup - t1*2;
			t3=(Yp-CompSup/2);
			t4=t2+t3;//pos y inicial
			t5=RToolt +(ProfTem-CompSup/2) + tpro2;
	
			Slot_Ranhu_Simples_e_Segmentada.Slot_Segmentado(Xp, t4,(CompSup)-(t2*2),CompFea, t5, DiaFertas[F1], Ori, false, listDadosFerramentas.get(F1),false,0,false);
			
			//System.out.println();
			//parte dos - simples
			Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup/2, CompSup,CompFea, Ori, false, DiaFertas[F1], ProfTem, ProfTem-1, listDadosFerramentas.get(F1),false,false);
			}

		}
	
		
		
	public static void CalPendenteXY(double DiametroInicial, double Diametrofinal, double Profundidad, double AdicionalAvance) {
		
		//DecimalFormat df = new DecimalFormat("0.00");
		
		///calculo de la recta
		CalculoDosPontosDaReta.DiaInicial=DiametroInicial;
		CalculoDosPontosDaReta.DiaFinal=Diametrofinal;
		CalculoDosPontosDaReta.profFuro=Profundidad;
		//valor del paso de avance 0.5 por defecto (val opcionales 0.7 - 0.8 - 0.9 - 1)
		CalculoDosPontosDaReta.Avance=(double)br.UNB.LAB.GerarCodG.GeneradorCodidoG.ValorPasoAngulo/(10 + AdicionalAvance);
		CalculoDosPontosDaReta.CalPuntosLin();
		
		for (int i = 0; i < CalculoDosPontosDaReta.DiaSeg.size(); i++) {
				X_CalPontosDaRecta.add(CalculoDosPontosDaReta.DiaSeg.get(i));//ancho
				Y_CalPontosDaRecta.add(Profundidad-Math.abs(CalculoDosPontosDaReta.ProfSeg.get(i)));//profundidad 
		}
		
		System.out.println();
		m=CalculoDosPontosDaReta.m;
		b=CalculoDosPontosDaReta.b;
		
	}
	
	public static void InicioG(){
		//FuroSimples.WriteLineNo("G90 G17 G21 G54 M05");
		FuroSimples.WriteLineNo("G90 G17 G54 M05");
	}
	
	
}
