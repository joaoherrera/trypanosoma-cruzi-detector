/*
 * Representa um elemento físico para o programa.
 */

package Adaptacao;

import java.awt.Color;
import java.util.LinkedList;

/**
 * @date 01/05/2014 
 * @Modificação: 17/06/2014
 * 
 * @author João
 */

public class Elemento{
    private LinkedList<Pixel> pixels;
    private Color corMedia;
    
    public Elemento(LinkedList<Pixel> pixels){
        this.pixels = pixels;
    }
    
    public Elemento(){}
    
    
    public int getSize(){
        return pixels.size();
    }

    public LinkedList<Pixel> getPixels() {
        return this.pixels;
    }

    public void setPixels(LinkedList<Pixel> pixels) {
        this.pixels = pixels;
    }

    public Color getCorMedia() {
        return this.corMedia;
    }

    public void setCorMedia(Color corMedia) {
        this.corMedia = corMedia;
    }        
}
