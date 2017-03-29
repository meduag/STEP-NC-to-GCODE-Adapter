package br.UNB.LAB.GerarCodG;

//import java.text.DecimalFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

//import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.FAcessorias.CalularNoPasosProfundidadeCorte;
import br.UNB.LAB.FAcessorias.ImprimirInfo;
import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;
//import br.UNB.LAB.FAcessorias.ImprimirSecuenciaLineaNo;
import br.UNB.LAB.FuncoesMat_Posicao.CalculoDosPontosDaReta;
import br.UNB.LAB.FuncoesMat_Posicao.CalculoDosPuntosDoCirculo;
import br.UNB.LAB.InfAvancadas.MapearMachining_workingstep;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;
import br.UNB.LAB.Integrador.Integrador;


public class A02_GerarGcode_Slot {

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
	
	public static int ValorPasoAngulo=br.UNB.LAB.GerarCodG.Gcode.ValorPasoAngulo;
	
	public static ArrayList<String> listDadosFerramentas = new ArrayList<String>();
	public static ArrayList<String> listFer = new ArrayList<String>();
	
	public static ArrayList<Double> X_CalPontosDaRecta = new ArrayList<Double>();
	public static ArrayList<Double> Y_CalPontosDaRecta = new ArrayList<Double>();
	
	static double diaFerFaceMill[] = new double[5];//array de pociciojnes para este Slot
	
	static double DiaFertas[] = new double[20];//array de pociciojnes para este Slot
	static boolean SalExt=false,copListaOrdFer=false, passou=false;
	
