package br.UNB.LAB.IndEnderecos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PasarArchivo {
        
	public static void Pasar(String rutaLec, String rutaMod) {

		File origen = new File(rutaLec);
        File destino = new File(rutaMod);
        try {
        	InputStream in = new FileInputStream(origen);
        	OutputStream out = new FileOutputStream(destino);
        	
        	byte[] buf = new byte[1024];
        	int len;
        	while ((len = in.read(buf)) > 0) {
        		out.write(buf, 0, len);
        		}
        	
        	in.close();
        	out.close();
        	} catch (IOException ioe){
        		ioe.printStackTrace();
        		if(br.UNB.LAB.Integrador.Integrador.ImpLedClass==1){
    				System.out.println("(no se puede pasar el archivo!)");
    				System.out.println("(Verifica la ruta o el nombre archivo en el integrador!)");
    				}
        		}
        }
	}