package Main_Package;

/**
 * @date 08/26/2012
 * @author João Paulo Herrera
 */

import Adaptacao.Elemento;
import Adaptacao.SystemManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Classe responsável no tratamento de imagens.
 */

public class ImageProperties {
    
    private BufferedImage imageBuffer;
    private String filePath;
    private String path;
    private String fileName;
    private String fileExtension;
    private ImageTools imageTools;
    private LinkedList<Elemento> elementos;
    private boolean isTemp;
            
    public ImageProperties(String fileName){
        this.filePath = fileName;
        this.path = fileName.substring(0,fileName.lastIndexOf(SystemManager.osSeparator()));
        this.fileName = fileName.substring(fileName.lastIndexOf(SystemManager.osSeparator())+1,fileName.lastIndexOf("."));
        this.fileExtension = fileName.substring(fileName.lastIndexOf("."));
        this.isTemp = false;
    }

    public boolean imageRead(){
        try{
            this.imageBuffer = ImageIO.read(new File(this.filePath));
            this.imageTools = new ImageTools(ImageIO.read(new File(this.filePath)));
            this.imageTools.getImageDataStructure().setImagePixels(this.imageTools.getImageProcessing().getData().getBackupImage());
        }
        catch(FileNotFoundException obj){ 
            JOptionPane.showMessageDialog( null, obj.getStackTrace(), "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        catch(IOException obj){
            JOptionPane.showMessageDialog( null, obj.getStackTrace(), "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            return true;
        }
    }
    
    public boolean imageWrite(){
        try{
            File curFile = new File(this.filePath);
            
            if(isTemp){
                curFile.deleteOnExit();
            }
            
            ImageIO.write(imageBuffer,"jpg", curFile);
        }
        catch(IOException obj){
            JOptionPane.showMessageDialog( null, obj.getStackTrace(), "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            return true;
        }
    }

    public boolean isIsTemp() {
        return this.isTemp;
    }

    public void setIsTemp(boolean isTemp) {
        this.isTemp = isTemp;
    }
        
    public void setFilePath(String filePath){
        this.filePath = filePath + this.fileExtension;
    }
    
    public String getFilePath(){
        return this.filePath;
    }
    
    public String getFileName(){
        return this.fileName;
    }
    
    public void setFileExtension(String newExtension){
        this.fileExtension = newExtension;
    }
    
    public BufferedImage getImage(){
        return this.imageBuffer;
    }

    public void setImage(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
    }
    
    public ImageTools getImageTools(){
        return this.imageTools;
    }

    public LinkedList<Elemento> getElementos() {
        return this.elementos;
    }

    public void setElementos(LinkedList<Elemento> elementos) {
        this.elementos = elementos;
    }
 
 }
