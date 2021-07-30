package Adaptacao;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * @data 06/23/2013
 * @author João
 * 
 * Classe que implementa metodos de ordenacao de dados
 */
public class DataSort implements Comparator<Pixel>{
    
    public void herreraSort(int[] array){
        final int SIZE = 10;
        
        int modulo = -1;
        int menor = 0;
        int index = 0;
        int seq = 0;
        
        LinkedList[] algarismos = new LinkedList[SIZE];
        
        for(int i=0; i<SIZE; i++){
            algarismos[i] = new <Integer> LinkedList();
        }
        
        for(int k=0; k<array.length; k++){
            modulo = array[k]%10;
            
            if(modulo != (-1)){
                algarismos[modulo].add(array[k]);
                modulo = -1;
            }
        }

        for(LinkedList elemento : algarismos){
            Collections.sort(elemento);
        }
        
        for(int j=0; j<array.length; j++){
            
            for(int i=0; i<SIZE; i++){
                if(!algarismos[i].isEmpty()){
                    menor = (int) algarismos[i].getFirst();
                    index = 0;
                    break;
                }
            }
                       
            for(int k=0; k<SIZE; k++){
                if(!algarismos[k].isEmpty()){
                    if(menor >= (int) algarismos[k].getFirst()){
                        menor = (int) algarismos[k].getFirst();
                        index = k;
                    }
                }
            }

            seq = 0;
            while(!algarismos[index].isEmpty()){
                if(!((int) algarismos[index].getFirst() == menor)){
                    break;
                }
                array[j+seq++] = (int) algarismos[index].removeFirst();
            }
            j += seq > 0 ? seq - 1 : seq;
        }
    }
    
    public void doCollectionsSort(int[] array){
        LinkedList collection = new <Integer> LinkedList();
        int index = 0;
        
        collection.add(array);
        Collections.sort(collection);
        
        for(int i=0; i<array.length; i++){
            array[index++] = (int) collection.getFirst();
        }
    }

    @Override
    public int compare(Pixel first, Pixel second) {
        if(first.getValue() == second.getValue())
            return 0;
        else if(first.getValue() > second.getValue())
            return 1;
        else
            return -1;
    }
    
}
