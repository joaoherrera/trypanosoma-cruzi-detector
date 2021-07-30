package Main_Package;

import Adaptacao.DataSort;
import Adaptacao.Pixel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @date 05/12/2013
 * @author joao
 * 
 * Classe responsável para as técnicas de Processamento Digital de Imagens
 */

public class ImageProcessing {
    private int WIDTH;
    private int HEIGHT;
    private DataStructure data;
    
    public ImageProcessing(DataStructure ds){
        this.data   = ds;
        this.WIDTH  = ds.getImageBuffer().getWidth();
        this.HEIGHT = ds.getImageBuffer().getHeight();
    }
    
    public ImageProcessing(){}
    
    /*==========================================================================================*
     *  Aloca valor 0 para as três matrizes binárias.                                           *
     *==========================================================================================*/
    
    public void AlocateBufferBinaries(){
        this.data.setRedBinaryPixels(new byte[this.WIDTH][this.HEIGHT]);
        this.data.setGreenBinaryPixels(new byte[this.WIDTH][this.HEIGHT]);
        this.data.setBlueBinaryPixels(new byte[this.WIDTH][this.HEIGHT]);
        
        for(int h=0; h < this.WIDTH; h++){
            for(int q=0; q < this.HEIGHT; q++){
                this.data.getRedBinaryPixels()[h][q] = 0;
                this.data.getGreenBinaryPixels()[h][q] = 0;
                this.data.getBlueBinaryPixels()[h][q] = 0;
            }
        }
    }
    
    /*==========================================================================================*
     * Retorna boolean para alterações de uma nova banda não alterar dados de uma outra banda já*
     * customizada.                                                                             *
     *==========================================================================================*/
    
    private boolean getComputeArrayBinary(int x, int y, int banda){
        switch(banda){
            case 0:
                return ((this.data.getGreenBinaryPixels()[x][y] + this.data.getBlueBinaryPixels()[x][y]) > 0);
                
            case 1:
                return ((this.data.getRedBinaryPixels()[x][y] + this.data.getBlueBinaryPixels()[x][y]) > 0);
                
            case 2:
                return ((this.data.getRedBinaryPixels()[x][y] + this.data.getGreenBinaryPixels()[x][y]) > 0);
        }        
        return false;
    }
    
    /*==========================================================================================*
     * Método que transforma um valor customizado em 0 ou 1 (preto ou branco).                  *
     *==========================================================================================*/
    
    private void computeArrayBinary(int x, int y, int banda, boolean option){
        switch(banda){
            case 0:
                this.data.getRedBinaryPixels()[x][y]   = (option == true ? (byte) 1 :(byte) 0);
                break;
                
            case 1:
                this.data.getGreenBinaryPixels()[x][y] = (option == true ? (byte) 1 :(byte) 0);
                break;
                
            case 2:
                this.data.getBlueBinaryPixels()[x][y]  = (option == true ? (byte) 1 :(byte) 0);
                break;
                
            default:
                break;
        }
    }
    
    /*==========================================================================================*
     * Exclusão de pixels com vermelhos de acordo com valores passados por parâmetros.          *
     *==========================================================================================*/
    
    /*
        Alteraçao 27/04/2014:
        if(red <= x || red >= y)
        if((red == Color.BLACK.getRed()) && (originalRed != red) && (originalRed > x && originalRed < y))
    */
    
    public void manualRedPixels(int x, int y){
        int red;
        int originalRed;
            
        for(int width=0; width<this.WIDTH; width++){
            for(int height=0; height<this.HEIGHT; height++){     
                red = (this.data.getImageBuffer().getRGB(width, height)&0xff0000) >> 16; //Pegando o vermelho do valor RGB de cada pixel
                originalRed = (this.data.getImagePixels()[width][height]&0xff0000) >> 16; // pegando o vermelho do valor RGB de um vetor com os tons iniciais
                    
                if(red < x || red > y){ //se o valor de vermelho do pixel não estiver entre x ou y 
                    this.data.getImageBuffer().setRGB(width, height, Color.BLACK.getRed());                  //pintar de preto 
                    this.computeArrayBinary(width, height, 0, true);
                }  
                if((red == Color.BLACK.getRed()) && (originalRed >= x && originalRed <= y)){ //recuo do slider 
                    this.computeArrayBinary(width, height, 0, false);
                    if(this.getComputeArrayBinary(width, height, 0) == false){
                        this.data.getImageBuffer().setRGB(width, height, this.data.getImagePixels()[width][height]); //se a cor é preta e esta entre x e y, coloque a cor original
                    }
                }
            } 
        }         
    }
    
