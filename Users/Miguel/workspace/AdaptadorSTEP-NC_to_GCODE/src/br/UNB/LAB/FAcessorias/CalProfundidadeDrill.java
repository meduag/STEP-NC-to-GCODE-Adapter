package br.UNB.LAB.FAcessorias;

public class CalProfundidadeDrill {

	public static double CalculoProfundidadeCorte(double DiametroTool, double ProfundidadeFuro) {
		// TODO Auto-generated method stub
		double Pasadas = 0;
		
		if(ProfundidadeFuro <= DiametroTool){
			Pasadas =1;
		}else {
			Pasadas = ProfundidadeFuro/DiametroTool;
		}
			
		return Pasadas;
	}

}
