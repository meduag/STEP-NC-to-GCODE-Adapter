package br.UNB.LAB.IndEnderecos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import br.UNB.LAB.Integrador.Integrador;

public class TirarEspacosEmBranco {

	public void EspacosEmBranco(){
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
		 System.out.println("(Entro en Tirar Espacios Em Branco)");
		}	
		
		
		File archivo=new File(Integrador.rutaLec);//archivo de entrada
		String sFichero = Integrador.rutaEsc;//ruta del archivo de salida
		
		//elimino el fichero antes de creearlo,
		File  fichero = new File (Integrador.rutaEsc);//para eliminar el fichero viejo
		if (fichero.delete()){
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			   System .out.println("(El fichero Antiguo ha sido borrado satisfactoriamente)");
			   System .out.println("(El fichero Nuevo ha sido generado satisfactoriamente)");
			}
		}else{
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			   System .out.println("(El fichero no puede ser borrado)");
			}
		}
		
		String   ln= System .getProperty("line.separator");
		
		int clin=0;//inicio del contador
		int cpos=0;//contador de posiciones
		int tamtok=0;//tamanho max de los token de una cadena
		int ccont=0;//continuar contantando despues de encontrar el data
		
		
		try{
			
			BufferedReader in=new BufferedReader(new FileReader(archivo));//crea el bufer para leer el archivo
			String data="";//variable data vacia
			String data2="";//variable data vacia
			data=in.readLine();//lee la primera linea 
			
			while(data!=null){//while para recorrer el archivo
				
				if(data.indexOf("PROJECT(")!=-1){
					clin=1;
					ccont=1;
				}
				
			if(clin>=1){
			//System.out.println(clin+ "  " + data);
			StringTokenizer st = new StringTokenizer(data, ",;",true);//buscador de tokens con separadores
			tamtok = st.countTokens();
			//System.out.println(tamtok);
			cpos=0;
			
			do{//recorro el chararray, en busca de los parentesis
				 String valores = st.nextToken();
				 char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
				 cpos++;
				 if(cpos==tamtok){
					 if(aCaracteres[0]==',' | aCaracteres[0]=='#' | aCaracteres[0]==' '){
						 data2=in.readLine();
						 
						// int tamtok2 = data2.length();
						 if(data2.endsWith(";")==true){
							 //System.out.println("termina en ;");
							 try{
							     BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero,true));
							       bw.write(data.replaceAll(" ", "") + data2.trim() + ln);
							       //bw.write(data + "\n");
							     // Hay que cerrar el fichero
							       bw.close();
							  } catch (IOException ioe){
							     ioe.printStackTrace();
							  }///acaba el guardado del archivo con el valor de project mod
							 
						 }else{
							 try{
							     BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero,true));
							       bw.write(data.replaceAll(" ", "") + data2.trim());
							       //bw.write(data + "\n");
							     // Hay que cerrar el fichero
							       bw.close();
							  } catch (IOException ioe){
							     ioe.printStackTrace();
							  }///acaba el guardado del archivo con el valor de project mod
//							 System.out.println(data2);
//							 System.out.println("termina en " + data2.charAt(tamtok2-1));
						 }
						 
						 
							
						 
					 }else{
						 
					 //System.out.println(data);
					 try{
					     BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero,true));
					       bw.write(data.replaceAll(" ", "") + ln);
					       //bw.write(data + "\n");
					     // Hay que cerrar el fichero
					       bw.close();
					  } catch (IOException ioe){
					     ioe.printStackTrace();
					  }///acaba el guardado del archivo con el valor de project mod
					 }
				 }
				 
			}while(st.hasMoreTokens());
			}
			data=in.readLine();
			
			if(ccont==1){//para continuar contando
				clin++;	
				}
			
			}
			
			in.close();
			
		}//fin try
		catch(IOException e){
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
				System.out.println("(no se encontro!)");
			}
		}
		
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
		System.out.println("(Fin================= Salio de Tirar Espacios Em Branco)");
		//System.out.println("");
		}
	}

}
