package br.UNB.LAB.FAcessorias;

public class CalularNoPasosEspiral {
	
	public static double NpasadasEsp=0;
	public static double NpasosEnterosEsp = 0;
	public static double NpasosDecimalesEsp = 0;

	public static void PassadasEspiral(double DiametroFuro, double DiametroTool) {
		
		NpasadasEsp=DiametroFuro/DiametroTool;
		NpasosEnterosEsp = (int)NpasadasEsp;
		NpasosDecimalesEsp = (NpasadasEsp - NpasosEnterosEsp);
		
	}
	

}
