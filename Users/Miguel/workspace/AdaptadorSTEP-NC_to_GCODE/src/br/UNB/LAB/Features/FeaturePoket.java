package br.UNB.LAB.Features;

import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.Axis2Placement3D;
import br.UNB.LAB.Entidades_E_Atributos.RectangularClosedProfile;
import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.InfBasicas.DadosDaPeca;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;
import br.UNB.LAB.Integrador.MapearOperacoesDaFeature;



public class FeaturePoket {

	static int salDP=1;
		
	//cadenas
	public String nVal="";//para asignacion de nuevos valores en la direccion
	public String Enderecos[] = new String[20];//array de pociciojnes para este Slot
	static int TempEnd=0;//endereco temporal para llamar las entidades y sacar los atributos
	public String Datos[] = new String[50];//array de pociciojnes para este Slot
	static String ComprimentoZ_DoBloco;
	
	//enteros
	public int ctokens=0;//contador de tokens
	public int sAsig=1;//salir de asignacion de nexttokens
	public int actAsig=1;//Activar asignacion de direcciones  0=no   1=si
	public int sigDir=0;//siguiente direcion
	public int cEnde=0;//contador de endereços
	public int asigEnde=0;//Asignacion de endereços
	public int cDat=0, cPoket=0;//contador de Datos
	public int finFer=1;//final de ferramentas
	static int sigT = 1;
	
	//separadores e identificadores tipo char
	public char parD=')';
	public char vir=',';
	public char parI='(';
	public char pyc=';';
	public char num='#';
	public char dol='$';
	

	
	public int salirCP=1;
	
	public String data="";

	
	@SuppressWarnings("static-access")
	public void FeaClosedPocket() {
		//para iniciar las entidades;
		Axis2Placement3D AxPl3d = new Axis2Placement3D();
		AxPl3d.PlanSeg=1;
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		RectangularClosedProfile ReCP = new RectangularClosedProfile();
		MapearOperacoesDaFeature MapFer =new MapearOperacoesDaFeature();
		
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		//SecEnder.SecEnderecosDoFormato();
		MapearMachining_workingstep DadosFea = new MapearMachining_workingstep();
		DadosFea.InformacoesAvancadas.add("Feature - Pocket" + cPoket);
		
			DadosDaPeca Bloco = new DadosDaPeca();
			//Bloco.DadosDoBloko();
			ComprimentoZ_DoBloco = Bloco.ComprimentoZ_DoBloco;

		
		//capturo los valores de las dimenciones de la piesa bruta

		
		
		//System.out.println(data);//imrpimo los valores mas un contador
		StringTokenizer st = new StringTokenizer(this.data, "(,)#",true);//buscador de tokens con separadores activados
		int cpar=1;//contador de parentesis
		//int temptok = st.countTokens();
		
		if(actAsig==1){
		 do{//asignacion de direcciones
			
			 ctokens++;//contador de tokens para encontrar el parentesis
		     String valores = st.nextToken();//leo el proximo token
		     //System.out.println(ctokens + " "+valores);//imrpimo los valores mas un contador

		     char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		     
		     /*******************************************************************************/
		                       /**          Ferramentas              **/
		     if(aCaracteres[0]==parD){//encuentro el token ')' parD para salir das Ferramientas
					cpar=0;//salgo del wile
					sigDir=ctokens;
					finFer=0;//saldo de ferramentas al encontrar la ultima
				}
		     if(cpar==1)//imprimo las ferramentas
		     {
				if(ctokens>=10){//copia el primer token con la primera ferramenta
					if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
						}else{
		    				 //System.out.println("T" + cEnde +"=#"+valores);
		    				 TempEnd = Integer.parseInt(valores);
		    				 String Tem0= "T" + (cEnde+1) +":#"+valores;
		    				 String Tem1= MapFer.MapFerOp(SecEnder.IndexacaoEnderecos.get(TempEnd));
		    				 DadosFea.InformacoesAvancadas.add(Tem0+"," + Tem1);
		    				 cEnde++;
		    				 asigEnde=cEnde;
		    				 }	
					}
				}
		     /*************************** Fin Ferramentas ******************************************/
		     
		     
		     
		     /*******************************************************************************/
                               /**          Ubicacion              **/
			if(ctokens==(sigDir+3) & finFer==0){//copia el primer token con la primera ferramenta
				nVal="";
				if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
					}else{
						for (int i = 0; i < aCaracteres.length; i++) {
							nVal=nVal+Character.toString(aCaracteres[i]);
							}
						TempEnd = Integer.parseInt(nVal);
						AxPl3d.RestarCompBloco=Double.parseDouble(ComprimentoZ_DoBloco);
						String data2 =AxPl3d.ExtrairDadosAP3D(SecEnder.IndexacaoEnderecos.get(TempEnd));
						DadosFea.InformacoesAvancadas.add("Ubicacion:"+data2);
						//System.out.println("Ubicacion:  "+data2);
						}
				}
			
			/*************************** Fin Ubicacion ******************************************/
		     
			
			
		     /*******************************************************************************/
            					/**          Profundidad             **/
			if(ctokens==(sigDir+6) & finFer==0){//copia el primer token con la primera ferramenta
				nVal="";
			if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
				}else
				{
					for (int i = 0; i < aCaracteres.length; i++) {
						nVal=nVal+Character.toString(aCaracteres[i]);
					}
					TempEnd = Integer.parseInt(nVal);
					AxPl3d.RestarCompBloco=0;//para no restar el valor del bnloque
					//System.out.println("Profundidad:  "+AxPl3d.ExtrairDadosAP3D(SecEnder.listEnderecos.get(TempEnd)));
					DadosFea.InformacoesAvancadas.add("Profundidad:"+AxPl3d.ExtrairDadosAP3D(SecEnder.IndexacaoEnderecos.get(TempEnd)));
					sigT=0;
				}	 
				}
			
