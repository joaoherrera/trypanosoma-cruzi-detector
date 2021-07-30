package Main_Package.GraphicalUserInterface;

import Adaptacao.SystemManager;
import Ferramentas_Extras.RockandRollButton;
import Ferramentas_Extras.RockandRollCheckListManager;
import Ferramentas_Extras.RockandRollImageView;
import Ferramentas_Extras.RockandRollList;
import Ferramentas_Extras.RockandRollProgressBar;
import Main_Package.PreProcessingTools;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

/**
 * @date 19/06/2014
 * @author João
 * 
 * Classe que automatiza a identificação dos elementos
 */

public class GUIAutoIdentificacao extends JPanel{
    
    private File diretorioAtual;
    private File diretorioBkp;
    private LinkedList<File> arquivos;
    private LinkedList<File> arqSelecionado;
    private final SystemManager sysMgr;
    private PreProcessingTools ppTools;
    private RockandRollList list;
    private RockandRollCheckListManager checkList;
    private RockandRollImageView imgView;
    private JScrollPane scrollPane;
    private JScrollPane scrollPaneView;
    private RockandRollProgressBar progressBar;
    private Thread exec;

    //Panels
    private JPanel bckGrd;
    private JPanel left;
    private JPanel right;
    private JPanel rightDown;
    private JPanel ldown;
    private JPanel imgTypePanel;
    private JPanel imgTypeBoxPanel;
    private JPanel boxProp;
    private JPanel buttonPanel;
    private JPanel buttonP1;
    private JPanel buttonP2;
    private JPanel buttonP3;
    private JPanel buttonP4;
    private JPanel buttonP5;
    private JPanel footer;
    private JPanel footerL;
    private JPanel footerR;
    
    //Checkboxes
    private JCheckBox cJPEG;
    private JCheckBox cPNG;
    private JCheckBox cTIFF;    
    
    //Botões
    private JButton bIdentificar;
    private JButton bRefazer;
    private JButton bcheckAll;
    private JButton buncheckAll;
    private RockandRollButton viewOrigImg;

    public GUIAutoIdentificacao(File diretorio){
        this.diretorioAtual = diretorio;
        this.diretorioBkp   = diretorio;
        this.sysMgr    = new SystemManager();
        this.arquivos  = this.sysMgr.getFilesFromDirectory(diretorio);
        
        if(this.arquivos.isEmpty()){
            JOptionPane.showMessageDialog(null, "Não há imagens neste diretório", "Diretório vazio", JOptionPane.ERROR_MESSAGE);
        }
        else{
            this.setLayout(new BorderLayout());
            this.createComponents();
        }
    }
    
