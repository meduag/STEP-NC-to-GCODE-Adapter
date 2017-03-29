package br.UNB.LAB.GerarCodG;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import br.UNB.LAB.FAcessorias.ImprimirInfo;
import br.UNB.LAB.FAcessorias.ImprimirPlanoSeg;
import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;
import br.UNB.LAB.InfAvancadas.MapearMachining_workingstep;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;
import br.UNB.LAB.Integrador.Integrador;

//import DadosDoCodigo.DadosDaPeca;

public class CodeG_Para__Pocket_E_Operaciones {
	
	static int ctokens=0,ImpFer=0;//contador de tokens
	int salif=1;
	int cont=0,conBEM=0;
	
	static double Xp=0,Yp=0,Zp=0,ProfTem=0,CompFea=0,lagura=0,CompSup=0,CompInf=0,AngBase=0,ValorX0=0,PlanarRadius=0,OrtogonalRadius=0,Largo=0,Ancho=0,LimXY=0;
	public static double Xi=0,Yi=0,Xf=0,Yf=0,MenosLim=0,OpFerIgual=0,ProIncOffset=1,impPlanSec=0;
	static String Ori="",OpenProfileTipo="",AddFer="";
	public static int ValorPasoAngulo=5,conJexis=0, conFerXFeature=0;
	
	//datos Ferramentas
	static String Ferramentas="",TipoFer="",DataFer="",NomeFer="", StrpasF1F2="";
	static double DiaFer=0,ContadorNumFer=0;
	static double diaFerFaceMill[] = new double[5];//array de pociciojnes para este Step
	static double DiaFertas[] = new double[5];//array de pociciojnes para este Step
	
	static double X_Temp_OrtogonalRadius[] = new double[30];//array de pociciojnes para este Slot
	static double Y_Temp_OrtogonalRadius[] = new double[30];//array de pociciojnes para este Slot
	
	public static double X_Temp_RadioFerramentas[] = new double[30];//array de pociciojnes para este Slot
	public static double Y_Temp_RadioFerramentas[] = new double[30];//array de pociciojnes para este Slot
	
	static boolean passou=false, iniTrocaFer=false;
	static int F1=0, F2=0;
	public static ArrayList<String> listDadosFerramentas = new ArrayList<String>();
	
