package br.UNB.LAB.Features;

//import java.util.ArrayList;
import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.Axis2Placement3D;
import br.UNB.LAB.Entidades_E_Atributos.DiameterTaper;
import br.UNB.LAB.Entidades_E_Atributos.HoleButtomCondition;
import br.UNB.LAB.Entidades_E_Atributos.TolerancedLengthMeasure;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.InfBasicas.DadosDaPeca;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;
import br.UNB.LAB.Integrador.MapearOperacoesDaFeature;


public class FeatureRoundHole {
	

	
		//cadenas
	public String data;//recepcion de datos
	public String nVal="";//para asignacion de nuevos valores en la direccion
	public String Enderecos[] = new String[20];//array de pociciojnes para este Slot
	static int TempEnd=0;//endereco temporal para llamar las entidades y sacar los atributos
	public String Datos[] = new String[50];//array de pociciojnes para este Slot
	static String ComprimentoZ_DoBloco="0";
	
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
	public int cDat=0;//contador de Datos
	public int finFer=1;//final de ferramentas
	static int sigT = 1;
	
	
	//enteros para features
	public int SuP=0;//SquareUpofile
	
	//decimales
	public double vAngV=0;//variable do Valor do Angulo em V
	public double comba=0;//variable do Comprimento da Base
	public double comCara=0,cFeaRHole=1;//Comprimento da cara
	
	//pivados
	//int cpos=0;//contador de posiciones
	
	
	//separadores e identificadores tipo char
	public char parD=')';
	public char vir=',';
	public char parI='(';
	public char pyc=';';
	public char num='#';
	public char dol='$';
	
	public int salirRH=1;
	
	//public static ArrayList<String> listDadosFeatures = new ArrayList<String>();
	
	@SuppressWarnings("static-access")
	public void FeaRoundHole(){
		
		//para iniciar las entidades;
		Axis2Placement3D AxPl3d = new Axis2Placement3D();
		TolerancedLengthMeasure TooLM = new TolerancedLengthMeasure();
		HoleButtomCondition HoleBC = new HoleButtomCondition();
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		MapearOperacoesDaFeature MapFer =new MapearOperacoesDaFeature();
		DiameterTaper DiametroTap = new DiameterTaper();
		MapearMachining_workingstep DadosFea = new MapearMachining_workingstep();
		//MapearFerramentasDasMachiningOperations MapFer = new MapearFerramentasDasMachiningOperations();
		
		DadosFea.InformacoesAvancadas.add("Feature - RoundHole"+cFeaRHole);
		 //listDadosFeatures.add("Feature - RoundHole");
		
		DadosDaPeca Bloco = new DadosDaPeca();
		ctokens=0;
		ComprimentoZ_DoBloco = Bloco.ComprimentoZ_DoBloco;

		
		//capturo los valores de las dimenciones de la piesa bruta

		
		
		//System.out.println(data);//imrpimo los valores mas un contador
		StringTokenizer st = new StringTokenizer(this.data, "(,)#",true);//buscador de tokens con separadores activados
		int cpar=1;//contador de parentesis
		int temptok = st.countTokens();
		
		if(actAsig==1){
		 do//asignacion de direcciones
			{
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
							/** RoundHole Diametro Inicial        		**/
				if(ctokens==(sigDir+9) & finFer==0){//copia el primer token con la primera ferramenta
					nVal="";
					if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
						
					}else{
						for (int i = 0; i < aCaracteres.length; i++) {
							nVal=nVal+Character.toString(aCaracteres[i]);
							}
						TempEnd = Integer.parseInt(nVal);
						String data2=SecEnder.IndexacaoEnderecos.get(TempEnd);
						if(data2.contains("=TOLERANCED_LENGTH_MEASURE(")){
							//System.out.println("Diametro Inicial: "+TooLM.ExtrairDadosTLM(SecEnder.listEnderecos.get(TempEnd))+";");
							DadosFea.InformacoesAvancadas.add("Diametro Inicial:"+TooLM.ExtrairDadosTLM(SecEnder.IndexacaoEnderecos.get(TempEnd))+";");
							}
						}	 
					}
				
				/*************************** Fin RoundHole Diametro inicial      ******************************************/
		     
				
				/*******************************************************************************/
							/**         RoundHole Diametro Final       		**/
				//System.out.println();
				if(ctokens==(sigDir+11) & finFer==0 | ctokens==(sigDir+12)){//copia el primer token con la primera ferramenta
					nVal="";
					if(aCaracteres[0]==dol){
						//colocar elmismo valor anterior para imprimir en el nuevo round hole
						Enderecos[cEnde] =Enderecos[cEnde-1];//array de pociciojnes para este Slot
						cEnde++;
					}else if(aCaracteres[0]==vir | aCaracteres[0]==parI | aCaracteres[0]==num){//solo copia numeros
						
					}else{
						for (int i = 0; i < aCaracteres.length; i++) {
							nVal=nVal+Character.toString(aCaracteres[i]);
							}
						TempEnd = Integer.parseInt(nVal);
						String data2=SecEnder.IndexacaoEnderecos.get(TempEnd);
						
						if(data2.contains("=DIAMETER_TAPER(")){
							//System.out.println("Diametro Final: "+TooLM.ExtrairDadosTLM(SecEnder.listEnderecos.get(TempEnd))+";");
							DadosFea.InformacoesAvancadas.add("Diametro Final:"+DiametroTap.ExtrairDadosDiaTaper(SecEnder.IndexacaoEnderecos.get(TempEnd))+";");
							}
						}
					}
				/*************************** Fin RoundHole Diametro Final   ******************************************/
				
				if(ctokens==(sigDir+12)){//copia el primer token con la primera ferramenta
					ctokens++;
					valores = st.nextToken();//leo el proximo token
				}
				
				/*******************************************************************************/
										/**          Hole Buttom Condition - Opc
										 * Throgh BC (pasante)           
										 * Blind BC (Ciego) - Opc
										 	*1-Conical Hole Buttom
										 	*2-Spherical Hole Buttom
										 	*3-Flat Hole Hole Buttom
										 	*4-Flat with radius Hole Buttom 	**/
				if(ctokens==(temptok-2) & finFer==0){//copia el primer token con la primera ferramenta
					
					TempEnd = Integer.parseInt(valores);
					//System.out.println("Hole Buttom Condition: "+ HoleBC.ExtrairDadosHBC(SecEnder.listEnderecos.get(TempEnd)));
					DadosFea.InformacoesAvancadas.add("Hole Buttom Condition:"+ HoleBC.ExtrairDadosHBC(SecEnder.IndexacaoEnderecos.get(TempEnd)));
					 cEnde++;
				}
				/*************************** Fin Fundo do RoundHole ******************************************/
		     

		     if(aCaracteres[0]==pyc){//encuentro el token ')' parD
		    	 Enderecos[ cEnde] ="-";//array de pociciojnes para este 
		    	 sAsig=0; 
		     }
			 
			}while(st.hasMoreTokens() & sAsig==1);

		 actAsig=0;
		 salirRH=0;
		}//termina asignacion de endereços
		
//		for (int i = 0; i < listDadosFeatures.size(); i++) {
//			System.out.println("Imprimiendo            "+listDadosFeatures.get(i));
//		}
		 
	}
	

	public void Imprimir() {
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(Fin================= Salio de Mapear o RoundHole)");
		}
	}
	
}
