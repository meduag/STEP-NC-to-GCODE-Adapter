package br.UNB.LAB.GerarCodG;

import java.util.StringTokenizer;

import javax.vecmath.Point3d;

import br.UNB.LAB.Entidades_E_Atributos.Axis2Placement3D;
import br.UNB.LAB.FAcessorias.ExtrairToken3_Profundidad_DiaInicial_DiaFinal;
import br.UNB.LAB.FAcessorias.ExtrairUbicacao;
import br.UNB.LAB.FuncoesDePosicao.*;
import br.UNB.LAB.InfBasicas.PlanoSeguranca;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;



public class CodeG_Para__RoundHole_E_Operaciones {
	
	int ctokens=0;//contador de tokens
	int salif=1;
	int paso_sig_lineNo = 0;
	int ExisteDiaFin=0;
	double diaIniTem=0;
	double diaFinTem=0;
	double ProfTem=0;
	double RadioBase=0;
	//double X=0,Y=0,Z=0;
	String HoleBoCond="";
	
	//activacion de herramientas
	int Cd=1,	Td=1,	FaM=1,	BaEM=0,	BuEM=0;
	
	//valores para generar el codigo G
	double X=0,Y=0,Z=0;
	public static double OffsetProfZ=0;
	
	public static int ValorPasoAngulo=10;
	public static double AngOffsetTwD=0;
	public static int LineNo=0;
	//public static double DiametroTempTwisDrill_Facemill=0;
	
	//boleanos
	public static boolean pasante=false;
	
	

