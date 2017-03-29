package br.UNB.LAB.GerarCodG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import br.UNB.LAB.Integrador.Integrador;
import br.UNB.LAB.Integrador.MapearMachining_workingstep;


public class OrdenarFerramentas {


	public static ArrayList<String> listDadosTrocaFerra = new ArrayList<String>();
	public static ArrayList<String> listCenterDrill = new ArrayList<String>();
	public static ArrayList<String> listTwisDrill = new ArrayList<String>();
	public static ArrayList<String> listFaceMill = new ArrayList<String>();
	public static ArrayList<String> listBallEndMill = new ArrayList<String>();
	public static ArrayList<String> listBullNoseEndMill = new ArrayList<String>();
	//adicionar ferramentas posteriormente
	
	//lista temp verificar herramientas de un mismo tipo
	static ArrayList<String> listTemp = new ArrayList<String>();
	
	static String tipo= "", diametro= "", radiobase= "", angoffset= "", Tlo= "";
	
	//ruta de salida para generar la tabla de herramientas
	//public static String rutaEscTLB="";
	
	public static void OrdenFer(){
		
		//variables para el fichero de salida
		String sFicheroTLB = Integrador.rutaEscTBL;//ruta del archivo de salida
		String   ln= System .getProperty("line.separator");
		
		int cDatList=0; 
		
		//adquirir todas las herramientas ///////////////////////////////////////////////////////////////// en construcion hacer una funcion especifica para esto
		int TamListFer=MapearMachining_workingstep.InformacoesAvancadas.size();
		int ctokens=0;
		
		do{
			
		String data=MapearMachining_workingstep.InformacoesAvancadas.get(cDatList);
		//System.out.println(data);
		ctokens=0;
		int ini=1;
			//asignacion de direcciones
			if(data.startsWith("T")==true){
				StringTokenizer stf = new StringTokenizer(data, ":=,;",true);//buscador de tokens con separadores activados
				do{
					ctokens++;//contador de tokens para encontrar el parentesis
					String valores = stf.nextToken();//leo el proximo token
					//System.out.println(ctokens + " "+valores);
					if(ctokens==11){//copia el primer token con la primera ferramenta
						tipo=valores;
						}
					if(ctokens==15){//copia el primer token con la primera ferramenta
						diametro=valores;
						//ini=0;
						}
					if(ctokens==19){//copia el primer token con la primera ferramenta
						Tlo=valores;
						//ini=0;
						}
					if(ctokens==23){//copia el primer token con la primera ferramenta
						radiobase=valores;
						//ini=0;
						}
					if(ctokens==27){//copia el primer token con la primera ferramenta
						angoffset=valores;
						//ini=0;
						}
					}while(stf.hasMoreTokens() & ini==1);
				
				////////////////////////////////Ordenar Herramientas
				
				/*****************              ferramentas para CenterDrill                *************/
					if(tipo.startsWith("CENTER_DRILL")==true){
						if(listCenterDrill.size()!=0){
							for (int i = 0; i < listCenterDrill.size(); i++) {
								String Data = listCenterDrill.get(i);
								StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
								String Tipo = st.nextToken();
								String Diametro = st.nextToken();
								
										if(Tipo.startsWith(tipo) & Diametro.startsWith(diametro)){
											//System.out.println("Herramientas Iguales");
											i = listCenterDrill.size();
											listTemp.add("0");
										}else{
											//System.out.println("Herramientas Dif Agregando a la Lista de CenterDrill");
											listTemp.add("1");
										}
										
							}
							
							//compruebo el resultado de las comparaciones
							int AddFer=0;
							for (int i = 0; i < listTemp.size(); i++) {
								String TemList = listTemp.get(i);
								if(TemList.startsWith("0")){
									AddFer=0;
								}else{
									AddFer=1;
								}
									
							}
							
							//agrego o no agrego
							if(AddFer==1){
								listCenterDrill.add(tipo +"," + diametro+"," + Tlo);
							}
							
	
						}else{
							//Primera Herramienta
							listCenterDrill.add(tipo +"," + diametro +"," + Tlo);
							}
					}//fin
					
					
					
					
					/*****************              ferramentas para TwisDrill                *************/
				
					if(tipo.startsWith("TWIST_DRILL")==true){
						//para decidir cuando guardar
						
						
						if(listTwisDrill.size()!=0){
							for (int i = 0; i < listTwisDrill.size(); i++) {
								String Data = listTwisDrill.get(i);
								//System.out.println("                                            "+Data);
								StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
								String Tipo = st.nextToken();
								String Diametro = st.nextToken();
								String RadioBase = st.nextToken();
								String AngOffset = st.nextToken();

										if(Tipo.startsWith(tipo) & Diametro.startsWith(diametro) & RadioBase.startsWith(radiobase) & AngOffset.startsWith(angoffset) ){
											//compruebo si esta repetido o no en la lista
											i = listTwisDrill.size();
											listTemp.add("0");
										}else{
											//System.out.println("Herramientas Dif Agregando a la Lista de CenterDrill");
											listTemp.add("1");
										}
							}
							//compruebo el resultado de las comparaciones
							int AddFer=0;
							for (int i = 0; i < listTemp.size(); i++) {
								String TemList = listTemp.get(i);
								if(TemList.startsWith("0")){
									AddFer=0;
								}else{
									AddFer=1;
								}
									
							}
							
							//agrego o no agrego
							if(AddFer==1){
								listTwisDrill.add(tipo +"," + diametro+","+radiobase+","+angoffset+"," + Tlo);
							}
								
						}else{
							//Primera Herramienta
							listTwisDrill.add(tipo +"," + diametro+","+radiobase+","+angoffset+"," + Tlo);
							}
	
					}//fin
					
					
					/*****************              ferramentas para FaceMill                *************/
					if(tipo.startsWith("FACEMILL")==true){
						if(listFaceMill.size()!=0){
							for (int i = 0; i < listFaceMill.size(); i++) {
								String Data = listFaceMill.get(i);
								StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
								String Tipo = st.nextToken();
								String Diametro = st.nextToken();
								String RadioBase = st.nextToken();
							
										if(Tipo.startsWith(tipo) & Diametro.startsWith(diametro) & RadioBase.startsWith(radiobase) ){
											//System.out.println("Herramientas Iguales");
											i = listFaceMill.size();
											listTemp.add("0");
										}else{
											//System.out.println("Herramientas Dif Agregando a la Lista de CenterDrill");
											listTemp.add("1");
										}
							}
							
							//compruebo el resultado de las comparaciones
							int AddFer=0;
							for (int i = 0; i < listTemp.size(); i++) {
								String TemList = listTemp.get(i);
								if(TemList.startsWith("0")){
									AddFer=0;
								}else{
									AddFer=1;
								}
									
							}
							
							//agrego o no agrego
							if(AddFer==1){
								listFaceMill.add(tipo +"," + diametro+","+radiobase+"," + Tlo);
							}
						}else{
							//Primera Herramienta
							listFaceMill.add(tipo +"," + diametro+","+radiobase+"," + Tlo);
							}
						
					}
					
					/*****************              ferramentas para BallEndMill                *************/
					if(tipo.startsWith("BALL_ENDMILL")==true){
						if(listBallEndMill.size()!=0){
							for (int i = 0; i < listBallEndMill.size(); i++) {
								String Data = listBallEndMill.get(i);
								StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
								String Tipo = st.nextToken();
								String Diametro = st.nextToken();
								String RadioBase = st.nextToken();
							
										if(Tipo.startsWith(tipo) & Diametro.startsWith(diametro) & RadioBase.startsWith(radiobase) ){
											//System.out.println("Herramientas Iguales");
											i = listBallEndMill.size();
											listTemp.add("0");
										}else{
											//System.out.println("Herramientas Dif Agregando a la Lista de CenterDrill");
											listTemp.add("1");
										}
							}
							
							//compruebo el resultado de las comparaciones
							int AddFer=0;
							for (int i = 0; i < listTemp.size(); i++) {
								String TemList = listTemp.get(i);
								if(TemList.startsWith("0")){
									AddFer=0;
								}else{
									AddFer=1;
								}
									
							}
							
							//agrego o no agrego
							if(AddFer==1){
								listBallEndMill.add(tipo +"," + diametro+","+radiobase+"," + Tlo);
							}
						}else{
							//Primera Herramienta
							listBallEndMill.add(tipo +"," + diametro+","+radiobase+"," + Tlo);
							}
						
					}
					
					/*****************              ferramentas para BullNoseEndMill                *************/
					if(tipo.startsWith("BULLNOSE_ENDMILL")==true){
						if(listBullNoseEndMill.size()!=0){
							for (int i = 0; i < listBullNoseEndMill.size(); i++) {
								String Data = listBullNoseEndMill.get(i);
								StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
								String Tipo = st.nextToken();
								String Diametro = st.nextToken();
								String RadioBase = st.nextToken();
							
										if(Tipo.startsWith(tipo) & Diametro.startsWith(diametro) & RadioBase.startsWith(radiobase) ){
											//System.out.println("Herramientas Iguales");
											i = listBullNoseEndMill.size();
											listTemp.add("0");
										}else{
											//System.out.println("Herramientas Dif Agregando a la Lista de CenterDrill");
											listTemp.add("1");
										}
							}
							
							//compruebo el resultado de las comparaciones
							int AddFer=0;
							for (int i = 0; i < listTemp.size(); i++) {
								String TemList = listTemp.get(i);
								if(TemList.startsWith("0")){
									AddFer=0;
								}else{
									AddFer=1;
								}
									
							}
							
							//agrego o no agrego
							if(AddFer==1){
								listBullNoseEndMill.add(tipo +"," + diametro+","+radiobase+"," + Tlo);
							}
						}else{
							//Primera Herramienta
							listBullNoseEndMill.add(tipo +"," + diametro+","+radiobase+"," + Tlo);
							}
						
					}
				
				
				
			}	
							
					//para guardar o no en el 
					cDatList++;
			}while(cDatList<TamListFer);
		
		
		
		
		

		//Ordenar las herramientas por numero y 
		//generar archivo *.tbl para la tabla de herramientas para el EMC2
		
		File  fichero = new File (Integrador.rutaEscTBL);//para eliminar el fichero viejo
		if (fichero.delete()){
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
				System .out.println("(El fichero Antiguo ha sido borrado satisfactoriamente)");
				System .out.println("(La tabla de Ferramentas ha sido generado satisfactoriamente)");
			}
			   
				//System.out.println("");
		}else{
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
				System .out.println("(El fichero no puede ser borrado)");
			}
		}
		
		
		
		
		int IdFer=1;
		for (int i = 0; i < listCenterDrill.size(); i++) {
			listDadosTrocaFerra.add("T"+IdFer+","+listCenterDrill.get(i)+";");
			
			String Data = listCenterDrill.get(i);
			StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
			String Tipo = st.nextToken();
			String Diametro = st.nextToken();
			String ToolLegOffset = st.nextToken();
			
			
			try{
			     BufferedWriter bw = new BufferedWriter(new FileWriter(sFicheroTLB,true));
			     String data = "T"+IdFer+" P"+IdFer+" Z" + ToolLegOffset + " D"+Diametro+";"+Tipo;
			       bw.write(data + ln);
			       //bw.write(data + "\n");
			     // Hay que cerrar el fichero
			       bw.close();
			  } catch (IOException ioe){
			     ioe.printStackTrace();
			  }///acaba el guardado del archivo con el valor de project mod
			
			IdFer++;
		}
		
		
		
		
		for (int i = 0; i <listTwisDrill.size(); i++) {
			listDadosTrocaFerra.add("T"+IdFer+","+listTwisDrill.get(i)+";");
			
			String Data = listTwisDrill.get(i);
			StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
			String Tipo = st.nextToken();
			String Diametro = st.nextToken();
			String ToolLegOffset = st.nextToken();
			ToolLegOffset = st.nextToken();
			ToolLegOffset = st.nextToken();//captura el 4to token
			
			try{
			     BufferedWriter bw = new BufferedWriter(new FileWriter(sFicheroTLB,true));
			     String data = "T"+IdFer+" P"+IdFer+" Z" + ToolLegOffset + " D"+Diametro+";"+Tipo;
			       bw.write(data + ln);
			       //bw.write(data + "\n");
			     // Hay que cerrar el fichero
			       bw.close();
			  } catch (IOException ioe){
			     ioe.printStackTrace();
			  }///acaba el guardado del archivo con el valor de project mod
			
			IdFer++;
		}
		
		
		
		
		
		
		for (int i = 0; i < listFaceMill.size(); i++) {
			listDadosTrocaFerra.add("T"+IdFer+","+listFaceMill.get(i)+";");
			
			String Data = listFaceMill.get(i);
			StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
			String Tipo = st.nextToken();
			String Diametro = st.nextToken();
			String ToolLegOffset = st.nextToken();
			ToolLegOffset = st.nextToken();//captura el tercer token
			
			try{
			     BufferedWriter bw = new BufferedWriter(new FileWriter(sFicheroTLB,true));
			     String data = "T"+IdFer+" P"+IdFer+" Z" + ToolLegOffset + " D"+Diametro+";"+Tipo;
			       bw.write(data + ln);
			       //bw.write(data + "\n");
			     // Hay que cerrar el fichero
			       bw.close();
			  } catch (IOException ioe){
			     ioe.printStackTrace();
			  }///acaba el guardado del archivo con el valor de project mod
			IdFer++;
		}
		
		
		
		
		
		for (int i = 0; i < listBallEndMill.size(); i++) {
			listDadosTrocaFerra.add("T"+IdFer+","+listBallEndMill.get(i)+";");
			
			String Data = listBallEndMill.get(i);
			StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
			String Tipo = st.nextToken();
			String Diametro = st.nextToken();
			String ToolLegOffset = st.nextToken();
			ToolLegOffset = st.nextToken();//captura el tercer token
			
			try{
			     BufferedWriter bw = new BufferedWriter(new FileWriter(sFicheroTLB,true));
			     String data = "T"+IdFer+" P"+IdFer+" Z" + ToolLegOffset + " D"+Diametro+";"+Tipo;
			       bw.write(data + ln);
			       //bw.write(data + "\n");
			     // Hay que cerrar el fichero
			       bw.close();
			  } catch (IOException ioe){
			     ioe.printStackTrace();
			  }///acaba el guardado del archivo con el valor de project mod
			
			IdFer++;
		}
		
		
		
		
		
		for (int i = 0; i < listBullNoseEndMill.size(); i++) {
			listDadosTrocaFerra.add("T"+IdFer+","+listBullNoseEndMill.get(i)+";");
			
			String Data = listBullNoseEndMill.get(i);
			StringTokenizer st = new StringTokenizer(Data, ",");//buscador de tokens con separadores activados
			String Tipo = st.nextToken();
			String Diametro = st.nextToken();
			String ToolLegOffset = st.nextToken();
			ToolLegOffset = st.nextToken();//captura el tercer token
			
			try{
			     BufferedWriter bw = new BufferedWriter(new FileWriter(sFicheroTLB,true));
			     String data = "T"+IdFer+" P"+IdFer+" Z" + ToolLegOffset + " D"+Diametro+";"+Tipo;
			       bw.write(data + ln);
			       //bw.write(data + "\n");
			     // Hay que cerrar el fichero
			       bw.close();
			  } catch (IOException ioe){
			     ioe.printStackTrace();
			  }///acaba el guardado del archivo con el valor de project mod
			
			IdFer++;
		}
		
		
		
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(Salio de ordenar las herramientas)");
		}
		

		

		
		
		
	}

}
