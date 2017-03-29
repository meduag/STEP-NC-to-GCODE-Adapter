package br.UNB.LAB.FAcessorias;

public class CalularNoPasosProfundidadeCorte {
	
	public static double NpasadasPC=0;
	public static double NpasosEnterosPC = 0;
	public static double NpasosDecimalesPC = 0;

	public static void PassadasPC(double ProfundidadeTotal, double DiametroTool) {
		
		NpasadasPC=ProfundidadeTotal/DiametroTool;
		NpasosEnterosPC = (int)NpasadasPC;
		NpasosDecimalesPC = (NpasadasPC - NpasosEnterosPC);
		
	}

}
