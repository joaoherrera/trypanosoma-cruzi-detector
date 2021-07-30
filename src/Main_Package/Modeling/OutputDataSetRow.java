package Main_Package.Modeling;

import Adaptacao.Aglomeracao;
import Adaptacao.Cell;
import Adaptacao.Defeito;
import Adaptacao.Parasitas;
import Adaptacao.Pixel;
import Main_Package.GraphicalUserInterface.GUITrainning;
import Main_Package.ImageProcessing;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.PerceptronLearning;
import org.neuroph.util.TrainingSetImport;

/**
 * @author João
 * @date 17/06/2014
 */

public class OutputDataSetRow{
    private final ImageProcessing ip;
    private final IOFunctions ioFunc;
    private LinkedList<Cell> cells;
    private LinkedList<Parasitas> parasitas;
    private LinkedList<Defeito> defx;
    private LinkedList<Aglomeracao> aglom;
            
    public OutputDataSetRow(String fPath){
        this.ioFunc      = new IOFunctions(fPath);
        this.ip          = new ImageProcessing();
        this.cells       = new LinkedList<>();
        this.parasitas   = new LinkedList<>();
        this.defx        = new LinkedList<>();
        this.aglom       = new LinkedList<>();
    }
    
    public void formatOutputByRGBSize(){
        String outputTxt = this.ioFunc.ler();
                        
        // Definição da string de gravação das celulas
        for(Cell cell : cells){
            outputTxt += cell.getCorMedia().getRed()   + "," +
                         cell.getCorMedia().getGreen() + "," +
                         cell.getCorMedia().getBlue()  + "," +
                         cell.getSize()                + "," + GUITrainning.CELL_PRESSED + System.lineSeparator();
        }
        
        // Definição da string de gravação dos parasitas
        for(Parasitas parasita : parasitas){
            outputTxt += parasita.getCorMedia().getRed()   + "," +
                         parasita.getCorMedia().getGreen() + "," +
                         parasita.getCorMedia().getBlue()  + "," +
                         parasita.getSize()                + "," + GUITrainning.PARASITE_PRESSED + System.lineSeparator();
        }
        
        ioFunc.gravar(outputTxt);
    }
    
    //Mesma versão do método formatOutputByRGBSize() para treinar uma rede que elimina defeitos de imagem
    public void formatOutputByRGBSize_Dfx(){
        String outputTxt = this.ioFunc.ler();
                        
        // Definição da string de gravação das celulas
        for(Cell cell : cells){
            outputTxt += cell.getCorMedia().getRed()   + "," +
                         cell.getCorMedia().getGreen() + "," +
                         cell.getCorMedia().getBlue()  + "," +
                         GUITrainning.ELEMENT_PRESSED  + System.lineSeparator();
        }
        
        // Definição da string de gravação dos parasitas
        for(Parasitas parasita : parasitas){
            outputTxt += parasita.getCorMedia().getRed()   + "," +
                         parasita.getCorMedia().getGreen() + "," +
                         parasita.getCorMedia().getBlue()  + "," +
                         GUITrainning.ELEMENT_PRESSED + System.lineSeparator();
        }
        
        // Definição da string de gravação dos defeitos
        for(Defeito dfx : defx){
            outputTxt += dfx.getCorMedia().getRed()   + "," +
                         dfx.getCorMedia().getGreen() + "," +
                         dfx.getCorMedia().getBlue()  + "," +
                         1  + System.lineSeparator();
        }
        
        ioFunc.gravar(outputTxt);
    }
    
    //Mesma versão do método formatOutputByRGBSize() para treinar uma rede que aglomerações em celulas
    public void formatOutputByRGBSize_Agl(){
        String outputTxt = this.ioFunc.ler();
                        
        // Definição da string de gravação das celulas
        for(Cell cell : cells){
            outputTxt += cell.getCorMedia().getRed()   + "," +
                         cell.getCorMedia().getGreen() + "," +
                         cell.getCorMedia().getBlue()  + "," +
                         cell.getSize() + "," + GUITrainning.CELL_PRESSED + System.lineSeparator();
        }
        
        // Definição da string de gravação das aglomerações
        for(Aglomeracao agl : aglom){
            outputTxt += agl.getCorMedia().getRed()   + "," +
                         agl.getCorMedia().getGreen() + "," +
                         agl.getCorMedia().getBlue()  + "," +
                         agl.getSize() + "," + 1 + System.lineSeparator();
        }
        
        ioFunc.gravar(outputTxt);
    }
    
