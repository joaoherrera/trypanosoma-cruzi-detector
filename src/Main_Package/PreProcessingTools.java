package Main_Package;

import Adaptacao.Cell;
import Adaptacao.Elemento;
import Adaptacao.Parasitas;
import Adaptacao.Pixel;
import Adaptacao.SystemManager;
import Ferramentas_Extras.RockandRollProgressBar;
import Main_Package.Modeling.IOFunctions;
import Main_Package.Modeling.InputDataSetRow;
import Main_Package.Modeling.InputDataSetRow_AGL;
import Main_Package.Modeling.InputDataSetRow_DFX;
import Main_Package.Modeling.NeuralNetworkOp;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.Thresholder;
import ij.plugin.filter.Binary;
import ij.plugin.filter.EDM;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import javax.swing.SwingUtilities;

/**
 * @date 24/06/2014
 * @author João
 * 
 * Esta classe prepara as imagens para serem processadas e ao final identificadas via rede neural
 */

public class PreProcessingTools implements Runnable{
    private final ImageProcessing imgProcess;
    private ImageProperties binarizedImg;
    private final LinkedList<File> imgArray;
    private InputDataSetRow inputDSR;
    private InputDataSetRow_AGL inputDSR_AGL;
    private final PosProcessingTools posProcTools;
    private NeuralNetworkOp nnOP;
    private RockandRollProgressBar progressBar;
    private String tempPath;

    private Color mCColor;
    
    private float totalPerc; 
    private float curLoop; 
    private boolean hasLoops;
    
    public PreProcessingTools(LinkedList images){
        this.imgArray = images;
        this.imgProcess = new ImageProcessing();
        this.posProcTools = new PosProcessingTools();
    }
    
    public PreProcessingTools(ImageProperties curImg, LinkedList imgName){
        this.imgArray = imgName;
        this.binarizedImg = curImg;     
        this.imgProcess = new ImageProcessing();
        this.posProcTools = new PosProcessingTools();
    }
    
    //Com base nas informações de treinamento, é possível definir uma tolerância no limiar estabelecendo:
    // RED, GREEN, BLUE = [x - Tmin | X | x + Tmax], onde x é a média dos valores das bandas RGB, Tmin é a tolerancia minima
    // adotado aqui como o menor valor encontrado no treinamento e Tmax o maior valor encontrado.
    public void createAutoThreshold(){
        IOFunctions ioFunctions = new IOFunctions("ttrain/trainning.txt");
        String tContent         = ioFunctions.ler();

        this.mCColor  = thresholdMedio(tContent, 0);
    }
    
    //Retorna a cor média de cada objeto
    //Objeto: 0 celula 1 parasita
    private Color thresholdMedio(String content, int object){
        int mRed   = 0;
        int mGreen = 0;
        int mBlue  = 0;
        
        int loopNum = 1;
        
        while(hasInfo(content)){
            if(Integer.parseInt(content.substring(content.indexOf("\n") - 1, content.indexOf("\n"))) == object){ //se é uma célula
                mRed     += Integer.parseInt(content.substring(0, content.indexOf(","))); 
                content  = content.substring(content.indexOf(",")+1, content.length());

                mGreen   += Integer.parseInt(content.substring(0, content.indexOf(","))); 
                content  = content.substring(content.indexOf(",")+1, content.length());

                mBlue    += Integer.parseInt(content.substring(0, content.indexOf(","))); 
                content  = content.substring(content.indexOf("\n")+1, content.length());

                loopNum++;
            }
            else{
                 content  = content.substring(content.indexOf("\n")+1, content.length());
            }
        }
        
        return loopNum == 1 ? new Color(mRed/(loopNum), mGreen/(loopNum), mBlue/(loopNum)) : new Color(mRed/(loopNum-1), mGreen/(loopNum-1), mBlue/(loopNum-1));
    }
    
