
package Ferramentas_Extras;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import static sun.util.calendar.CalendarUtils.mod;

/**
 * @date 19/06/2014
 * @author João
 * 
 * Renderer específico com checkboxes
 */

public class RockandRollRendererCheckbox extends RockandRollRenderer{
   
    private ListSelectionModel listSM;
    
    public RockandRollRendererCheckbox(ListCellRenderer defaultCellRenderer, ListSelectionModel listSM){
        super(defaultCellRenderer);
        this.listSM = listSM;
    }
    
    public RockandRollRendererCheckbox(){
        super();
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel panel     = new JPanel(new BorderLayout()); 
        JCheckBox checkB = new JCheckBox(value.toString());
        
        panel.setLayout(new BorderLayout());
        checkB.setSelected(this.listSM.isSelectedIndex(index));
       
        //Definindo o estilo do LookandFeel
        if(index % 2 == 0){
            panel.setBackground(new Color(224, 221, 216));
        }
        
        if(isSelected){
            panel.setBackground(list.getSelectionBackground());
        }
        
        panel.add(checkB, BorderLayout.WEST);
       
        return panel;
    }

    public ListSelectionModel getListSM() {
        return listSM;
    } 
}
