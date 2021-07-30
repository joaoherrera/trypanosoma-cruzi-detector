package Main_Package.Modelling;

import Adaptacao.Cell;
import Adaptacao.Elemento;
import java.util.LinkedList;

/**
 * @date 20/08/2014
 * @author João
 * 
 * Utilizada para detectar aglomerações nos parasitas
 */

public class InputDataSetRow_AGL{
    private final LinkedList<Cell> nucleos;
    private final LinkedList<double[]> inputFormat;
    
    public InputDataSetRow_AGL(LinkedList<Cell> nucleos) {
        this.nucleos = nucleos;
        this.inputFormat = new LinkedList<>();
    }

    public void formatInput() {
        if(this.nucleos.isEmpty()) return;
        
        for(Cell nucleoCelular : this.nucleos){
            this.inputFormat.add(new double[]{(double) nucleoCelular.getCorMedia().getRed(), 
                                              (double) nucleoCelular.getCorMedia().getGreen(),
                                              (double) nucleoCelular.getCorMedia().getBlue(),
                                              (double) nucleoCelular.getSize()});
        }
    }

    public LinkedList<Cell> getNucleos() {
        return this.nucleos;
    }

    public LinkedList<double[]> getInputFormat() {
        return this.inputFormat;
    }
}