    //Identifica e cria um grupo de pixels que pertence ao limiar, definido como a média das bandas R, G e B no arquivo de treinamento.
    public void binarize(BufferedImage imgData){
        int red;
        int green;
        int blue;
        
        for(int i=0; i<imgData.getWidth(); i++){
            for(int j=0; j<imgData.getHeight(); j++){
                red   = (imgData.getRGB(i, j)&0xff0000) >> 16;
                green = (imgData.getRGB(i, j)&0x00ff00) >> 8;
                blue  =  imgData.getRGB(i, j)&0x0000ff;
                    
                if(red <= this.mCColor.getRed()*2 && green <= this.mCColor.getGreen()*2 && blue <= this.mCColor.getBlue()*2){
                    imgData.setRGB(i, j, Color.BLACK.getRGB());
                }
                else{
                    imgData.setRGB(i, j, Color.WHITE.getRGB());
                }                     
            }
        }
    }

    
    /*Este método trata automaticamente de identificar os preElementos através de uma limiariação
      definida pela média das bandas R,G e B.
      O método de watershed é utilizado para que preElementos muito próximos sejam divididos.
    
      *É Utilizado a API ImageJ. Para informações do comportamento das classes, acessar: http://trac.imagej.net/browser/ImageJA/ij/
    */

    @Override
    public void run(){
        synchronized(this){
            LinkedList<Elemento> elementos;     //elementos identificados
            LinkedList<Cell> nucleos;           //nucleos identificados
            LinkedList<Parasitas> parasitas;    //parasitas identificados

            ImageProperties curImg;
            ImagePlus curImgJ;

            SystemManager sysMgr = new SystemManager();
            
            Pixel[][] backupImg;
            
            //Utilizado para atualizar corretamente a barra de progresso
            if(100/imgArray.size() > 0){
                this.totalPerc = 100/imgArray.size();
                this.hasLoops  = false;
            }
            else{
                this.totalPerc = (float) imgArray.size()/100;
                this.hasLoops  = true;
                this.curLoop   = 0;
            }
            
            if(this.progressBar != null)
                this.progressBar.getCurProcess().setText("Processando: ");
            
            for(File fileName : imgArray){
                
                if(this.binarizedImg == null){
                    curImg  = new ImageProperties(fileName.getAbsolutePath());
                    curImg.imageRead();
                    
                    backupImg = curImg.getImageTools().getImageDataStructure().bufferedToMatrix(); //backup dos pixels originais da imagem.
                }
                else{
                    curImg  = new ImageProperties(fileName.getAbsolutePath());
                    curImg.imageRead();
                    
                    backupImg = curImg.getImageTools().getImageDataStructure().bufferedToMatrix(); //backup dos pixels originais da imagem.
                     
                    curImg = this.binarizedImg;
                }    
                
                nucleos   = new LinkedList<>();
                parasitas = new LinkedList<>();
              
                if(this.mCColor != null)
                    this.binarize(curImg.getImageTools().getImageDataStructure().getImageBuffer()); //Binarização baseada na média encontrada no arquivo de treinamento
                else
                    curImg.getImageTools().getImageProcessing().createLimiar();

                curImgJ = new ImagePlus(curImg.getFileName(), curImg.getImageTools().getImageDataStructure().getImageBuffer());
                
                if(this.progressBar != null)
                    progressBar.manualChangeStatus();
                
                this.makeBinary(curImgJ);               
                this.fillHoles(curImgJ);
                this.makeWatershed(curImgJ);

                curImg.getImageTools().getImageDataStructure().setImageBuffer(curImgJ.getBufferedImage());
                curImg.getImageTools().getImageDataStructure().setPixelBuffer(curImg.getImageTools().getImageDataStructure().bufferedToMatrix()); //definindo os preElementos processados pela API

                LinkedList<LinkedList<Pixel>> preElementos  = curImg.getImageTools().getImageProcessing().getData().encontraElementos();
                curImg.getImageTools().getImageDataStructure().setPixelBuffer(backupImg); 
                curImg.getImageTools().getImageDataStructure().translate(preElementos);
                
                if(this.hasLoops){
                    this.curLoop += .2;
                }
                
                if(this.progressBar != null)
                    this.updateProgressBar((float)((20 * totalPerc)/100));

                //Transformando conj. de pixels em objs
                elementos = this.createElementos(preElementos);

                //IA - Elementos x Defeitos
                this.inputDSR = new InputDataSetRow_DFX(elementos);
                this.inputDSR.formatInput();
                this.nnOP = new NeuralNetworkOp(this.inputDSR.getInputFormat(), "ttrain/iaDFX.nnet");

                this.especifyElements(elementos);
                //IA - Celulas x Parasitas
                this.inputDSR = new InputDataSetRow(elementos);
                this.inputDSR.formatInput();
                this.nnOP = new NeuralNetworkOp(this.inputDSR.getInputFormat(), "ttrain/ia.nnet");

                this.especifyElements(curImg, preElementos, nucleos, parasitas);
                //IA - Celulas x Aglomerações
                this.inputDSR_AGL = new InputDataSetRow_AGL(nucleos);
                this.inputDSR_AGL.formatInput();
                this.nnOP = new NeuralNetworkOp(this.inputDSR_AGL.getInputFormat(), "ttrain/iaAGL.nnet");
                
                if(this.hasLoops){
                    this.curLoop += .3;
                }
                
                if(this.progressBar != null)
                    this.updateProgressBar((float)((30 * totalPerc)/100));
                
                this.createGrid(curImg, parasitas, nucleos);
                this.posProcTools.relationByCitoplasma(curImg, nucleos, parasitas);
                
                if(this.hasLoops){
                    this.curLoop += .3;
                }
                
                if(this.progressBar != null)
                    this.updateProgressBar((float)((30 * totalPerc)/100));
                
                this.posProcTools.relocateParasites(curImg);
                this.posProcTools.paintElementsDiffCollors(curImg);
                this.posProcTools.gravarIdentifacao(curImg);

                if(this.hasLoops){
                    this.curLoop += .2;
                }
                
                if(this.progressBar != null)
                    this.updateProgressBar((float)((20 * totalPerc)/100));
                
                if(this.tempPath.length() > 0){
                    sysMgr.saveTempImg(curImg, this.tempPath);
                }
            }
            
            if(this.progressBar != null){
                if(this.progressBar.getProgressBar().getValue() < 100){
                    this.updateProgressBar(100 - this.getProgressBar().getProgressBar().getValue());
                }

                this.progressBar.getCurProcess().setText("Finalizado.");
                this.progressBar.getCurProcessImgName().setText("");            
                this.progressBar.setCurrentIndex(0);
            }
            this.notify();
        }
    }
    
