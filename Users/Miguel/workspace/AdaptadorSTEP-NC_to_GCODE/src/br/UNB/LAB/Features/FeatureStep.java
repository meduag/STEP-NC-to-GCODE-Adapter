package br.UNB.LAB.Features;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.*;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.InfBasicas.DadosDaPeca;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;
import br.UNB.LAB.Integrador.MapearOperacoesDaFeature;


public class FeatureStep {

	//cadenas
	public String data;//recepcion de datos
	public String nVal="";//para asignacion de nuevos valores en la direccion
	public String Enderecos[] = new String[20];//array de pociciojnes para este Slot
	public String DatosSlot[] = new String[50];//array de pociciojnes para este Slot
	static int TempEnd=0;//endereco temporal para llamar las entidades y sacar los atributos
	static String ComprimentoZ_DoBloco;
	
	//enteros
	public int ctokens=0;//contador de tokens
	public int sAsig=1;//salir de asignacion de nexttokens
	public int actAsig=1;//Activar asignacion de direcciones  0=no   1=si
	public int sigDir=0;//siguiente direcion
	public int cEnde=0;//contador de endereços
	public int asigEnde=0;//Asignacion de endereços
	public int a2p=1;//axis2placement3d para cartesianpoint
	public int a2p3D=1;//axis2placement3d para tolerance length measure
	public int so1=1;//solo una impresion
	public int cDat=0,cFeaStep=0;//contador de Datos
	static int sigT = 1;
	
	//enteros para features
	public int SuP=0;
	
	//decimales
	public double vAngV=0;//variable do Valor do Angulo em V
	public double comba=0;//variable do Comprimento da Base
	public double comCara=0;//Comprimento da cara
	
	//pivados
	//int cpos=0;//contador de posiciones
	
	
		//separadores e identificadores tipo char
		public char parD=')';
		public char vir=',';
		public char parI='(';
		public char pyc=';';
		public char num='#';
		public char dol='$';
		
		public int salirSP=1;
	
	@SuppressWarnings({ "static-access" })
	public void FeaStep() {
		
		//para iniciar las entidades;
				Axis2Placement3D AxPl3d = new Axis2Placement3D();
				//TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
				//HoleButtomCondition HoleBC = new HoleButtomCondition();
				IndexEnderecosPro SecEnder = new IndexEnderecosPro();
				MapearOperacoesDaFeature MapFer =new MapearOperacoesDaFeature();
				//DiameterTaper DiametroTap = new DiameterTaper();
				MapearMachining_workingstep DadosFea = new MapearMachining_workingstep();
				//MapearFerramentasDasMachiningOperations MapFer = new MapearFerramentasDasMachiningOperations();
				DadosFea.InformacoesAvancadas.add("Feature - Step" + cFeaStep);
				 //listDadosFeatures.add("Feature - RoundHole");
				
				DadosDaPeca Bloco = new DadosDaPeca();
				ComprimentoZ_DoBloco = Bloco.ComprimentoZ_DoBloco;
		
		LinearPath LinPathTlmDir = new LinearPath();


		
		StringTokenizer st = new StringTokenizer(this.data, "(,)#",true);//buscador de tokens con separadores activados
		int cpar=1;//contador de parentesis
	
		
		if(actAsig==1){
		 do{//asignacion de direcciones
			 ctokens++;//contador de tokens para encontrar el parentesis
			 String valores = st.nextToken();//leo el proximo token
		     //System.out.println(ctokens + " "+valores);

		     char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		     
		     
		     
		     /*******************************************************************************/
		                       /**          Ferramentas              **/
		     if(aCaracteres[0]==parD){//encuentro el token ')' parD para salir das Ferramientas
		    	 cpar=0;//salgo del wile
		    	 sigDir=ctokens;
		    	 }
		     if(cpar==1){//imprimo las ferramentas
		    	 if(ctokens>=10){//copia el primer token con la primera ferramenta
		    		 if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
						}else{
		    				 //System.out.println("T" + cEnde +"=#"+valores);
		    				 TempEnd = Integer.parseInt(valores);
		    				 //MapFer.rutaEsc=SecEnder.rutaEsc;//le paso la misma ruta que ya fue dada a secuencia de enderecos
		    				 //System.out.println(MapFer.MapFerOp(SecEnder.listEnderecos.get(TempEnd)));
		    				 
		    				 String Tem0= "T" + (cEnde+1) +":#"+valores;
		    				 String Tem1= MapFer.MapFerOp(SecEnder.IndexacaoEnderecos.get(TempEnd));
		    				 
		    				 DadosFea.InformacoesAvancadas.add(Tem0+"," + Tem1);
		    				 cEnde++;
		    				 asigEnde=cEnde;
//							System.out.println("T" + cEnde +"   #"+valores);
							}	 
		    		 }
		    	 }
		     /*************************** Fin Ferramentas ******************************************/
		     
		     
		     
		     /*******************************************************************************/
                               /**          Ubicacion              **/
			if(ctokens==(sigDir+3)){//copia el primer token con la primera ferramenta
				nVal="";
				if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
					}else{
						for (int i = 0; i < aCaracteres.length; i++){
							nVal=nVal+Character.toString(aCaracteres[i]);
							}
//						TempEnd = Integer.parseInt(nVal);
//						AxPl3d.RestarCompBloco=Double.parseDouble(ComprimentoZ_DoBloco);
//						String data2 =AxPl3d.ExtrairDadosAP3D(SecEnder.listEnderecos.get(TempEnd));
//						System.out.println("Ubicacion:  "+data2); 
						
						TempEnd = Integer.parseInt(nVal);
						AxPl3d.RestarCompBloco=Double.parseDouble(ComprimentoZ_DoBloco);
						String data2 =AxPl3d.ExtrairDadosAP3D(SecEnder.IndexacaoEnderecos.get(TempEnd));
						DadosFea.InformacoesAvancadas.add("Ubicacion:"+data2);
						
						
					}	 
					}
			
			/*************************** Fin Ubicacion ******************************************/
		     
			
		     /*******************************************************************************/
            					/**          Profundidad             **/
			if(ctokens==(sigDir+6)){//copia el primer token con la primera ferramenta
				nVal="";
				if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros					
				}else{
					for (int i = 0; i < aCaracteres.length; i++){
						nVal=nVal+Character.toString(aCaracteres[i]);
						}
//					TempEnd = Integer.parseInt(nVal);
//					AxPl3d.RestarCompBloco=0;//para no restar el valor del bnloque
//					System.out.println("Profundidad:  "+AxPl3d.ExtrairDadosAP3D(SecEnder.listEnderecos.get(TempEnd)));
					TempEnd = Integer.parseInt(nVal);
					AxPl3d.RestarCompBloco=0;//para no restar el valor del bnloque
					//System.out.println("Profundidad:  "+AxPl3d.ExtrairDadosAP3D(SecEnder.listEnderecos.get(TempEnd)));
					DadosFea.InformacoesAvancadas.add("Profundidad:"+AxPl3d.ExtrairDadosAP3D(SecEnder.IndexacaoEnderecos.get(TempEnd)));
					sigT=0;
					}				
				}
			
