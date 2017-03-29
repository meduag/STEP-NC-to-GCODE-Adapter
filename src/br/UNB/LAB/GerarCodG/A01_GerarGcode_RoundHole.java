package br.UNB.LAB.GerarCodG;

import java.util.StringTokenizer;

import javax.vecmath.Point3d;

import br.UNB.LAB.FAcessorias.ExtrairDiametroTool;
import br.UNB.LAB.FAcessorias.ExtrairToken3_Profundidad_DiaInicial_DiaFinal;
import br.UNB.LAB.FAcessorias.ExtrairUbicacao;
import br.UNB.LAB.FuncoesMat_Posicao.*;
import br.UNB.LAB.InfAvancadas.MapearMachining_workingstep;
import br.UNB.LAB.InfBasicas.DadosDaPeca;


public class A01_GerarGcode_RoundHole {
	
	int ctokens=0;//contador de tokens
	int salif=1;
	int paso_sig_lineNo = 0;
	//int ExisteDiaFin=0;
	double diametroInicialFuro=0;
	double diametroFinalFuro=0;
	double ProfundidadeFuro=0;
	double RadioBase=0;
	//double X=0,Y=0,Z=0;
	String HoleBoCond="";
	
	//activacion de herramientas
	int Cd=1,	Td=1,	FaM=1,	BaEM=0,	BuEM=0;
	
	//valores para generar el codigo G
	double CemtroX=0,CentroY=0,Z=0;
	public static double OffsetProfZ=0;
	
	public static int ValorPasoAngulo=10;
	public static double AngOffsetTwD=0;
	public static int LineNo=0;
	//public static double DiametroTempTwisDrill_Facemill=0;
	
	//boleanos
	//public static boolean FuroPasante=false;
	
	

