package Adaptacao;

import Main_Package.ImageProperties;
import java.util.LinkedList;

/**
 * @date: 15/09/2014
 * @author João
 */

public class Metricas {
    
    public static Citoplasma distance(Celula celula, LinkedList<Citoplasma> citoplasmas){
        int[] coordenadas = {celula.getNucleo().getPixels().getFirst().getXcoordenate(), celula.getNucleo().getPixels().getFirst().getYcoordenate()};
        
        for(Citoplasma citoplasma : citoplasmas){
            for(Pixel pix : citoplasma.getPixels()){
                if(pix.getXcoordenate() == coordenadas[0] && pix.getYcoordenate() == coordenadas[1]){
                    return citoplasma;
                }
            }
        }
        
        return null;
    }
    
    public static Parasitas distance(Celula celula, Parasitas parasita){
        int[] menorDist = {0,0};
        int[] curDist;
        Parasitas menorDistancia = null;
        
        for(int i=0; i<celula.getParasitas().size(); i++){
            curDist = Metricas.distance(celula.getParasitas().get(i), parasita);
            
            if(i == 0){
                menorDist = curDist;
                menorDistancia = celula.getParasitas().get(i);
            }
            
            if((curDist[0] + curDist[1]) < (menorDist[0] + menorDist[1])){
                menorDist = curDist;
                menorDistancia = celula.getParasitas().get(i);
            }
        }
        
        return menorDistancia;
    }
    
    public static int[] distance(Cell cell, Parasitas parasita){
        int[] coordenate = new int[2];
        int diffX = 0;
        int diffY = 0;
        int diffXX = 0;
        int diffXY = 0;
        
        for(Pixel pixCell : cell.getPixels()){
            if(diffX == 0 && diffY == 0){
                diffX = pixCell.getXcoordenate() > parasita.getPixels().getFirst().getXcoordenate() ? pixCell.getXcoordenate() - parasita.getPixels().getFirst().getXcoordenate() : parasita.getPixels().getFirst().getXcoordenate() - pixCell.getXcoordenate();
                diffY = pixCell.getYcoordenate() > parasita.getPixels().getFirst().getYcoordenate() ? pixCell.getYcoordenate() - parasita.getPixels().getFirst().getYcoordenate() : parasita.getPixels().getFirst().getYcoordenate() - pixCell.getYcoordenate();            
            }
            
            for(Pixel pixParasita : parasita.getPixels()){
                diffXX = pixCell.getXcoordenate() > pixParasita.getXcoordenate() ? pixCell.getXcoordenate() - pixParasita.getXcoordenate() : pixParasita.getXcoordenate() - pixCell.getXcoordenate();
                diffXY = pixCell.getYcoordenate() > pixParasita.getYcoordenate() ? pixCell.getYcoordenate() - pixParasita.getYcoordenate() : pixParasita.getYcoordenate() - pixCell.getYcoordenate();
                
                if((diffXX + diffXY) < (diffX + diffY)){
                    diffX = diffXX;
                    diffY = diffXY;
                }
            }
        }
        coordenate[0] = diffX;
        coordenate[1] = diffY;
        
        return coordenate;
    }
    
    public static int[] distance(Parasitas parasita1, Parasitas parasita2){
        int[] coordenate = new int[2];
        int diffX = 0;
        int diffY = 0;
        int diffXX = 0;
        int diffXY = 0;
        
        for(Pixel pixParasita1 : parasita1.getPixels()){
            if(diffX == 0 && diffY == 0){
               diffX = pixParasita1.getXcoordenate() > parasita2.getPixels().getFirst().getXcoordenate() ? pixParasita1.getXcoordenate() - parasita2.getPixels().getFirst().getXcoordenate() : parasita2.getPixels().getFirst().getXcoordenate() - pixParasita1.getXcoordenate();
               diffY = pixParasita1.getYcoordenate() > parasita2.getPixels().getFirst().getYcoordenate() ? pixParasita1.getYcoordenate() - parasita2.getPixels().getFirst().getYcoordenate() : parasita2.getPixels().getFirst().getYcoordenate() - pixParasita1.getYcoordenate();             
            }
            
            for(Pixel pixParasita : parasita2.getPixels()){
                diffXX = pixParasita1.getXcoordenate() > pixParasita.getXcoordenate() ? pixParasita1.getXcoordenate() - pixParasita.getXcoordenate() : pixParasita.getXcoordenate() - pixParasita1.getXcoordenate();
                diffXY = pixParasita1.getYcoordenate() > pixParasita.getYcoordenate() ? pixParasita1.getYcoordenate() - pixParasita.getYcoordenate() : pixParasita.getYcoordenate() - pixParasita1.getYcoordenate();
                
                if((diffXX + diffXY) < (diffX + diffY)){
                    diffX = diffXX;
                    diffY = diffXY;
                }
            }
        }
        coordenate[0] = diffX;
        coordenate[1] = diffY;
        
        return coordenate;
    }
    
    // Calcula a distancia provável de um parasita com a borda da imagem.
    // servirá para alocar parasitas perdidos no meio da imagem, caso a distância entre o núcleo < distancia entre a borda.
    // o valor é estimado com apenas um pixel do parasita.
    public static int[] distanceFromEdge(Parasitas parasito, ImageProperties curImg){
        int[] coordenates = new int[2];
                
        //Ponto de partida
        int menorHeight = parasito.getPixels().getFirst().getXcoordenate();
        int menorWidth  = parasito.getPixels().getFirst().getYcoordenate();
        
        coordenates[0] = menorHeight < (curImg.getImage().getHeight()/2) ? menorHeight : curImg.getImage().getHeight() - menorHeight;
        coordenates[1] = menorWidth < (curImg.getImage().getWidth()/2) ? menorWidth : curImg.getImage().getWidth()- menorWidth;
        
        
        return coordenates;
    }
}
