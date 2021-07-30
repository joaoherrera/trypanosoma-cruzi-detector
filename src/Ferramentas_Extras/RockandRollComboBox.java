package Ferramentas_Extras;

import Adaptacao.SystemManager;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JComboBox;

/**
 * @date 13/01/2014
 * @author João
 */

public class RockandRollComboBox extends JComboBox<String>{
    private File                currentPath;
    private final SystemManager systemMgr;
    private RockandRollList     fileList;
            
    public RockandRollComboBox(String defaultPath){
        LinkedList child;
        
        this.currentPath = new File(defaultPath);
        this.systemMgr   = new SystemManager();
       
        if((child = this.systemMgr.getChildrensDir(this.currentPath)) != null){
            this.addItem(defaultPath);
            this.populate(this.systemMgr.makeConvert(child));
        }
    }
    
    @Override
    public void setSelectedItem(Object topItem) {
        LinkedList child;
        File       newPath;
        String     selectedItem;
        
        if(topItem != null){
            selectedItem = topItem.toString();
            
            if(!this.currentPath.getPath().equals(selectedItem) && !selectedItem.isEmpty()){
                
                if(!selectedItem.toString().contains(SystemManager.osSeparator())){ //Avançar diretório
                    newPath = this.getNextCurrentPath(selectedItem);
                }
                else{ //Retroceder diretório
                    newPath = (File) topItem;
                }
                
                if((child = this.systemMgr.getChildrensDir(newPath)) != null){ //Acesso permitido
                    this.currentPath = newPath;
                    this.removeAllItems();
                    this.addItem(this.currentPath.toString());
                    this.populate(this.systemMgr.makeConvert(child));
                    
                    if(this.fileList != null){
                        this.fileList.setNewLocation(this.currentPath);
                        this.fileList.checkPermission();
                    }
                }
            }
        }   
    }
    
    public File getNextCurrentPath(String newLocation) {
       return new File(this.currentPath.getAbsolutePath() + SystemManager.osSeparator() + newLocation);
    }
    
    public File getPreviousCurrentPath(){
        int findOccur  = 0;
        int tokenCount = 0;
        String subStr  = this.currentPath.getPath();
        
        do{
            tokenCount += findOccur;
            findOccur  = subStr.indexOf(SystemManager.osSeparator()) + 1;
            subStr     = subStr.substring(findOccur);
        }while(findOccur > 0);
        
        return new File(this.currentPath.getPath().substring(0, tokenCount));
    }
    
    public String getCurrentPath(){
        return this.currentPath.getPath();
    }

    private void populate(String[] strPath){
        int findOccur = 0;
        
        for(String path : strPath){
            do{
                findOccur = path.indexOf(SystemManager.osSeparator()) + 1;
                path = path.substring(findOccur);
            }while(findOccur > 1);
            
            this.addItem(path);
        }
    }
    
    public void addComplementarList(RockandRollList list){
        this.fileList = list;               
    }    
}