			/*************************** Fin Profundidad ******************************************/
			
			
			
	     	/*******************************************************************************/
						/** Step - CourseOfTravel - TravelPath Opcoes
						 * 1 - General Path
						 * 2 - Linear Path
						 * 3 - Circular Path        		**/
				if(ctokens==(sigDir+9)){//copia el primer token con la primera ferramenta
					nVal="";
					if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
						}else{
							for (int i = 0; i < aCaracteres.length; i++){
								nVal=nVal+Character.toString(aCaracteres[i]);//copio hasta el ultimo valor
								}
//							TempEnd = Integer.parseInt(nVal);
//							System.out.println("TravelPath  "+LinPathTlmDir.ExtrairDadosLinPath(SecEnder.listEnderecos.get(TempEnd)));
							TempEnd = Integer.parseInt(nVal);
							//System.out.println("TravelPath:  "+LinPathTlmDir.ExtrairDadosLinPath(SecEnder.listEnderecos.get(TempEnd)));
							DadosFea.InformacoesAvancadas.add("TravelPath:  "+LinPathTlmDir.ExtrairDadosLinPath(SecEnder.IndexacaoEnderecos.get(TempEnd)));
							}
					}
				
				//falta cacular el ancho dela feature
				
				/*************************** Fin Fundo do Slot ******************************************/
		     
		     //char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		     if(aCaracteres[0]==pyc){//encuentro el token ')' parD
		    	 Enderecos[ cEnde] ="-";//array de pociciojnes para este Slot
		    	 sAsig=0; 
		     }
			 
			}while(st.hasMoreTokens() & sAsig==1);

		 actAsig=0;
		 salirSP = 0;
		}//termina asignacion de endereços
}
	
	
	/**** Outros void ****/
	public void Imprimir() {
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(Fin================= Salio de Step)");
		}
		//System.out.println("DirFer: "+this.FerramentaCD[0]+", Tipo: "+this.FerramentaCD[1]+", Nombre: "+this.FerramentaCD[2] +", Diametro: "+this.FerramentaCD[3]+ ", TLO: "+this.FerramentaCD[4]);

	}
	
	
	
}
