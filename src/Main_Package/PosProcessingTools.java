package Main_Package;

import Adaptacao.Cell;
import Adaptacao.Celula;
import Adaptacao.Citoplasma;
import Adaptacao.Metricas;
import Adaptacao.Parasitas;
import Adaptacao.Pixel;
import Main_Package.Modelling.IOFunctions;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * @data 15/07/2014
 * @author João
 * 
 * To do: Eliminar os defeitos da imagem (pesquisar sobre métodos que possam ser usados) x
 *        Implementar o Progressbar x
 *        Distribuir melhor citoplasmas que envolvem duas ou mais celulas.
 *        Detectar aglomeração de células e parasitas 
 *        Determinar o numero de células/parasitas em cada aglomerado. x
 */

public class PosProcessingTools {
    private final IOFunctions outputInfo;
    private LinkedList<Citoplasma> citoplasmas;
    
    public PosProcessingTools(){
        DateFormat curFormatDate  = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date       curDate        = new Date();
        this.outputInfo = new IOFunctions("rst/"+curFormatDate.format(curDate)+".trst");
    }
    
    //Subtrai elementos das imagens restando apenas o background e o citoplasma
    public void subtract(ImageProperties curImg, LinkedList<Cell> nucleos, LinkedList<Parasitas> parasitas){
        Pixel[][] diff = newArrayPixel(curImg.getImageTools().getImageDataStructure().getPixelBuffer(), curImg.getImage().getWidth(), curImg.getImage().getHeight());
        
        for(Cell celulas : nucleos){
            for(Pixel pix : celulas.getPixels()){
                diff[pix.getXcoordenate()][pix.getYcoordenate()].setValue(Color.WHITE.getRGB());
            }
        }
        
        for(Parasitas parasit : parasitas){
            for(Pixel pix : parasit.getPixels()){
                diff[pix.getXcoordenate()][pix.getYcoordenate()].setValue(Color.WHITE.getRGB());
            }
        }
        
        //Apenas para testes
        for(Pixel[] line : diff){
            for(Pixel pix : line){
                curImg.getImage().setRGB(pix.getXcoordenate(), pix.getYcoordenate(), pix.getValue());
            }
        }
    }
    
    private Pixel[][] newArrayPixel(Pixel[][] oldArrayPixel,int width,int height){
        Pixel[][] newArrayPixel = new Pixel[width][height];
        
        for(Pixel[] line : oldArrayPixel){
            for(Pixel pix : line){
                newArrayPixel[pix.getXcoordenate()][pix.getYcoordenate()] = new Pixel(0, 0, pix.getValue(), pix.getXcoordenate(), pix.getYcoordenate());
            }
        }
        
        return newArrayPixel;
    }
    
    public void gravarIdentifacao(ImageProperties curImg){
        String imgName      = curImg.getFileName();
        DateFormat curFormatDate  = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date curDate = new Date();
        String info         = this.outputInfo.ler();
        
        info += imgName + "," + curFormatDate.format(curDate) + "$";
        
        for(Celula cell : curImg.getImageTools().getImageData().getCelula()){
            info += cell.getParasitas().size() + ",";
        }
        
        info += System.lineSeparator();
        this.outputInfo.gravar(info);
    }
    
