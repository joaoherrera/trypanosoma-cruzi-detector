package Adaptacao;

import java.util.LinkedList;

/**
 * @data 16/07/2014
 * @author João
 */

public class Celula {
    private Cell nucleo;
    private LinkedList<Parasitas> parasitas;
    
    public Celula(Cell nucleo){
        this.nucleo = nucleo;
        this.parasitas = new LinkedList<>();
    }

    public Cell getNucleo() {
        return nucleo;
    }

    public LinkedList<Parasitas> getParasitas() {
        return parasitas;
    }

    public void setNucleos(Cell nucleo) {
        this.nucleo = nucleo;
    }

    public void setParasitas(LinkedList<Parasitas> parasitas) {
        this.parasitas = parasitas;
    }
}
