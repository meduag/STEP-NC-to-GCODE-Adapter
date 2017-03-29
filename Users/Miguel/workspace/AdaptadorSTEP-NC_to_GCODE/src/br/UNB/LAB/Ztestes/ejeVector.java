package br.UNB.LAB.Ztestes;

import java.util.ArrayList;

public class ejeVector {
	
	//Vector b[][] = new Vector [3][3];

	//public ArrayList list;
	public ArrayList<String> lista = new ArrayList<String>();
	
    public void vector() {

    	 //
    	
    	lista.add("Hola");
    	
    	
    	for (int i = 0; i < 100; i++) {
    		lista.add(String.valueOf(i));
		}
    	
    	for (int i = 0; i < lista.size(); i++) {
    		String val= lista.get(i);
			if(val.startsWith("30")==true){
				System.out.println("Es treinta  " + i);
			}
		}
    	
    	System.out.println(lista.get(0) + "  "+lista.size());
    	lista.remove(1);
    	
    	for (int i = 0; i < lista.size(); i++) {
    		String val= lista.get(i);
			if(val.startsWith("30")==true){
				System.out.println("Es treinta  " + i);
			}
		}
    	
    	
    	
    	System.out.println(lista.get(0) + "  "+lista.size());


    }
}