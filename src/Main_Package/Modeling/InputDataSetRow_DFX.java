package Main_Package.Modeling;

import Adaptacao.Elemento;
import java.util.LinkedList;

/**
 * @data13/08/2014
 * @author João
 * 
 * Utilizada para detectar defeitos na imagem que não são citoplasmas, nem células ou parasitas
 */

public class InputDataSetRow_DFX extends InputDataSetRow{

    public InputDataSetRow_DFX(LinkedList<Elemento> elementos) {
        super(elementos);
    }

    @Override
    public void formatInput() {
        if(this.elementos.isEmpty()) return;
        
        for(Elemento elemento : this.elementos){
            this.inputFormat.add(new double[]{(double) elemento.getCorMedia().getRed(), 
                                              (double) elemento.getCorMedia().getGreen(),
                                              (double) elemento.getCorMedia().getBlue()});
        }
    }
}
