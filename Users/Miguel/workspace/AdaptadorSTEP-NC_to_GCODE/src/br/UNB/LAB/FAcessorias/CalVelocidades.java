package br.UNB.LAB.FAcessorias;

import java.util.StringTokenizer;

public class CalVelocidades {

	public static double CalculoVelC(double DiametroTool, double SpindleRPM, double PorcentageVelCorSpindle) {
		// TODO Auto-generated method stub
		double VelCut=0;
		VelCut = (Math.PI * DiametroTool * (SpindleRPM * (PorcentageVelCorSpindle/100)))/1000;
		return VelCut;
		
	}
	
	public static double CalculoVelAvan(double DiametroTool, double SpindleRPM, double PorcentageVelCorSpindle) {
		// TODO Auto-generated method stub
		double VelCut=0;
		VelCut = (Math.PI * DiametroTool * (SpindleRPM * (PorcentageVelCorSpindle/100)))/1000;
		return VelCut;
		
	}


}