	public static ArrayList<Double> X_AngBase_PlanarRadius = new ArrayList<Double>();
	public static ArrayList<Double> Y_AngBase_PlanarRadius = new ArrayList<Double>();
	static String VerficarExistenciaFer="",Ferjaexiste="";

	
	@SuppressWarnings("static-access")
	public void GenCodG_PocketeOper(){
		//System.out.println("Entro en poket para pasar datos");
		//ferramenta 1 y 2
		passou=false;
		F1=0;
		F2=1;
		StrpasF1F2="";
		listDadosFerramentas.clear();
		conBEM=0;
		conFerXFeature=0;
//		for (int i = 0; i < DiaFertas.length; i++) {
//			DiaFertas[i]=0;
//		}
		
		
		PlanoSeguranca SecPlan = new PlanoSeguranca();
		MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
		RectPocketComplexo RanhuraCompl = new RectPocketComplexo();
		//RectSimples RanLinSimp = new RectSimples();
		double Xp2=0, Yp2=0;
		
		///////////////Plano de seguranca
		String data2=SecPlan.ComprimentoPlanoSeg;
	
		//asignacion del valor que trabajara el angulo
		ValorPasoAngulo=Gcode.ValorPasoAngulo;
		
		StringTokenizer stp = new StringTokenizer(data2, "=;",true);//buscador de tokens con separadores activados
		ctokens=0;
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = stp.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);
			double menosPS=0;///////////////////////////////////////////////////////////////vALOR A MODIFICAR DEL PLANO DE SEGURIDAD
			if(ctokens==3){//copia el primer token con la primera ferramenta
				//System.out.println("Imprimiendo Secplan            "+ (Double.parseDouble(valores)-menosPS));
				RanhuraCompl.PlanSeg=Double.parseDouble(valores)-menosPS;
				RectSimples.PlanSeg=Double.parseDouble(valores)-menosPS; 
				RectPocketSimples.PlanSeg=Double.parseDouble(valores)-menosPS;
			}
		}while(stp.hasMoreTokens());
		
		
		int TamList=ExtrairDatos.InformacoesAvancadas.size();
		int cDatList=0;
			
			do{
				String data=ExtrairDatos.InformacoesAvancadas.get(cDatList);
				
				//System.out.println("(----------------------------------------------------Data de extracion " +data +")");
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
						Xp2=Xp;
//						System.out.println(Xp);
						}
					if(ctokens==9){//para Y
						Yp=Double.parseDouble(valores);
						Yp2=Yp;
//						System.out.println(Yp);
						}
					if(ctokens==13){//para Y
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
			
		if(data.startsWith("PlanarRadius=")==true){
			StringTokenizer st = new StringTokenizer(data, "=,;",true);//buscador de tokens con separadores activados
			do{//asignacion de direcciones
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = st.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				if(ctokens==3){//copia el primer token con la primera ferramenta
					PlanarRadius = Double.parseDouble(valores);//temporal para furos segmentados
//					System.out.println(ProfTem);
				}

				}while(st.hasMoreTokens());

			}
		
		if(data.startsWith("OrtogonalRadius=")==true){
			StringTokenizer st = new StringTokenizer(data, "=,;",true);//buscador de tokens con separadores activados
			do{//asignacion de direcciones
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = st.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				if(ctokens==3){//copia el primer token con la primera ferramenta
					OrtogonalRadius = Double.parseDouble(valores);//temporal para furos segmentados
//					System.out.println(ProfTem);
				}
				}while(st.hasMoreTokens());

			}
		
		if(data.startsWith("Pocket:")==true){
			StringTokenizer st = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
			do{//asignacion de direcciones
				ctokens++;//contador de tokens para encontrar el parentesis
				String valores = st.nextToken();//leo el proximo token
				//System.out.println(ctokens + " "+valores);
				if(ctokens==5){//copia el primer token con la primera ferramenta
					Largo = Double.parseDouble(valores);//temporal para furos segmentados
//					System.out.println(ProfTem);
				}
				if(ctokens==9){//copia el primer token con la primera ferramenta
					Ancho = Double.parseDouble(valores);//temporal para furos segmentados
//					System.out.println(ProfTem);
				}
				}while(st.hasMoreTokens());

			salif=0;
			}
		
		
		
		if(data.indexOf("TipoFer=")!=-1){
			conFerXFeature++;
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
							//System.out.println("(Troca: "+data+")");
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
			//System.out.println();
			
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
			
			
			
			
		}//fin tercera ferramenta adicionada
			
			
		}//fin tipo fer
			
			//System.out.println("("+data+" "+Ferjaexiste+")");
		
		
		
			if(salif==0){
			ImprimirInfo.ImprimirInformacion("(Fin feature)");
			salif=0;
			}
		
		

		
		cDatList++;
		
	}while(cDatList<TamList & salif==1);
			
		
		
		/********************** extraer informacion de las herramienras******************************/
		
		cDatList=0;
		//int LineNo=0;
		salif=1;
		ctokens=0;
		 
		//cantFerFacemill=0;
		boolean UsarProxFerBEM = false;
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
						//esta linea es la original
						//DiaFertas[cont++]=Double.parseDouble(valores);
				
						if(DiaFertas[0]==0){
							AddFer = AddFer + "F"+DiaFer;
							DiaFertas[cont++]=DiaFer;
						}else{
							String convToStr = "F"+DiaFer;
							if(AddFer.indexOf(convToStr)!=-1){
								//System.out.println("(A Ferra já Existe " + convToStr +")");
							}else{
								AddFer = AddFer + convToStr;
								DiaFertas[cont++]=DiaFer;
							}
	
						}
						
					}
				
				}while(st.hasMoreTokens());
				
				int cDatList2=1;
				do{
					String ProxFer=ExtrairDatos.InformacoesAvancadas.get(cDatList2);
					
					if(ProxFer.indexOf("BALL_ENDMILL")!=-1){
						 UsarProxFerBEM = true;
					}
					
					if(ProxFer.indexOf("Feature")!=-1){
						cDatList2=TamList+1;
						cDatList=TamList+1;
					}else{
						cDatList2++;
					}
					
				}while(cDatList2<TamList);
					
					
				
				
				
				
				
			}//fin face mill 
		
			//cDatList=0;
			salif=1;
			ctokens=0;
			if(data.indexOf("BALL_ENDMILL")!=-1 & UsarProxFerBEM == true){
				//System.out.println("("+data+")");
				StringTokenizer st = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
				
				do{//asignacion de direcciones
					ctokens++;//contador de tokens para encontrar el parentesis
					String valores = st.nextToken();//leo el proximo token
					//System.out.println(ctokens + " "+valores);
					
					if(ctokens==1){//copia el primer token con la primera ferramenta
						Ferramentas=valores;
						}
					if(ctokens==7){
						NomeFer=valores;
						}
					if(ctokens==11){
						TipoFer=valores;
						}
					if(ctokens==15){
						DiaFer = Double.parseDouble(valores);
						//DiaFertas[cont++]= Double.parseDouble(valores);
						String convToStr = "F"+DiaFer;
						if(AddFer.indexOf(convToStr)!=-1){
							System.out.println("(A Ferra já Existe " + convToStr + ")");
						}else{
							AddFer = AddFer + convToStr;
							DiaFertas[cont++]=DiaFer;
						}
						//AddFer = AddFer + DiaFer;
						}
					
					}while(st.hasMoreTokens());
				
				//cantFerFacemill++;
				cDatList=TamList;
				
				conBEM=1;
			}
			
			
			cDatList++;
			
		}while(cDatList<TamList);
			
	////////////fin area de herramientas
		
		/**********************Calculo de las pociciones en las esquinas*************************/
		Xi=Xp-(Largo/2);
		Yi=Yp-(Ancho/2);
		
		Xf=Largo+Xi;
		Yf=Ancho+Yi;
		
		/*System.out.println();
		System.out.println("(Pos Iniciales e finales)");
		System.out.println("(Xi:"+Xi+" Yi:"+Yi+")");
		System.out.println("(Xf:"+Xf+" Yf:"+Yf+")");
		System.out.println();*/

		/*****************************Fin Cal posiciones*******************************/
		
		
		
		ImprimirInfo.ImprimirInformacion("(//////////////////////////////////   "+ conFerXFeature +" //////////////////////////)");
		
		
		
		
		
		
		/****************************************************************************  Tipo Rectangular-Profile  - generar cod G con 2 ferramentas***********************************************************/	
		
		if(conBEM==0){//Pocket Rectangular con fondo plano
			//hay dos opciones con una ferramenta o con 2
			
		/****op1 -- 1 ferramenta
			la opcion 1 tiene dos subopciones
			      Op a)el diametro de la ferramenta es menor que el diametro del borde
				  Op b)el diametro de la ferramenta es igual al angulo del borde 	
			
			op2 -- 2 ferramentas
			la opcion 2 tiene tres subopciones
		      Op a)el diametro de la ferramenta 1 es mayor que el diametro del borde 
				Op a1)el diametro de la ferramenta 1 es igual que el ancho - hacer usinagem ranhura linear simple
				Op a1)el diametro de la ferramenta 1 es menor que el ancho - hacer usinagem proximo al borde
			  Op b1)el diametro de la ferramenta 2 es igual al angulo del borde
			  Op b2)el diametro de la ferramenta es igual al angulo del borde ***/

//			int OpcionFer=0;
//			//CUENTA LAS HERRAMIENTAS
//			for (int i = 0; i < DiaFertas.length; i++) {
//				if(DiaFertas[i]!=0){
//					OpcionFer++;
//				}else{
//					i  = DiaFertas.length;
//				}
//			}
			
			int OpcionFer=conFerXFeature;
			
			//System.out.println(Opcion);
			
			//int Acabamiento=0;
			
			/********* Op1 - SubOp a e b  ***********/
			if(OpcionFer==1){
				
				//SubOp a
				if((DiaFertas[0]/2)<OrtogonalRadius){/****************Op1 SubOp a ************************/
					//System.out.println("Op a) El diametro de la ferramenta es menor que el diametro del borde");
					UnaFerAcab_0_1(Xi,Yi,Largo,Ancho,0,true,false);

				}else{/****************Op1 SubOp b ************************/
					//System.out.println("Op b)el diametro de la ferramenta es igual al angulo del borde 	");
					UnaFerAcab_0_2(Xi,Yi,Largo,Ancho,0,true,false);
				}

			}//fin Op 1
			
			
			/////////////////////////////////////////////////DOS HERRAMIENTAS
			if(OpcionFer==2){
				if((DiaFertas[0]/2)>OrtogonalRadius){/****************Op2 SubOp a ************************/
					//System.out.println("Hola");
					
					/******************************************** Op2 SubOp a1 diametro dela herramienta 0 es igual que la anchura  ****************************************************************/
					if(DiaFertas[0]==Ancho){
						//imprime un poket simples
						poketSimples(Xi,Yi,Largo,true,DiaFertas[0],ProfTem,ProfTem,0);
						//SubOp a
						if((DiaFertas[1]/2)<OrtogonalRadius){/****************Op2 SubOp a ************************/
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,1,listDadosFerramentas.get(1),true,0);
						}else{/****************Op1 SubOp b ************************/
							//System.out.println("Op b)el diametro de la ferramenta es igual al angulo del borde 	");
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,2,listDadosFerramentas.get(1),true,0);
						}

					/********************************************Op2 SubOp a2 diametro dela herramienta 0 es menor que la anchura ****************************************************************/
					}else{
						poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[0],OrtogonalRadius,0,listDadosFerramentas.get(0),true,0);
					//para ferramenta 1
						if((DiaFertas[1]/2)<OrtogonalRadius){/****************Op2 SubOp b1 ************************/ //MENOR
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,1,listDadosFerramentas.get(1),true,0);
						}else{//IGUAL
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,2,listDadosFerramentas.get(1),true,0);
						}

					}//fin ferramenta 2
					
				}//FIN CUANDO LA HERRAMIENTA 1 ES MAYOR QUE EL RADIO DE LA ESQUINA
			}
			

			
			
			
		//FinPocket Rectangular con fondo plano	
		
			
			
			
			/****************************************************************************  Tipo Rectangular-Profile fondo arredondado  - generar cod G con 3 ferramentas***********************************************************/
		}else{//Pocket Rectangular con fondo arredondado
			
			//System.out.println("3 ferramentas");
			
			/****op1 -- 2 ferramenta 
			la opcion 1 tiene dos subopciones (la herramienta 1 tiene el mismo valor de radio que el ortogonalradius)
			      Op a)el diametro de la ferramenta 2 es menor que el diametro del borde
				  Op b)el diametro de la ferramenta 2 es igual al angulo del borde 	
			
			op2 -- 3 ferramentas
			la opcion 2 tiene tres subopciones con f1 f2 y f3
		      Op a) el diametro de la ferramenta 1: 
		        Op a1) es mayor que el diametro del borde Ortogonal radius
				Op a2)el diametro de la ferramenta 1 es igual que el ancho - hacer usinagem ranhura linear simple
				Op a1)el diametro de la ferramenta 1 es menor que el ancho - hacer usinagem proximo al borde
			  Op b1)el diametro de la ferramenta 2 es igual al angulo del borde
			  Op b2)el diametro de la ferramenta es igual al angulo del borde ***/
			
			

//			int OpcionFer=0;
//			for (int i = 0; i < DiaFertas.length; i++) {
//				if(DiaFertas[i]!=0){
//					OpcionFer++;
//				}else{
//					i  = DiaFertas.length;
//				}
//			}
			
			int OpcionFer=conFerXFeature;
			
			//System.out.println(Opcion);
			
			//int Acabamiento=0;
			
			/********* Op1 - SubOp a e b  ***********/ //conto dos ferra una para corte y angulo de esquina y otra para el angulo de la base
			if(OpcionFer==2){
				
				ProfTem=ProfTem-PlanarRadius;
				//System.out.println("(Nueva Prof: Z=" + ProfTem+")");
				
				//SubOp a
				if((DiaFertas[0]/2)<OrtogonalRadius){/****************Op1 SubOp a ************************/
					//System.out.println("\n\tOp a) \n\t\tEl diametro de la ferramenta es menor que el diametro del borde");
					UnaFerAcab_0_1(Xi,Yi,Largo,Ancho,0,true,false);
					
					ProfTem=ProfTem+PlanarRadius;
					UnaFerAcab_0_1(Xi+PlanarRadius,Yi+PlanarRadius,Largo-(PlanarRadius*2),Ancho-(PlanarRadius*2),(ProfTem-PlanarRadius),false,false);

					/*********************************************************************************************************************************************************************************************
					 * para colocar la ball end mill siempre hay 
					   Hay dos Opciones
					 * 1ra- Que la herramienta sea menor al PlanarRadius
					 * 2da- Que la herramienta sea igual al PlanarRadius ***/
					/***********************************************************   BallEndMill  1 2 FERRAMENTAS 1 FACE<ORGRA 2 BEM CON LAS 2 OPC *****************************************************************/
					if((DiaFertas[1]/2)<PlanarRadius){//1ra Opcion BEM
						CalculoAnguloBase(PlanarRadius);
						RectPocketComplexo.Imp_G42=true;
						poketSegmentado(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,1,listDadosFerramentas.get(1),true,(int)(ProfTem-PlanarRadius));
					}else{//2da Opcion BEM cuando tiene el mismo valor del diametro
						//activo la impresion de G42 mas la trancionde de WS2 para WS 3
						RectPocketComplexo.Imp_G42=true;
						poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,2,listDadosFerramentas.get(1),true,ProfTem-(DiaFertas[1]/2));
					}

					/***********************************************************   Fin BallEndMill   1 *****************************************************************/
					
					
					
					
				}else{/****************Op1 SubOp b ************************/
					//System.out.println("Op b)el diametro de la ferramenta es igual al angulo del borde 	");
					//utiliza la ferramenta 0 WS 1
					UnaFerAcab_0_2(Xi,Yi,Largo,Ancho,0,true,false);
					//cambio la profuncidad general a valor inicial para imprimir el WS 2
					ProfTem=ProfTem+PlanarRadius;
					UnaFerAcab_0_2(Xi+PlanarRadius,Yi+PlanarRadius,Largo-PlanarRadius*2,Ancho-PlanarRadius*2,ProfTem-PlanarRadius,false,false);
										
					/***************************************************************************************************************************************
					 * para colocar la ball end mill siempre hay 
					   Hay dos Opciones
					 * 1ra- Que la herramienta sea menor al PlanarRadius
					 * 2da- Que la herramienta sea igual al PlanarRadius ***/
					/***********************************************************   BallEndMill  2 - 2 FERRAMENTAS 1 FACE=ORGRA 2 BEM CON LAS 2 OPC *****************************************************************/
					
					//utiliza a ferramenta 1
					if((DiaFertas[1]/2)<PlanarRadius){//1ra Opcion BEM
						
						CalculoAnguloBase(PlanarRadius);
						RectPocketComplexo.Imp_G42=true;
						poketSegmentado(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,1,listDadosFerramentas.get(1),true,(int)PlanarRadius);

					}else{//2da Opcion BEM cuando tiene el mismo valor del diametro
						//activo la impresion de G42 mas la trancionde de WS2 para WS 3
						RectPocketComplexo.Imp_G42=true;
						poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,2,listDadosFerramentas.get(1),true,ProfTem-(DiaFertas[1]/2));
					}

		
					/***********************************************************   Fin BallEndMill   *****************************************************************/

				}

			}//fin Op 1
			

			if(OpcionFer==3){//cuando tiene tres ferramentas///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				//System.out.println("tres ferramentas");
				//ProfTem=ProfTem;
				//Acabamiento=0;
				if((DiaFertas[0]/2)>OrtogonalRadius){/****************Op2 SubOp a ************************/
					//System.out.println("Hola");
					
					/******************************************** Op2 SubOp a1 diametro dela herramienta 0 es igual que la anchura  ****************************************************************/
					if(DiaFertas[0]==Ancho){
						
						///----------------------------------------------------------------------primera WS0
						//imprime un poket simples
						poketSimples(Xi,Yi,Largo,true,DiaFertas[0],ProfTem-PlanarRadius,ProfTem-PlanarRadius,0);
						
						//para hacer la correcion de paridad
						if (((ProfTem-PlanarRadius) % 2) == 0) {
							//esPar
							RectPocketComplexo.CorrecionDeParidad(RectSimples.correcionX, RectSimples.posYi);
						}
						
						if((DiaFertas[1]/2)<OrtogonalRadius){/****************Op2 SubOp a ************************/
							poket(Xi,Yi,Ancho,Largo,ProfTem-PlanarRadius,DiaFertas[1],OrtogonalRadius,1,listDadosFerramentas.get(1),true,0);
						}else{/****************Op1 SubOp b ************************/
							//System.out.println("Op b)el diametro de la ferramenta es igual al angulo del borde 	");
							RectPocketComplexo.Imp_G42=true;
							poket(Xi,Yi,Ancho,Largo,ProfTem-PlanarRadius,DiaFertas[1],OrtogonalRadius,2,listDadosFerramentas.get(1),true,0);
							//poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[2],OrtogonalRadius,2,listDadosFerramentas.get(2),true,0);
						}
						
						///----------------------------------------------------------------------primera WS1
						if((DiaFertas[1]+PlanarRadius*2)>=Ancho){
							poketSimples(Xi+PlanarRadius,Yi+PlanarRadius,Largo-PlanarRadius*2,false,DiaFertas[1],ProfTem-PlanarRadius,ProfTem,1);
							
							if ((ProfTem % 2) == 0) {
								//esPar
								RectPocketComplexo.CorrecionDeParidad(RectSimples.correcionX, RectSimples.posYi);
							}
							
						}else{
							poket(Xi+PlanarRadius,Yi+PlanarRadius,Ancho,Largo,ProfTem,DiaFertas[1],OrtogonalRadius,0,listDadosFerramentas.get(1),true,0);
						}
						
						
//						poket(Xi,Yi,Ancho,Largo,ProfTem-PlanarRadius,DiaFertas[1],OrtogonalRadius,1,listDadosFerramentas.get(1),true,0);
//						
//						poket(Xi+PlanarRadius,Yi+PlanarRadius,Ancho-(PlanarRadius*2),Largo-(PlanarRadius*2),ProfTem,DiaFertas[1],OrtogonalRadius,0,listDadosFerramentas.get(1),false,(ProfTem-PlanarRadius));
						
						///----------------------------------------------------------------------primera WS2
						if((DiaFertas[2]/2)<OrtogonalRadius){/****************Op2 SubOp a ************************/
							RectPocketComplexo.Imp_G42=true;
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[2],OrtogonalRadius,1,listDadosFerramentas.get(2),true,ProfTem-PlanarRadius);
						}else{/****************Op1 SubOp b ************************/
							//System.out.println("Op b)el diametro de la ferramenta es igual al angulo del borde 	");
							RectPocketComplexo.Imp_G42=true;
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[2],OrtogonalRadius,2,listDadosFerramentas.get(2),true,ProfTem-PlanarRadius);
						}

					/********************************************Op2 SubOp a2 diametro dela herramienta 0 es menor que la anchura ****************************************************************/
					}else{
						poket(Xi,Yi,Ancho,Largo,ProfTem-PlanarRadius,DiaFertas[0],OrtogonalRadius,0,listDadosFerramentas.get(0),true,0);

						poket(Xi+PlanarRadius,Yi+PlanarRadius,Ancho-(PlanarRadius*2),Largo-(PlanarRadius*2),ProfTem,DiaFertas[0],OrtogonalRadius,0,listDadosFerramentas.get(0),false,(ProfTem-PlanarRadius));
						
					//para ferramenta 2
						if((DiaFertas[2]/2)<PlanarRadius){/****************Op2 SubOp b1 ************************/ //MENOR
							CalculoAnguloBase(PlanarRadius);
							RectPocketComplexo.Imp_G42=true;
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[2],OrtogonalRadius,1,listDadosFerramentas.get(2),true,(ProfTem-PlanarRadius));
						}else{//IGUAL
							RectPocketComplexo.Imp_G42=true;
							poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[2],OrtogonalRadius,2,listDadosFerramentas.get(2),true,(ProfTem-PlanarRadius));
						}

					}//fin ferramenta 2
					
						
				}else{//la herramienta 0 es menor que el Ortogonal radius
					//System.out.println("\n\tOp a) \n\t\tEl diametro de la ferramenta es menor que el diametro del borde");
					UnaFerAcab_0_1(Xi,Yi,Largo,Ancho,0,true,false);
					
					ProfTem=ProfTem+PlanarRadius;
					UnaFerAcab_0_1(Xi+PlanarRadius,Yi+PlanarRadius,Largo-(PlanarRadius*2),Ancho-(PlanarRadius*2),(ProfTem-PlanarRadius),false,false);

						
						
					}//fin ferramenta 1 y 2
				
				
				
				
			}

		}
		
		/*Xi=Xp2-(Largo/2);
		Yi=Yp2-(Ancho/2);*/
		ImprimirSecuenciaLineaNo.EscribirLinea("G00 Z" + Gcode.df.format(0));
		ImprimirSecuenciaLineaNo.EscribirLinea("G90 G40 G00 X" + Gcode.df.format(Xp2) + " Y" + Gcode.df.format(Yp2));
		ImprimirPlanoSeg.ImpPlanoSeguranza();
		
	}//fin generar cod g
	
	
	@SuppressWarnings("static-access")
	public static void poket(double X1, double Y1, double Anchop, double Largop, double Profundidade, double diaFeramenta, double OrtogonalRadius,int Acabamiento,String Ferramenta,boolean ImpTrocaFer, double ProfunOffset){
		//System.out.println(X1+" "  + Y1+" " + Ancho+" " + Largo+" " + Profundidade+" " + diaFeramenta+" " + OrtogonalRadius+" " + Acabamiento+" " + Ferramenta);
		
		RectPocketComplexo RanhuraCompl = new RectPocketComplexo();
		//PlanoSeguranca SecPlan = new PlanoSeguranca();
		if(Acabamiento==0){
			RanhuraCompl.IniA=true;
		}else if(Acabamiento==1 | Acabamiento==2 & iniTrocaFer==false){
			RanhuraCompl.IniA=false;
		}
		
		double inc=ProfunOffset + 1;
		//para imprimir una sola vez y al comienzo
		RanhuraCompl.ImpTrocaFer=ImpTrocaFer;
		
		do{
			RanhuraCompl.Acabamiento=Acabamiento;
			RanhuraCompl.dadosFer=Ferramenta;
			RanhuraCompl.OrtogonalRadius=OrtogonalRadius;
			RanhuraCompl.X1=X1;
			RanhuraCompl.Y1=Y1;
			RanhuraCompl.Largo=Largop;
			RanhuraCompl.Ancho=Anchop;
			RanhuraCompl.DTool= diaFeramenta;
			RanhuraCompl.Prof=inc;
			RanhuraCompl.RanPocketComplexoNc();
			inc++;
			}while(inc<=Profundidade);
		br.UNB.LAB.GerarCodG.Gcode.LineNo=RanhuraCompl.LineNo;
	}
	
	
	@SuppressWarnings("static-access")
	public static void poketSegmentado(double X1, double Y1, double Ancho, double Largo, double Profundidade, double diaFeramenta, double OrtogonalRadius,int Acabamiento,String Ferramenta,boolean ImpTrocaFer,int ProfunOffsetSeg){
		//System.out.println(X1+" "  + Y1+" " + Ancho+" " + Largo+" " + Profundidade+" " + diaFeramenta+" " + OrtogonalRadius+" " + Acabamiento+" " + Ferramenta);
		
		RectPocketComplexo RanhuraCompl = new RectPocketComplexo();
		//PlanoSeguranca SecPlan = new PlanoSeguranca();
			RanhuraCompl.IniA=true;

		int inc=0;
		//para imprimir una sola vez y al comienzo
		RanhuraCompl.ImpTrocaFer=ImpTrocaFer;
		RanhuraCompl.Acabamiento=Acabamiento;
		RanhuraCompl.dadosFer=Ferramenta;
		RanhuraCompl.OrtogonalRadius=OrtogonalRadius;
		RanhuraCompl.DTool= diaFeramenta;
		
		do{
			//con el valor de Y hacemos todo el recorrido desde x hasta la profundidad
			RanhuraCompl.X1=X1 + (PlanarRadius - X_AngBase_PlanarRadius.get(inc));
			RanhuraCompl.Y1=Y1 + (PlanarRadius - X_AngBase_PlanarRadius.get(inc));
			RanhuraCompl.Largo=Largo - (PlanarRadius - X_AngBase_PlanarRadius.get(inc))*2;
			RanhuraCompl.Ancho=Ancho - (PlanarRadius - X_AngBase_PlanarRadius.get(inc))*2;
			RanhuraCompl.Prof=ProfunOffsetSeg + Y_AngBase_PlanarRadius.get(inc);
			RanhuraCompl.RanPocketComplexoNc();
			inc++;
			//System.out.println(Y_AngBase_PlanarRadius.size());
			}while(inc<=(Y_AngBase_PlanarRadius.size()-1));
		
		br.UNB.LAB.GerarCodG.Gcode.LineNo=RanhuraCompl.LineNo;
	}
	
	
	
	
	
	@SuppressWarnings("static-access")
	//para imprimir la troca de herramienta ---------------------------------------------------------------------------------------------
	public static void poketSimples(double X1, double Y1, double Largo,boolean ImpTrocaFer,double DiaFerEscolido, double Profundidade, double OffsetProfundidade, int FerramentaDaLista){
		RectSimples RanLinSimp = new RectSimples();
		//System.out.println("Hola feature");
		
		double RadFerEscolido = DiaFerEscolido/2;
		
		//RanLinSimp.impIni=ImpTrocaFer;
		if(Largo>Ancho){
			RanLinSimp.Ori="Hor";
		}else{
			RanLinSimp.Ori="Ver";
		}
		
		RanLinSimp.posXi=X1+DiaFerEscolido;
		RanLinSimp.posYi=Y1+RadFerEscolido;
		RanLinSimp.posF=X1+Largo-DiaFerEscolido;
		RanLinSimp.Prof=Profundidade;
		RanLinSimp.dadosFer=listDadosFerramentas.get(FerramentaDaLista);
		RanLinSimp.DTool=DiaFerEscolido;
		RanLinSimp.offsetProf=-OffsetProfundidade;
		RanLinSimp.LineNo=br.UNB.LAB.GerarCodG.Gcode.LineNo;
		RanLinSimp.impIni=0;
		if(ImpTrocaFer==true){
			RanLinSimp.Imp_TrocaDeFer_Orden_Externa();
		}
		RanLinSimp.RanLinSimplesNc();
		br.UNB.LAB.GerarCodG.Gcode.LineNo=RanLinSimp.LineNo;
		
	}
	
	
	
	
	public void UnaFerAcab_0_1(double Xi,double Yi, double Largo, double Ancho,double ProfundadeOffset,boolean iniTrocaFerAca0,boolean iniTrocaFerAca1) {
		//System.out.println("Op a) El diametro de la ferramenta es menor que el diametro del borde");
		if(Integrador.ImpLedAcabamentoPocket==1){
			System.out.println("(01--------------------------------------------------------Ws0)");
		}
		/////
		/////
		/////-problema con iniTrocaFer
		poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[0],OrtogonalRadius,0,listDadosFerramentas.get(0),iniTrocaFerAca0,ProfundadeOffset);
		if(Integrador.ImpLedAcabamentoPocket==1){
			System.out.println("(01--------------------------------------------------------Ws1)");
		}
		/////
		/////
		/////-problema con iniTrocaFer
		poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[0],OrtogonalRadius,1,listDadosFerramentas.get(0),iniTrocaFerAca1,ProfundadeOffset);
		
	}
	
	public void UnaFerAcab_0_2(double Xi,double Yi, double Largo, double Ancho,double ProfundadeOffset,boolean iniTrocaFerAca0,boolean iniTrocaFerAca2) {
//		if(iniTrocaFerramenta==false){
			iniTrocaFer=false;
			//System.out.println("Op a) El diametro de la ferramenta es menor que el diametro del borde");
			if(Integrador.ImpLedAcabamentoPocket==1){
				System.out.println("(02--------------------------------------------------------Ws0)");
			}
			poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[0],OrtogonalRadius,0,listDadosFerramentas.get(0),iniTrocaFerAca0,ProfundadeOffset);
			if(Integrador.ImpLedAcabamentoPocket==1){
				System.out.println("(02--------------------------------------------------------Ws2)");
			}
			poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[0],OrtogonalRadius,2,listDadosFerramentas.get(0),iniTrocaFerAca2,ProfundadeOffset);
