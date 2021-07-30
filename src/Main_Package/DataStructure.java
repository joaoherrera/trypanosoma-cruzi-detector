package Main_Package;

import Adaptacao.Pixel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * @date 12/07/2013
 * @author João
 * Classe que envolve operações de estrutura de dados baseados em elementos de uma imagem.
 */
public class DataStructure{  
    private BufferedImage imageBuffer;
    private Pixel[][] pixelBuffer;
    private int[][]   imagePixels;
    private byte[][]  redBinaryPixels;
    private byte[][]  greenBinaryPixels;
    private byte[][]  blueBinaryPixels;
    
    public DataStructure(BufferedImage imageBuffer){
        this.imageBuffer = imageBuffer;
    }
    
    /*==========================================================================================*
     | Responsável por salvar uma cópia do buffer de imagem, que será editado com novas pro-    |
     | priedades.                                                                               |
     *==========================================================================================*/
    
    public int[][] getBackupImage(){
        int[][] auxArray = new int[this.imageBuffer.getWidth()][this.imageBuffer.getHeight()];
        
        for(int i=0; i<this.imageBuffer.getWidth(); i++){
            for(int j=0; j<this.imageBuffer.getHeight(); j++){
                auxArray[i][j] = this.imageBuffer.getRGB(i, j);
            }
        }
        return auxArray;
    }
    
    /*==========================================================================================*
     | Cria um BufferedImage identico à referência.                                             |
     *==========================================================================================*/
    
    public static BufferedImage cloneImage(BufferedImage image) {
        return new BufferedImage(image.getColorModel(), image.copyData(null), image.isAlphaPremultiplied(), null);
    }

    /*==========================================================================================*
     | Passa os pixels de uma imagem para um array.                                             |
     *==========================================================================================*/
    
    public int[] toArray(BufferedImage imageBuffer){
        int contador = 0;
        int[] newArray = new int[imageBuffer.getHeight()*imageBuffer.getWidth()];
        
        for(int i=0; i<imageBuffer.getWidth(); i++){
            for(int j=0; j<imageBuffer.getHeight(); j++){
                newArray[contador++] = imageBuffer.getRGB(i, j)&0xFF;
            }
        }
        return newArray;
    }
    
    /*==========================================================================================*
     | Passa os pixels de uma array para uma matriz 8-Bits com a dimensão da imagem.            |
     *==========================================================================================*/
    
    public int[][] toMatrix(BufferedImage imageBuffer){
        int[][] matrix = new int[imageBuffer.getWidth()][imageBuffer.getHeight()];
        
        for(int i=0; i<imageBuffer.getWidth(); i++){
            for(int j=0; j<imageBuffer.getHeight(); j++){
                matrix[i][j] = (imageBuffer.getRGB(i, j)&0xff0000) >> 16;
            }
        }
        return matrix;
    }
        
    /*==========================================================================================*
     | Passa os pixels de uma imagem para uma matriz de pixels                                  |
     *==========================================================================================*/
    
    public Pixel[][] bufferedToMatrix(){
        Pixel[][] matrix = new Pixel[this.imageBuffer.getWidth()][this.imageBuffer.getHeight()];
        
        for(int i=0; i<imageBuffer.getWidth(); i++){
            for(int j=0; j<imageBuffer.getHeight(); j++){
                matrix[i][j] = new Pixel(-1, 0, this.imageBuffer.getRGB(i, j), i, j);
            }
        }
        
        return matrix;
    }
    
    /*==========================================================================================*
     | Passa os pixels de uma imagem para um array de Pixels                                    |
     *==========================================================================================*/
    
