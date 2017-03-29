package br.UNB.LAB.GerarCodG;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class OrganizarFerraEntreFeatures {

	static String dataSalida="",dataComp="";
	static boolean addFer=false,salcomp=false;
	
	static int ctokens=0;
	public static ArrayList<String> registroFer = new ArrayList<String>();
	static String Contenedor="";
	//static int registroFer[] = new int[10];//array de pociciojnes para este Slot
	
	//dados de comparacion
	static String TipoFer,DiaFer,RadioBase,angoffset,Tlo;
	
//	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Funcion principal
//	public static boolean OrganizarFerra_Entre_Features(String dataNuevo,String TipoFeatute) {
//		if(TipoFeatute.startsWith("Slot")){
//			dataSalida = ExtracDados(dataNuevo);
//		}
//		return addFer=comparacion(dataSalida);
//	}
	
	
	
//	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////funcion de comparacion
//	public static boolean comparacion(String dataSalida) {
//		Contenedor="";
//		for (int i = 0; i < CodeG_Para__Slot_E_Operaciones.listDadosFerramentas.size(); i++) {
//			
//			//obtener los datos de la lista
//			String dataListFer = CodeG_Para__Slot_E_Operaciones.listDadosFerramentas.get(i);
//			dataComp = ExtracDados(dataListFer);
//			
//			if(dataSalida.indexOf(dataComp)!=-1){//comparacion y registro 
//				registroFer.add("true");//son iguales true
//				Contenedor=Contenedor+"true";
//			}else{
//				registroFer.add("false");
//				Contenedor=Contenedor+"false";
//				
//			}
//	
//		}//fin del for poara comp y obntencion de datos
//
//		if(Contenedor.indexOf("true")!=-1){
//			salcomp=false;
//		}else{
//			salcomp=true;
//		}
//		
//		
//		
//		return salcomp;
//	}
//	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////funcion complementar para asquirir datos
	public static String ExtracDados(String data) {
		ctokens=0;
		//obtener los datos de la lista
		
		StringTokenizer st = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = st.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);
			
			if(ctokens==11){//copia el primer token con la primera ferramenta
				TipoFer=valores;
				}
			if(ctokens==15){
				DiaFer=valores;
				}
			if(ctokens==19){//copia el primer token con la primera ferramenta
				Tlo=valores;
				//ini=0;
				}
			if(ctokens==23){
				RadioBase=valores;
				}
			if(ctokens==27){//copia el primer token con la primera ferramenta
				angoffset=valores;
				}
			}while(st.hasMoreTokens());
		
		
		if(TipoFer.startsWith("CENTER_DRILL")==true){
			 data= TipoFer+"," + DiaFer+";";//entrada;
		}
		
		if(TipoFer.startsWith("TWIST_DRILL")==true){
			 data= TipoFer+"," + DiaFer+"," + RadioBase +","+ angoffset+";";//entrada;
		}
		
		if(TipoFer.startsWith("FACEMILL")==true){
			 data= TipoFer+"," + DiaFer+"," + RadioBase +"," + Tlo +";";//entrada;
		}
		
		if(TipoFer.startsWith("BALL_ENDMILL")==true){
			 data= TipoFer+"," + DiaFer+"," + RadioBase +"," + Tlo +";";//entrada;
		}
		
		if(TipoFer.startsWith("BULLNOSE_ENDMILL")==true){
			 data= TipoFer+"," + DiaFer+"," + RadioBase +"," + Tlo +";";//entrada;
		}
		
		return data;
	}
	

}
