package Main_Package.Modelling;

import Adaptacao.Elemento;
import java.util.LinkedList;

/**
 * @data 05/07/2014
 * @author João
 */

public class InputDataSetRow{
    LinkedList<Elemento> elementos;
    LinkedList<double[]> inputFormat;
            
    public InputDataSetRow(LinkedList<Elemento> elementos){
        this.elementos = elementos;
        this.inputFormat = new LinkedList<>();
    }
    
    public void formatInput(){
        if(this.elementos.isEmpty()) return;
        
        for(Elemento elemento : this.elementos){
            this.inputFormat.add(new double[]{(double) elemento.getCorMedia().getRed(), 
                                              (double) elemento.getCorMedia().getGreen(),
                                              (double) elemento.getCorMedia().getBlue(),
                                              (double) elemento.getSize()});
        }
    }

    public LinkedList<Elemento> getElementos() {
        return this.elementos;
    }

    public LinkedList<double[]> getInputFormat() {
        return this.inputFormat;
    }
 
}
