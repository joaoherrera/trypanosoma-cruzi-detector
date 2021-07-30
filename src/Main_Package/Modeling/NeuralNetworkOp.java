package Main_Package.Modeling;

import java.io.File;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.neuroph.core.NeuralNetwork;

/**
 * @date 05/07/2014
 * @author João
 */

public class NeuralNetworkOp {
    private final LinkedList<double[]> input;
    private LinkedList<Double> output;
    private final NeuralNetwork neuralNetwork;

    public NeuralNetworkOp(LinkedList<double[]> input, String Tfile) {
        this.input = input;
        
        if(!OutputDataSetRow.hasIAFile())
            JOptionPane.showMessageDialog(null, "Acesso à base de dados restrita, verifique o arquivo de treinamento", "Erro de leitura", JOptionPane.ERROR);
        
        this.neuralNetwork = NeuralNetwork.createFromFile(new File(Tfile)); //Origem default
    }
    
    public LinkedList<Double> getResultados(){   
        if(this.neuralNetwork == null) return null;
        
        this.output = new LinkedList<>();
        
        for(double[] input : this.input){
            this.neuralNetwork.setInput(input);
            this.neuralNetwork.calculate();
            
            if(this.neuralNetwork.getOutput()[0] == 1.0){
                this.output.add(1.0);
            }
            else{
                this.output.add(0.0);
            }
            
        }
        
        return this.output;
    }
}
