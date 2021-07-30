package Ferramentas_Extras;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @date 20/06/2014
 * @author João
 * 
 * Adapação para permitir a multipla seleção em uma Jlist com checkBoxes.
 * Retirado de: http://www.jroller.com/santhosh/entry/jlist_with_checkboxes
 */

public class RockandRollCheckListManager extends MouseAdapter implements ListSelectionListener, ActionListener{
    private final ListSelectionModel listSelectionModel;
    private RockandRollList list;
    
    public RockandRollCheckListManager(RockandRollList list){
        this.list = list;
        this.listSelectionModel = new DefaultListSelectionModel();
        
        this.list.setCellRenderer(new RockandRollRendererCheckbox(this.list.getCellRenderer(), this.listSelectionModel));
        this.list.addMouseListener(this);
        this.listSelectionModel.addListSelectionListener(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        this.list.repaint(this.list.getCellBounds(e.getFirstIndex(), e.getLastIndex()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.verificarValores(list.getSelectedIndex());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int location = list.locationToIndex(e.getPoint());
       
        //Detectando a correta posição da célula selecionada
        if(location < 0 || location > list.getCellBounds(location, location).y + (new JCheckBox()).getPreferredSize().height)
            return;
        
        //Verificar se o local clicado é a área do checkbox
        if(e.getPoint().x >= 8 && e.getPoint().x < (new JCheckBox()).getPreferredSize().width){
            this.verificarValores(location);
        }
        else{
            list.setSelectedIndex(location);
        }
        
        this.list.repaint();
    }
    
    private void verificarValores(int location){      
        if(this.listSelectionModel.isSelectedIndex(location)){
            this.listSelectionModel.removeSelectionInterval(location, location); 
        }
        else{
            this.listSelectionModel.addSelectionInterval(location, location);
        }      
    }
    
    public LinkedList<File> getSelectedFiles(){
        LinkedList<File> selected  = new LinkedList<>();
        LinkedList<File> origFiles = this.list.getSystemMgr().getFilesFromDirectory(this.list.getCurrentPath()); //Utilizado porque havia erro de Thread se eu criar apenas um novo file.
                
         for(int i=0; i<this.list.getStrModel().getSize(); i++){
            if(((RockandRollRendererCheckbox)this.list.getCellRenderer()).getListSM().isSelectedIndex(i)){
                selected.add(origFiles.get(i));
            }
        }
         
        return selected.size() > 0 ? selected : null;
    }

    public RockandRollList getList() {
        return this.list;
    }

    public void setList(RockandRollList list) {
        this.list = list;
    }
    
    public void allChecked(){
        this.listSelectionModel.addSelectionInterval(0, this.list.getModel().getSize());   
    }
    
    public void allUnchecked(){
        this.listSelectionModel.removeSelectionInterval(0, this.list.getModel().getSize());   
    }

    public ListSelectionModel getListSelectionModel() {
        return this.listSelectionModel;
    } 
}
