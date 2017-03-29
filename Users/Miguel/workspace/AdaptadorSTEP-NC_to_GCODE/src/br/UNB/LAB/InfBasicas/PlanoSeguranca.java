package br.UNB.LAB.InfBasicas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import br.UNB.LAB.Entidades_E_Atributos.Axis2Placement3D;
import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.Integrador.Integrador;


public class PlanoSeguranca {
	
	public static String ComprimentoPlanoSeg="";
	public static double ComprimentoPlanoSegDouble=0;
	static int salPs=1;
	static int ctok=0;

	//separadores e identificadores tipo char
	//char parD=')';
	//char vir=',';
	//char parI='(';

	@SuppressWarnings("static-access")
	public void PlanSegur(){
		
		Axis2Placement3D AxPl3d = new Axis2Placement3D();
		AxPl3d.PlanSeg=1;
		
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(Entro no Plano de Seguranca)");
		}
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		SecEnder.SecEnderecosDoFormato();
		
		File archivo=new File(Integrador.rutaEsc);
		try{
			
			BufferedReader in=new BufferedReader(new FileReader(archivo));//crea el bufer para leer el archivo
			String data="";//variable data vacia
			data=in.readLine();//lee el valor de la linea y se lo pasa a data

			while(data!=null & salPs==1){//while para recorrer el archivo

				if(data.indexOf("MACHINING_WORKINGSTEP")!=-1){
					StringTokenizer st = new StringTokenizer(data, ",#",true);//buscador de tokens con separadores
					do{
					String valores = st.nextToken();
					//System.out.println(ctok + valores);
					
					if(ctok==4){
						//System.out.println("Imprimiendo "+valores);
						int TempEnd = Integer.parseInt(valores);
						//String data2=SecEnder.listEnderecos.get(TempEnd);
						//System.out.println("Imprimiendo "+ data2);
						ComprimentoPlanoSeg = AxPl3d.ExtrairDadosAP3D(SecEnder.IndexacaoEnderecos.get(TempEnd));
						//System.out.println("PlaSec:  "+ComprimentoPlanoSeg);
						

					}
						
					ctok++;
					salPs=0;
				}while(st.hasMoreTokens());
				
				}
				
				
				data=in.readLine();	
			}//fin while lectura de las lineas del archivo
			
			
			
			if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
				System.out.println("(Fin================= Salio de Plano de Seguranca)");
			}
			in.close();

			
		
	}//fin try
	catch(IOException e){
		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
			System.out.println("(no se encontro el Archivo!)");
		}
	}
		
		
		StringTokenizer stp = new StringTokenizer(ComprimentoPlanoSeg, "=;",true);//buscador de tokens con separadores activados
		int ctokens = 0;
		do{//asignacion de direcciones
			ctokens++;//contador de tokens para encontrar el parentesis
			String valores = stp.nextToken();//leo el proximo token
			//System.out.println(ctokens + " "+valores);
			if(ctokens==3){//copia el primer token con la primera ferramenta
				ComprimentoPlanoSegDouble = Double.parseDouble(valores);	
			}
		}while(stp.hasMoreTokens());
		
	}
	
	
}
