package Ferramentas_Extras;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * @date 11/12/2013
 * @author João Paulo Herrera
 * Lista os arquivos disponíveis em um diretório
 */
public class RockandRollFileChooser extends JList{
    
    private String filePath;
    private DefaultListModel listModel;
    
    public RockandRollFileChooser(){
        //Propriedades da Lista
        this.listModel = new DefaultListModel();
        this.setLayoutOrientation(JList.VERTICAL_WRAP);
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //this.setCellRenderer(new RockandRollRenderer());
        
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }    
}
