package br.UNB.LAB.FuncoesDePosicao;

public class Intecepcion {

static double X1,X2,Y1,Y2,m,b;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		X1=-20;
		Y1=0;
		
		X2=0;
		Y2=-10;
		
		m=(Y2-Y1)/(X2-X1);
		b=Y1-(m*X1);
		
		if(b<0){
			System.out.println("y="+m+"x" + b);
		}else{
			System.out.println("y="+m+"x+" + b);
		}
	}

}
