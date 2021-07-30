package Main_Package;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author João
 * @date 24/09/2013
 */
public class Statistics {
    
    public static int medianBackground(int[] grayScale){
        int soma = 0;
        
        for(int pixel : grayScale){
            if(pixel != 0) //se não é uma marcação, então entra como background
                soma += pixel;
        }
    
        return soma/grayScale.length;
    }
    
    
    
}