	@SuppressWarnings("static-access")
	public void GenCodG_RoundHoleOper(){
		
		
		MapearMachining_workingstep ExtrairDatos = new MapearMachining_workingstep();
		FuroSimples Fsimp = new FuroSimples();
		FuroComplexo Fcomp = new FuroComplexo();
		PlanoSeguranca SecPlan = new PlanoSeguranca();
		//Axis2Placement3D OffCD = new Axis2Placement3D();
		FuroSegmentado Fseg = new FuroSegmentado();
		CalculoDosPontosDaReta CalPonRect = new CalculoDosPontosDaReta();
		CalculoDosPuntosDoCirculo CalPonCir = new CalculoDosPuntosDoCirculo();
		
		
		LineNo=GeneradorCodidoG.LineNo;
		String data2=SecPlan.ComprimentoPlanoSeg;
		
		StringTokenizer stp = new StringTokenizer(data2, "=;",true);//buscador de tokens con separadores activados
		ctokens=0;
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = stp.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);
			double menosPS=0;
			if(ctokens==3){//copia el primer token con la primera ferramenta
				//System.out.println("Imprimiendo             "+valores);
				//Fsimp.SecPlan=Double.parseDouble(valores)-menosPS;
				Fcomp.SecPlan=Double.parseDouble(valores)-menosPS;
				Fseg.SecPlan=Double.parseDouble(valores)-menosPS;
			}
		}while(stp.hasMoreTokens());
		
		

		int TamList=ExtrairDatos.InformacoesAvancadas.size();
		int cDatList=0;
		
		do{
			String data=ExtrairDatos.InformacoesAvancadas.get(cDatList);
	
			Point3d PocisaoXYZ = null;//utilizo esta variable para adquirir los valores extraidos 
			if(data.startsWith("Ubicacion:")==true){
				PocisaoXYZ = ExtrairUbicacao.ExtPocisaoXYZ(data);
				//valores X
				Fsimp.CenterX=PocisaoXYZ.x;	
				Fcomp.CentroX=PocisaoXYZ.x;
				Fseg.CentroX=PocisaoXYZ.x;
				X=PocisaoXYZ.x;
				//valores Y
				Fsimp.CenterY=PocisaoXYZ.y;	
				Fcomp.CentroY=PocisaoXYZ.y;
				Fseg.CentroY=PocisaoXYZ.y;
				Y=PocisaoXYZ.y;
				//valores Z
				OffsetProfZ=PocisaoXYZ.z;
				Z=PocisaoXYZ.z;
			}
			
			if(data.startsWith("Profundidad:")==true){
				Fsimp.BoreDepth=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"P");
				Fcomp.ProfTotalFuro=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"P");
				ProfTem=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"P");
			}
			
			if(data.startsWith("Diametro Inicial:")==true){//diametro del furo en la cara Xy
				Fcomp.DiaFuro=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"D");
				Fseg.DiaFuro=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"D");
				diaIniTem=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"D");
			}
			
			
			if(data.startsWith("Diametro Final:")==true){//diametro del furo en la cara Xy
				Fseg.DiaFuro=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"D");
				diaFinTem=ExtrairToken3_Profundidad_DiaInicial_DiaFinal.ExtrairToken3(data,"D");
				ExisteDiaFin=1;
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
				//para NO colocar la compensacion de tool al inicio del cod g
				
				Fcomp.Inicio=1;
				
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
			Fcomp.Inicio=0;
		}
		
		if(HoleBoCond.startsWith("FLAT_WITH_RADIUS_HOLE_BOTTOM")==true){
			Cd=1;
			Td=1;
			FaM=1;
			BaEM=0;
			BuEM=1;
			//para colocar la compensacion de tool al inicio del cod g
			Fcomp.Inicio=1;
		}
		
		if(HoleBoCond.startsWith("THROUGH_BOTTOM_CONDITION")==true){
			pasante=true;
			Cd=1;
			Td=1;
			FaM=1;
			BaEM=0;
			BuEM=0;
			//para colocar la compensacion de tool al inicio del cod g
			Fcomp.Inicio=1;
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
			}

			/*************************************************************************************************************************************************************************************************************/
			///hace un furo simple
			if(data.indexOf("CENTER_DRILL")!=-1 & Cd==1){
				//br.UNB.LAB.FAcessorias.CalProfundidadeDrill.CalculoProfundidadeCorte(DiametroTool, ProfundidadeFuro)
			
				if(pasante==false){//el furo no es pasante 
					//solo tiene en cuenta la profundidad inicial
					if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
						System.out.println("("+data+")");
					}
					Fsimp.LineNo=GeneradorCodidoG.LineNo;
					Fsimp.Ferramenta=data;
					Fsimp.OffsetProfZ=OffsetProfZ;
					Fsimp.BoreDepth=ProfTem;
					///generar codigo G paa furacion simple con Twisdrill
					Fsimp.FSimplesNc();
					LineNo = Fsimp.LineNo;
					Cd=2;
					
				}else{//furo pasante
					//tiene en cuenta la profundidad final
					if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
						System.out.println("("+data+")");
					}
					//StringTokenizer st = new StringTokenizer(data, ":",true);//buscador de tokens con separadores activados
					Fsimp.LineNo=GeneradorCodidoG.LineNo;
					Fsimp.Ferramenta=data;
					Fsimp.OffsetProfZ=OffsetProfZ;
					Fsimp.BoreDepth=(ProfTem-OffsetProfZ);
					///generar codigo G paa furacion simple con Twisdrill
					Fsimp.FSimplesNc();
					//LineNo = Fsimp.LineNo;
					Cd=2;
				}
				
			}
			
			
			/*************************************************************************************************************************************************************************************************************/
			//furo simples 
			if(data.indexOf("TWIST_DRILL")!=-1 & Td==1){
				if(pasante==false){
					if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
						System.out.println("(Dados da Nova Ferramenta: "+data+")");
					}
					StringTokenizer st = new StringTokenizer(data, ":,=;",true);//buscador de tokens con separadores activados
					Fsimp.Ferramenta=data;
					Fsimp.OffsetProfZ=OffsetProfZ;
					//Fsimp.LineNo = LineNo;
					Fsimp.BoreDepth=ProfTem;
					ctokens=2;
					
					do{
						String valores = st.nextToken();//leo el proximo token
						//System.out.println(ctokens + " "+valores);
						if(ctokens==16){//copia el primer token con la primera ferramenta
							Fsimp.DiaTool=Double.parseDouble(valores);
							//DiametroTempTwisDrill_Facemill=Double.parseDouble(valores);
						}
						
						if(ctokens==28){//copia el primer token con la primera ferramenta
							AngOffsetTwD=Double.parseDouble(valores);
						}
						
						ctokens++;
					}while(st.hasMoreTokens());
					
					//calculo del angulo ofset del twisdrill cuando se fura una feature con SPHERICAL_HOLE_BOTTOM furo con base conica
					if(BaEM==1){
						
						//double cal=Math.toDegrees(AngOffsetTwD);
						//System.out.println("Valor del Ang: " + cal);
						double c,a,b;
						a=Fsimp.DiaTool/2;
						c=a/(Math.cos(AngOffsetTwD));
						//System.out.println("Valor de la Hipotenusa: " + c);
						b=Math.sqrt(Math.abs((c*c) - (a*a)));
						b = (Math.round(b*10000)/10000.0);
						//System.out.println("Valor de la Prof: " + b);
						
						Fsimp.BoreDepth=Fsimp.BoreDepth+b;
					}
					
					if(Cd==1 & Td==1 & FaM==0 & BaEM==0 & BuEM==0){
						double c,a,b;
						a=Fsimp.DiaTool/2;
						c=a/(Math.cos(AngOffsetTwD));
						//System.out.println("Valor de la Hipotenusa: " + c);
						b=Math.sqrt(Math.abs((c*c) - (a*a)));
						b = (Math.round(b*10000)/10000.0);
						//System.out.println("Valor de la Prof: " + b);
						
						Fsimp.BoreDepth=Fsimp.BoreDepth+b;
					}
					
		
					///generar codigo G paa furacion simple con Twisdrill			
					Fsimp.FSimplesNc();
					//LineNo = Fsimp.LineNo;
					Td=2;
					if(Cd==2 & Td==2 & FaM==0 & BaEM==0 & BuEM==0){
						salir=2;
						}
					
				}else{
					///////////////////////////////////////////////////////////////////////////////////////////////para FURO PASANTE
					if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
						System.out.println("(Dados da Nova Ferramenta: "+data+")");
					}
					StringTokenizer st = new StringTokenizer(data, ":,=;",true);//buscador de tokens con separadores activados
					Fsimp.Ferramenta=data;
					Fsimp.OffsetProfZ=OffsetProfZ;
					//Fsimp.LineNo = LineNo;
					Fsimp.BoreDepth=(ProfTem-OffsetProfZ);
					ctokens=2;
					
					do{
						String valores = st.nextToken();//leo el proximo token
						//System.out.println(ctokens + " "+valores);
						if(ctokens==16){//copia el primer token con la primera ferramenta
							Fsimp.DiaTool=Double.parseDouble(valores);
						}
						
						if(ctokens==28){//copia el primer token con la primera ferramenta
							AngOffsetTwD=Double.parseDouble(valores);
						}
						
						ctokens++;
					}while(st.hasMoreTokens());
					
					//calculo del angulo ofset del twisdrill cuando se fura una feature con SPHERICAL_HOLE_BOTTOM furo con base conica
					if(BaEM==1){
						
						//double cal=Math.toDegrees(AngOffsetTwD);
						//System.out.println("Valor del Ang: " + cal);
						double c,a,b;
						a=Fsimp.DiaTool/2;
						c=a/(Math.cos(AngOffsetTwD));
						//System.out.println("Valor de la Hipotenusa: " + c);
						b=Math.sqrt(Math.abs((c*c) - (a*a)));
						b = (Math.round(b*10000)/10000.0);
						//System.out.println("Valor de la Prof: " + b);
						
						Fsimp.BoreDepth=Fsimp.BoreDepth+b;
						
					}
					
					//cuando es solo usando las dos ferramentas
					if(Cd==1 & Td==1 & FaM==0 & BaEM==0 & BuEM==0){
						double c,a,b;
						a=Fsimp.DiaTool/2;
						c=a/(Math.cos(AngOffsetTwD));
						//System.out.println("Valor de la Hipotenusa: " + c);
						b=Math.sqrt(Math.abs((c*c) - (a*a)));
						b = (Math.round(b*10000)/10000.0);
						//System.out.println("Valor de la Prof: " + b);
						
						Fsimp.BoreDepth=Fsimp.BoreDepth+b;
					}
					
					///generar codigo G paa furacion simple con Twisdrill			
					Fsimp.FSimplesNc();
					//LineNo = Fsimp.LineNo;
					Td=2;
					if(Cd==2 & Td==2 & FaM==0 & BaEM==0 & BuEM==0){
						salir=2;
						}
					
				}
				//LineNo = Fsimp.LineNo;
			}//fin twisdrill
			
			
			
			
			
			
			/*************************************************************************************************************************************************************************************************************/
			//furo complexo
			if(data.indexOf("FACEMILL")!=-1 & FaM==1){
				
				if(ExisteDiaFin==0){//para saber si el furo es conico (ojo NOOO es base conica) 0 no es --- 1 si es
					if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
						System.out.println("(Dados da Nova Ferramenta: "+data+")");
					}
					
					Fcomp.DiaTool = TrocaDeFerramenta.DatoDiametroTool(data);
					
					if(Fcomp.DiaTool==Fcomp.DiaFuro){//herramienta con diametro igual al furo 
						//StringTokenizer st3 = new StringTokenizer(data, ":",true);//buscador de tokens con separadores activados
						Fsimp.Ferramenta=data;
						Fsimp.FSimplesNc();
						//LineNo = Fsimp.LineNo;
					}else{//herramienta con diametro menor al furo
						//System.out.println("Tool "+Fcomp.DiaTool+"   Furo"+Fcomp.DiaFuro);
						Fcomp.ImpGcodeFuroComplexo_F2(X, Y, diaIniTem, diaFinTem, ProfTem, data);//creo el codigo G
						//LineNo = Fcomp.LineNo;
					}
				   
				}else{//para crear el furo conico----------------------------
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
						System.out.println("("+data+")");
					}
					Fseg.Ferramenta=data;
					StringTokenizer st = new StringTokenizer(data, ":,=;",true);//buscador de tokens con separadores activados
					ctokens=0;
					do{//asignacion de direcciones
						ctokens++;//contador de tokens para encontrar el parentesis
						String valores = st.nextToken();//leo el proximo token
						//System.out.println(ctokens + " "+valores);

						if(ctokens==15){//copia el primer token con la primera ferramenta
							Fseg.DiaTool=Double.parseDouble(valores);
							//CalPonRect.diaTool=Double.parseDouble(valores);
							CalPonRect.DiaInicial=diaIniTem;
							CalPonRect.DiaFinal=diaFinTem;
							CalPonRect.profFuro=ProfTem;
							
							//valor del paso de avance 0.5 por defecto (val opcionales 0.7 - 0.8 - 0.9 - 1)
							CalPonRect.Avance=0.5;
							CalPonRect.CalPuntosLin();
						}
					}while(st.hasMoreTokens());
					
					if(Fcomp.DiaTool==Fcomp.DiaFuro){
						//StringTokenizer st3 = new StringTokenizer(data, ":",true);//buscador de tokens con separadores activados
						Fsimp.Ferramenta=data;
						//Fcomp.LineNo = LineNo;
						Fsimp.FSimplesNc();
						//LineNo = Fsimp.LineNo;
					}else{

						//Fseg.LineNo = LineNo;
						
						for (int i = 0; i < CalPonRect.DiaSeg.size(); i++) {//pasando el valor del diametro
							if((CalPonRect.DiaSeg.get(i)*2)>=Fseg.DiaTool){
								Fseg.DiaFuro = CalPonRect.DiaSeg.get(i)*2;
								//System.out.println("(Imprimiendooo           "+Fseg.DiaFuro+ "         Diametro Fer:       " + Fseg.DiaTool+")");
								Fseg.ProfPorPaso = CalPonRect.ProfSeg.get(i+1)+ProfTem;
								Fseg.FSegmentadoNc();
								//LineNo = Fseg.LineNo;
							}
						}
					}
					
					Fsimp.Ferramenta=Fseg.Ferramenta;
					Fsimp.BoreDepth =  ProfTem;//profundidad final
					Fsimp.ImpFinal=0;
					//Fsimp.OffSecPla=Fsimp.SecPlan;
					Fsimp.FeedRate=200;
					//Fsimp.LineNo = LineNo;
					Fsimp.FSimplesNc();
					//vuelve a la configuracion actual
					//Fsimp.OffSecPla=0;
					Fsimp.ImpFinal=1;
					//LineNo = Fsimp.LineNo;

				}
				
				//LineNo = Fcomp.LineNo;
				FaM=2;
				if(Cd==2 & Td==2 & FaM==2 & BaEM==0 & BuEM==0){
					salir=2;
					}
			}

			
			
			/*************************************************************************************************************************************************************************************************************/
			if(data.indexOf("BALL_ENDMILL")!=-1 & BaEM==1){
				double ProfundidadeAnterior=0;
				
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Dados da Nova Ferramenta: "+data+")");
				}
				//double DiamTool =  br.UNB.LAB.FAcessorias.ExtrairDiametroTool(data);
				double DiaToolBallEM = br.UNB.LAB.FAcessorias.ExtrairDiametroTool.ExtrairDiametroFerrameta(data);
				
				CalPonCir.CalcularPuntosdaSemiCircunferencia(diaIniTem, DiaToolBallEM);
				
				if(DiaToolBallEM<diaIniTem){//ferramenta menor que el hueco
					/*Imprimi el primer circulo*/
					Fseg.ImpGcodeFuroSegmentado_F2(X, Y, diaIniTem, ProfTem , data, "Sim_Imprimir_cabecera",ProfTem);
					//la profundida sera modificada despues (en la funcion de furo seg) para colocar el radio del hueco como prof adicional
					for (int i = 0; i < CalPonCir.ProfundidadeSegmentada.size()-1; i++) {
						if(CalPonCir.DiametroSegmentado.get(i+1)<=DiaToolBallEM){
							i=CalPonCir.ProfundidadeSegmentada.size()-1;
						}else{
							Fseg.ImpGcodeFuroSegmentado_F2(X, Y, CalPonCir.DiametroSegmentado.get(i+1), ProfTem , data, "Nao_Imprimir_cabecera",ProfTem+CalPonCir.ProfundidadeSegmentada.get(i+1));
							ProfundidadeAnterior=ProfTem+CalPonCir.ProfundidadeSegmentada.get(i+1);
						}
					}
					/* paso final */
					FuroSimples.FSimplePassoFinal(X, Y, ProfundidadeAnterior, ProfTem+(diaIniTem/2));
				}else{//ferramenta igual que el hueco
					Fsimp.Ferramenta=data;
					Fsimp.BoreDepth =  ProfTem+(diaIniTem/2);//profundidad final
					Fsimp.FSimplesNc();
				}
			}//fin de ball end mill
			
			
			
			
			/*************************************************************************************************************************************************************************************************************/
			if(data.indexOf("BULLNOSE_ENDMILL")!=-1 & BuEM==1){
				if(br.UNB.LAB.Integrador.Integrador.ImpLedFer==1){
					System.out.println("(Dados da Nova Ferramenta: "+data+")");
				}
				
				if(FaM==2){
					Fseg.inicio=1;
					Fseg.ini1=1;
					Fseg.ini2=1;
					Fseg.ini3=1;
				}
				
				Fseg.Ferramenta=data;
				Fsimp.Ferramenta=data;
				StringTokenizer st = new StringTokenizer(data, ":,=;",true);//buscador de tokens con separadores activados
				ctokens=0;
				do{//asignacion de direcciones
					ctokens++;//contador de tokens para encontrar el parentesis
					String valores = st.nextToken();//leo el proximo token
					//System.out.println(ctokens + " "+valores);

					
					if(ctokens==15){//diametro Herramienta
						Fseg.DiaTool=Double.parseDouble(valores);
					}
					
					if(ctokens==23){//diametro Herramienta
						//// Valores pára calcular el diametro y La profundidad
						CalPonCir.graAvan=ValorPasoAngulo;//avance por grados
						CalPonCir.diaFuro = RadioBase*2;//diametro del furo
						//System.out.println("Diametro Base" + CalPonCir.diaFuro );
						CalPonCir.diaTool=Double.parseDouble(valores)*2;
						//System.out.println("Diametro Base Herramienta" + CalPonCir.diaTool );
						CalPonCir.CalPuntosCir();
						}
				}while(st.hasMoreTokens());
				
				if(Fseg.DiaTool==Fseg.DiaFuro & CalPonCir.diaTool==CalPonCir.diaFuro){
					//StringTokenizer st3 = new StringTokenizer(data, ":",true);//buscador de tokens con separadores activados
					Fsimp.Ferramenta=data;
					//Fsimp.LineNo = LineNo;
					//*-*-*
					Fcomp.ImpGcodeFuroComplexo_F2(X, Y, diaIniTem, diaFinTem, ProfTem, data);//creo el codigo G
					//Fsimp.FSimplesNc();
					//LineNo = Fsimp.LineNo;
				}else{
					
					if(FaM==2){
						Fseg.inicio=1;
						Fseg.ini1=1;
						Fseg.ini2=1;
						Fseg.ini3=1;
					}
					
					//Fseg.LineNo = LineNo;
					//Fseg.ini3=1;
					
					for (int i = 0; i < CalPonCir.DiaSeg.length; i++) {//pasando el valor del diametro
						double tem=CalPonCir.DiaSeg[i]+(diaIniTem-RadioBase*2);
						
						//System.out.println("Valor de la base"+tem);
						if(tem>=Fseg.DiaTool & i<((90/ValorPasoAngulo)+1)){
								Fseg.DiaFuro = tem;
								//Fseg.DiaTool=Fseg.DiaTool-CalPonCir.diaTool;
								Fseg.XRadBase=CalPonCir.diaTool /2;
								//System.out.println("(Imprimiendooo           "+Fseg.DiaFuro+ "         Diametro Fer:       " + Fseg.DiaTool+")");
								Fseg.ProfPorPaso = CalPonCir.ProfSeg[i]+ProfTem;
								Fseg.FSegmentadoNc();
								//LineNo=Fseg.LineNo;
							}else{
								i =CalPonCir.DiaSeg.length;
							}
					}

				}
				
				//utilizo la funcion de ret poiket para imprimir el plano de seguranza
				RectPocketSimples.FeedRate=GeneradorCodidoG.FeedRate;
				RectPocketSimples.PlanSeg=Fseg.SecPlan;
				//RectPocketSimples.LineNo=Fseg.LineNo;
				RectPocketSimples.ImprimirPlaSeg();
				LineNo=RectPocketSimples.LineNo;
				
				BuEM=2;
				
				if(Cd==2 & Td==2 & FaM==2 & BaEM==0 & BuEM==2){
					salir=2;
					}
			}
			
			
			

			
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