    /*===========================================================================================*
     * Exclusão de pixels com verde de acordo com valores passados por parâmetros.              *
     *==========================================================================================*/
    
    public void manualGreenPixels(int x, int y){
        int green;
        int originalGreen;
            
        for(int width=0; width<this.WIDTH; width++){
            for(int height=0; height<this.HEIGHT; height++){     
                green = (this.data.getImageBuffer().getRGB(width, height)&0x00ff00) >> 8;
                originalGreen = (this.data.getImagePixels()[width][height]&0x00ff00) >> 8;
                    
                if(green < x || green > y){
                    this.data.getImageBuffer().setRGB(width, height, Color.BLACK.getRed());
                    this.computeArrayBinary(width, height, 1, true);
                }  
                if((green == Color.BLACK.getRed()) && (originalGreen >= x && originalGreen <= y)){  
                     this.computeArrayBinary(width, height, 1, false);
                    if(this.getComputeArrayBinary(width, height, 1) == false){
                        this.data.getImageBuffer().setRGB(width, height, this.data.getImagePixels()[width][height]);
                    }
                }
            }               
        }   
    }
    
    /*==========================================================================================*
     * Exclusão de pixels com azul de acordo com valores passados por parâmetros.               *
     *==========================================================================================*/
    
    public void manualBluePixels(int x, int y){
        int blue;
        int originalBlue;
            
        for(int width=0; width<this.WIDTH; width++){
            for(int height=0; height<this.HEIGHT; height++){     
                blue = (this.data.getImageBuffer().getRGB(width, height)&0x0000ff);
                originalBlue = (this.data.getImagePixels()[width][height]&0x0000ff);
                    
                if(blue < x || blue > y){
                   this.data.getImageBuffer().setRGB(width, height, Color.BLACK.getBlue());
                   this.computeArrayBinary(width, height, 2, true);
                }
                if((blue == Color.BLACK.getBlue()) && (originalBlue >= x && originalBlue <= y)){ 
                   this.computeArrayBinary(width, height, 2, false);
                   if(this.getComputeArrayBinary(width, height, 2) == false){ 
                      this.data.getImageBuffer().setRGB(width, height, this.data.getImagePixels()[width][height]);
                   }
                }
            }               
        }   
    }
    
    /*==========================================================================================*
     | Transforma a imagem em tons de cinza.                                                    |
     *==========================================================================================*/
        
    public void grayScale(BufferedImage imageBuffer){
        int red;
        int green;
        int blue;
        
        Color grayScale;
        
        for(int x=0; x<imageBuffer.getWidth(); x++){
            for(int y=0; y<imageBuffer.getHeight(); y++){                               
                red   = (imageBuffer.getRGB(x, y)&0xff0000) >> 16;
                green = (imageBuffer.getRGB(x, y)&0x00ff00) >> 8;
                blue  = (imageBuffer.getRGB(x, y)&0x0000ff);
                
                grayScale = new Color((red + green + blue)/3, (red + green + blue)/3, (red + green + blue)/3);
                
                imageBuffer.setRGB(x, y, grayScale.getRGB());
            }
        }
    }
    
    /*==========================================================================================*
     * Limiar de 128.                                                                           *
     *==========================================================================================*/
    
    public void createLimiar(){
        for(int i=0; i<this.WIDTH; i++){
            for(int j=0; j<this.HEIGHT; j++){
                if((this.data.getImageBuffer().getRGB(i, j)&0xff0000) == (Color.BLACK.getRed())){
                    this.data.getImageBuffer().setRGB(i, j, Color.BLACK.getRGB());
                }
                else {
                    this.data.getImageBuffer().setRGB(i, j, Color.WHITE.getRGB());
                }
            }
        }
    }
    