    public void initializeByLKPixel(LinkedList<LinkedList<Pixel>> lkListPix){
        for(LinkedList<Pixel> lkPixel : lkListPix){
            switch(lkPixel.getFirst().getLabel()){
                case GUITrainning.CELL_PRESSED:
                    this.cells.add(new Cell(lkPixel));
                    this.cells.getLast().setCorMedia(ip.findMedColor(lkPixel));
                break;
                
                case GUITrainning.PARASITE_PRESSED:
                    this.parasitas.add(new Parasitas(lkPixel));
                    this.parasitas.getLast().setCorMedia(ip.findMedColor(lkPixel));
                break;            
            }
        }
    }
    
    //Mesma versão do método initializeByLKPixel() para treinar uma rede que elimina defeitos de imagem
    public void initializeByLKPixel_Dfx(LinkedList<LinkedList<Pixel>> lkListPix){
        for(LinkedList<Pixel> lkPixel : lkListPix){
            switch(lkPixel.getFirst().getLabel()){
                case GUITrainning.CELL_PRESSED:
                    this.cells.add(new Cell(lkPixel));
                    this.cells.getLast().setCorMedia(ip.findMedColor(lkPixel));
                break;
                
                case GUITrainning.PARASITE_PRESSED:
                    this.parasitas.add(new Parasitas(lkPixel));
                    this.parasitas.getLast().setCorMedia(ip.findMedColor(lkPixel));
                break;   
                    
                case GUITrainning.DEFECT_PRESSED:
                    this.defx.add(new Defeito(lkPixel));
                    this.defx.getLast().setCorMedia(ip.findMedColor(lkPixel));
                break;
            }
        }
    }
    
    //Mesma versão do método initializeByLKPixel() para treinar uma rede que elimina aglomerações
    public void initializeByLKPixel_Agl(LinkedList<LinkedList<Pixel>> lkListPix){
        for(LinkedList<Pixel> lkPixel : lkListPix){
            switch(lkPixel.getFirst().getLabel()){
                case GUITrainning.CELL_PRESSED:
                    this.cells.add(new Cell(lkPixel));
                    this.cells.getLast().setCorMedia(ip.findMedColor(lkPixel));
                break;
                
                case GUITrainning.AGLOMERACAO_PRESSED:
                    this.aglom.add(new Aglomeracao(lkPixel));
                    this.aglom.getLast().setCorMedia(ip.findMedColor(lkPixel));
                break;            
            }
        }
    }
    
    public void aprender(String path, int input, int output){
        try{
            //BufferedDataSet buffDS = new BufferedDataSet(this.ioFunc.getFile(), 4, 1, ",");
            NeuralNetwork perceptron  = new Perceptron(input, output);
            DataSet set               = TrainingSetImport.importFromFile(this.ioFunc.getFile().getAbsolutePath(), input, output, ",");
            
            perceptron.setLearningRule(new PerceptronLearning());
            //set.normalize();
            
            ((PerceptronLearning) perceptron.getLearningRule()).setMaxError(0.001); //0-1
            ((PerceptronLearning) perceptron.getLearningRule()).setLearningRate(0.2); //0-1
            ((PerceptronLearning) perceptron.getLearningRule()).setMaxIterations(3000); //0-1

            perceptron.learn(set);
            perceptron.save(path);
        }
        catch(FileNotFoundException fNfound){
            JOptionPane.showMessageDialog(null, "Acesso à base de dados restrita, verifique o arquivo de treinamento", "Erro de leitura", JOptionPane.ERROR);
        }
        catch(NumberFormatException | IOException exp){
            
        }
    }
    
    public static boolean hasTrainningFile(){
        return new File("ttrain/trainning.txt").exists();
    }
    
    public static boolean hasIAFile(){
         return new File("ttrain/ia.nnet").exists();
    }

    public LinkedList<Cell> getCells() {
        return this.cells;
    }

    public void setCells(LinkedList<Cell> cells) {
        this.cells = cells;
    }

    public LinkedList<Parasitas> getParasitas() {
        return this.parasitas;
    }

    public void setParasitas(LinkedList<Parasitas> parasitas) {
        this.parasitas = parasitas;
    }

  
}
