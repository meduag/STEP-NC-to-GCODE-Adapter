package br.UNB.LAB.IndEnderecos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import br.UNB.LAB.Integrador.Integrador;



public class IndexEnderecosPro {
	
	public static  ArrayList<String> IndexacaoEnderecos = new ArrayList<String>();
	
	public void SecEnderecosDoFormato(){
		
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(Entor en Asignacion de Enderecos)");
			}
		
		File archivo=new File(Integrador.rutaEsc);
		
		int clin=1;//inicio del contador
		int cpos=0;//contador de posiciones
		//int conLin=0; //contador de lineas

		
		try{
			
			BufferedReader in=new BufferedReader(new FileReader(archivo));//crea el bufer para leer el archivo
			String data="";//variable data vacia
			//String data2="";//variable data vacia
			data=in.readLine();//lee la primera linea 
			IndexacaoEnderecos.add("Inicio");
			
			
			while(data!=null){//while para recorrer el archivo
				//System.out.println(clin+ "  " + data);
				
				StringTokenizer st = new StringTokenizer(data, "#=",true);//buscador de tokens con separadores
				
				cpos=0;//reinicio el contador de tokens
				
				do{
					String valores = st.nextToken();
					char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
					
					 if(aCaracteres[0]=='#' & cpos==0){
						 String valores2 = st.nextToken();
						 int nVal=Integer.parseInt(valores2);
						 do{
							 if(clin==nVal){
								 //System.out.println(data);
								 IndexacaoEnderecos.add(data);
								// Enderecos[clin]=data;
								 cpos=1;//para salir de todo
							 }else{
								 
								 //System.out.println(clin);
								 IndexacaoEnderecos.add(String.valueOf(clin));
								 //Enderecos[clin]=data;
								 clin++;	
								 cpos=0;
							 }
						 }while(cpos==0);
						 
				 
					 }

						
					
				}while(st.hasMoreTokens() & cpos==0);
				
				
				
				
				
				
				
			data=in.readLine();

			clin++;	
			

			}
			
			in.close();
		}//fin try
		catch(IOException e){
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
				System.out.println("(no se encontro el archivo!)");
			}
		}
		
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("   (Endereços atribuidos e arrumados)");
			System.out.println("(Fin================= Asignacion de Enderecos)");
		}
	}

}