   /*==========================================================================================*
    * Transforma um valor de nível variavel entre 0 e 255 para escala de cinza                 *
    *==========================================================================================*/
    
    public int level_to_greyscale(int level) {
        return (level << 16) | (level << 8) | level;
    }
    
   /*==========================================================================================*
    * Retorna um valor de cor através da junção dos valores rgb                                *
    *==========================================================================================*/
    public int lum(int r, int g, int b) {
        return (r + r + r + b + g + g + g + g) >> 3;
    }
 
    public int rgb_to_luminance(int rgb) {
        int r = (rgb & 0xff0000) >> 16;
        int g = (rgb & 0x00ff00) >> 8;
        int b = (rgb & 0x0000ff);

        return lum(r, g, b);
    }
    
   /*==========================================================================================*
    * Algoritmo de Segmentação de Sobel                                                         *
    *==========================================================================================*/
    public void sobelEdgeDetection(BufferedImage image) {
        int[][] sobelx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobely = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
    
        BufferedImage ret = DataStructure.cloneImage(image);
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int level = 255;
                if ((x > 0) && (x < (width - 1)) && (y > 0) && (y < (height - 1))) {
                    int sumX = 0;
                    int sumY = 0;
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            sumX += rgb_to_luminance(ret.getRGB(x+i, y+j)) * sobelx[i+1][j+1];
                            sumY += rgb_to_luminance(ret.getRGB(x+i, y+j)) * sobely[i+1][j+1];
                        }
                    }
                    level = Math.abs(sumX) + Math.abs(sumY);
                    if (level < 0) {
                        level = 0;
                    } else if (level > 255) {
                        level = 255;
                    }
                    level = 255 - level;
                }
                image.setRGB(x, y, level_to_greyscale(level));
            }
        }
    }
       
    public Pixel[][] watershed(BufferedImage image){
        final int HIGHEST = 256;    
        final int LOWEST  = 0;
        int[] sortedArray;     
        int background;
        int currentLabel = 0,
            currentDist = 0,
            _heightFirst = 0,
            _heightSecond = 0;

        LinkedList<Pixel> queue = new LinkedList();
        LinkedList<Pixel> sortedList = new LinkedList();
        Pixel[] arrayPixel;
        Pixel[][] matrixPixel;

        this.grayScale(image); // imagem em tons de cinza
        sortedArray = data.toArray(image); //passando para um array
        background = Statistics.medianBackground(sortedArray);
        sortedList.addAll(Arrays.asList(data.toArrayPixel(sortedArray, image.getWidth(), image.getHeight())));
        arrayPixel = sortedList.toArray(new Pixel[sortedList.size()]);
        matrixPixel = data.arrayPixelToMatrixPixel(arrayPixel, image.getWidth(), image.getHeight());
        Collections.sort(sortedList, new DataSort());
        arrayPixel = sortedList.toArray(new Pixel[sortedList.size()]);
        
        for(int level = LOWEST; level < HIGHEST; level++){ //nível geodésico
            for(int index = _heightFirst; index < arrayPixel.length; index++){
                if(arrayPixel[index].getValue() != level){
                    _heightFirst = index;
                    break;
                }
                arrayPixel[index].setLabel(Pixel.MASK);
                for(Pixel neighbor : data.findNeighbors(arrayPixel[index], matrixPixel)){
                    if(neighbor.getLabel() >= 0){
                        neighbor.setDistance(1);          
                        queue.addLast(neighbor);
                        break;
                    }
                }
            }
            
            currentDist = 1;
            queue.addLast(Pixel.GHOST);
            
            while(true){
                Pixel pixel = queue.removeFirst();
                if(pixel.getLabel() == Pixel.GHOST.getLabel()){
                    if(queue.isEmpty())
                        break;
                    else{
                        queue.addLast(Pixel.GHOST);
                        currentDist++;
                        pixel = queue.removeFirst();
                    } 
                }
                for(Pixel neighbor : data.findNeighbors(pixel, matrixPixel)){
                    if(neighbor.getDistance() <= currentDist && neighbor.getLabel() >= 0){
                        if(neighbor.getLabel() > 0){
                            if(pixel.getLabel() == Pixel.MASK){
                                pixel.setLabel(neighbor.getLabel());
                            }
                            else if(pixel.getLabel() != neighbor.getLabel()){
                                pixel.setLabel(Pixel.WSHED);  
                            }
                        }
                        else if(pixel.getLabel() == Pixel.MASK){
                            pixel.setLabel(Pixel.WSHED);
                        }
                    }
                    else if(neighbor.getLabel() == Pixel.MASK && neighbor.getDistance() == 0){
                        neighbor.setDistance(currentDist+1);
                        queue.addLast(neighbor);
                    }
                }
            }
            
            for(int index = _heightSecond; index < arrayPixel.length; index++){
                if(arrayPixel[index].getValue() != level){
                    _heightSecond = index;
                    break;
                }               
                arrayPixel[index].setDistance(0);
                
                if(arrayPixel[index].getLabel() == Pixel.MASK){
                    currentLabel++;
                    arrayPixel[index].setLabel(currentLabel);
                    queue.addLast(arrayPixel[index]);
                    
                    while(!queue.isEmpty()){
                        for(Pixel neighbor : data.findNeighbors(queue.removeFirst(), matrixPixel)){
                            if(neighbor.getValue() == Pixel.MASK){
                                neighbor.setLabel(currentLabel);
                                queue.addLast(neighbor);
                            }
                        }
                    }
                }               
            }
        }
        
        for(Pixel pixel : arrayPixel){
            if(pixel.getLabel() == Pixel.WSHED && ((data.hasNeighborsWatershed(pixel, matrixPixel) && data.hasNeighborsNatives(pixel, matrixPixel)) || (pixel.getValue() - 0) <= (background - pixel.getValue()))){
                pixel.setValue(-1);
                image.setRGB(pixel.getXcoordenate(), pixel.getYcoordenate(), Color.RED.getRGB());
            }
        }
        
        return matrixPixel;
    }
    
    // Identifica a seguimentaçao feita pelo método watershed()
    public void identificarSegmentacao(Pixel[][] segmentado){ 
        for(Pixel[] lines : segmentado){
            for(Pixel pixel : lines){
                if(pixel.getValue() == -1)
                    data.getImageBuffer().setRGB(pixel.getXcoordenate(), pixel.getYcoordenate(), Color.ORANGE.getRGB()); 
                else
                    data.getImageBuffer().setRGB(pixel.getXcoordenate(), pixel.getYcoordenate(), 
                                                 data.getImagePixels()[pixel.getXcoordenate()][pixel.getYcoordenate()]);
            }         
        }
    }
    
    //Utiliza o crescimento de regioes para a identificaçao de padroes
    public LinkedList<Pixel> regionGrowing(int x, int y, int limiarX, int limiarY){
        Pixel      init   = new Pixel(-1, 0, data.getImageBuffer().getRGB(x, y), x, y);
        Pixel[][]  matrix = data.bufferedToMatrix();       
        Collection<Pixel> pixels;        
        LinkedList<Pixel> processed = new LinkedList<>();
        
        int rgbViz;
        int rgbXY = this.rgb_to_luminance(this.data.getImageBuffer().getRGB(x, y)); 
        
        //Idenficar os pixels vizinhos do ponto escolhido
        pixels = data.findNeighbors(init, matrix);
        
        //Verificar se esse esse vizinho possui os pixels dentro do limiar estabelecido      
        while(pixels.iterator().hasNext()){
            Pixel pix = pixels.iterator().next();
            pixels.remove(pix);
             
            rgbViz = this.rgb_to_luminance(this.data.getImageBuffer().getRGB(pix.getXcoordenate(), pix.getYcoordenate()));

            if(rgbViz > (rgbXY - limiarX) && rgbViz < (rgbXY + limiarY)){                 
            //this.getData().getImageBuffer().setRGB(pix.getXcoordenate(), pix.getYcoordenate(), new Color(255, 150, 100, 99).getRGB());
            
                for(Pixel vizinhos : data.findNeighbors(pix, matrix)){
                    if(vizinhos.getLabel() != 77){
                       pixels.add(vizinhos);
                       vizinhos.setLabel(77);                    
                    }                 
                }
                processed.add(pix);      
            }        
        }

        return processed;
    }
    
    //Pinta objetos cor a cor especificada
    public void paintElements(Object element, int rgb){
        
        //Implementação para LinkedList com Pixels
        if(element.getClass().equals(LinkedList.class)){
           LinkedList<Pixel> list = (LinkedList<Pixel>) element;
           
           for(Pixel pixel : list){
               this.data.getImageBuffer().setRGB(pixel.getXcoordenate(), pixel.getYcoordenate(), rgb);
           }
        }
    }
    
    //Backup para voltar as alterações feitas no trinamento
    public void paintElementsBackup(LinkedList<Pixel> element){         
           for(Pixel pixel : element){
               this.data.getImageBuffer().setRGB(pixel.getXcoordenate(), pixel.getYcoordenate(), pixel.getValue());
           }      
    }
    
    //Identifica a cor média de um conj. de pixels
    public Color findMedColor(LinkedList<Pixel> conjPix){
        int red = 0;
        int green = 0;
        int blue = 0;
        
        for(Pixel pix : conjPix){
            Color cor = new Color(pix.getValue());
            
            red   += cor.getRed();
            green += cor.getGreen();
            blue  += cor.getBlue();        
        }
        
        return new Color(red/conjPix.size(), green/conjPix.size(), blue/conjPix.size());
    }
 
    public void erode(Pixel[][] imgPixel){
        LinkedList<Pixel> vizinhos;
        
        for(Pixel[] line : imgPixel){
            for(Pixel pix : line){
                if(pix.getValue() == Color.BLACK.getRGB()){
                    vizinhos = this.data.findNeighbors(pix, imgPixel);
                    for(Pixel viz : vizinhos){
                        if(viz.getValue() == Color.WHITE.getRGB() && viz.getLabel() != 9){
                            pix.setLabel(9);
                            pix.setValue(Color.WHITE.getRGB());
                            this.data.getImageBuffer().setRGB(pix.getXcoordenate(), pix.getYcoordenate(), Color.WHITE.getRGB());
                        }
                    }
                }
            }
        }
        
        this.data.setPixelBuffer(imgPixel);
    }
    
    public void makeSmooth(Pixel[][] imgPix){
        int media = 0;
        int loop  = 0;
        
        for(Pixel[] line : imgPix){
            for(Pixel pix : line){
                for(Pixel p : this.data.findNeighbors(pix, imgPix)){
                    media += p.getValue();
                    loop++;
                }
                
                this.data.getImageBuffer().setRGB(pix.getXcoordenate(), pix.getYcoordenate(), media/loop);
                media = loop = 0;
            }
        }
    }
    
    //Média do citoplasma na banda Red.
    public int meanRed(BufferedImage curImg){
        int mean = 0;
        
        for(int i=0; i<curImg.getWidth(); i++){
            for(int j=0; j<curImg.getHeight(); j++){
                mean += (curImg.getRGB(i, j)&0xff0000) >> 16;
            }
        }
        
        return mean/(curImg.getWidth()*curImg.getHeight());
    }
    
    //Média do citoplasma na banda Green.
    public int meanGreen(BufferedImage curImg){
        int mean = 0;
        
        for(int i=0; i<curImg.getWidth(); i++){
            for(int j=0; j<curImg.getHeight(); j++){
                mean += (curImg.getRGB(i, j)&0x00ff00) >> 8;
            }
        }
        
        return mean/(curImg.getWidth()*curImg.getHeight());
    }
    
    public DataStructure getData() {
        return data;
    }

    public void setData(DataStructure data) {
        this.data = data;
    }
 
}
