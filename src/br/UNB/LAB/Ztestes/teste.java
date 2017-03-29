package br.UNB.LAB.Ztestes;

public class teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double a = 1; 
		double b = 2; 
		
		System.out.println(a);
		System.out.println(b);
		
		dosvalores Numeros; // Criamos uma variável do tipo Numeros 
		Numeros = aumentaDoisValores(a,b); // Dizemos que número aponta para a mesma referência do retorno da função aumentaDoisValores 
		a = Numeros.x; 
		b = Numeros.y; // Fazemos com que os nossos números primários sejam modificados 
		
		System.out.println(a);
		System.out.println(b);

	}
	
	public static dosvalores aumentaDoisValores(double a , double b) 
	{ 
	dosvalores objDoisValores = new dosvalores(a,b); 
	objDoisValores.x = objDoisValores.x + 1 ; 
	objDoisValores.y = objDoisValores.y + 1 ; 
	return objDoisValores; 
	} //Esse método retorna um objeto do Tipo classeDoisValores , que contém seus atributos 

}

