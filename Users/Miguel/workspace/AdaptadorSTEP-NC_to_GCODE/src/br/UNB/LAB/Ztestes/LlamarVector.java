package br.UNB.LAB.Ztestes;

import br.UNB.LAB.IndEnderecos.IndexEnderecosPro;
import br.UNB.LAB.Ztestes.ejeVector;

public class LlamarVector {

	/**
	 * @param args
	 */
	static String rutaEsc = "C:/mod/f2.txt";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ejeVector Vect = new ejeVector();
		Vect.vector();
		
		IndexEnderecosPro SecEnder = new IndexEnderecosPro();
		//SecEnder.rutaEsc=rutaEsc;//llamando al valor
		SecEnder.SecEnderecosDoFormato();
		
		
		for (int i = 0; i < SecEnder.IndexacaoEnderecos.size(); i++) {
			System.out.println(SecEnder.IndexacaoEnderecos.get(i));
			
		}
		


	}

}
