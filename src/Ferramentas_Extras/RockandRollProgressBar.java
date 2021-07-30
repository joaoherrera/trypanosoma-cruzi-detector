package Ferramentas_Extras;

import java.io.File;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * @data 14/08/2014
 * @author João
 */
public class RockandRollProgressBar implements Runnable{
    private JProgressBar progressBar;
    private LinkedList<File> arquivos;
    private JLabel curProcess;
    private JLabel curProcessImgName;
    private JLabel processoLabel;
    
    private int currentIndex;

    @Override
    public void run() {
        this.progressBar        = new JProgressBar();
        this.curProcess         = new JLabel();
        this.curProcessImgName  = new JLabel();
        this.processoLabel      = new JLabel("Progresso: ");
        
        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum(100);
        this.progressBar.setStringPainted(true);
    }
        
    public void manualChangeStatus(){
        curProcessImgName.setText(arquivos.get(currentIndex++).getName());
    }
    
    public void setValue(int value){
        this.progressBar.setValue(value);
    }

    public void setArquivos(LinkedList<File> arquivos) {
        this.arquivos = arquivos;
    }
    
    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JLabel getCurProcess() {
        return curProcess;
    }

    public void setCurProcess(JLabel curProcess) {
        this.curProcess = curProcess;
    }

    public JLabel getCurProcessImgName() {
        return curProcessImgName;
    }

    public void setCurProcessImgName(JLabel curProcessImgName) {
        this.curProcessImgName = curProcessImgName;
    }

    public JLabel getProcessoLabel() {
        return processoLabel;
    }

    public void setProcessoLabel(JLabel processoLabel) {
        this.processoLabel = processoLabel;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
