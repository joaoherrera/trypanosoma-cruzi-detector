package Ferramentas_Extras;

import Adaptacao.SystemManager;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * @param <T>
 * @26/01/2014
 * @author João
 */
public class RockandRollList<T> extends JList<String>{
    
    private File currentPath;
    private final SystemManager systemMgr;
    private final DefaultListModel strModel;
    private RockandRollRenderer rockAndRollRenderer;
    
    public static int NORMAL = 0;
    public static int IMAGE_LABBELED = 1;
    public static int ONLY_FILE = 2;
    
    public RockandRollList(String currentPath, int model){
        this.currentPath         = new File(currentPath);
        this.strModel            = new DefaultListModel<String>();
        this.systemMgr           = new SystemManager();
        this.rockAndRollRenderer = null;
               
        this.setModel(this.strModel);
               
        switch(model){
            case 1:
                this.rockAndRollRenderer = new RockandRollRenderer(this.getCellRenderer());
                this.setCellRenderer(rockAndRollRenderer);
            break;
        }
        if(model == RockandRollList.ONLY_FILE)
            this.checkPermissionForFiles();
        else
            this.checkPermission();
    }
    
    public void checkPermission(){
        this.checkPermissionForDirectories();
        this.checkPermissionForFiles();
    }
    
    public void checkPermissionForFiles(){
        LinkedList child;
        
        if(!(child = this.systemMgr.getChildrensFile(this.currentPath)).isEmpty()){
            this.populate(this.systemMgr.makeConvert(child));
        }
    }
    
    public void checkPermissionForDirectories(){
        LinkedList child;
        
        if(!(child = this.systemMgr.getChildrensDir(this.currentPath)).isEmpty()){
            this.populate(this.systemMgr.makeConvert(child));
        }
    }
    
    public void setNewLocation(File newLocation){
        this.currentPath = newLocation;
        this.strModel.removeAllElements();
    }
    
    private void populate(String[] strFile){
        for(String file : strFile){
            this.strModel.addElement(this.formatFileName(file));
        }
    }
    
    private String formatFileName(String fileName){
        return fileName.substring(fileName.lastIndexOf(SystemManager.osSeparator()) + 1);
    }

    public File getCurrentPath() {
        return this.currentPath;
    }   

    public void setCurrentPath(File currentPath) {
        this.currentPath = currentPath;
    }

    public DefaultListModel getStrModel() {
        return this.strModel;
    }
    
    public SystemManager getSystemMgr() {
        return this.systemMgr;
    }
    
}