			/*************************** Fin Profundidad ******************************************/
			
			
			
		     	/*******************************************************************************/
							/** Diametro de la base del Pocket  - Planar Radius     		**/
				if(ctokens==(sigDir+7) | ctokens==(sigDir+8) & finFer==0 & sigT==0){//copia el primer token con la primera ferramenta
					
					//System.out.println(data);//imrpimo los valores mas un contador
					
					nVal="";
					if(aCaracteres[0]==dol | aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros						
						//System.out.println("Es el simbolo de dolar o un numeral");
						}else{
							for (int i = 0; i < aCaracteres.length; i++) {
								nVal=nVal+Character.toString(aCaracteres[i]);
								}
							TempEnd = Integer.parseInt(nVal);
							String data2=SecEnder.IndexacaoEnderecos.get(TempEnd);
							if(data2.contains("=TOLERANCED_LENGTH_MEASURE(")){
								//System.out.println("Planar Radius: "+TooLM.ExtrairDadosTLM(SecEnder.listEnderecos.get(TempEnd)));
								DadosFea.InformacoesAvancadas.add("PlanarRadius="+TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd))+";");
								}
							}
					}
				
				/*************************** Fin Diametro de la base del Pocket      ******************************************/
		     
				
				
						/*******************************************************************************/
										/** Diametro de las esquinas del Pocket  - Ortogonal Radius     		**/
					if(ctokens==(sigDir+11) | ctokens==(sigDir+10) & finFer==0){//copia el primer token con la primera ferramenta
						
						nVal="";
						if(aCaracteres[0]==dol | aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
							//System.out.println("Es el simbolo de dolar o un numeral");
							}else{
								for (int i = 0; i < aCaracteres.length; i++) {
									nVal=nVal+Character.toString(aCaracteres[i]);
									}
								TempEnd = Integer.parseInt(nVal);
								//System.out.println("TOLERANCED_LENGTH_MEASURE      :"+SecEnder.Enderecos[TempEnd]);
								String data2=SecEnder.IndexacaoEnderecos.get(TempEnd);
								if(data2.contains("=TOLERANCED_LENGTH_MEASURE(")){
									//System.out.println("Ortogonal Radius: "+TooLM.ExtrairDadosTLM(SecEnder.listEnderecos.get(TempEnd)));
									DadosFea.InformacoesAvancadas.add("OrtogonalRadius="+TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd))+";");
									}
								}					
						}
					
					/*************************** Fin Diametro de las esquinas del Pocket      ******************************************/

				
					
					/********************************************************************************************************************/
										/**     RECTANGULAR_CLOSED_PROFILE - dimenciones del pocket ancho y Largo     		**/
					if(ctokens==(sigDir+13) | ctokens==(sigDir+14) & finFer==0){//copia el primer token con la primera ferramenta
						
						nVal="";
						if(aCaracteres[0]==dol | aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
							//System.out.println("Es el simbolo de dolar o un numeral");
							}else{
								for (int i = 0; i < aCaracteres.length; i++) {
									nVal=nVal+Character.toString(aCaracteres[i]);
									}
								TempEnd = Integer.parseInt(nVal);						
								//System.out.println("RECTANGULAR_CLOSED_PROFILE      :"+SecEnder.Enderecos[TempEnd]);
								String data2=SecEnder.IndexacaoEnderecos.get(TempEnd);
								if(data2.contains("=RECTANGULAR_CLOSED_PROFILE(")){
									//System.out.println("Dimeciones do Pocket "+ ReCP.ExtrairDadosRecgCP(SecEnder.listEnderecos.get(TempEnd)));
									DadosFea.InformacoesAvancadas.add("Pocket:"+ReCP.ExtrairDadosRecgCP(SecEnder.IndexacaoEnderecos.get(TempEnd))+";");
									}
								}
						}
					
					/*************************** Fin Diametro de la base del Pocket      ******************************************/
					
					
				

				

				
				
				
				
				
				/*************************** Fin Fundo do RoundHole ******************************************/
		     
		     //char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
		     if(aCaracteres[0]==pyc){//encuentro el token ')' parD
		    	 Enderecos[ cEnde] ="-";//array de pociciojnes para este 
		    	 sAsig=0; 
		     }
			 
			}while(st.hasMoreTokens() & sAsig==1);

		 actAsig=0;
		 salirCP=0;//da la salida del mapeador de features
		}//termina asignacion de endereços e recuperacion de informacion

	}
	
	
	
	public void Imprimir() {
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(Fin*************** Salio de ClosedPocket)");
		}
		//System.out.println("DirFer: "+this.FerramentaCD[0]+", Tipo: "+this.FerramentaCD[1]+", Nombre: "+this.FerramentaCD[2] +", Diametro: "+this.FerramentaCD[3]+ ", TLO: "+this.FerramentaCD[4]);

	}
	
	
}