    private void createComponents(){
        this.bckGrd          = new JPanel(new GridLayout(1,2));
        this.left            = new JPanel(new BorderLayout());
        this.right           = new JPanel(new BorderLayout());
        this.ldown           = new JPanel(new BorderLayout());
        this.imgTypePanel    = new JPanel(new FlowLayout());
        this.imgTypeBoxPanel = new JPanel(new MigLayout());
        this.boxProp         = new JPanel(new GridLayout(1,2));
        this.buttonPanel     = new JPanel(new MigLayout()); 
        this.buttonP1        = new JPanel(new MigLayout()); 
        this.buttonP2        = new JPanel(new MigLayout()); 
        this.buttonP3        = new JPanel(new MigLayout()); 
        this.buttonP4        = new JPanel(new MigLayout());
        this.buttonP5        = new JPanel(new MigLayout());
        this.rightDown       = new JPanel(new BorderLayout());
        
        this.cJPEG     = new JCheckBox(".JPEG");
        this.cPNG      = new JCheckBox(".PNG");
        this.cTIFF     = new JCheckBox(".TIFF");
        
        this.list       = new RockandRollList(this.diretorioAtual.getAbsolutePath(), RockandRollList.ONLY_FILE);
        this.checkList  = new RockandRollCheckListManager(this.list);
        this.checkList.allChecked();
        this.imgView    = new RockandRollImageView(new ImageIcon(this.arquivos.getFirst().getAbsolutePath()));        
        this.scrollPane = new JScrollPane(this.list);
        this.scrollPaneView = new JScrollPane(this.imgView);

        this.cJPEG.setBorderPaintedFlat(true);
        this.cPNG.setBorderPaintedFlat(true);
        this.cTIFF.setBorderPaintedFlat(true);
        
        this.cJPEG.setSelected(true);
        this.cPNG.setSelected(true);
        this.cTIFF.setSelected(true);
        
        this.cJPEG.setFocusable(false);
        this.cPNG.setFocusable(false);
        this.cTIFF.setFocusable(false);
        
        this.bIdentificar   = new JButton("Identificar");
        this.bRefazer       = new JButton("Refazer");
        this.bcheckAll      = new JButton(new ImageIcon(this.getClass().getResource("/Sprites/check_tryp.png")));
        this.buncheckAll    = new JButton(new ImageIcon(this.getClass().getResource("/Sprites/uncheck_tryp.png")));
        this.viewOrigImg    = new RockandRollButton(new ImageIcon(this.getClass().getResource("/Sprites/origImg_tryp.png")));
        
        this.bIdentificar.setFocusable(false);
        this.bRefazer.setFocusable(false);
        this.bcheckAll.setFocusable(false);
        this.buncheckAll.setFocusable(false);
        this.viewOrigImg.setFocusable(false);
        
        this.bRefazer.setEnabled(false);
        this.viewOrigImg.setEnabled(false);
        
        this.bcheckAll.setToolTipText("Marcar tudo");
        this.buncheckAll.setToolTipText("Desmarcar tudo");
        this.viewOrigImg.setToolTipText("Visualizar original");
        
        this.buttonP1.add(this.bIdentificar);
        this.buttonP2.add(this.bRefazer);
        this.buttonP3.add(this.bcheckAll);
        this.buttonP4.add(this.buncheckAll);
        this.buttonP5.add(this.viewOrigImg);
        
        this.list.setBorder(BorderFactory.createEtchedBorder());
        this.boxProp.setBorder(BorderFactory.createEtchedBorder());
        
        this.progressBar = new RockandRollProgressBar();
        this.progressBar.run();
        
        this.footer  = new JPanel(new BorderLayout());
        this.footerL = new JPanel(new MigLayout());
        this.footerR = new JPanel(new MigLayout());
   
        this.footerL.add(this.progressBar.getCurProcess());
        this.footerL.add(this.progressBar.getCurProcessImgName());
        this.footerR.add(this.progressBar.getProcessoLabel());
        this.footerR.add(this.progressBar.getProgressBar());
        this.footer.add(this.footerL, BorderLayout.WEST);
        this.footer.add(this.footerR, BorderLayout.EAST);
        
        this.footer.setBorder(BorderFactory.createEtchedBorder());
        
        this.imgTypePanel.add(this.cJPEG);
        this.imgTypePanel.add(this.cPNG);
        this.imgTypePanel.add(this.cTIFF);
        this.imgTypeBoxPanel.add(this.imgTypePanel);
        
        this.buttonPanel.add(this.buttonP3);
        this.buttonPanel.add(this.buttonP4);
        this.buttonPanel.add(this.buttonP1);
        this.buttonPanel.add(this.buttonP2);
        this.buttonPanel.add(this.buttonP5);

        this.boxProp.add(this.imgTypeBoxPanel);
        this.boxProp.add(this.buttonPanel);
        
        this.rightDown.add(this.scrollPaneView);
        
        this.ldown.add(this.scrollPane);
        this.right.add(this.rightDown);
        
        this.left.add(this.boxProp, BorderLayout.NORTH);
        this.left.add(this.ldown);
        this.left.add(this.footer, BorderLayout.SOUTH);
        this.bckGrd.add(this.left);
        this.bckGrd.add(this.right);
        
        this.add(this.bckGrd);
        this.buttonActions();
        this.checkBoxActions();
        this.listListener();
    }
    
    private void listListener(){
        this.list.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(!viewOrigImg.isPressed()){
                    imgView.setIcon(new ImageIcon(arquivos.get(list.getSelectedIndex()).getAbsolutePath()));
                }
                else{
                    for(File origImgs : sysMgr.getFilesFromDirectory(diretorioBkp)){
                        if(arquivos.get(list.getSelectedIndex()).getName().equals(origImgs.getName())){
                            imgView.setIcon(new ImageIcon(origImgs.getAbsolutePath()));
                        }
                    }
                }
                
                imgView.revalidate();
            }

            @Override
            public void mousePressed(MouseEvent e){}