    //Simplifica o conjunto de pixels identificado em um conjunto de preElementos
    public LinkedList<Elemento> createElementos(LinkedList<LinkedList<Pixel>> elementoPixel){
        LinkedList<Elemento> elementos = new LinkedList<>();
        
        for(LinkedList<Pixel> elemento : elementoPixel){
            elementos.add(new Elemento(elemento));
            elementos.getLast().setCorMedia(this.imgProcess.findMedColor(elemento));
        }
        
        return elementos;
    }
    
    public void makeBinary(ImagePlus curImgJ){
        Thresholder tHold = new Thresholder(); //Conversor para 8-bits
        
        WindowManager.setTempCurrentImage(curImgJ); //Definindo a imagem em WindowManager, necessario para converter a image em 8-bits
        tHold.run("skip"); //Convertendo a imagem em 8-bits e aplicando um threshold, que não terá muito efeito, pois a imagem já foi binarizada
    }
    
    public void fillHoles(ImagePlus curImgJ){
        Binary binaryIJ   = new Binary(); //Classe para solucionar o problema de preElementos fragmentados no processo de binarização
        
        binaryIJ.setup("fill", curImgJ); //Configuração de parâmetros para executar o "Fill Holes"
        binaryIJ.run(curImgJ.getProcessor()); //Fill Holes em imagens binarizadas
    }
    
    public void erode(ImagePlus curImgJ){
        Binary binaryIJ   = new Binary();
        
        binaryIJ.setup("erode", curImgJ); //Configuração de parâmetros para executar o "Erode"
        binaryIJ.run(curImgJ.getProcessor()); //Erosão em imagens binarizadass
    }
    
