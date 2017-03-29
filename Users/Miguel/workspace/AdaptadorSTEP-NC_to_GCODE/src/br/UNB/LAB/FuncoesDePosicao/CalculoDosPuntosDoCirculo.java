package br.UNB.LAB.FuncoesDePosicao;


import java.text.DecimalFormat;
import java.util.ArrayList;

import br.UNB.LAB.GerarCodG.GeneradorCodidoG;

public class CalculoDosPuntosDoCirculo {

	static double x=0;
	static double y=0;
	 
	
	
	static int cAng=0;

	
	public double xp=0;
	public double yp=0;
	public double diaFuro=1;
	public double diaTool=1;
	public static int graAvan=10;
	
	//public double ArrayList<double> listDadosFeatures = new ArrayList<double>();
	
	public double DiaSeg[] = new double[91];//array de pociciojnes para este Slot
	public double ProfSeg[] = new double[91];//array de pociciojnes para este Slot
	
	public static ArrayList<Double> DiametroSegmentado = new ArrayList<Double>();
	public static ArrayList<Double> ProfundidadeSegmentada = new ArrayList<Double>();
	
	
	//public static void main(String[] args) {
		public void CalPuntosCir(){
			
		DecimalFormat df = new DecimalFormat("0.0000");

		//double radioFuro=1/2;

		cAng=0;
		//System.out.println();
		do{
			x=xp+(diaFuro)*Math.cos(Math.toRadians(cAng));
			x = Double.parseDouble(df.format(x).replaceAll(",", "."));//verificar si da error
			DiaSeg[cAng/graAvan]=x;

			
			y=yp+(diaFuro/2)*Math.sin(Math.toRadians(cAng));
			y = (Math.round(y*10000)/10000.0);
			ProfSeg[cAng/graAvan]=y;
			//System.out.println("Ang: "+cAng +"   D"+df.format(x)+"    Z"+df.format(y));
			//System.out.println("Ang: "+cAng +"   D"+DiaSeg[cAng/graAvan]+"    Z"+ProfSeg[cAng/graAvan]);
			cAng += graAvan;
			
		}while(cAng<=90);
		//System.out.println("(Saliio de cacular los puntos del ciculo)");
	}
	
	public static void CalcularPuntosdaSemiCircunferencia(double diametroFuro, double diametroTool){
		double PontoX=0, PontoY=0; 
		DecimalFormat df = new DecimalFormat("0.0000");
		//double radioFuro=1/2;
		cAng=0;
		//System.out.println();
		do{
			x=PontoX+(diametroFuro)*Math.cos(Math.toRadians(cAng));
			x = Double.parseDouble(df.format(x).replaceAll(",", "."));//verificar si da error
			DiametroSegmentado.add(x);
			
			y=PontoY+(diametroFuro/2)*Math.sin(Math.toRadians(cAng));
			y = (Math.round(y*10000)/10000.0);
			ProfundidadeSegmentada.add(y);
			//System.out.println("Ang: "+cAng +"   D"+df.format(x)+"    Z"+df.format(y));
			//System.out.println("Ang: "+cAng +"   D"+DiaSeg[cAng/graAvan]+"    Z"+ProfSeg[cAng/graAvan]);
			cAng += GeneradorCodidoG.ValorPasoAngulo;
			
		}while(cAng<=90);
		//System.out.println("(Saliio de cacular los puntos del ciculo)");
		}
		
		
		
}
