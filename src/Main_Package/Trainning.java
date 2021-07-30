package Main_Package;

import Adaptacao.Pixel;
import Main_Package.GraphicalUserInterface.GUITrainning;
import Main_Package.Modeling.OutputDataSetRow;
import java.awt.Color;
import java.util.LinkedList;

/**
 * @date 12/05/2014
 * @author João
 */

public class Trainning{
    private final ImageProcessing         imgProc;
    private final ImageProperties         image;
    private OutputDataSetRow              outProc;
    private LinkedList<Pixel>             eSelected;
    private LinkedList<LinkedList<Pixel>> backup;
    private boolean hasDFX;
    private boolean hasAGL;
    
    public Trainning(ImageProperties  image){
        this.image = image;
        this.imgProc = new ImageProcessing(new DataStructure(this.image.getImage()));
        this.backup = new LinkedList<>();
    }
    
    public void addCell(int x, int y, int limiarX, int limiarY){
        this.eSelected = this.imgProc.regionGrowing(x, y, limiarX, limiarY);
        this.eSelected.getFirst().setLabel(GUITrainning.CELL_PRESSED); //Identificar que é conjunto de célula
        
        if(!this.hasInQueue(eSelected)){
            this.backup.add(this.eSelected);
            this.imgProc.paintElements(eSelected, new Color(181, 230, 29, 99).getRGB());
        }
    }
    
    public void addParasite(int x, int y, int limiarX, int limiarY){
        this.eSelected = this.imgProc.regionGrowing(x, y, limiarX, limiarY);
        this.eSelected.getFirst().setLabel(GUITrainning.PARASITE_PRESSED); //Identificar que é conjunto de parasitas
        
        if(!this.hasInQueue(eSelected)){
            this.backup.add(this.eSelected);
            this.imgProc.paintElements(eSelected, new Color(100, 233, 250, 99).getRGB());
        }
    }
    
    public void addDefects(int x, int y, int limiarX, int limiarY){
        this.eSelected = this.imgProc.regionGrowing(x, y, limiarX, limiarY);
        this.eSelected.getFirst().setLabel(GUITrainning.DEFECT_PRESSED); //Identificar que é conjunto de defeitos
        
        if(!this.hasInQueue(eSelected)){
            this.backup.add(this.eSelected);
            this.imgProc.paintElements(eSelected, new Color(237, 28, 36, 99).getRGB());
        }
        
        if(!this.hasDFX){
            this.hasDFX = true;
        }
    }
    
    public void addAglomeracoes(int x, int y, int limiarX, int limiarY){
        this.eSelected = this.imgProc.regionGrowing(x, y, limiarX, limiarY);
        this.eSelected.getFirst().setLabel(GUITrainning.AGLOMERACAO_PRESSED); //Identificar que é conjunto de aglomeracoes
        
        if(!this.hasInQueue(eSelected)){
            this.backup.add(this.eSelected);
            this.imgProc.paintElements(eSelected, new Color(163, 73, 164, 99).getRGB());
        }
        
        if(!this.hasAGL){
            this.hasAGL = true;
        }
    }
    
    public void undoModif(){
        if(!this.backup.isEmpty())
            this.imgProc.paintElementsBackup(this.backup.removeLast());
    }
    
    public void remake(){
        while(!this.backup.isEmpty()){
            this.imgProc.paintElementsBackup(this.backup.removeLast());
        }
    }
    
    //Este método prepara cada objeto para ser salvo base de dados
    public void prepObj(){
        this.outProc = new OutputDataSetRow("ttrain/trainning.txt"); //Celula x Parasita
        //this.outProcED = new OutputDataSetRow("ttrain/defects.txt");   //Elemento x Defeito
        
        this.backup  = this.imgProc.getData().mergePixels(backup);   //Merge que junta objetos em comum
        this.outProc.initializeByLKPixel(backup);                  //Organização e separação dos elementos celulas e parasitas
        this.outProc.formatOutputByRGBSize();                      //Criação do arquivo txt - Celulas x Parasitas
        this.outProc.aprender("ttrain/ia.nnet", 4, 1);
        
        // ~~ Alteração realizada em 12/08/2014 ~~~ //
        if(this.hasDFX){
            this.outProc = new OutputDataSetRow("ttrain/trainningDFX.txt");
            this.outProc.initializeByLKPixel_Dfx(backup);              //Organização e separação dos elementos
            this.outProc.formatOutputByRGBSize_Dfx();                  //Criação do arquivo txt - Elementos x Defeitos
            this.outProc.aprender("ttrain/iaDFX.nnet", 3, 1);
        }
        
        // ~~ Alteração realizada em 20/08/2014 ~~~ //
        if(this.hasAGL){
            this.outProc = new OutputDataSetRow("ttrain/trainningAGL.txt");
            this.outProc.initializeByLKPixel_Agl(backup);              //Organização e separação dos elementos
            this.outProc.formatOutputByRGBSize_Agl();//Criação do arquivo txt - Células x Aglomerações
            this.outProc.aprender("ttrain/iaAGL.nnet", 4, 1);
        }
        
    }
    
    private boolean hasInQueue(LinkedList<Pixel> pre){
        for(LinkedList<Pixel> listHead : backup){
            for(Pixel pix : listHead){
                if(pre.getFirst().getXcoordenate() == pix.getXcoordenate() && pre.getFirst().getYcoordenate() == pix.getYcoordenate()){
                    return true;
                }
            }
        }
        return false;
    }
}
