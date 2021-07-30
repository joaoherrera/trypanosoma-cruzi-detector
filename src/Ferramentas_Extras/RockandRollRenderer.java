package Ferramentas_Extras;

import Adaptacao.SystemManager;
import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @date 11/12/2013
 * @author João
 * 
 * @Modificação 18/06/2014
 * Implementa um Renderer para os elementos da RockandRollList
 * Baseado em: http://www.java2s.com/Code/Java/Swing-JFC/extendsListCellRenderertodisplayicons.htm
 */
public class RockandRollRenderer implements ListCellRenderer{
    protected final ListCellRenderer defaultListRenderer;

    
    public RockandRollRenderer(ListCellRenderer defaultCellRenderer){
        this.defaultListRenderer = defaultCellRenderer;
    }
    
    public RockandRollRenderer(){
        this.defaultListRenderer = new DefaultListCellRenderer();
    }
        
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) this.defaultListRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if(SystemManager.isArquivo(((RockandRollList) list).getCurrentPath().getAbsolutePath() + SystemManager.osSeparator() +(String) value)){
            renderer.setIcon(new ImageIcon(this.getClass().getResource("../Sprites/pic_tryp.png")));
        }
        else if(SystemManager.isDiretorio(((RockandRollList) list).getCurrentPath().getAbsolutePath() + SystemManager.osSeparator() +(String) value)){
            renderer.setIcon(new ImageIcon(this.getClass().getResource("../Sprites/path_tryp.png")));
        }
        
        return renderer;
    }

    public ListCellRenderer getDefaultListRenderer() {
        return this.defaultListRenderer;
    } 
}
