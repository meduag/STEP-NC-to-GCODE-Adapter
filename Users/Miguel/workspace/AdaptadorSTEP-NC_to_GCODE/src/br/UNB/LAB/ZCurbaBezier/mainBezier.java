package br.UNB.LAB.ZCurbaBezier;

import java.net.URL;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.vecmath.Point3d;

import br.UNB.LAB.ZCurbaBezier.*;


public class mainBezier {

	static int numPontosControle=3, numSegmentos=10;
	static int eixo = 1;
	public static String Ori="Hor";
	//private static Point3d[] arrayPontosDeControle;
	static double profundidade = 0;
	static double profundidadeMaxima = 0;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//numPontosControle=3;//estos son la cantidad de puntos de control
		//numSegmentos=10;//estos son la cantidad de puntos para la segmentcion
		
		
		Point3d [] pontosDeControleJanela = new Point3d[numPontosControle];
		
		//para verficar donde se deve sumar o restar las pociciones X Y (Hor y Ver)
		   if (Ori.startsWith("Hor")) {
			   eixo = 0;//ranhuraHorizontalModelo
		   }else {
			   eixo = 1;//ranhuraVerticalModelo
		   }
		   
		Point3d p1 = new Point3d(0, 0, 75);
		Point3d p2 = new Point3d(25, -5.0, 75);
		Point3d p3 = new Point3d(27, -10.0, 75);
		Point3d p4 = new Point3d(30, 0, 75);
		Point3d p5 = new Point3d(30, 0, 75);
		Point3d p6 = new Point3d(30, 0, 75);
		Point3d p7 = new Point3d(30, 0, 75);
		Point3d p8 = new Point3d(30, 0, 75);
		Point3d p9 = new Point3d(30, 0, 75);
		Point3d p10 = new Point3d(30, 0, 75);
		
		
		if(numPontosControle==3){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
		}
		else if(numPontosControle==4){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
			pontosDeControleJanela[3] = p4;
		}
		else if(numPontosControle==5){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
			pontosDeControleJanela[3] = p4;
			pontosDeControleJanela[4] = p5;
		}
		else if(numPontosControle==6){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
			pontosDeControleJanela[3] = p4;
			pontosDeControleJanela[4] = p5;
			pontosDeControleJanela[5] = p6;
		}
		else if(numPontosControle==7){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
			pontosDeControleJanela[3] = p4;
			pontosDeControleJanela[4] = p5;
			pontosDeControleJanela[5] = p6;
			pontosDeControleJanela[6] = p7;
		}
		else if(numPontosControle==8){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
			pontosDeControleJanela[3] = p4;
			pontosDeControleJanela[4] = p5;
			pontosDeControleJanela[5] = p6;
			pontosDeControleJanela[6] = p7;
			pontosDeControleJanela[7] = p8;
		}
		else if(numPontosControle==9){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
			pontosDeControleJanela[3] = p4;
			pontosDeControleJanela[4] = p5;
			pontosDeControleJanela[5] = p6;
			pontosDeControleJanela[6] = p7;
			pontosDeControleJanela[7] = p8;
			pontosDeControleJanela[8] = p9;
		}
		else if(numPontosControle==10){
			pontosDeControleJanela[0] = p1;
			pontosDeControleJanela[1] = p2;
			pontosDeControleJanela[2] = p3;
			pontosDeControleJanela[3] = p4;
			pontosDeControleJanela[4] = p5;
			pontosDeControleJanela[5] = p6;
			pontosDeControleJanela[6] = p7;
			pontosDeControleJanela[7] = p8;
			pontosDeControleJanela[8] = p9;
			pontosDeControleJanela[9] = p10;
		}
		
 
 
	   boolean ok = true;//para activar los datos
	   
	   if (ok) /** PROFUNDIDADE **/
		{
		   Point3d[] pontosDaCurva = (new br.UNB.LAB.ZCurbaBezier.bezier(pontosDeControleJanela,numSegmentos)).meshArray;
			double maior = 0;
			double menor = 0;
			
			double zTmp = pontosDaCurva[0].y;
			System.out.println(zTmp);
			
			maior = zTmp;
		
			for (int i = 0; i < pontosDaCurva.length; i++)
			{
				System.out.println("N: " + i + "  Pontos da curva : " + pontosDaCurva[i]);
				zTmp = pontosDaCurva[i].y;
				if (zTmp > maior)
				{
					maior = zTmp;
				}
				
				if(zTmp < menor)
				{
					menor = zTmp;
				}
				
			}
			profundidade = (int)(-maior);
			profundidadeMaxima = -menor;
			
			System.out.println("MAIOR PONTO : " + maior);
			System.out.println("MENOR PONTO : " + menor);
		}
	   
	   

	}

}
