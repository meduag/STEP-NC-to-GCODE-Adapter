package br.UNB.LAB.Integrador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class lerDadosArqConfg {

	public static  ArrayList<String> DadosarqConfg = new ArrayList<String>();
	
	public static void LeerDadConfg() {
		DadosarqConfg.clear();
		try{
			BufferedReader in=new BufferedReader(new FileReader("./arqConf.txt"));//crea el bufer para leer el archivo
			String data="";//variable data vacia
			data=in.readLine();//lee la primera linea 
			
			DadosarqConfg.add(data.replaceAll("Repositorio=", ""));
	
			//System.out.println(DadosarqConfg.get(0));
			int cont = 0;
			while(data!=null){//while para recorrer el archivo
				if(data.startsWith("Code_p21=")){
					DadosarqConfg.add(data.replaceAll("Code_p21=", ""));
					//System.out.println(DadosarqConfg.get(1));
				}
				
				if(data.startsWith("ComentariosInf=")){
					data=data.replaceAll("ComentariosInf=", "");
					if(data.startsWith("true")){
						DadosarqConfg.add("1");
					}else{
						DadosarqConfg.add("0");
					}
					//System.out.println(DadosarqConfg.get(2));
				}
				
				if(data.startsWith("ComentariosCod=")){
					data=data.replaceAll("ComentariosCod=", "");
					if(data.startsWith("true")){
						DadosarqConfg.add("1");
					}else{
						DadosarqConfg.add("0");
					}
					//System.out.println(DadosarqConfg.get(3));
				}
				
				if(data.startsWith("Pla_Seg=")){
					DadosarqConfg.add(data.replaceAll("Pla_Seg=", ""));
					//System.out.println(DadosarqConfg.get(4));
				}
				
				if(data.startsWith("VelAvaMaq=")){
					DadosarqConfg.add(data.replaceAll("VelAvaMaq=", ""));
					//System.out.println(DadosarqConfg.get(5));
				}
				
				if(data.startsWith("VelAvaCorte=")){
					DadosarqConfg.add(data.replaceAll("VelAvaCorte=", ""));
					//System.out.println(DadosarqConfg.get(6));
				}
				
				if(data.startsWith("AvaProfCorte=")){
					DadosarqConfg.add(data.replaceAll("AvaProfCorte=", ""));
					//System.out.println(DadosarqConfg.get(7));
				}
				
				if(data.startsWith("AvaAngSemiCir=")){
					DadosarqConfg.add(data.replaceAll("AvaAngSemiCir=", ""));
					//System.out.println(DadosarqConfg.get(8));
				}
				
				if(data.startsWith("PorFerEspiral=")){
					DadosarqConfg.add(data.replaceAll("PorFerEspiral=", ""));
					//System.out.println(DadosarqConfg.get(9));
				}
				
				if(data.startsWith("AvaLinhaProf=")){
					DadosarqConfg.add(data.replaceAll("AvaLinhaProf=", ""));
					//System.out.println(DadosarqConfg.get(10));
				}
				
				if(data.startsWith("tooltbl=")){
					DadosarqConfg.add(data.replaceAll("tooltbl=", ""));
					//System.out.println(DadosarqConfg.get(11));
				}
				
				//System.out.println("datos   " + data);
				data=in.readLine();//lee la primera linea
				cont= cont +1;
			}//fin try
			
			//System.out.println("Cant linhas: " + cont);
			in.close();
		}catch(IOException e){
			System.out.println("no fue posible abrir el archivo de configuraciones, verificar!!");
		}
			
		
		

	}

}
