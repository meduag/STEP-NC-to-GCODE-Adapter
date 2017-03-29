package br.UNB.LAB.GerarCodG;

public class RectSimples {
	
	static public double posYi=0,posXi=0,posF=0,Prof=0,PlanSeg=10,offsetProf=0,DTool=0,RTool=0,FeedRate=100,correcionX=0,correcionY=0;
	public static double impIni=1;
	static public String Ori="Hor",dadosFer;
	static String implin="";
	static int LineNo, salir=1;//contador de linea sucesiva
	
	public void RanLinSimplesNc() {
		
		FeedRate=br.UNB.LAB.GerarCodG.GeneradorCodidoG.FeedRate;
		
		
		RTool=DTool/2;
		//Prof = Prof + ;
		if(impIni==1) {
			LineNo=GeneradorCodidoG.LineNo;
			implin = "G00 Z" + PlanSeg;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			TrocaDeFerramenta.LineNo=LineNo;
			TrocaDeFerramenta.ImpriFer(dadosFer);
		}
		
		if(Ori.startsWith("Hor")==true){
			implin = "G40 G01 F"+FeedRate + " X" + (posXi-RTool) + " Y" + posYi;//imprimo la pocicion del hueco
			WriteLineNo(implin);
			}else {
				implin = "G40 G01 F"+FeedRate  + " X" + posXi + " Y" + (posYi-RTool);//imprimo la pocicion del hueco
				WriteLineNo(implin);
			}

		double inc=Prof;
		int salir=0;
		
		if(((posXi-DTool)-RTool)<0){
			posXi=-RTool;
		}else{
			posXi=posXi-RTool;
		}
		
		implin = "G00 Z0.0";//imprimo la pocicion del hueco
		WriteLineNo(implin);
		
		do{
			
			inc--;
			if(inc>=0){
				
				implin = "G01 F"+FeedRate*0.75  +" Z"+(inc+offsetProf);//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);
				
				if(Ori.startsWith("Hor")==true){
					implin = "G01 F"+FeedRate  + " X" + (posF+RTool);//imprimo la pocicion del hueco
					correcionX=(posF+RTool);
					WriteLineNo(implin);
					
						
					
					}else{
						implin = "G01 F"+FeedRate  + " Y" + (posF+RTool);//imprimo la pocicion del hueco
						WriteLineNo(implin);
						posYi=-RTool;
						}
				}else{
					salir=1;
					}
			
			inc--;
			if(inc>=0){
				implin = "G01 F"+FeedRate*0.75  +" Z"+(inc+offsetProf);//imprimo la pocicion del hueco con el 75%
				WriteLineNo(implin);
				
				implin = "G01 F"+FeedRate  + " X" + posXi + " Y" + posYi;//imprimo la pocicion del hueco
				WriteLineNo(implin);
				}else{
					salir=1;
					}
			
		}while(salir==0);
		
		if(impIni==1) {
		implin = "G00 Z" + PlanSeg;//imprimo la pocicion del hueco
		WriteLineNo(implin);
		}

	}

	
	public static void WriteLineNo(String lin) {
		
		System.out.println("N" + LineNo + " " + lin.replaceAll(",", "."));
		LineNo += 1;
		
	}
	
	public static void Imp_TrocaDeFer_Orden_Externa() {
		LineNo=GeneradorCodidoG.LineNo;
		implin = "G00 Z" + PlanSeg;//imprimo la pocicion del hueco
		WriteLineNo(implin);
		TrocaDeFerramenta.LineNo=LineNo;
		TrocaDeFerramenta.ImpriFer(dadosFer);
	}
	
	
}
