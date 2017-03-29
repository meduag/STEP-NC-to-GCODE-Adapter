package br.UNB.LAB.ZCurbaBezier;

import java.text.DecimalFormat;

import javax.vecmath.Point3d;

public class bezier {
	
	public Point3d [] meshArray;
	private Point3d [] arrayControlPoints;
	private int splitOfU = 40;
	
	public bezier(Point3d[] arrayControlePoints)
	{
		this.arrayControlPoints = arrayControlePoints;
		meshArray = generatePointsInCurve();
	}
	
	public bezier(Point3d[] arrayControlePoints, int splitOfU)
	{
		this.arrayControlPoints = arrayControlePoints;
		this.splitOfU = splitOfU;
		meshArray = generatePointsInCurve();
	}
	
	
	/**
	 * 
	 * @return 	--> A array with points of Bezier curve
	 */
	private Point3d[] generatePointsInCurve()
	{
		DecimalFormat df = new DecimalFormat("0.0000");
		Point3d [] output = new Point3d[splitOfU];
		int n = arrayControlPoints.length - 1;
		double u ;
		
		for (int i = 0; i < splitOfU; i++)
		{			
			u = i * Math.pow(splitOfU - 1, -1);
			//System.out.println("u = " + u);
			double x = 0, y = 0, z = 0;

			for (int j = 0; j < arrayControlPoints.length; j++)
			{
				x = x + arrayControlPoints[j].x * Math.pow(1 - u, n - j) * calculateC(n, j) * Math.pow(u, j);
				y = y + arrayControlPoints[j].y * Math.pow(1 - u, n - j) * calculateC(n, j) * Math.pow(u, j);
				z = z + arrayControlPoints[j].z * Math.pow(1 - u, n - j) * calculateC(n, j) * Math.pow(u, j);
				x=Double.valueOf(df.format(x).replaceAll(",", "."));
				y=Double.valueOf(df.format(y).replaceAll(",", "."));
				z=Double.valueOf(df.format(z).replaceAll(",", "."));
			}
			output[i] = new Point3d(x, y, z);
		}
		
//		for (int i = 0; i < output.length; i++)
//		{
//			System.out.println(i + " output " + output[i]);
//		}

		return output;
	}
	
	/**
	 * 
	 * @param nI	--> number of control points - 1
	 * @param i 	--> index of the control point i
	 * @return 		--> multiply factor C (necessary in Bezier equation)
	 */
	private double calculateC(int nI, int i)
	{
		double result = calculateFactorial(nI)/(calculateFactorial(i) * calculateFactorial(nI - i));
		return result;
	}
	
	
	
	/**
	 * 
	 * @param n --> number which we want to calculate the factorial (n!)
	 * @return Factorial of n
	 */
	private double calculateFactorial(int n)
	{
		int factorial = 1;  
		  
		for( int k = 2; k <= n; k++ )  
		{   
		     factorial *= k;  
		}  
		//System.out.println("fatorial = " + factorial);
		return factorial;
	}

	public Point3d[] getMeshArray() {
		return meshArray;
	}

//	//public static ArrayList<String> arrayControlPoints = new ArrayList<String>();
//	private Point3d [] arrayControlPoints;
//	
//	public static void main(String[] args) {
//		//arrayControlPoints.add("Hola");
//		int splitOfU=40;
//		Point3d [] output = new Point3d[splitOfU];
//		
//		int n = arrayControlPoints.length - 1;
//		double u ;
//		
//		for (int i = 0; i < splitOfU; i++)
//		{			
//			u = i * Math.pow(splitOfU - 1, -1);
//			//System.out.println("u = " + u);
//			double x = 0, y = 0, z = 0;
//
//			for (int j = 0; j < arrayControlPoints.size(); j++)
//			{
//				x = x + arrayControlPoints[j].x * Math.pow(1 - u, n - j) * calculateC(n, j) * Math.pow(u, j);
//				y = y + arrayControlPoints[j].y * Math.pow(1 - u, n - j) * calculateC(n, j) * Math.pow(u, j);
//				z = z + arrayControlPoints[j].z * Math.pow(1 - u, n - j) * calculateC(n, j) * Math.pow(u, j);
//			}
//			output[i] = new Point3d(x, y, z);
//		}
//		
//	}
//	
//	private double calculateC(int nI, int i)
//	{
//		double result = calculateFactorial(nI)/(calculateFactorial(i) * calculateFactorial(nI - i));
//		return result;
//	}
//	
//	private double calculateFactorial(int n)
//	{
//		int factorial = 1;  
//		  
//		for( int k = 2; k <= n; k++ )  
//		{   
//		     factorial *= k;  
//		}  
//		//System.out.println("fatorial = " + factorial);
//		return factorial;
//	}

}
