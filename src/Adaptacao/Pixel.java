package Adaptacao;

/**
 * @date 2013/06/23
 * @author João
 * 
 * Classe que representa pixels
 */

public class Pixel{
    private int  label;
    private int  distance;
    private int  value;
    private int  Xcoordenate;
    private int  Ycoordenate;
    
    public static final Pixel GHOST = new Pixel(-3,0,0,0,0);
    public static final int INIT    = -1;
    public static final int MASK    = -2;
    public static final int WSHED   =  0;

    public Pixel(int label, int distance, int value, int Xcoordenate, int Ycoordenate) {
        this.label = label;
        this.distance = distance;
        this.value = value;
        this.Xcoordenate = Xcoordenate;
        this.Ycoordenate = Ycoordenate;
    }
    
    public Pixel(int label){
        this.label = label;
    }
    
    public boolean isWatershed(){
        return this.label == 0;
    }
    
    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getXcoordenate() {
        return Xcoordenate;
    }

    public void setXcoordenate(int Xcoordenate) {
        this.Xcoordenate = Xcoordenate;
    }

    public int getYcoordenate() {
        return Ycoordenate;
    }

    public void setYcoordenate(int Ycoordenate) {
        this.Ycoordenate = Ycoordenate;
    }  
    
}
