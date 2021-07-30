package Main_Package.Modeling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.swing.JOptionPane;

/**
 * @author João
 * @date 17/06/2014
 */

public final class IOFunctions{
    private File file;
    private FileInputStream fInput;
    
    public IOFunctions(String fPath){      
        String dir      = fPath.substring(0, fPath.lastIndexOf("/"));
        
        if(!this.hasDirectory(dir)){
            File dirFile = new File(dir);
            dirFile.mkdir();
        }
        
        this.file = new File(fPath);
        
        if(!this.hasFile(fPath)){
            this.gravar("");
        }
    }
    
    public IOFunctions(File file){
        this.file = file;
    }

    public void gravar(String data){
        try{
            try (FileOutputStream fOutput = new FileOutputStream(this.file)) {
                fOutput.write(data.getBytes());
                fOutput.close();
            }
        }
        catch(IOException ioExc){
            JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo, verifique a localização", "Erro ao salvar o arquivo", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public String ler(){
        String content = "";
        
        try{
            this.fInput              = new FileInputStream(this.file);
            StringBuilder   fInputSB = new StringBuilder();
            BufferedReader  fInputBR = new BufferedReader(new InputStreamReader(fInput, "UTF-8"));
            
            while((content = fInputBR.readLine()) != null){
                fInputSB.append(content);
                fInputSB.append(System.lineSeparator());
            }
            
            content = fInputSB.toString();
            
            fInputBR.close();
            fInputBR.close();
        }
        catch(IOException fNotFound){
            JOptionPane.showMessageDialog(null, "Erro ao abrir o arquivo, verifique a localização", "Erro ao abrir o arquivo", JOptionPane.ERROR_MESSAGE);
        }
        
        return content;
    }
    
    //Conta o número de linhas do arquivo
    public int count(){
        String info = this.ler();
        int count = 0;
        
        while(info.length() > 0){
            info = info.substring(info.indexOf("\n")+1);
            count++;
        }
        
        return count;
    }
    
    public boolean hasDirectory(String dir){
        File diretorio;
        
        if(!(diretorio = new File(dir)).exists()){
            if(!diretorio.isDirectory()){
                return false;
            }
        }
        
        return true;
    }
    
    public boolean hasFile(String filePath){
        File fileCreated;
        
        if(!(fileCreated = new File(filePath)).exists()){
            if(!fileCreated.isFile()){
                return false;
            }
        }
        
        return true;
    }
    
    public File getFile(){
        return this.file;
    }

    public void setFile(File file){
        this.file = file;
    }

    public FileInputStream getfInput() {
        return this.fInput;
    }
    
}