	@SuppressWarnings("static-access")
	public void GenCodG_RoundHoleOper(){
		//FuroPasante=false;
		
		MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
		CalculoDosPuntosDoCirculo CalPonCir = new CalculoDosPuntosDoCirculo();

		LineNo=Gcode.LineNo;
		ctokens=0;

		int TamList=ExtrairDatos.InformacoesAvancadas.size();
		int cDatList=0;
		
		do{
			String data=ExtrairDatos.InformacoesAvancadas.get(cDatList);
			Point3d PocisaoXYZ = null;//utilizo esta variable para adquirir los valores extraidos 
			if(data.startsWith("Ubicacion:")==true){
				PocisaoXYZ = ExtrairUbicacao.ExtPocisaoXYZ(data);
				//valores X
				CemtroX=PocisaoXYZ.x;
				//valores Y
				CentroY=PocisaoXYZ.y;
				//valores Z
				OffsetProfZ=PocisaoXYZ.z;
				Z=PocisaoXYZ.z;
			}
			
			if(data.startsWith("Profundidad:")==true){
				ProfundidadeFuro=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"P");
			}
			
			if(data.startsWith("Diametro Inicial:")==true){//diametro del furo en la cara Xy
				diametroInicialFuro=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"D");
			}
			
			
			if(data.startsWith("Diametro Final:")==true){//diametro del furo en la cara Xy
				diametroFinalFuro=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"D");
				//ExisteDiaFin=1;
			}
			
			
			if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
				System.out.println("("+data+")");
			}
			
			

			if(data.indexOf("Hole Buttom Condition")!=-1){
				StringTokenizer st4 = new StringTokenizer(data, ":,=;",true);//buscador de tokens con separadores activados
				ctokens=0;
				do{//asignacion de direcciones
					ctokens++;//contador de tokens para encontrar el parentesis
					String valores = st4.nextToken();//leo el proximo token
					//System.out.println(ctokens + " "+valores);
					if(ctokens==3){//copia el primer token con la primera ferramenta
						HoleBoCond=valores;
						//System.out.println("Imprimiendo             "+valores);
					}
					if(ctokens==7){//copia el primer token con la primera ferramenta
						RadioBase=Double.parseDouble(valores);
						//System.out.println("Imprimiendo             "+valores);
					}
				}while(st4.hasMoreTokens());
		
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Fin feature)");
				}
				salif=0;
			}
			cDatList++;//sumno la siguiente linea del codigo de la feature
		}while(cDatList<TamList & salif==1);
		
		
		
		//Activar las herramientas dependiendo das operaciones de la feature (Hole Bottom Condition)
		
		if(HoleBoCond.startsWith("FLAT_HOLE_BOTTOM")==true){
			Cd=1;
			Td=1;
			FaM=1;
			BaEM=0;
			BuEM=0;
		}
		
		if(HoleBoCond.startsWith("CONICAL_HOLE_BOTTOM")==true){
			Cd=1;
			Td=1;
			FaM=0;
			BaEM=0;
			BuEM=0;
		}
		
		if(HoleBoCond.startsWith("SPHERICAL_HOLE_BOTTOM")==true){
			Cd=1;
			Td=1;
			FaM=1;
			BaEM=1;
			BuEM=0;
			//para colocar la compensacion de tool al inicio del cod g
			//Fcomp.Inicio=0;
		}
		
		if(HoleBoCond.startsWith("FLAT_WITH_RADIUS_HOLE_BOTTOM")==true){
			Cd=1;
			Td=1;
			FaM=1;
			BaEM=0;
			BuEM=1;
			//para colocar la compensacion de tool al inicio del cod g
			//Fcomp.Inicio=1;
		}
		
		if(HoleBoCond.startsWith("THROUGH_BOTTOM_CONDITION")==true){
			//FuroPasante=true;
			ProfundidadeFuro = Double.parseDouble(DadosDaPeca.ComprimentoZ_DoBloco);
			Cd=1;
			Td=1;
			FaM=1;
			BaEM=0;
			BuEM=0;
			//para colocar la compensacion de tool al inicio del cod g
			//Fcomp.Inicio=1;
		}

		/**************************************** Generar o Cod G *********************************************************/
		/**************************************** Generar o Cod G *********************************************************/
		/**************************************** Generar o Cod G *********************************************************/
		cDatList=1;
		int salir=0;
		salif=1;
		String data="";
		
		do{	
			data=ExtrairDatos.InformacoesAvancadas.get(cDatList);

			if(data.indexOf("Feature")!=-1){
				salir++;
				//salif = 2; 
			}

			/*************************************************************************************************************************************************************************************************************/
			/*************************************************************************************************************************************************************************************************************/
			/*************************************************************************************************************************************************************************************************************/
			///hace un furo simple
			if(data.indexOf("CENTER_DRILL")!=-1 & Cd==1){
			
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Dados da Nova Ferramenta: "+data+")");
				}

				Gcode_01RH.GCode_Furos(CemtroX, CentroY, ProfundidadeFuro, diametroInicialFuro, diametroFinalFuro, diametroFinalFuro, data,1);
				Cd=2;
			}
			
			
			/*************************************************************************************************************************************************************************************************************/
			/*************************************************************************************************************************************************************************************************************/
			/*************************************************************************************************************************************************************************************************************/
			//furo simples 
			if(data.indexOf("TWIST_DRILL")!=-1 & Td==1){
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Dados da Nova Ferramenta: "+data+")");
				}
				
				double c=0,a=0,b=0;//para calcular la profundidad de la herramienta y sumarsela a la prof total
				if(BaEM==1 ){
					a=TrocaDeFerramenta.DatoDiametroTool(data)/2;
					c=a/(Math.cos(TrocaDeFerramenta.DatoAngBAseTool(data)));
					b=Math.sqrt(Math.abs((c*c) - (a*a)));
					b = (Math.round(b*10000)/10000.0);
				}
				
				if(Cd==1 & Td==1 & FaM==0 & BaEM==0 & BuEM==0){
					a=TrocaDeFerramenta.DatoDiametroTool(data)/2;
					c=a/(Math.cos(TrocaDeFerramenta.DatoAngBAseTool(data)));
					b=Math.sqrt(Math.abs((c*c) - (a*a)));
					b = (Math.round(b*10000)/10000.0);
				}
				
				Gcode_01RH.GCode_Furos(CemtroX, CentroY, ProfundidadeFuro+b, diametroInicialFuro, diametroFinalFuro, diametroFinalFuro, data,1);//el 0 sig Offset
				
				Td=2;
				if(Cd==2 & Td==2 & FaM==0 & BaEM==0 & BuEM==0){//salir de Round Hole
					salir=2;
					}
			}//fin twisdrill
			
			
			/*************************************************************************************************************************************************************************************************************/
			/*************************************************************************************************************************************************************************************************************/
			/*************************************************************************************************************************************************************************************************************/
			//furo complexo
			if(data.indexOf("FACEMILL")!=-1 & FaM==1){
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Dados da Nova Ferramenta: "+data+")");
				}
				
				if(diametroFinalFuro==0){//para saber si el furo es conico (ojo NOOO es base conica) 0 no es --- 1 si es
					
					if(TrocaDeFerramenta.DatoDiametroTool(data)==diametroInicialFuro){//herramienta con diametro igual al furo 
						Gcode_01RH.GCode_Furos(CemtroX, CentroY, ProfundidadeFuro, diametroInicialFuro, diametroFinalFuro, diametroFinalFuro, data,1);//furo simples
					}else{//herramienta con diametro menor al furo
						Gcode_01RH.GCode_Furos(CemtroX, CentroY, ProfundidadeFuro, diametroInicialFuro, diametroFinalFuro, diametroFinalFuro, data,2);//furo espiral recto
					}
				   
				}else{//para crear el furo conico----------------------------
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					double diametroTool=ExtrairDiametroTool.ExtrairDiametroFerrameta(data);
					CalculoDosPontosDaReta.CalculosDoPuntosLinha_F2(diametroInicialFuro, diametroFinalFuro, ProfundidadeFuro);

					double DiaMePasada = CalculoDosPontosDaReta.DiaSeg.get(0);
					double ProfCPasada = CalculoDosPontosDaReta.ProfSeg.get(1);
					double RadioMenos = CalculoDosPontosDaReta.DiaSeg.get(1) - CalculoDosPontosDaReta.DiaSeg.get(2);
					//double RadioMenos = CalPonCir.DiametroSegmentado.get(i+1) - CalPonCir.DiametroSegmentado.get(i+2);
					
					Gcode_01RH.imprimirBaseUnaPasada=true;
					Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, Math.abs(ProfCPasada/2), DiaMePasada, 0,data,true,RadioMenos, false);
					Gcode_01RH.imprimirBaseUnaPasada=false;
					if(diametroTool==diametroFinalFuro){
						for (int i = 0; i < CalculoDosPontosDaReta.DiaSeg.size()-1; i++) {
							DiaMePasada = CalculoDosPontosDaReta.DiaSeg.get(i+1);
							ProfCPasada = CalculoDosPontosDaReta.ProfSeg.get(i+1);
							if(DiaMePasada>diametroTool){
								RadioMenos = CalculoDosPontosDaReta.DiaSeg.get(i+1) - CalculoDosPontosDaReta.DiaSeg.get(i+2);
								Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, Math.abs(ProfCPasada), DiaMePasada, 0,data,false,RadioMenos, false);
							}else{
								Gcode_01RH.FuroSimplePassoFinal(CemtroX, CentroY, Math.abs(CalculoDosPontosDaReta.ProfSeg.get(CalculoDosPontosDaReta.DiaSeg.size()-2)), ProfundidadeFuro, Gcode.MaxVelcorteTool);	
							}
						}
					}else{
						////*****
						for (int i = 0; i < CalculoDosPontosDaReta.DiaSeg.size()-1; i++) {
							DiaMePasada = CalculoDosPontosDaReta.DiaSeg.get(i+1);
							ProfCPasada = CalculoDosPontosDaReta.ProfSeg.get(i+1);
							if((CalculoDosPontosDaReta.DiaSeg.size()-1)>(i+2)){
								RadioMenos = CalculoDosPontosDaReta.DiaSeg.get(i+1) - CalculoDosPontosDaReta.DiaSeg.get(i+2);
								}
							Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, Math.abs(ProfCPasada), DiaMePasada, 0,data,false,RadioMenos, false);
						}
						
						
						
					}

			}
				
				//LineNo = Fcomp.LineNo;
				FaM=2;
				if(Cd==2 & Td==2 & FaM==2 & BaEM==0 & BuEM==0){
					salir=2;
					}
			}

			
			
			/*************************************************************************************************************************************************************************************************************/
			if(data.indexOf("BALL_ENDMILL")!=-1 & BaEM==1){
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Dados da Nova Ferramenta: "+data+")");
				}
				
				if(TrocaDeFerramenta.DatoDiametroTool(data)==diametroInicialFuro){//herramienta con diametro igual al furo 
					Gcode_01RH.GCode_Furos(CemtroX, CentroY, ProfundidadeFuro +(diametroInicialFuro/2) , diametroInicialFuro, diametroFinalFuro, diametroFinalFuro, data,1);//furo simples
				}else{//herramienta con diametro menor al furo
					double diametroTool=ExtrairDiametroTool.ExtrairDiametroFerrameta(data);
					CalPonCir.CalcularPuntosdaSemiCircunferencia(diametroInicialFuro, diametroInicialFuro);
					double RadioMenos = CalPonCir.DiamSeg.get(0) - CalPonCir.DiamSeg.get(1);
					Gcode_01RH.imprimirBaseUnaPasada=true;
					Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, CalPonCir.ProfSegm.get(0), diametroInicialFuro, ProfundidadeFuro,data,true,RadioMenos, true);
					for (int i = 0; i < CalPonCir.ProfSegm.size()-1; i++) {
						double DiametroPaso = CalPonCir.DiamSeg.get(i+1);
						double ProfCortePaso = CalPonCir.ProfSegm.get(i+1);
						//arreglo para que no se calcule mas sino hasta el dimetro de la herramienta
						//System.out.println("(*****************************************************************************************"+DiametroPaso+")");
						if(DiametroPaso>diametroTool & ProfCortePaso<=((diametroInicialFuro/2)-(diametroTool/2))){
							Gcode_01RH.imprimirBaseUnaPasada=false;
							RadioMenos = CalPonCir.DiamSeg.get(i+1) - CalPonCir.DiamSeg.get(i+2);
							Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, ProfCortePaso, DiametroPaso, ProfundidadeFuro,data,false,RadioMenos, true);
						}else{
							Gcode_01RH.imprimirBaseUnaPasada=false;
							double PrxoValDiame = (DiametroPaso * ((diametroInicialFuro/2)-(diametroTool/2))) / ProfCortePaso;
							Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, ((diametroInicialFuro/2)-(diametroTool/2)), PrxoValDiame, ProfundidadeFuro,data,false,RadioMenos, true);
							i = CalPonCir.ProfSegm.size();
						}
					}
				}//fil else primario
				BaEM=2;
				if(Cd==2 & Td==2 & FaM==2 & BaEM==2 & BuEM==0){
					salir=2;
					}
			}//fin de ball end mill
			
			
			
			
			/*************************************************************************************************************************************************************************************************************/
			if(data.indexOf("BULLNOSE_ENDMILL")!=-1 & BuEM==1){
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Dados da Nova Ferramenta: "+data+")");
				}
				
				if(TrocaDeFerramenta.DatoDiametroTool(data)==diametroInicialFuro & TrocaDeFerramenta.DatoAngBAseTool(data)==RadioBase){//herramienta con diametro igual al furo 
					Gcode_01RH.GCode_Furos(CemtroX, CentroY, ProfundidadeFuro + RadioBase, diametroInicialFuro, diametroFinalFuro, diametroFinalFuro, data,1);//furo simples
				}else{
					//Hay dos opciones
					//1) herramienta con diametro menor al furo pero con el angulo base igual
					//1) herramienta con diametro menor al furo pero con el angulo base diferente
					if(TrocaDeFerramenta.DatoDiametroTool(data)<diametroInicialFuro & TrocaDeFerramenta.DatoAngBAseTool(data)==RadioBase){//herramienta con diametro igual al furo 
						Gcode_01RH.GCode_Furos(CemtroX, CentroY, ProfundidadeFuro + RadioBase, diametroInicialFuro, diametroFinalFuro, diametroFinalFuro, data,2);//furo simples
					}
					
					if(TrocaDeFerramenta.DatoDiametroTool(data)<diametroInicialFuro & TrocaDeFerramenta.DatoAngBAseTool(data)<RadioBase){//herramienta con diametro igual al furo
						double diametroTool=ExtrairDiametroTool.ExtrairDiametroFerrameta(data);
						CalPonCir.CalcularPuntosdaSemiCircunferencia(RadioBase, RadioBase*2);
						double RadioMenos = CalPonCir.DiamSeg.get(0) - CalPonCir.DiamSeg.get(1);
						Gcode_01RH.imprimirBaseUnaPasada=true;
						double AngBas = TrocaDeFerramenta.DatoAngBAseTool(data);
						Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, AngBas, diametroInicialFuro, ProfundidadeFuro,data,true,RadioMenos,false);
						double valprofant=0;
						for (int i = 0; i < CalPonCir.ProfSegm.size()-1; i++) {
							double DiametroPaso = CalPonCir.DiamSeg.get(i*2) + (diametroInicialFuro- RadioBase);
							double ProfCortePaso = CalPonCir.ProfSegm.get(i+1);
							//arreglo para que no se calcule mas sino hasta el dimetro de la herramienta
							//System.out.println("(*****************************************************************************************"+DiametroPaso+")");
							
							if(DiametroPaso>diametroTool & ProfCortePaso<=(RadioBase-AngBas)){
								Gcode_01RH.imprimirBaseUnaPasada=false;
								RadioMenos = CalPonCir.DiamSeg.get(i+1) - CalPonCir.DiamSeg.get(i+2);
								Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, ProfCortePaso, DiametroPaso, ProfundidadeFuro,data,false,RadioMenos,false);
								valprofant = ProfCortePaso + ProfundidadeFuro + AngBas;
							}else{
								if((CalPonCir.ProfSegm.get(i)+ AngBas + ProfundidadeFuro)<valprofant ){
									Gcode_01RH.imprimirBaseUnaPasada=false;
									double PrxoValDiame = (DiametroPaso * ((diametroInicialFuro/2)-(diametroTool/2))) / ProfCortePaso;
									Gcode_01RH.GCode_FuroSegmentadoUnaPasada(CemtroX, CentroY, ((diametroInicialFuro/2)-(diametroTool/2)), PrxoValDiame, ProfundidadeFuro,data,false,RadioMenos,false);
									i = CalPonCir.ProfSegm.size();
								}else{
									i = CalPonCir.ProfSegm.size()-1;//salio de todo
									}				
								}
							}
						}
					}//fin else
				BuEM=2;
				if(Cd==2 & Td==2 & FaM==2 & BaEM==0 & BuEM==2){
					salir=2;
					}
				}//fin BullEM
			
			
			

			
			if(salir==2){
				Cd=0;
				Td=0;
				FaM=0;
				BaEM=0;
				BuEM=0;
			}
			
			cDatList++;
			//GeneradorCodidoG.LineNo=LineNo;
		}while(cDatList<TamList & salif==1);
		
		
	}
	
	
	

}