    //Responsável por identificar a relação entre celulas e parasitas por meio do citoplasma
    public void relationByCitoplasma(ImageProperties curImg, LinkedList<Cell> nucleos, LinkedList<Parasitas> parasitas){
        LinkedList<LinkedList<Pixel>> fronteira  = new LinkedList<>();
        LinkedList<Celula> celulas   = new LinkedList<>();
        LinkedList<LinkedList<Pixel>> pixelBuffer = new LinkedList<>();
        LinkedList<Parasitas> toRemove = new LinkedList<>();

        final int checked   = 39; //mascara para elementos já processados.
        final int meanRed   = curImg.getImageTools().getImageProcessing().meanRed(curImg.getImage());
        final int meanGreen = curImg.getImageTools().getImageProcessing().meanGreen(curImg.getImage());
        
        int red;
        int green;
        
        for(Cell nucleo : nucleos){
            celulas.add(new Celula(nucleo));
            pixelBuffer.add(new LinkedList<Pixel>());
            fronteira.add(new LinkedList<Pixel>());
        }
        
        //Inicialmente é preciso identificar os pixels da borda do núcleo.
        Pixel[][] matrizPixel = curImg.getImageTools().getImageDataStructure().getPixelBuffer();
        
        for(int i=0; i<nucleos.size(); i++){
            for(Pixel pix : nucleos.get(i).getPixels()){
                if(curImg.getImageTools().getImageDataStructure().findNeighborsFromElement(pix, nucleos.get(i).getPixels()).size() < 8){
                    pix.setLabel(checked);
                    pixelBuffer.get(i).add(pix);
                }
            }
        }
        
        do{
            for(int i=0; i<pixelBuffer.size(); i++){
                for(Pixel pix : pixelBuffer.get(i)){
                    for(Pixel vzPix : curImg.getImageTools().getImageDataStructure().findNeighbors(pix, matrizPixel)){
                        if(vzPix.getLabel() != checked){
                            red   = (curImg.getImageTools().getImageDataStructure().getPixelBuffer()[vzPix.getXcoordenate()][vzPix.getYcoordenate()].getValue() &0xff0000) >> 16;
                            green = (curImg.getImageTools().getImageDataStructure().getPixelBuffer()[vzPix.getXcoordenate()][vzPix.getYcoordenate()].getValue() &0x00ff00) >> 8;

                            if(red < meanRed && green < meanGreen){
                                //Verificar se o pixel faz parte de um prasita
                                for(Parasitas parasitos : parasitas){
                                    for(Pixel pPix : parasitos.getPixels()){
                                        if(vzPix.getXcoordenate() == pPix.getXcoordenate() &&  vzPix.getYcoordenate() == pPix.getYcoordenate()){
                                            for(int j=0; j<celulas.size(); j++){
                                                if(celulas.get(j).getParasitas().contains(parasitos)){
                                                    break;
                                                }
                                                if(j == celulas.size()-1){
                                                    for(Celula celula : celulas){
                                                        if(celula.getNucleo() == nucleos.get(i)){
                                                            celula.getParasitas().add(parasitos);                                                            
                                                            toRemove.add(parasitos);
                                                            //parasitas.remove(parasitos);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                vzPix.setLabel(checked);
                                fronteira.get(i).add(vzPix);
                                //curImg.getImage().setRGB(vzPix.getXcoordenate(), vzPix.getYcoordenate(), new Color(228, 228, 228).getRGB());
                            }
                        }
                    }
                }
            }
           
            for(int i=0; i<pixelBuffer.size(); i++){
                pixelBuffer.get(i).removeAll(pixelBuffer.get(i));
                pixelBuffer.get(i).addAll(fronteira.get(i));
                fronteira.get(i).removeAll(fronteira.get(i));
            }
            
        }while(this.hasData(pixelBuffer));
        parasitas.removeAll(toRemove);
        curImg.getImageTools().getImageData().setCelulas(celulas);
        //this.lostParasites(parasitas, curImg);
    }
    
    //Realoca parasitas que estejam longe da borda, que não conseguiram ser alocados devido à má formação do citoplasma.
    private void lostParasites(LinkedList<Parasitas> lostParasitos, ImageProperties curImg){
        int[] edgeDist;
        int[] cellDist;
        int[] winnerCore = new int[2];
        
        Celula winnerCell;
        
        for(int i=0; i<lostParasitos.size(); i++){
            if(lostParasitos.get(i).getPixels().size() > 0){
                edgeDist = Metricas.distanceFromEdge(lostParasitos.get(i), curImg); 
                winnerCore[0] = curImg.getImage().getWidth();
                winnerCore[1] = curImg.getImage().getHeight();
                winnerCell = new Celula(null);

                for(Celula celula : curImg.getImageTools().getImageData().getCelula()){
                    cellDist = Metricas.distance(celula.getNucleo(), lostParasitos.get(i));

                    if(cellDist[0] < edgeDist[0] && cellDist[1] < edgeDist[1] && cellDist[0] < winnerCore[0] && cellDist[1] < winnerCore[1]){
                        winnerCore = cellDist;
                        winnerCell = celula;
                    }
                }

                if(winnerCell.getNucleo() != null && !winnerCell.getParasitas().contains(lostParasitos.get(i))){
                    winnerCell.getParasitas().add(lostParasitos.get(i));
                }
            }
        }
    }    
    
    //Identifica os citoplasmas da imagem e os transforma como objetos manipuláveis.    
    private void initiateCitoplasma(ImageProperties curImg){
        LinkedList<Pixel> joinPixels = new LinkedList<>();
        LinkedList<LinkedList<Pixel>> citop = new LinkedList<>();
        final int checked = 19;
        int listIndex = 0;
        
        for(Pixel[] pixelLine : curImg.getImageTools().getImageDataStructure().getPixelBuffer()){
            for(Pixel pix : pixelLine){
                if(pix.getLabel() == 39){
                    joinPixels.removeAll(joinPixels);
                    citop.add(new LinkedList<Pixel>());
                    joinPixels.add(pix);
                    pix.setLabel(checked);
          
                    while(!joinPixels.isEmpty()){
                        for(Pixel vzPix : curImg.getImageTools().getImageDataStructure().findNeighbors(joinPixels.getFirst(), curImg.getImageTools().getImageDataStructure().getPixelBuffer())){
                            if(vzPix.getLabel() == 39){
                                vzPix.setLabel(checked);
                                joinPixels.addLast(vzPix);
                            }
                        }
                        //curImg.getImage().setRGB(joinPixels.getFirst().getXcoordenate(), joinPixels.getFirst().getYcoordenate(), Color.ORANGE.getRGB());
                        citop.get(listIndex).add(joinPixels.removeFirst());
                    }
                    
                    listIndex++;
                }       
            }
        }
        
        this.citoplasmas = new LinkedList<>();
        
        for(LinkedList<Pixel> citPix : citop){
            this.citoplasmas.add(new Citoplasma(citPix));
        }
    }
    
    //Algumas identificações de citoplasma se juntaram, gerando assim um erro de associação.
    //Este método visa ajustar esse erro baseando-se na distância de outros parasitas,
    //ou seja, parasitas aglomerados são de uma determinada célula dentro de um citoplasma.
    //O algoritmo será executado apenas para citoplasma que atingiu duas ou mais células.
    public void relocateParasites(ImageProperties curImg){
        LinkedList<Celula> cellToStudy = new LinkedList<>();
        int[] menorDist = {0,0};
        int[] curDist = null;
        int[] cellDist;
        int[] parasitoDist;
        Celula    celulaInfluencia = null;
        Celula    cellInfluencia_bgnd = null;
        boolean gatilho;
      
        
        //Parte #1 - Percorrer os citoplasmas para verificar se há mais de uma célula.
        this.initiateCitoplasma(curImg);
        if(this.citoplasmas == null){
            return;
        }
        
        for(Citoplasma citop : this.citoplasmas){
            cellToStudy.removeAll(cellToStudy);

          
            for(Celula celula : curImg.getImageTools().getImageData().getCelula()){
                if(Metricas.distance(celula, this.citoplasmas) == citop){
                    cellToStudy.add(celula);
                }
            }
            
            //Parte #2 - Agora que temos as células que estão em um mesmo citoplasma, precisamos verificar o parasita que está mais perto do núcleo celular.
            //Pois é certeza que aquele parasita realmente pertence àquela célula.
            if(!cellToStudy.isEmpty()){
                for(int j=0; j<cellToStudy.size(); j++){

                    if(j == 0 && cellToStudy.get(j).getParasitas().size() > 0){
                        celulaInfluencia = cellToStudy.get(j);
                        curDist = Metricas.distance(cellToStudy.get(j).getNucleo(), cellToStudy.get(j).getParasitas().get(j));
                        menorDist = curDist;
                    }

                    for(int i=0; i<cellToStudy.get(j).getParasitas().size(); i++){
                        if(cellToStudy.get(j).getParasitas().size() > 0){
                            curDist = Metricas.distance(cellToStudy.get(j).getNucleo(), cellToStudy.get(j).getParasitas().get(i));
                        }

                        if((curDist[0] + curDist[1]) < (menorDist[0] + menorDist[1])){
                            celulaInfluencia = cellToStudy.get(j);
                            //parasitaInfluencia = celula.getParasitas().get(i);
                            menorDist = curDist;
                        }
                    }
                }

                if(celulaInfluencia != null){
                    cellInfluencia_bgnd = celulaInfluencia;//new Celula(new Cell(celulaInfluencia.getNucleo().getPixels()));
                    cellInfluencia_bgnd.setParasitas(new LinkedList<>(celulaInfluencia.getParasitas()));

                    //Parte #3 - O parasita que possui menor distância com sua célula influenciará as demais vizinhas, CASO a distância: PARASITA <-> PARASITA < CÉLULA <-> PARASITA
                    //Encontrar o parasita da celula de influência mais proximo do parasita a ser estudado
                    for(Celula celula : cellToStudy){
                        if(celula != celulaInfluencia){
                            for(Parasitas parasito : celula.getParasitas()){
                                parasitoDist = Metricas.distance(Metricas.distance(cellInfluencia_bgnd, parasito), parasito);
                                cellDist     = Metricas.distance(celula.getNucleo(), parasito);
                                
                                if((parasitoDist[0] + parasitoDist[1]) < (cellDist[0] + cellDist[1])){
                                    cellInfluencia_bgnd.getParasitas().add(parasito);
                                }
                            }
                        }
                    }

                    celulaInfluencia = cellInfluencia_bgnd;
                    
                    for(Celula celula : cellToStudy){
                        if(celula != cellInfluencia_bgnd){
                            for(Parasitas parasitos : cellInfluencia_bgnd.getParasitas()){
                                if(celula.getParasitas().contains(parasitos)){
                                    celula.getParasitas().remove(parasitos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void paintElementsDiffCollors(ImageProperties curImg){
        for(Celula celula : curImg.getImageTools().getImageData().getCelula()){
            Color currentColor = new Color((float) Math.random(),(float) Math.random(),(float) Math.random(), (float) 0.0);
            
            for(Pixel pix : celula.getNucleo().getPixels()){
                curImg.getImage().setRGB(pix.getXcoordenate(), pix.getYcoordenate(), currentColor.getRGB());                
            }
            
            for(Parasitas parasito : celula.getParasitas()){
                for(Pixel pix : parasito.getPixels()){
                    curImg.getImage().setRGB(pix.getXcoordenate(), pix.getYcoordenate(), currentColor.getRGB());
                }
            }
        }
    }
  
    private boolean hasData(LinkedList<LinkedList<Pixel>> fronteira){
        for(LinkedList<Pixel> list : fronteira){
            if(list.size()> 0){
                return true;
            }
        }
        return false;
    }
}