    public Pixel[] toArrayPixel(int[] array, int width, int height){
        int contador = 0;
        Pixel[] newArray = new Pixel[width*height];
        
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                newArray[contador] = new Pixel(Pixel.INIT, 0, array[contador++], i, j);
            }
        }
        return newArray;
    }
    
    /*==========================================================================================*
     | Transforma os pixels de uma imagem em uma matriz de objeto Pixels                        |
     *==========================================================================================*/
    
    public Pixel[][] toMatrixPixel(int[] array, int width, int height){
        Pixel[][] pixelMatrix = new Pixel[width][height];
        
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                pixelMatrix[i][j] = new Pixel(Pixel.INIT, 0, (imageBuffer.getRGB(j, j)&0xff0000) >> 16, i, j);
            }
        }
        return pixelMatrix;
    }
    
    public Pixel[] matrixPixelToArrayPixel(Pixel[][] matrix, int width, int height){
        int contador = 0;
        Pixel[] array = new Pixel[width*height];
        
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                array[contador++] = matrix[i][j];
            }
        }
        
        return array;
    }
    
    public Pixel[][] arrayPixelToMatrixPixel(Pixel[] array, int width, int height){
        int contador = 0;
        Pixel[][] matrix = new Pixel[width][height];
        
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                matrix[i][j] = array[contador++];
            }
        }
        
        return matrix;
    }
    
    public void matrixPixelToBuffered(Pixel[][] mPixel, BufferedImage buffImage){
        for(Pixel[] line : mPixel){
            for(Pixel pix : line){
                buffImage.setRGB(pix.getXcoordenate(), pix.getYcoordenate(), pix.getValue());
            }
        }
    }
    
    /*==========================================================================================*
     | Identifica e retorna os vizinhos de um determinado pixel.                                |
     | Independente se esta em escala de cinza ou nao                                           |
     *==========================================================================================*/
    
    public LinkedList<Pixel> findNeighbors(Pixel pixel, Pixel[][] matrixPixels){           //[x-1,y-1] [x-1,y] [x-1,y+1]
        int x = pixel.getXcoordenate();                                                    // [x,y-1]   [x,y]   [x,y+1]
        int y = pixel.getYcoordenate();                                                    //[x+1,y-1] [x+1,y] [x+1,y+1]
 
        LinkedList<Pixel> map = new LinkedList();
        
        //Utilizando o mesmo algoritmo para selecao dos vizinhos
        for(int i=-1; i<=1; i++){
            for(int j= -1; j<=1; j++){
               if(x+i >= 0 && x+i < this.imageBuffer.getWidth()){
                   if(y+j >= 0 && y+j < this.imageBuffer.getHeight() && ((x+i) != x || (y+j) != y)){
                       map.add(matrixPixels[x+i][y+j]);
                   }
               }
            }
        }
        
        return map;
    }
    
    public LinkedList<Pixel> findNeighborsFromElement(Pixel pixels, LinkedList<Pixel> elemento){
        LinkedList<Pixel> neighbors = new LinkedList<>();
        
        for(Pixel pix : this.findNeighbors(pixels, this.pixelBuffer)){
           for(Pixel pixel : elemento){
               if(pixel.getXcoordenate() == pix.getXcoordenate() && pixel.getYcoordenate() == pix.getYcoordenate()){
                   neighbors.add(pix);
               }
           }
        }
        
        return neighbors;
    }
    
    public boolean neighborsWatershed(Pixel pixel, Pixel[][] matrix){
        for(Pixel neighbor : this.findNeighbors(pixel, matrix)){
            if(neighbor.getLabel() != Pixel.WSHED){
                return false;
            }
        }
        return true;
    }
    
    public boolean hasNeighborsWatershed(Pixel pixel, Pixel[][] matrix){
        for(Pixel elements : this.findNeighbors(pixel, matrix)){
            if(elements.getValue() == -1)
                return true;
        }
        return false;
    }
    
    public boolean hasNeighborsNatives(Pixel pixel, Pixel[][] matrix){
        for(Pixel elements : this.findNeighbors(pixel, matrix)){
            if(elements.getValue() == 0)
                return true;
        }
        return false;
    }
    
    
    //Identifica os pixels definidos pelo limiar
    public LinkedList<LinkedList<Pixel>> encontraElementos(){
        LinkedList<LinkedList<Pixel>> elementos    = new LinkedList();
        LinkedList<Pixel>             intermediate = new LinkedList<>();
        
        for(Pixel[] line : this.pixelBuffer){
            for(Pixel pix : line){
                if(pix.getValue() == Color.BLACK.getRGB() && pix.getLabel() != 30){                  
                    pix.setLabel(30);
                    
                    intermediate.add(pix);
                    LinkedList<Pixel> auxiliar = this.findNeighbors(pix, this.pixelBuffer);
                    
                    while(!auxiliar.isEmpty()){
                        Pixel p = auxiliar.remove();
                        
                        if(p.getValue() == Color.BLACK.getRGB() && p.getLabel() != 30){
                            p.setLabel(30);
                            intermediate.add(p);
                            auxiliar.addAll(this.findNeighbors(p, this.pixelBuffer));
                        }
                    }
                }
                
                if(!intermediate.isEmpty()){
                    elementos.add(intermediate);
                    intermediate = new LinkedList<>(); 
                }
            } 
        }
        
        return elementos;
    }
    
    //Devolve a cor original para cada elemento reconhecido como relevante para o estudo
    //Checar se antes de chamar o algoritmo, a estrutura pixelBuffer contém o backup das cores originais da imagem
    public void translate(LinkedList<LinkedList<Pixel>> elementos){
        for(LinkedList<Pixel> elem : elementos){
            for(Pixel pix : elem){
                pix.setValue(this.pixelBuffer[pix.getXcoordenate()][pix.getYcoordenate()].getValue());
            }
        }
    }
    
    //Este método unifica pequenos conj. de pixels próximos com certas similaridades
    public LinkedList<LinkedList<Pixel>> mergePixels(LinkedList<LinkedList<Pixel>> data){
        
        LinkedList<LinkedList<Pixel>> dataAux = new LinkedList<>();
        
        for(LinkedList<Pixel> pivo : data){
            for(LinkedList<Pixel> frag : data){
                
 //               if(frag == null)
 //                   data.remove(frag);
                
                if(frag.isEmpty() || pivo.isEmpty() || frag == pivo) 
                    continue;
                
                if(frag.getFirst().getLabel() != pivo.getFirst().getLabel()) //O primeiro elemento possui uma ident. que determina se é celula ou parasita
                    continue;
                
                //Verificação pixel a pixel
                for(Pixel pix : frag){
                    for(Pixel pixX : pivo){
                        if(((pixX.getXcoordenate() + 1) == pix.getXcoordenate() && (pixX.getYcoordenate() + 1) == pix.getYcoordenate()) ||
                           ((pixX.getXcoordenate() - 1) == pix.getXcoordenate() && (pixX.getYcoordenate() - 1) == pix.getYcoordenate()) ||
                           ((pixX.getXcoordenate() + 1) == pix.getXcoordenate() && (pixX.getYcoordenate() - 1) == pix.getYcoordenate()) ||
                           ((pixX.getXcoordenate() - 1) == pix.getXcoordenate() && (pixX.getYcoordenate() + 1) == pix.getYcoordenate()) ||
                           ((pixX.getXcoordenate() + 1) == pix.getXcoordenate() && (pixX.getYcoordenate()    ) == pix.getYcoordenate()) ||
                           ((pixX.getXcoordenate()    ) == pix.getXcoordenate() && (pixX.getYcoordenate() + 1) == pix.getYcoordenate()) ||
                           ((pixX.getXcoordenate() - 1) == pix.getXcoordenate() && (pixX.getYcoordenate()    ) == pix.getYcoordenate()) ||
                           ((pixX.getXcoordenate()    ) == pix.getXcoordenate() && (pixX.getYcoordenate() - 1) == pix.getYcoordenate())){
                            
                            pivo.addAll(frag); //Merge de pixels
                            frag.removeAll(frag);
                            //data.remove(frag); //Limpar o array fragmentado ---> O java nao permite alteração na quantidade de arrays - Exception
                            frag = null;
                            break;
                        }
                    }
                    
                    if(frag == null){
                        break;
                    }
                }             
            }
        }
        
        for(LinkedList<Pixel> array : data){
            if(!array.isEmpty()){
                dataAux.add(array);
            }
        }
        
        return dataAux;
    }
    
    
    /*=============================================================================*
     |                             Setters e Getters.                              |
     *=============================================================================*/

    public BufferedImage getImageBuffer() {
        return imageBuffer;
    }

    public void setImageBuffer(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    public int[][] getImagePixels() {
        return imagePixels;
    }

    public void setImagePixels(int[][] imagePixels) {
        this.imagePixels = imagePixels;
    }

    public byte[][] getRedBinaryPixels() {
        return redBinaryPixels;
    }

    public void setRedBinaryPixels(byte[][] redBinaryPixels) {
        this.redBinaryPixels = redBinaryPixels;
    }

    public byte[][] getGreenBinaryPixels() {
        return greenBinaryPixels;
    }

    public Pixel[][] getPixelBuffer() {
        return pixelBuffer;
    }

    public void setPixelBuffer(Pixel[][] pixelBuffer) {
        this.pixelBuffer = pixelBuffer;
    }

    public void setGreenBinaryPixels(byte[][] greenBinaryPixels) {
        this.greenBinaryPixels = greenBinaryPixels;
    }

    public byte[][] getBlueBinaryPixels() {
        return blueBinaryPixels;
    }

    public void setBlueBinaryPixels(byte[][] blueBinaryPixels) {
        this.blueBinaryPixels = blueBinaryPixels;
    } 
}
