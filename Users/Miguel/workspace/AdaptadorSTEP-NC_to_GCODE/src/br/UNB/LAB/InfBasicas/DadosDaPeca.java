package br.UNB.LAB.InfBasicas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import br.UNB.LAB.Integrador.Integrador;

//import SecEnderecos.SecuencaEnderecosDoFormato;

public class DadosDaPeca {

	public static String ComprimentoX_DoBloco;
	public static String ComprimentoY_DoBloco;
	public static String ComprimentoZ_DoBloco;
	
	static int salB=1;
	static int ctok=0;
	//static int cEjeZ=0;
	//String nVal="";
	//String fin="";
	
	//separadores e identificadores tipo char
	//char parD=')';
	//char vir=',';
	//char parI='(';
	
	//public static void main(String[] args) {
		public void DadosDoBloko(){
			
		//System.out.println("");	
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
				System.out.println("(Entro Adquirir las dimenciones del Bloco)");
			}
		
		//SecuencaEnderecosDoFormato SecEnder = new SecuencaEnderecosDoFormato();

		
		File archivo=new File(Integrador.rutaEsc);
		
		try{
			
			BufferedReader in=new BufferedReader(new FileReader(archivo));//crea el bufer para leer el archivo
			String data="";//variable data vacia
			data=in.readLine();//lee el valor de la linea y se lo pasa a data

			while(data!=null & salB==1 ){//while para recorrer el archivo
				
				if(data.indexOf("=BLOCK(")!=-1){
					StringTokenizer st = new StringTokenizer(data, ",)",true);//buscador de tokens con separadores
					
					do{
					String valores = st.nextToken();
					//System.out.println(ctok + valores);
					char[] aCaracteres = valores.toCharArray();//convierto la cadena a un chararray
					
					if(ctok>=4){
						if(aCaracteres[0]==',' | aCaracteres[0]=='('){//solo copia numeros
						}else
						{
							//System.out.println(valores);
							switch (ctok) {
							case 4:
								//System.out.print("X="+valores);
								ComprimentoX_DoBloco=valores;
								break;
							case 6:
								//System.out.print(",Y="+valores);
								ComprimentoY_DoBloco=valores;
								break;
							case 8:
								//System.out.println(",Z="+valores+";");
								ComprimentoZ_DoBloco=valores;
								break;
							}
						}
					}
					ctok++;
					}while(st.hasMoreTokens());
					
					
					salB=0;
				}else{
					//
					ComprimentoX_DoBloco="0";
					ComprimentoY_DoBloco="0";
					ComprimentoZ_DoBloco="0";
					
				}
					
				
				
				data=in.readLine();	
			}//fin while lectura de las lineas del archivo
			
			in.close();
			if(ComprimentoZ_DoBloco=="0"){
				if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
					System.out.println("(No existem Dados do Bloco, Todos os Comprimentos sao 0)");
				}
			}
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
				System.out.println("(Fin================= Salio de Adquirir las dimenciones del loco)");
			}
			//System.out.println("");
			
	}//fin try
	catch(IOException e){
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(no se encontro el Archivo! Para extraer los dados do Bloco)");
		}
	}
		
	}
		
		

}