    public void makeWatershed(ImagePlus curImgJ){
        EDM watershedIJ   = new EDM();          //Classe de implementação binária
        
        watershedIJ.setup("watershed", curImgJ);   //Configuração de parâmetros para executar o "Watershed"
        watershedIJ.run(curImgJ.getProcessor());   //Watershed em imagem binarizada
    }
    
    //Retorna a média do tamanho dos parasitas de uma imagem
    private int mediaParasitaSize(LinkedList<Parasitas> parasitas){
        int size = 0;
        
        for(Parasitas parasita : parasitas){
            size += parasita.getSize();
        }
        
        return size/parasitas.size();
    }
    
       
    //Ignora os defeitos encontrados pela rede neural
    public void especifyElements(LinkedList<Elemento> elementos){
        LinkedList<Integer> indexes = new LinkedList();
        int count = 0;
        
        for(double elements : this.nnOP.getResultados()){
            if(elements == 1.0){ //Caso for defeito
                indexes.add(count);
            }
            
            count++;
        }
        
        for(Integer index : indexes){
            elementos.remove(index);
        }
    }
    
    //Cria objetos de acordo com o resultado da I.A.
    public void especifyElements(ImageProperties curImg, LinkedList<LinkedList<Pixel>> elementos, LinkedList<Cell> nucleos, LinkedList<Parasitas> parasitas){
        int cont = 0;
        Parasitas parasita;
        Cell celula;
        LinkedList<Pixel> pixels;
        
        for(double elements : this.nnOP.getResultados()){
             pixels = new LinkedList<>();
                
            if(elements == 0.0){
                pixels.addAll(elementos.get(cont));
                    
                if(!pixels.isEmpty()){
                    celula = new Cell(pixels);
                    celula.setCorMedia(curImg.getImageTools().getImageProcessing().findMedColor(pixels));
                    nucleos.add(celula);
                }
            }
            else if(elements == 1.0){
                pixels.addAll(elementos.get(cont));
                    
                if(!pixels.isEmpty()){
                    parasita = new Parasitas(pixels);
                    parasita.setCorMedia(curImg.getImageTools().getImageProcessing().findMedColor(pixels));
                    parasitas.add(new Parasitas(pixels));
                }
            }
            
            cont++;
        }
    }
    
    private void createGrid(ImageProperties curImg, LinkedList<Parasitas> parasitas, LinkedList<Cell> nucleos){
        LinkedList<Cell>  aglomeracao = new LinkedList<>();
        LinkedList<Pixel> buffer;
        
        int media = this.mediaParasitaSize(parasitas);
        int qtdParasitas;
        
        for(int i=0; i<nucleos.size(); i++){
           if(this.nnOP.getResultados().get(i) == 1.0){
               aglomeracao.add(nucleos.get(i));
           }
        }
        
        for(Cell aglomerado : aglomeracao){
            qtdParasitas = Math.round((float) aglomerado.getSize()/media);
 
            for(int i=0; i<qtdParasitas; i++){
                buffer = new LinkedList<>();
                
                for(int j=0; j<media; j++){
                    if(aglomerado.getPixels().size() > media){
                        buffer.addLast(aglomerado.getPixels().remove(j));
                    }
                    else if(aglomerado.getPixels().size() > 0){
                        for(int k=0; k<aglomerado.getSize(); k++){
                            buffer.add(aglomerado.getPixels().remove(k));
                        }
                    }
                }
                
                parasitas.add(new Parasitas(buffer));
            }
            
            nucleos.remove(aglomerado);
        }
    }

    private void updateProgressBar(float soma){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                
                if(hasLoops){
                    if(curLoop >= totalPerc){
                        progressBar.setValue(progressBar.getProgressBar().getValue() + 1);  
                        curLoop = 0;
                    }
                }
             
                progressBar.setValue(progressBar.getProgressBar().getValue() + Math.round(soma));         
            }
        });            
    }
 
    
    private boolean hasInfo(String info){
        return !info.isEmpty();
    }

    public InputDataSetRow getInputDSR() {
        return this.inputDSR;
    }

    public RockandRollProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar(RockandRollProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public String getTempPath() {
        return this.tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }   
}