	/**variable nueva junio 2013**/
	private static boolean ImprimirInicio = true;//variable adicional para imprimir inicio
	private static boolean ImprimirFinal = true;//variable adicional para imprimir inicio
	private static boolean ImprimirFinal_perfilcircular = true;//variable adicional para imprimir inicio
	private static int NumeroToolActual = 0;//variable adicional para imprimir inicio
	static double PuntoX=0,PuntoY=0;
	
	
	@SuppressWarnings({ "static-access"})
	public void GenCodG_SloteOper(){
		/**variable nueva junio 2013**/
		double InicioX=0, InicioY=0, InicioZ=0, ProfundidadTotalRanura=0, LarguraRanura=0, AnchoSuperior=0, AnchoInferior=0, AnguloDaBase=0;
		boolean OrientacionHt_Vf = true;
		
		
		
		
		//ferramenta 1 y 2
		passou=false;
		F1=0;
		F2=1;
		StrpasF1F2="";
		//listDadosFerramentas.clear();
		
		
		//adicionar ferramentas en falso
		addFerAOlist="";
	
		PlanoSeguranca SecPlan = new PlanoSeguranca();
		MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
		RectPocketSimples RanhuraSimp = new RectPocketSimples();
		//CalculoDosPuntosDoCirculo CalPonCir = new CalculoDosPuntosDoCirculo();
		
		//DecimalFormat L3 = new DecimalFormat("0.0000"); 
		SalExt=false;
		
		
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
						InicioX = Double.parseDouble(valores);
//						System.out.println(Xp);
						}
					if(ctokens==9){//para Y
						Yp=Double.parseDouble(valores);
						InicioY= Double.parseDouble(valores);
//						System.out.println(Yp);
						}
					if(ctokens==13){//para Z
						Zp=Double.parseDouble(valores);
						InicioZ= Double.parseDouble(valores);
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
							ProfundidadTotalRanura= Double.parseDouble(valores);
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
					LarguraRanura= Double.parseDouble(valores);
					}
				if(ctokens==11){//copia el primer token con la primera ferramenta
					Ori = valores;
					if(valores.startsWith("Hor")){OrientacionHt_Vf = true;}else{OrientacionHt_Vf = false;}
					
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
					AnchoSuperior=Double.parseDouble(valores);
					}
				if(ctokens==11){
					//correcion para open profice en V
					if(valores.startsWith("=")){
						valores = st.nextToken();
					}
					AnchoInferior=Double.parseDouble(valores);
					CompInf = Double.parseDouble(valores);//VeeProf - AngV
					
					}
				if(ctokens==15){
					AnguloDaBase=Double.parseDouble(valores);
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
		int cFerSt=0;
		int cantFerrAdq = OrdenarFerramentas.listDadosTrocaFerra.size();
		//System.out.println("cantidad de ferramentas" + cantFerrAdq);
		cantFerrAdq = cantFerrAdq-1;
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
					//if(Integrador.ImpLedFer==1){System.out.println("(---------------- Valor ferramenta"+valores+")");}
					DiaFertas[cFerSt]= Double.parseDouble(valores);						
					cFerSt++;
					salif=0;
				}
			}while(salif==1);
		cDatList++;
		}while(cDatList<=cantFerrAdq);
		salif=1;
		////////////fin area de herramientas
	
		/****************************************************************************  Tipo de OpenProfile  - generar cod G***********************************************************/
		//ProfundidadTotalRanura=1;
		if(ProfundidadTotalRanura<Gcode.ProfundidadeCorte){//arreglar la profundidad
			Gcode.ProfundidadeCorte=ProfundidadTotalRanura;
		}
		 
		/***********************       SQUARE_U_PROFILE        *******************************/
	if(OpenProfileTipo.startsWith("SQUARE_U_PROFILE")==true){////////////////////////////////////////////////////////////////////////////******************************
		
		if(OrientacionHt_Vf==true){//horizontal
			String ToolData = listDadosFerramentas.get(0);
			
			
			/** Primera Opcion Ranura recta**/
			if(AnchoSuperior==AnchoInferior & AnguloDaBase==0){
				double ProfundidadeOffset=0;
				int NumTool = ExtrairNumTool(ToolData);
				RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,NumTool);
			}//fin ranura paralela con base plana
			
			/** ranura paralela base areedondada **/
			if(AnchoSuperior==AnchoInferior & AnguloDaBase>0){ //esta dividida en dos partes la parte paralela e la base redonda Ferramenta 1
				Gcode_02ST.AnguloBase = AnguloDaBase;
				double ProfundidadeOffset=0;
				ToolData = listDadosFerramentas.get(0);
				RanuraBaseRedonda(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, AnchoSuperior, ToolData, ProfundidadeOffset, 0, AnguloDaBase,true);
				Gcode_02ST.AnguloBase = 0;//retorno el valor a 0
			}
			
			/** ranura en V base areedondada **/
			if(AnchoSuperior>AnchoInferior & AnguloDaBase>0){ //esta dividida en dos partes la parte paralela e la base redonda Ferramenta 1
				double ProfundidadeOffset=0;
				//toca hacer el calculo de la rectga
				CalAnguloIntercepcion(AnchoSuperior, AnchoInferior,ProfundidadTotalRanura,AnguloDaBase);//con esta funcion calculo la nueva profundidad total				
				
				RanuraEnV(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, AnchoSuperior, AnchoInferior, ToolData, ProfundidadeOffset, 1, AnguloDaBase);
				//ImprimirSecuenciaLineaNo.EscribirLinea("G40 G90 G01 F" + Gcode.MaxVelAvanceMaquina + " X" + Gcode.df.format(0) + " Y" + Gcode.df.format(0));

				double AnchoSuperiorOffset = CalculoDosPontosDaReta.DiaSeg.get(CalculoDosPontosDaReta.DiaSeg.size()-1)*2;
				//System.out.println("(Dados da Nova Ferramenta: "+ listDadosFerramentas.get(NumeroToolActual+1) +")");
				ToolData = listDadosFerramentas.get(NumeroToolActual+1);//seguinte ferramenta
				Gcode_02ST.AnguloBase = AnguloDaBase;
				
				
				//busco las herramientas Facemill, depues busco la mas cercana al valor del ancho en el punto de intercepcion
				double DiameTemp=0;
				boolean InselTool=false;
				for (int i = 0; i < listDadosFerramentas.size(); i++) {
					String ToolDataTemp = listDadosFerramentas.get(i);
					if(listDadosFerramentas.get(i).indexOf("TipoFer=BALL_ENDMILL")!=-1){//data.indexOf("=BLOCK(")!=-1
						if(TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)<=AnchoSuperiorOffset & TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)>=DiameTemp){
							DiameTemp=TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp);
							ToolData = listDadosFerramentas.get(i);		
							InselTool=true;
						}}}
				
				//int SelDiaTool=0;
				if(InselTool==true){
					//colocar el valor correcto de diametro
					StringTokenizer st = new StringTokenizer(ToolData, "T:)",false);//buscador de tokens con separadores
					String valores = st.nextToken();
					//System.out.println(valores);
					NumeroToolActual = (int)Double.parseDouble(valores)-1;
					ImprimirInfo.ImprimirInformacion("Correcion Dados da Nova Ferramenta: "+ ToolData);}

				RanuraBaseRedonda(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, AnchoSuperiorOffset, ToolData, ProfundidadeOffset, NumeroToolActual, AnguloDaBase,false);
			}
					
		}else{//vertical --- no da para fazer
			
			
			
		}
		
	}//fin SQUARE_U_PROFILE


	/***********************       PARTIAL_CIRCULAR_PROFILE        *******************************/
	if(OpenProfileTipo.startsWith("PARTIAL_CIRCULAR_PROFILE")==true){
		
		
		if(DiaFertas[1]==AnchoSuperior*2){//condicion para saber sison iguales el diamtro del furo con el diametro de la herramienta, teniendo en cuenta el offset
			String ToolData = listDadosFerramentas.get(1);
			double ProfundidadeOffset=0;
			RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, AnchoSuperior*2, ToolData,ProfundidadeOffset,1);
			//Slot_Ranhu_Simples_e_Segmentada.SlotSimples(Xp, Yp-CompSup, CompSup*2,CompFea, Ori, true, DiaFertas[1], ProfTem, Zp, listDadosFerramentas.get(1),true,true);
		}
		
		if(DiaFertas[1]<AnchoSuperior*2){//condicion para saber sison iguales el diamtro del furo con el diametro de la herramienta, teniendo en cuenta el offset
			String ToolData = listDadosFerramentas.get(1);
			double ProfundidadeOffset=InicioZ;
			ImprimirFinal_perfilcircular=false;
			RanuraBaseRedonda(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, AnchoSuperior*2, ToolData, ProfundidadeOffset, 1, ProfundidadTotalRanura,false);
			ImprimirSecuenciaLineaNo.G41Impreso=false;
			ImprimirFinal_perfilcircular=true;
		}
	
	}//fin PARTIAL_CIRCULAR_PROFILE
	
	
	
	

	
	/***********************       ROUNDED_U_PROFILE        *******************************/
	if(OpenProfileTipo.startsWith("ROUNDED_U_PROFILE")==true){
				
		String ToolData = listDadosFerramentas.get(0);
		
		//busco las herramientas Facemill, depues busco la mas cercana al valor del ancho en el punto de intercepcion
		double DiameTemp=0;
		boolean InselTool=false;
		for (int i = 0; i < listDadosFerramentas.size(); i++) {
			String ToolDataTemp = listDadosFerramentas.get(i);
			if(listDadosFerramentas.get(i).indexOf("TipoFer=FACEMILL")!=-1){//data.indexOf("=BLOCK(")!=-1
				if(TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)<=AnchoSuperior & TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)>=DiameTemp){
					DiameTemp=TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp);
					ToolData = listDadosFerramentas.get(i);		
					InselTool=true;
				}}}
		
		//int SelDiaTool=0;
		if(InselTool==true){
			//colocar el valor correcto de diametro
			StringTokenizer st = new StringTokenizer(ToolData, "T:)",false);//buscador de tokens con separadores
			String valores = st.nextToken();
			//System.out.println(valores);
			NumeroToolActual = (int)Double.parseDouble(valores)-1;
			ImprimirInfo.ImprimirInformacion("Correcion Dados da Nova Ferramenta: "+ ToolData);}
		
		double ProfundidadeOffset=0;
		double ProfWS1=ProfundidadTotalRanura -(AnchoSuperior/2);
		if(ProfWS1>0){
			RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfWS1, LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,NumeroToolActual);
		}
		
		
		//busco las herramientas Facemill, depues busco la mas cercana al valor del ancho en el punto de intercepcion
		DiameTemp=0;
		InselTool=false;
		for (int i = 0; i < listDadosFerramentas.size(); i++) {
			String ToolDataTemp = listDadosFerramentas.get(i);
			if(listDadosFerramentas.get(i).indexOf("TipoFer=BALL_ENDMILL")!=-1){//data.indexOf("=BLOCK(")!=-1
				if(TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)<=AnchoSuperior & TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)>=DiameTemp){
					DiameTemp=TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp);
					ToolData = listDadosFerramentas.get(i);		
					InselTool=true;
				}}}
		
		//int SelDiaTool=0;
		if(InselTool==true){
			//colocar el valor correcto de diametro
			StringTokenizer st = new StringTokenizer(ToolData, "T:)",false);//buscador de tokens con separadores
			String valores = st.nextToken();
			//System.out.println(valores);
			NumeroToolActual = (int)Double.parseDouble(valores)-1;
			ImprimirInfo.ImprimirInformacion("Correcion Dados da Nova Ferramenta: "+ ToolData);}
		
		if(DiaFertas[NumeroToolActual]==AnchoSuperior){//condicion para saber sison iguales el diamtro del furo con el diametro de la herramienta, teniendo en cuenta el offset
			ToolData = listDadosFerramentas.get(NumeroToolActual);
			RanuraParedesParalelasHorizontal(InicioX, InicioY, (AnchoSuperior/2), LarguraRanura, AnchoSuperior, ToolData,ProfWS1,NumeroToolActual);
		}
		
		if(DiaFertas[NumeroToolActual]<AnchoSuperior){//condicion para saber sison iguales el diamtro del furo con el diametro de la herramienta, teniendo en cuenta el offset
			ToolData = listDadosFerramentas.get(NumeroToolActual);
			ImprimirFinal_perfilcircular=false;
			ImprimirSecuenciaLineaNo.G41Impreso=false;
			RanuraBaseRedonda(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, AnchoSuperior, ToolData,ProfWS1, NumeroToolActual,(AnchoSuperior/2),false);
			ImprimirFinal_perfilcircular=true;
		}
	}//fin ROUNDED_U_PROFILE
	
	
	
	
	//System.out.println();
	/***************************       VEE_PROFILE        ***********************************/
	//int inc = 0;
	if(OpenProfileTipo.startsWith("VEE_PROFILE")==true){
		/*//InicioG();
		
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
		
		*//** la salida esta en x y
		X_CalPontosDaRecta.get(inc);
		Y_CalPontosDaRecta.get(inc);
		
									**//*
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

		
	}*/
	
		
		
	}
	
	//System.out.println();
	
	//PARA LA CURVA BEZIER --- No da para fazer
	if(OpenProfileTipo.startsWith("GENERAL_PROFILE")==true){
	}
		
		
		

	}//fin de los profiles
	
	
	
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

	public static void CalAngDeIntercepcion(double X, double Y,double AngBaseC) {
			DecimalFormat df = new DecimalFormat("0.000");
			double Hip=0;
			
			Hip = Math.sqrt((X*X) + (Y*Y));
			//System.out.println("Hipotenusa: "+ Hip);
			
			double Angulo_Intercepcion = Math.acos(X/Hip);
			Angulo_Intercepcion = Double.valueOf(df.format(Math.toDegrees(Angulo_Intercepcion)).replaceAll(",", "."));
			Angulo_Intercepcion = (90-Angulo_Intercepcion);
			
			ImprimirInfo.ImprimirInformacion("Angulo: "+ Angulo_Intercepcion);
			
			
			X = AngBaseC * Math.cos(Math.toRadians(Angulo_Intercepcion));
			X =  Double.valueOf(df.format(X).replaceAll(",", "."));
			//System.out.println("(Val X: "+X+ ")");
			
			Y = AngBaseC * Math.sin(Math.toRadians(Angulo_Intercepcion));
			Y =  Double.valueOf(df.format(Y).replaceAll(",", "."));
			//System.out.println("(Val Y: "+Y+ ")");
			
			Xint=X;
			
			Yint=Y;
			
		}
	
	public static void CalAnguloIntercepcion(double Distancia1, double Distancia2,double Profundidad, double AngBaseC) {
		DecimalFormat df = new DecimalFormat("0.0000");
		double Hip=0;
		double DistanciaCalculo = (Distancia1/2)-(Distancia2/2);
		Hip = Math.sqrt((DistanciaCalculo*DistanciaCalculo) + (Profundidad*Profundidad));
		
		//System.out.println("Hipotenusa: "+ Hip);
		double Angulo_Intercepcion = Math.acos(DistanciaCalculo/Hip);
		Angulo_Intercepcion = Double.valueOf(df.format(Math.toDegrees(Angulo_Intercepcion)).replaceAll(",", "."));
		Angulo_Intercepcion = (90-Angulo_Intercepcion);
		
		//System.out.println("(Angulo: "+ Angulo_Intercepcion + ")");
		PuntoX=Double.valueOf(AngBaseC * Math.cos(Math.toRadians(Angulo_Intercepcion)));
		PuntoY=Double.valueOf(AngBaseC * Math.sin(Math.toRadians(Angulo_Intercepcion)));
		//System.out.println();
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
		CalculoDosPontosDaReta.Avance=(double)br.UNB.LAB.GerarCodG.Gcode.ValorPasoAngulo/(10 + AdicionalAvance);
		CalculoDosPontosDaReta.CalPuntosLin();
		
		for (int i = 0; i < CalculoDosPontosDaReta.DiaSeg.size(); i++) {
				X_CalPontosDaRecta.add(CalculoDosPontosDaReta.DiaSeg.get(i));//ancho
				Y_CalPontosDaRecta.add(Profundidad-Math.abs(CalculoDosPontosDaReta.ProfSeg.get(i)));//profundidad 
		}
		
		ImprimirInfo.ImprimirInformacion("");
		m=CalculoDosPontosDaReta.m;
		b=CalculoDosPontosDaReta.b;
		
	}
	
	
	
	/*** Funciones nuevas Junio2013***/
	public static void RanuraParedesParalelasHorizontal(double InicioX, double InicioY, double ProfundidadTotalRanura,double LarguraRanura, double AnchoSuperior, String ToolData, double ProfundidadeOffset,int SelecionFer){
		
		double YPartida = InicioY - (AnchoSuperior/2);
		if(Integrador.ImpLedFer==1){
			ImprimirInfo.ImprimirInformacion("Ypartida Interno "+YPartida);}
		if(DiaFertas[SelecionFer]==AnchoSuperior){
			//Gcode_02ST.GCode_Ranuras(InicioX, YPartida, ProfundidadTotalRanura, AnchoSuperior, AnchoInferior,LarguraRanura,ToolData, 1);
			Gcode_02ST.GCode_RanhuraSimple(InicioX, InicioY, ProfundidadTotalRanura, LarguraRanura, ToolData,ProfundidadeOffset,ImprimirInicio);
		}else{		
			//hay dos tipos de anchura, la que da el borde y la mayor al borde
			//AnchoSuperior = 11;//para probar
			double Barredura = AnchoSuperior / DiaFertas[SelecionFer];//aqui esta para hacer con pendiente e circulo
			if(Barredura<=2){
				if(Integrador.ImpLedFer==1){ImprimirInfo.ImprimirInformacion("barredura menor a 2 Ancho: "+ AnchoSuperior +"  DiaF: "+DiaFertas[SelecionFer]);}
				CalularNoPasosProfundidadeCorte.PassadasPC(ProfundidadTotalRanura, Gcode.ProfundidadeCorte);
				if(CalularNoPasosProfundidadeCorte.NpasosEnterosPC ==1 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC==0){//aqui se cuando la profundidade es igual al diametro dela Herramienta
					Gcode_02ST.imprimirlinhaAdd=true;
					Gcode_02ST.GCode_RanhuraRecorrerBorde3ConFinal(InicioX, InicioY, ProfundidadTotalRanura, ToolData, LarguraRanura, AnchoSuperior,ImprimirInicio,ImprimirFinal,ProfundidadeOffset);//inicial
					Gcode_02ST.imprimirlinhaAdd=false;
				}else{
					Gcode_02ST.GCode_RanhuraRecorrerBordeSegmentada(InicioX, InicioY, Gcode.ProfundidadeCorte, ToolData, LarguraRanura, AnchoSuperior,true,false,ProfundidadeOffset);//inicial
					for (int i = 1; i < CalularNoPasosProfundidadeCorte.NpasosEnterosPC; i++) {
						if(i == CalularNoPasosProfundidadeCorte.NpasosEnterosPC-1 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC==0){
							Gcode_02ST.GCode_RanhuraRecorrerBordeSegmentada(InicioX, InicioY, Gcode.ProfundidadeCorte, ToolData, LarguraRanura, AnchoSuperior,false,true,ProfundidadeOffset);//intermedio
						}else{
							Gcode_02ST.GCode_RanhuraRecorrerBordeSegmentada(InicioX,InicioY, Gcode.ProfundidadeCorte, ToolData, LarguraRanura, AnchoSuperior,false,false,ProfundidadeOffset);//intermedio	
						}
					}
					if(CalularNoPasosProfundidadeCorte.NpasosDecimalesPC>0 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC<1){
						Gcode_02ST.GCode_RanhuraRecorrerBordeSegmentada(InicioX, InicioY, Gcode.ProfundidadeCorte *CalularNoPasosProfundidadeCorte.NpasosDecimalesPC, ToolData, LarguraRanura, AnchoSuperior,false,ImprimirFinal_perfilcircular,ProfundidadeOffset);
					}
				} 
			}else{//para colocar barredura mayor a tres 
				CalularNoPasosProfundidadeCorte.PassadasPC(ProfundidadTotalRanura, Gcode.ProfundidadeCorte);
				if(CalularNoPasosProfundidadeCorte.NpasosEnterosPC ==1 & CalularNoPasosProfundidadeCorte.NpasosDecimalesPC==0){//aqui se cuando la profundidade es igual al diametro dela Herramienta
					ImprimirInfo.ImprimirInformacion("barredura mayor a 2, Un solo paso");
					Gcode_02ST.GCode_RanhuraEspiralSegmentada2(InicioX, InicioY, Gcode.ProfundidadeCorte, ToolData, LarguraRanura, AnchoSuperior, ImprimirInicio, ImprimirFinal,ProfundidadeOffset);//inicial ---- mod
				}else{
					ImprimirInfo.ImprimirInformacion("barredura mayor a 2, varios paso Barredura: "+ Barredura );
					Gcode_02ST.GCode_RanhuraEspiralSegmentada2(InicioX, InicioY, Gcode.ProfundidadeCorte, ToolData, LarguraRanura, AnchoSuperior, true, false,ProfundidadeOffset);//inicial
					for (int i = 1; i < CalularNoPasosProfundidadeCorte.NpasosEnterosPC; i++){
						Gcode_02ST.GCode_RanhuraEspiralSegmentada2(InicioX, InicioY, Gcode.ProfundidadeCorte, ToolData, LarguraRanura, AnchoSuperior, false, false,ProfundidadeOffset);//intermedio
					}
					if(CalularNoPasosProfundidadeCorte.NpasosDecimalesPC>0){
						Gcode_02ST.GCode_RanhuraEspiralSegmentada2(InicioX, InicioY, Gcode.ProfundidadeCorte * CalularNoPasosProfundidadeCorte.NpasosDecimalesPC, ToolData, LarguraRanura, AnchoSuperior, false, false,ProfundidadeOffset);//final
					}
				}
			}
		}
	}

	public static void RanuraBaseRedonda(double InicioX, double InicioY, double ProfundidadTotalRanura,double LarguraRanura, double AnchoSuperior, String ToolData, double ProfundidadeOffset,int SelecionFer,double AnguloDaBase,boolean ImprimirRanuraParalela){
		
		double ProfundidadeSinlaBase = ProfundidadTotalRanura - AnguloDaBase;
		ProfundidadeOffset=0;
		ImprimirInfo.ImprimirInformacion("Profundidade 1: "+ ProfundidadeSinlaBase);
		if(ImprimirRanuraParalela==true){
			RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfundidadeSinlaBase, LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,SelecionFer);
			//String
			NumeroTool(false, AnguloDaBase*2);
			//SelecionFer=1;
		}
		ProfundidadeOffset=ProfundidadeSinlaBase;
		if((ProfundidadTotalRanura - ProfundidadeOffset)<Gcode.ProfundidadeCorte){
			Gcode.ProfundidadeCorte=ProfundidadTotalRanura- ProfundidadeOffset;
			}
		
		//para hacer la ranura dependiendo de la herramienta hay 3 tipos
		/*1 el angulo de la base (rad*2) es igual al diametro de la herramienta y el anchosup de la ranura es igual al angdabase*2, en ese caso solo hacer una ranura simple*/
		if((AnguloDaBase*2) == AnchoSuperior & DiaFertas[NumeroToolActual]==(AnguloDaBase*2)){
			//System.out.println("(Dados da Nova Ferramenta: "+ ToolData +")");
			ToolData=listDadosFerramentas.get(NumeroToolActual);
			RanuraParedesParalelasHorizontal(InicioX, InicioY, AnguloDaBase, LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,NumeroToolActual);//al final es la herramienta
		}
		
		/*2 el angulo de la base (rad*2) es igual al diametro de la herramienta y el anchosup de la ranura es menor al angdabase*2, en ese caso solo hacer barredurade borde o espiral*/
		if((AnguloDaBase*2) < AnchoSuperior & AnguloDaBase == (DiaFertas[NumeroToolActual]/2)){
			//System.out.println("(la lista es---------)" + listDadosFerramentas.size());
			if(listDadosFerramentas.size() == NumeroToolActual){
				NumeroToolActual = NumeroToolActual-1;
			}
			ToolData=listDadosFerramentas.get(NumeroToolActual);
			//RanuraParedesParalelasHorizontal(InicioX, InicioY+(AnchoSuperior/2), AnguloDaBase, LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,SelecionFer);
			//double YinicialMod = (AnchoPasada/2)+Yinicial; 
			RanuraParedesParalelasHorizontal(InicioX, InicioY, AnguloDaBase, LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,NumeroToolActual);
		}
		
		/*2 el angulo de la base (rad*2) es igual al diametro de la herramienta y el anchosup de la ranura es menor al angdabase*2, en ese caso solo hacer barredurade borde o espiral*/
		if((AnguloDaBase*2) < AnchoSuperior & AnguloDaBase > (DiaFertas[NumeroToolActual]/2)){
			ToolData=listDadosFerramentas.get(NumeroToolActual);
			ImprimirInfo.ImprimirInformacion("Dados da Nova Ferramenta: "+ ToolData);
			CalculoDosPuntosDoCirculo.CalcularPuntosdaSemiCircunferencia(AnguloDaBase, AnguloDaBase);
			CalculoDosPuntosDoCirculo.CalcProfOffSemiCir(AnguloDaBase-(DiaFertas[NumeroToolActual]/2), AnguloDaBase-(DiaFertas[NumeroToolActual]/2));
			//primera pasada
			ImprimirFinal = false;//ojo esta variable debe modificarse despues de ser usada para verdadero - true;
			if(Gcode.ProfundidadeCorte>(DiaFertas[NumeroToolActual]/2)){
				Gcode.ProfundidadeCorte=(DiaFertas[NumeroToolActual]/2);
			}
			RanuraParedesParalelasHorizontal(InicioX, InicioY, (DiaFertas[NumeroToolActual]/2), LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,NumeroToolActual);
			//modifico la velocidad de corte para que sea la maxima velocida de la maquina pues el corte es muy superficial
			double VeloCorteAnterior = Gcode.MaxVelcorteTool;
			Gcode.MaxVelcorteTool = Gcode.MaxVelAvanceMaquina;
			//double Yinicial = InicioY - (AnchoSuperior/2);
			double offsetAnchura =0;
			if(PuntoX==0){
				offsetAnchura = AnchoSuperior-(AnguloDaBase*2) ;
			}else{
				offsetAnchura = AnchoSuperior-(PuntoX*2) ;
			}
			
			
			double ProfCPasada=0;//lo coloco antes para que guarde el valor anterior y asi poder usarlo en el offset de profundidad
			double AnchoPasada=AnchoSuperior;
			
			for (int i = 1; i < CalculoDosPuntosDoCirculo.DiamSeg.size(); i++) {
				//Arreglos de datos
				//double DiaOffsetpasada= AnchoPasada;
				AnchoPasada = CalculoDosPuntosDoCirculo.DiamSeg.get(i);
				double ProfOffsetpasada= ProfCPasada;
				ProfCPasada = CalculoDosPuntosDoCirculo.ProfOff.get(i);
				if((AnchoPasada*2)>AnchoSuperior){
					AnchoPasada = AnchoPasada + offsetAnchura;
				}else{
					AnchoPasada = (AnchoPasada*2) + offsetAnchura;
				}
			
				//double YinicialMod = (AnchoPasada/2)+Yinicial; System.out.println("( Valor fijo        "+ AnchoPasada +")");
				Gcode.ProfundidadeCorte=ProfCPasada-ProfOffsetpasada;
				double ComProfPaso=(ProfundidadeOffset + (DiaFertas[NumeroToolActual]/2) +  ProfCPasada);
				double diaanterior111=(Math.abs((AnchoPasada-AnchoSuperior)))/2;
				//System.out.println("("+diaanterior111+")");
				Gcode_02ST.DiametroAnterior=diaanterior111;
				if(ComProfPaso<=ProfundidadTotalRanura & (ProfCPasada-ProfOffsetpasada)>0.01){
					ImprimirInicio = false;//ojo esta variable debe modificarse despues de ser usada para verdadero - true;
					double Barredura = AnchoPasada / DiaFertas[NumeroToolActual];//aqui esta para hacer con pendiente e circulo
					
					if(Barredura<=2 & Barredura>1){
						ImprimirFinal = true;
						Gcode_02ST.imprimirlinhaAdd=true;
						RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[NumeroToolActual]/2)+ ProfOffsetpasada),NumeroToolActual);
					}else{		
						if(Barredura==1){
							//RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[SelecionFer]/2)+ ProfOffsetpasada),SelecionFer);
						}else{
							RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[NumeroToolActual]/2)+ ProfOffsetpasada),NumeroToolActual);
						}
					}
					
					}else{
						double Barredura = AnchoPasada / DiaFertas[NumeroToolActual];//aqui esta para hacer con pendiente e circulo
						if(Barredura==1){
							RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[NumeroToolActual]/2)+ ProfOffsetpasada),NumeroToolActual);
						}
					//i = CalculoDosPuntosDoCirculo.DiamSeg.size();
					}
				}
				//regenero los valores de las variables a su condicion inicial
				Gcode_02ST.imprimirlinhaAdd=false;
				Gcode_02ST.DiametroAnterior=0;
				ImprimirInicio = true;
				ImprimirFinal = true;//ojo esta variable debe modificarse despues de ser usada para verdadero - true;
				Gcode.MaxVelcorteTool =VeloCorteAnterior;
				Gcode.ProfundidadeCorte=2;
			}
		
		if((AnguloDaBase*2) == AnchoSuperior & DiaFertas[NumeroToolActual]<(AnguloDaBase*2)){//este es para perfil circular.....
			//System.out.println
			ToolData=listDadosFerramentas.get(NumeroToolActual);
			ImprimirInfo.ImprimirInformacion("Dados da Nova Ferramenta: "+ ToolData);
			CalculoDosPuntosDoCirculo.CalcularPuntosdaSemiCircunferencia(AnguloDaBase, AnguloDaBase);
			CalculoDosPuntosDoCirculo.CalcProfOffSemiCir(AnguloDaBase-(DiaFertas[NumeroToolActual]/2), AnguloDaBase-(DiaFertas[NumeroToolActual]/2));
			//primera pasada
			ImprimirFinal = false;//ojo esta variable debe modificarse despues de ser usada para verdadero - true;
			if(Gcode.ProfundidadeCorte>(DiaFertas[NumeroToolActual]/2)){
				Gcode.ProfundidadeCorte=(DiaFertas[NumeroToolActual]/2);
			}
			RanuraParedesParalelasHorizontal(InicioX, InicioY, (DiaFertas[NumeroToolActual]/2), LarguraRanura, AnchoSuperior, ToolData,ProfundidadeOffset,NumeroToolActual);
			
			//modifico la velocidad de corte para que sea la maxima velocida de la maquina pues el corte es muy superficial
			double VeloCorteAnterior = Gcode.MaxVelcorteTool;
			Gcode.MaxVelcorteTool = Gcode.MaxVelAvanceMaquina;
			//double Yinicial = InicioY - (AnchoSuperior/2);
			double offsetAnchura = 0 ;
			double ProfCPasada=0;//lo coloco antes para que guarde el valor anterior y asi poder usarlo en el offset de profundidad
			double AnchoPasada=AnchoSuperior;
			
			for (int i = 1; i < CalculoDosPuntosDoCirculo.DiamSeg.size(); i++) {
				//Arreglos de datos
				//double DiaOffsetpasada= AnchoPasada;
				AnchoPasada = CalculoDosPuntosDoCirculo.DiamSeg.get(i);
				double ProfOffsetpasada= ProfCPasada;
				ProfCPasada = CalculoDosPuntosDoCirculo.ProfOff.get(i);
				if((AnchoPasada*2)>AnchoSuperior){
					AnchoPasada = AnchoPasada + offsetAnchura;
				}else{
					AnchoPasada = (AnchoPasada*2) + offsetAnchura;
				}
				//double YinicialMod = (AnchoPasada/2)+Yinicial; System.out.println("( Valor fijo        "+ AnchoPasada +")");
				Gcode.ProfundidadeCorte=ProfCPasada-ProfOffsetpasada;
				//double ComProfPaso=(ProfundidadeOffset + (DiaFertas[SelecionFer]/2) +  ProfCPasada);
				double diaanterior111=(Math.abs((AnchoPasada-AnchoSuperior)))/2;
				//System.out.println("("+diaanterior111+")");
				Gcode_02ST.DiametroAnterior=diaanterior111;
				if((AnchoPasada-(DiaFertas[NumeroToolActual]))>0){
					ImprimirInicio = false;//ojo esta variable debe modificarse despues de ser usada para verdadero - true;
					double Barredura = AnchoPasada / DiaFertas[NumeroToolActual];//aqui esta para hacer con pendiente e circulo
					
					if(Barredura<=2 & Barredura>1){
						ImprimirFinal = true;
						Gcode_02ST.imprimirlinhaAdd=true;
						RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[NumeroToolActual]/2)+ ProfOffsetpasada),NumeroToolActual);
					}else{		
						if(Barredura==1){
							//RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[SelecionFer]/2)+ ProfOffsetpasada),SelecionFer);
						}else{
							RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[NumeroToolActual]/2)+ ProfOffsetpasada),NumeroToolActual);
						}
					}
					
					}else{
						double Barredura = AnchoPasada / DiaFertas[NumeroToolActual];//aqui esta para hacer con pendiente e circulo
						if(Barredura==1){
							RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfCPasada-ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,(ProfundidadeOffset + (DiaFertas[NumeroToolActual]/2)+ ProfOffsetpasada),NumeroToolActual);
						}
					//i = CalculoDosPuntosDoCirculo.DiamSeg.size();
					}
				}
				//regenero los valores de las variables a su condicion inicial
				Gcode_02ST.imprimirlinhaAdd=false;
				Gcode_02ST.DiametroAnterior=0;
				ImprimirInicio = true;
				ImprimirFinal = true;//ojo esta variable debe modificarse despues de ser usada para verdadero - true;
				Gcode.MaxVelcorteTool =VeloCorteAnterior;
				Gcode.ProfundidadeCorte=2;
			

		}
		
	
		
	}
	
	public static void RanuraEnV(double InicioX, double InicioY, double ProfundidadTotalRanura,double LarguraRanura, double AnchoSuperior, double AnchoInferior, String ToolData, double ProfundidadeOffset,int SelecionFer,double AnguloDaBase){
		CalculoDosPontosDaReta.CalculoDosPuntosLinha_F3(AnchoSuperior,AnchoInferior,ProfundidadTotalRanura,(ProfundidadTotalRanura-AnguloDaBase)+PuntoY);
		//CalculoDosPontosDaReta.DiaSeg.get(1);
		double AnchoSelecTool = CalculoDosPontosDaReta.DiaSeg.get(CalculoDosPontosDaReta.DiaSeg.size()-1);
		
		//organizar la herramienta a usar
		//busco las herramientas Facemill, depues busco la mas cercana al valor del ancho en el punto de intercepcion
		double DiameTemp=0;
		boolean InselTool=false;
		for (int i = 0; i < listDadosFerramentas.size(); i++) {
			String ToolDataTemp = listDadosFerramentas.get(i);
			//System.out.println(ToolDataTemp);
			if(listDadosFerramentas.get(i).indexOf("TipoFer=FACEMILL")!=-1){//data.indexOf("=BLOCK(")!=-1
				if((TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)-0.1)<=AnchoSelecTool & TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)>=DiameTemp){
					DiameTemp=TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp);
					ToolData = listDadosFerramentas.get(i);		
					InselTool=true;
				}}}
		
		//int SelDiaTool=0;
		if(InselTool==true){
			//colocar el valor correcto de diametro
			StringTokenizer st = new StringTokenizer(ToolData, "T:)",false);//buscador de tokens con separadores
			String valores = st.nextToken();
			//System.out.println(valores);
			NumeroToolActual = (int)Double.parseDouble(valores)-1;
			ImprimirInfo.ImprimirInformacion("Correcion Dados da Nova Ferramenta: "+ ToolData );}

		//primera pasada
		double VeloCorteAnterior = Gcode.MaxVelcorteTool;//para restaurar despues
		Gcode.MaxVelcorteTool = Gcode.MaxVelAvanceMaquina/2;
		Gcode.ProfundidadeCorte=CalculoDosPontosDaReta.ProfSeg.get(1)/4;
		ImprimirFinal = false;
		//InicioY=InicioY+(TrocaDeFerramenta.DatoDiametroTool(ToolData)/4);
		
		RanuraParedesParalelasHorizontal(InicioX, InicioY, CalculoDosPontosDaReta.ProfSeg.get(1)/4, LarguraRanura, CalculoDosPontosDaReta.DiaSeg.get(0)*2, ToolData,ProfundidadeOffset,NumeroToolActual);//al final es la herramienta
		ImprimirSecuenciaLineaNo.EscribirLinea("Z"+ Gcode.df.format(0));
		
		//double ProfCPasada=0;//lo coloco antes para que guarde el valor anterior y asi poder usarlo en el offset de profundidad
		//double AnchoPasada=AnchoSuperior;
		double Yinicial = InicioY - (AnchoSuperior/2);
		double ProfCPasada=0;//lo coloco antes para que guarde el valor anterior y asi poder usarlo en el offset de profundidad
		double AnchoPasada=AnchoSuperior;
	
		for (int i = 1; i < CalculoDosPontosDaReta.DiaSeg.size(); i++) {
			ImprimirInicio = false;
			//Arreglos de datos
			double ProfOffsetpasada= Math.abs(ProfCPasada-CalculoDosPontosDaReta.ProfSeg.get(i)); 
			Gcode.ProfundidadeCorte=ProfOffsetpasada;
			ProfCPasada=CalculoDosPontosDaReta.ProfSeg.get(i);
			
			AnchoPasada = CalculoDosPontosDaReta.DiaSeg.get(i)*2;
			ImprimirInfo.ImprimirInformacion("Ancho padada        "+ AnchoPasada);
			double YinicialMod = (AnchoPasada/2)+Yinicial; 
			ImprimirInfo.ImprimirInformacion("Yinicial Mod      "+ YinicialMod);
			//double ComProfPaso=(ProfundidadeOffset + (DiaFertas[1]/2) +  ProfCPasada);
			double diaanterior111=(Math.abs((AnchoPasada-AnchoSuperior)))/2;
			ImprimirInfo.ImprimirInformacion("Diametro anterior        "+diaanterior111);
			Gcode_02ST.DiametroAnterior=diaanterior111;
			double yt= (YinicialMod+(diaanterior111));
			ImprimirInfo.ImprimirInformacion("Diametro anterior        "+yt);
			//RanuraParedesParalelasHorizontal(InicioX, YinicialMod, ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,ProfundidadeOffset,NumeroToolActual);//original actual
			RanuraParedesParalelasHorizontal(InicioX, InicioY, ProfOffsetpasada, LarguraRanura, AnchoPasada, ToolData,ProfundidadeOffset,NumeroToolActual);
			//RanuraParedesParalelasHorizontal(InicioX, YinicialMod+(diaanterior111), ProfOffsetpasada, LarguraRanura, AnchoPasada-(diaanterior111/2), ToolData,ProfundidadeOffset,NumeroToolActual);//original viejo
		}
		
		//System.out.println();
		//regenero los valores de las variables a su condicion inicial
		Gcode_02ST.imprimirlinhaAdd=false;
		Gcode_02ST.DiametroAnterior=0;
		ImprimirInicio = true;
		ImprimirFinal = true;//ojo esta variable debe modificarse despues de ser usada para verdadero - true;
		Gcode.MaxVelcorteTool =VeloCorteAnterior;
		Gcode.ProfundidadeCorte=2;
	}
	
	public static int ExtrairNumTool(String Tooldata){
		int NumTool=0;
		StringTokenizer st = new StringTokenizer(Tooldata, "T:",false);//buscador de tokens con separadores activados
		String valores = st.nextToken();//leo el proximo token
		//System.out.println(valores);
		
		NumTool = (int)Double.parseDouble(valores);
		if(NumTool>0){
			NumTool=NumTool-1;
		}else{
			NumTool=0;
		}
		return NumTool;
	}
	
	public static void NumeroTool(boolean FaceTrue_BEmFalse, double ValorAComparar_or_AnchoSuperior){
		//busco las herramientas Facemill, depues busco la mas cercana al valor del ancho en el punto de intercepcion
		String ToolData="";
		String TipoFer="";
		if(FaceTrue_BEmFalse==true){
			TipoFer="TipoFer=FACEMILL";
		}else{
			TipoFer="TipoFer=BALL_ENDMILL";
		}
		double DiameTemp=0;
		boolean InselTool=false;
		for (int i = 0; i < listDadosFerramentas.size(); i++) {
			String ToolDataTemp = listDadosFerramentas.get(i);
			if(listDadosFerramentas.get(i).indexOf(TipoFer)!=-1){//data.indexOf("=BLOCK(")!=-1
				if(TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)<=ValorAComparar_or_AnchoSuperior & TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp)>=DiameTemp){
					DiameTemp=TrocaDeFerramenta.DatoDiametroTool(ToolDataTemp);
					ToolData = listDadosFerramentas.get(i);		
					InselTool=true;
				}}}
		
		//int SelDiaTool=0;
		if(InselTool==true){
			//colocar el valor correcto de diametro
			StringTokenizer st = new StringTokenizer(ToolData, "T:)",false);//buscador de tokens con separadores
			String valores = st.nextToken();
			//System.out.println(valores);
			NumeroToolActual = (int)Double.parseDouble(valores)-1;
			ImprimirInfo.ImprimirInformacion("Correcion Dados da Nova Ferramenta: "+ ToolData);}
		
	}
	
	
	}//fin da clase
