package Adaptacao;

import Main_Package.ImageProperties;
import Main_Package.InitParameters;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @date 13/01/2014
 * @author João
 */
public class SystemManager {
    private LinkedList<String> filters;
    
    public SystemManager(){
        this.filters = new LinkedList<>();
        
        this.filters.add(".jpg");
        this.filters.add(".png");
        this.filters.add(".tif");
    }
    
    public LinkedList<File> getChildrensDir(File currentPath){
        LinkedList<File> childrenDirectories = new LinkedList();
        
        try{
            for(File children : currentPath.listFiles()){
                if(children.isDirectory() && (!children.isHidden()))
                    childrenDirectories.add(children);
            }
        }
        catch(NullPointerException exption){
            childrenDirectories = null;
            JOptionPane.showMessageDialog(new Frame(), currentPath.getPath() + " não está acessível", "Erro de Acesso",JOptionPane.ERROR_MESSAGE);
        }
        
        return childrenDirectories;
    }
    
    public LinkedList<File> getChildrensFile(File currentPath){
        LinkedList<File> childrenDirectories = new LinkedList();
        
        try{
            for(File children : currentPath.listFiles()){
                if(children.isFile() && (!children.isHidden())){
                    String fileExtension = this.getExtension(children);
                    if(fileExtension != null && this.filters.contains(fileExtension))
                        childrenDirectories.add(children);
                }
            }
        }
        catch(NullPointerException exption){
            childrenDirectories = null;
            JOptionPane.showMessageDialog(new Frame(), currentPath.getPath() + " não está acessível", "Erro de Acesso",JOptionPane.ERROR_MESSAGE);
        }
        
        return childrenDirectories;
    }
    
    public String[] makeConvert(Object unrecognized){
        String[] pathString;
        int      iterator = 0;
        
        if(unrecognized != null && unrecognized.getClass() == LinkedList.class){
            LinkedList<File> childrens = (LinkedList<File>) unrecognized;
            pathString = new String[childrens.size()];
            
            for(int i=0; i<pathString.length; i++){
                pathString[iterator++] = InitParameters.getOS().contains("win") ? childrens.remove().getPath() : childrens.remove().getName();
            }
        }
        else{
            pathString = new String[0];
        }
            
        return pathString;
    }
    
    public LinkedList<File> getFilesFromDirectory(File directory){
        
        if(!SystemManager.isDiretorio(directory.getAbsolutePath())){
            return null;
        }  
        
        return this.getChildrensFile(directory);
    }
    
    public String getExtension(File file){
        if(file.isFile() && file.getName().lastIndexOf(".") > 0){
            return file.getName().substring(file.getName().lastIndexOf("."));
        }
        
        return null;
    }
    
    public static String osSeparator(){
        return InitParameters.getOS().contains("win") ? "\\" : "/";
    }
    
    public static boolean isDiretorio(String path){
        return(new File(path).isDirectory());
    }
    
    public static boolean isArquivo(String path){
        return(new File(path).isFile());
    }
    
    public boolean removeExtensionFromFilter(String extension){
        return this.filters.remove(extension);
    }
    
    public boolean addExtensionToFilter(String extension){
        if(!this.filters.contains(extension))
            return this.filters.add(extension);
        
        return false;
    }

    public LinkedList<String> getFilters() {
        return this.filters;
    }

    public void setFilters(LinkedList<String> filters) {
        this.filters = filters;
    }
    
    public static String formatName(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
    
    public static String formatExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+ 1, fileName.length());
    }
    
    public LinkedList<File> getFilesWExtension(File diretorio, String extension){
        LinkedList<File> child;
        LinkedList<File> files = new LinkedList<>();
        
        child = this.getChildrensFile(diretorio);
        
        for(File file : child){
            if(this.getExtension(file).equals(extension)){
                files.add(file);
            }
        }
        
        return files;
    }
    
    public void sortByLastModifield(LinkedList<File> files){
        Collections.sort(files, new CompareFiles());
    }
    
    private class CompareFiles implements Comparator<File>{

        @Override
        public int compare(File o1, File o2) {
            if(o1.lastModified() == o2.lastModified())
                return 0;
            
            else if(o1.lastModified() < o2.lastModified())
                return 1;
            
            else
                return -1;
        }
    }
    
    //Cria um diretório base em que os resultados (imagens identificadas) serão armazenadas temporariamente.
    public File createBaseDirectory(){
        DateFormat curFormatDate  = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date curDate = new Date();
        
        File directory = new File("rst/" + curFormatDate.format(curDate));
        directory.mkdir();
        directory.deleteOnExit();
        
        return directory;        
    }
    
    //Salva uma imagem temporaria para ser visualizada
    public void saveTempImg(ImageProperties curImg, String baseDir){
        curImg.setFilePath(baseDir + "/" + curImg.getFileName());
        curImg.setIsTemp(true);
        curImg.imageWrite();
    }
}