            @Override
            public void mouseReleased(MouseEvent e){}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }
    
    private void buttonActions(){
        this.bcheckAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkList.allChecked();
                list.update(list.getGraphics());
            }
        });
        
        this.buncheckAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkList.allUnchecked();
                list.update(list.getGraphics());
            }
        });
        
        this.viewOrigImg.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!viewOrigImg.isPressed()){
                    viewOrigImg.setPressed(true);
                    viewOrigImg.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/origImgEd_tryp.png")));
                    viewOrigImg.setToolTipText("Visualizar identificada");
                }
                else{
                    viewOrigImg.setPressed(false);
                    viewOrigImg.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/origImg_tryp.png")));
                    viewOrigImg.setToolTipText("Visualizar original");
                }
                       
                list.getMouseListeners()[4].mouseClicked(null);
            }
        });
        
        this.bIdentificar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){                 
                SystemManager sysMgr = new SystemManager();
                
                //Criando o dir temporario
                File baseDirectory = sysMgr.createBaseDirectory();
                arqSelecionado = getActivated();
                progressBar.setArquivos(arqSelecionado);
                
                ppTools = new PreProcessingTools(arqSelecionado);
                ppTools.createAutoThreshold();
                ppTools.setProgressBar(progressBar);
                ppTools.setTempPath(baseDirectory.getAbsolutePath());
                
                bIdentificar.setEnabled(false);
                
                exec = new Thread(ppTools);
                exec.start();
                
                (new Thread(new Runnable() {

                    @Override
                    public void run() {
                        synchronized(exec){
                            try {
                                exec.wait();

                                if(baseDirectory.exists() && baseDirectory.isDirectory() && baseDirectory.listFiles().length > 0){
                                    //diretorio = baseDirectory;
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            diretorioAtual = baseDirectory;
                                            list.setCurrentPath(diretorioAtual);
                                            
                                            //Atualizar a lista para apenas os itens escolhidos
                                            list.getStrModel().removeAllElements();
                                            list.checkPermissionForFiles();
                                            
                                            checkList.allChecked();
                                            checkList.getList().setSelectedIndex(0);
                                            
                                            bRefazer.setEnabled(true);
                                            viewOrigImg.setEnabled(true);
                                            
                                            arquivos = list.getSystemMgr().getFilesFromDirectory(baseDirectory); 
                                            imgView.setIcon(new ImageIcon(arquivos.getFirst().getAbsolutePath()));
                                        }
                                    });
                                   
                                }

                            } catch (InterruptedException ex) {
                                Logger.getLogger(GUIAutoIdentificacao.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                })).start();
            }
        });
        
        this.bRefazer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(exec.isAlive()){
                    exec.interrupt();
                }
                
                diretorioAtual.delete();
                diretorioAtual = diretorioBkp;
                
                ppTools.getProgressBar().getCurProcess().setText("");
                ppTools.getProgressBar().getCurProcessImgName().setText("");
                ppTools.getProgressBar().getProgressBar().setValue(0);
                
                list.setCurrentPath(diretorioAtual);
                bIdentificar.setEnabled(true);
                                            
                //Atualizar a lista para apenas os itens escolhidos
                list.getStrModel().removeAllElements();
                list.checkPermissionForFiles();
                
                checkList.allChecked();
                checkList.getList().setSelectedIndex(0);
                
                bRefazer.setEnabled(false);
                viewOrigImg.setEnabled(false);
                                            
                arquivos = list.getSystemMgr().getFilesFromDirectory(diretorioAtual); 
                imgView.setIcon(new ImageIcon(arquivos.getFirst().getAbsolutePath()));
            }
        });
    }
    
    private void findOriginalImages(boolean foward){
        if(foward){
            for(File imgPath : arqSelecionado){
                
            }
        }
    }
    
    private LinkedList<File> getActivated(){
        LinkedList<File> selectedFiles = checkList.getSelectedFiles();
        
        return selectedFiles.size() > 0 ? selectedFiles : arquivos;
    }
    
    private void checkBoxActions(){
        this.cJPEG.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(cJPEG.isSelected()){
                    list.getSystemMgr().addExtensionToFilter(".jpg");
                }
                else{
                    list.getSystemMgr().removeExtensionFromFilter(".jpg");
                }
                
                list.getStrModel().removeAllElements();
                list.checkPermissionForFiles();
                arquivos = list.getSystemMgr().getFilesFromDirectory(diretorioAtual);
            }
        });
        
        this.cPNG.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(cPNG.isSelected()){
                    list.getSystemMgr().addExtensionToFilter(".png");
                }
                else{
                    list.getSystemMgr().removeExtensionFromFilter(".png");
                }
                
                list.getStrModel().removeAllElements();
                list.checkPermissionForFiles();
                arquivos = list.getSystemMgr().getFilesFromDirectory(diretorioAtual);
            }
        });
        
        this.cTIFF.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(cTIFF.isSelected()){
                    list.getSystemMgr().addExtensionToFilter(".tif");
                }
                else{
                    list.getSystemMgr().removeExtensionFromFilter(".tif");
                }
                
                list.getStrModel().removeAllElements();
                list.checkPermissionForFiles();
                arquivos = list.getSystemMgr().getFilesFromDirectory(diretorioAtual);
            }
        });
    }

    public LinkedList<File> getArquivos() {
        return this.arquivos;
    }

}
