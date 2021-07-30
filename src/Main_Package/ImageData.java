package Main_Package;

import Adaptacao.Celula;
import java.util.LinkedList;

/**
 * @data 15/07/2014
 * @author João
 * 
 * Guarda Info. sobre o conteúdo da imagem relevante ao estudo
 */

public class ImageData {
    private LinkedList<Celula> celulas;
    
    public ImageData(){
        this.celulas = new LinkedList<>();
    }

    public LinkedList<Celula> getCelula() {
        return celulas;
    }

    public void setCelulas(LinkedList<Celula> celulas) {
        this.celulas = celulas;
    }
}