//		}else{
//			//System.out.println("Op a) El diametro de la ferramenta es menor que el diametro del borde");
//			if(Integrador.ImpLedAcabamentoPocket==1){
//				System.out.println("(02--------------------------------------------------------Ws0)");
//			}
//			poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[0],OrtogonalRadius,0,listDadosFerramentas.get(0),iniTrocaFerAca0,ProfundadeOffset);
//			iniTrocaFer=true;
//			
//			if(Integrador.ImpLedAcabamentoPocket==1){
//				System.out.println("(02--------------------------------------------------------Ws2)");
//			}
//			poket(Xi,Yi,Ancho,Largo,ProfTem,DiaFertas[0],OrtogonalRadius,2,listDadosFerramentas.get(0),iniTrocaFerAca2,ProfundadeOffset);
//			
//		}
	}
	
	public static void CalculoAnguloBase(double Angulo) {
		DecimalFormat L3 = new DecimalFormat("0.000");
		double X_Ang=0,Y_Ang=0,NValAng=0;
		if(ValorPasoAngulo<10){
			NValAng=10;
		}else{
			NValAng=ValorPasoAngulo;
		}
		for (int i = 0; i <= 90; i+=NValAng) {//calculo los valores del OrtogonalRadius
			X_Ang=(Angulo*Math.cos(Math.toRadians(i)));
			X_Ang = Double.parseDouble(L3.format(X_Ang).replace(",", "."));
			Y_Ang=(Angulo*Math.sin(Math.toRadians(i)));
			Y_Ang = Double.parseDouble(L3.format(Y_Ang).replace(",", "."));
			X_AngBase_PlanarRadius.add(X_Ang);
			Y_AngBase_PlanarRadius.add(Y_Ang);
			//System.out.println("Ang"+i+"  X"+X_Ang+"   Y"+Y_Ang);
		}
		//System.out.println();
		
	}
	
	
	

}//fin da clase



