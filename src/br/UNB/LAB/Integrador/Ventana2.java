package br.UNB.LAB.Integrador;
/**
 * Este ejemplo muestra el uso de la nueva característica de las barras
 * de progreso ques e ha incorporado a Swing.
 * Esta nueva característica permite que la barra de progreso esté
 * realizando algún tipo de animación para indicar al usuario que el
 * sistema está ocupado. Se utiliza cuando no se sabe exactamente el
 * tiempo que se va a consumir en la realización de la acción que se ha
 * encomendado a la aplicación o el tamaño exacto del fichero, datos, o
 * cualquier otra información que se esté manipulando.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Ventana2 extends JPanel {
    JProgressBar barra = new JProgressBar();
    JButton btArrancar = new JButton();
    JButton btParar = new JButton();
    

    public Ventana2() {
        // No usamos un layout para colocar los elementos en donde se
        // quiera
        setLayout( null );

        // Fijamos el tamaño de la barra de progreso
        barra.setBounds( 5,10,315,20 );
        // La comocamos en la pantalla
        add( barra );
        
     // Fijamos los valores de la barra de progreso. Intervalo de
        // repintado
        UIManager.put( "ProgressBar.repaintInterval",new Integer(20) );
        // Ciclo de tiempo
        UIManager.put( "ProgressBar.cycleTime",new Integer(2000) );
        // Esta es la línea importante, ya que es donde se indica el
        // tipo de barra de progreso que se va a utilizar y se inicia
        // la animación de la barra
        barra.setIndeterminate( true );
        
        //aqui termina
		/*try {
			Thread.sleep(5000);
			//frame.setVisible( false );
			barra.setIndeterminate( false );
		} catch (Exception e) {}*/
        
        
        
        
        

/*        // Colocamos el botón que permitirá mover la barra
       // btArrancar.setText( " Arrancar ");
        //btArrancar.setBounds( 20,50,100,25 );
        add( btArrancar );
        btArrancar.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent evt ) {
                // Cambiamos la activación de los botones
                btArrancar.setEnabled( false );
                btParar.setEnabled( true );
                // Fijamos los valores de la barra de progreso. Intervalo de
                // repintado
                UIManager.put( "ProgressBar.repaintInterval",new Integer(20) );
                // Ciclo de tiempo
                UIManager.put( "ProgressBar.cycleTime",new Integer(3000) );
                // Esta es la línea importante, ya que es donde se indica el
                // tipo de barra de progreso que se va a utilizar y se inicia
                // la animación de la barra
                barra.setIndeterminate( true );
            }
        } );
*/
/*        // Colocamos el botón que va a permitir parar el movimiento de
        // la barra
        btParar.setText( " Parar " );
        // En principio aparecerá desactivado
        btParar.setEnabled( false );
        btParar.setBounds( 185,50,100,25 );
        add( btParar );
        btParar.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent evt ) {
                // Detenemos el movimiento de la barra
                barra.setIndeterminate( false );
                // Cambiamos la activación de los botones
                btArrancar.setEnabled( true );
                btParar.setEnabled( false );
            }
        } );*/
    }

    public static void barra( boolean inicio) {
    	JFrame frame = new JFrame( "Convertendo STEP-NC a Codigo G" );
        frame.setDefaultCloseOperation( frame.EXIT_ON_CLOSE );
        frame.getContentPane().add( new Ventana2(),BorderLayout.CENTER );
        frame.setSize( 340,80 );
        //aqui midifico la pocicion de la ventana
        frame.setBounds(100, 100, 340,80);
        
    	if(inicio==true){
    		frame.setVisible( true );
    	}else{
    		frame.setVisible( false );
    	}
        
		
    }
}
