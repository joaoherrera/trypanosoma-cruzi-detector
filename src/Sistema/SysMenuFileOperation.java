package Sistema;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @date 07/12/2013
 * @author João
 */
public class SysMenuFileOperation {
    
    public static final int PICTURE = 1;
    private String PathWay;
            
    public SysMenuFileOperation() {}
    
    public String openFile(int mode){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter;
        
        switch(mode){
            case SysMenuFileOperation.PICTURE:
                filter = new FileNameExtensionFilter("Arquivos *.jpeg,*.jpg,*.png","jpeg","jpg","png");
                break;
                
            default:
                filter = new FileNameExtensionFilter("Arquivos *.jpeg,*.jpg,*.png","jpeg","jpg","png");
                break;
        }
                         
        chooser.addChoosableFileFilter(filter); //setando qual será o filtro utilizado
        chooser.setAcceptAllFileFilterUsed(false); //não será permitido qualquer extenção(Opção Todos os arquivos).
        chooser.setFileFilter(filter);
                           
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            this.PathWay = chooser.getSelectedFile().getAbsolutePath();
        
            return this.PathWay;
    }
    
    public String saveAsFile(int mode, String fileName){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter mainFilter;
        
        switch(mode){
            case SysMenuFileOperation.PICTURE:
                mainFilter = new FileNameExtensionFilter("JPEG (*.jpg,*.jpeg,*.jpe,*.jfif)","jpg");
                
                chooser.addChoosableFileFilter(mainFilter);
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG (*.png)","png"));
                break;
        }
        
        chooser.setSelectedFile(new File(fileName));
        
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            this.PathWay = chooser.getSelectedFile().getAbsolutePath();
                   
        return ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
    }

    public String getPathWay() {
        return this.PathWay;
    }

}