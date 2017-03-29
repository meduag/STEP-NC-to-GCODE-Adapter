package br.UNB.LAB.Integrador;

import br.UNB.LAB.MachiningOperations_E_Ferramentas.*;

public class MapearOperacoesDaFeature {

	public String rutaEsc="";
	public String data="";
	public static String SalidaMapFOp="";
	
	@SuppressWarnings("static-access")
	
	public static String MapFerOp(String data) {
		
		//System.out.println(data);
		SalidaMapFOp="Sin Informacion";
	
		//nuevas funciones
		OperationsMill OperationMill = new OperationsMill();

		
		if(data.indexOf("CENTER_DRILLING(")!=-1){
			SalidaMapFOp=OperationMill.ExtrairDadosCD(data);
		}
		
		if(data.indexOf("DRILLING(")!=-1){
			OperationMill.ProfOpe=1;
			SalidaMapFOp=OperationMill.ExtrairDadosCD(data);
			//System.out.println("Valor da prof Operacion" + OperationMill.ProfOpe);
		}
		
		if(data.indexOf("BOTTOM_AND_SIDE_ROUGH_MILLING(")!=-1){
			SalidaMapFOp=OperationMill.ExtrairDadosCD(data);
		}
		
		if(data.indexOf("REAMING(")!=-1){
			SalidaMapFOp=OperationMill.ExtrairDadosCD(data);
		}
		
		
		return SalidaMapFOp;

	}

}
