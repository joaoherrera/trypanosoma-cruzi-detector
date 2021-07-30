/*
 * Responsavel pelo treinamento da rede neural
 */

package Main_Package.GraphicalUserInterface;

import Ferramentas_Extras.RockandRollImageView;
import static Main_Package.GraphicalUserInterface.GUI.CheckResolution;
import Main_Package.ImageProperties;
import Main_Package.Trainning;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;

/**
 * @date 02/05/2014
 * @author João
 */

public class GUITrainning extends JFrame{
    private final ImageProperties  image;
    private RockandRollImageView   imgSupport;
    private JScrollPane            scroll;
    private JPanel                 rightPanel;
    private JPanel                 subPanel;
    private JPanel                 btnPanel;
    private JPanel                 zoomPanel;
    private JPanel                 remakePanel;
    private JPanel                 undoPanel;
    private JPanel                 okButtonPanel;
    private JButton                cell;
    private JButton                parasites;
    private JButton                defects;
    private JButton                aglomeracao;
    private JButton                remake;
    private JButton                undo;
    private JButton                okButton;
    private JSlider                zoom;
    private JLabel                 infLabel;
    private JLabel                 supLabel;
    private JLabel                 limitLabel;
    private JFormattedTextField    infText;
    private JFormattedTextField    supText;
    private NumberFormat           numberFormat;
    private Trainning              trainning;
    private ImageIcon              imgIcon;
    private int                    menuButton;
    
    public static final int ELEMENT_PRESSED = 0;
    public static final int CELL_PRESSED = 0;
    public static final int PARASITE_PRESSED = 1;
    public static final int DEFECT_PRESSED = 2;
    public static final int AGLOMERACAO_PRESSED = 3;
    
    public GUITrainning(ImageProperties image){       
        this.image = image;
        this.image.imageRead();
        
        this.setLayout(new MigLayout());
        this.setFrameSize(this);       
        this.setTitle("Treinamento - Analise Trypanosoma");
        this.createComponents();
        this.setSize(new Dimension(this.getSize().width + this.getSize().width / 5, this.getSize().height +60));
        this.setResizable(false);
        this.setVisible(true);
    }
    
    private void createComponents(){
        this.imgIcon       = new ImageIcon(this.image.getFilePath());
        this.imgSupport    = new RockandRollImageView(this.imgIcon);
        this.trainning     = new Trainning(this.image);
        this.rightPanel    = new JPanel(new MigLayout());
        this.subPanel      = new JPanel(new MigLayout("insets 0 0 0 10"));
        this.btnPanel      = new JPanel(new MigLayout("insets 30 0 0 0"));
        this.zoomPanel     = new JPanel(new MigLayout("insets 30 0 0 0"));
        this.remakePanel   = new JPanel(new MigLayout("insets 30 0 50 0"));
        this.undoPanel     = new JPanel(new MigLayout("insets 30 0 5 0"));
        this.okButtonPanel = new JPanel(new MigLayout("insets 170 0 0 0"));
        this.scroll        = new JScrollPane(this.imgSupport);
        this.cell          = new JButton("Núcleo");
        this.parasites     = new JButton("Amastigota");
        this.defects       = new JButton("Defeitos");
        this.aglomeracao   = new JButton("Aglomerações");
        this.remake        = new JButton("Limpar");
        this.undo          = new JButton("Desfazer");
        this.okButton      = new JButton("Ok");
        this.zoom          = new JSlider(0, 100, 0);
        this.infLabel      = new JLabel("Inferior: ");
        this.supLabel      = new JLabel("Superior: ");
        this.limitLabel    = new JLabel("Limites");
        this.numberFormat  = NumberFormat.getNumberInstance();
        this.numberFormat.setMaximumIntegerDigits(2);
        this.infText       = new JFormattedTextField(this.numberFormat);
        this.supText       = new JFormattedTextField(this.numberFormat);
                
        //this.rightPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.scroll.setBorder(null);
        
        this.infText.setText("20");
        this.supText.setText("20");
        
        this.zoom.setPaintTrack(true);
        this.zoom.setPaintLabels(true);
        this.zoom.setPaintTicks(true);
        this.zoom.setMajorTickSpacing(50);
        
        this.implementsButtonsActions();
        this.implementsImageActions();
        this.implementsZoomAction();
        
        this.subPanel.add(this.limitLabel, "wrap");
        this.subPanel.add(this.infLabel);
        this.subPanel.add(this.infText, "width 30:30:30, wrap");
        this.subPanel.add(this.supLabel);
        this.subPanel.add(this.supText, "width 30:30:30");
        
        this.btnPanel.add(this.cell, "span, wrap, width "+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                            ", height "+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5);
        
        this.btnPanel.add(this.parasites, "span, wrap,width "+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                            ", height "+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5);
        
        this.btnPanel.add(this.defects, "span, wrap, width "+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                            ", height "+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5);
        
        this.btnPanel.add(this.aglomeracao, "span, wrap, width "+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getWidth() * 1.5 +
                                            ", height "+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.parasites.getPreferredSize().getHeight()* 1.5);
        
        this.zoomPanel.add(this.zoom);
        
        this.undoPanel.add(this.undo, "wrap, span,width "  + this.undo.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.undo.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.undo.getPreferredSize().getWidth() * 1.5 +
                                            ", height "+ this.undo.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.undo.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.undo.getPreferredSize().getHeight()* 1.5);
        
        this.remakePanel.add(this.remake, "span,width "+ this.remake.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.remake.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.remake.getPreferredSize().getWidth() * 1.5 +
                                            ", height "+ this.remake.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.remake.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.remake.getPreferredSize().getHeight()* 1.5);
        
        this.okButtonPanel.add(this.okButton, "span,width "+ this.okButton.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.okButton.getPreferredSize().getWidth() * 1.5 +
                                                    ":"+ this.okButton.getPreferredSize().getWidth() * 1.5 +
                                            ", height "+ this.okButton.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.okButton.getPreferredSize().getHeight()* 1.5 +
                                                    ":"+ this.okButton.getPreferredSize().getHeight()* 1.5);
        
        this.rightPanel.add(this.subPanel, "wrap");
        this.rightPanel.add(this.btnPanel, "wrap");
        this.rightPanel.add(this.zoomPanel, "wrap");
        this.rightPanel.add(this.undoPanel, "wrap");
        this.rightPanel.add(this.remakePanel, "wrap");
        this.rightPanel.add(this.okButtonPanel);
        
       // this.rightPanel.add(cell, "wrap, width "+ this.getSize().width / 8 +":"+ this.getSize().width / 8 +":"+ this.getSize().width / 8);
        //this.rightPanel.add(parasites,  "width "+ this.getSize().width / 8 +":"+ this.getSize().width / 8 +":"+ this.getSize().width / 8);
        
        this.add(this.scroll, "width "+ this.getSize().width + ":"+ this.getSize().width +":"+ this.getSize().width);
        this.add(this.rightPanel, "width "+ this.getSize().width / 6 + 
                                       ":"+ this.getSize().width / 6 +
                                       ":"+ this.getSize().width / 6 +
                               ", height "+ this.getSize().height    + 
                                       ":"+ this.getSize().height    +
                                       ":"+ this.getSize().height);                
    }
    
    private void implementsZoomAction(){
        this.zoom.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent actZoom) {
                int percX;
                int percY;
                
                if(zoom.getValue() > 0){
                    percX = (zoom.getValue()) * image.getImage().getWidth()/100;
                    percY = (zoom.getValue()) * image.getImage().getHeight()/100;
                }
                else{
                    percX = 0;
                    percY = 0;
                }
                
                imgSupport.zoom(percX, percY);
                imgSupport.revalidate();
            }
        });
    }
    
    private void implementsButtonsActions(){
        this.cell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }              
                
                menuButton = GUITrainning.CELL_PRESSED;
            }
        });
        
        this.parasites.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }              
                
                menuButton = GUITrainning.PARASITE_PRESSED;
            }
        });
        
        this.defects.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }              
                
                menuButton = GUITrainning.DEFECT_PRESSED;
            }
        });
        
        this.aglomeracao.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }              
                
                menuButton = GUITrainning.AGLOMERACAO_PRESSED;
            }
        });
    
        this.undo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }       
                
                trainning.undoModif();
                imgSupport.repaint();
                
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }       
            }
        });
        
        this.remake.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }       
                
                trainning.remake();
                imgSupport.repaint();
                
                if(imgSupport.getCursor() != Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)){
                    imgSupport.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }    
            }
        });
        
        this.okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                trainning.prepObj();
                dispose();
            }
        });
    }
    
    private void implementsImageActions(){
        
        this.imgSupport.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent object) {
                
                //É preciso descontar o tamanho do panel para caso a imagem não seja do tamanho max.                
                if(menuButton == GUITrainning.CELL_PRESSED){
                    if(imgSupport.getScaleX() == 0 && imgSupport.getScaleY() == 0){
                        trainning.addCell(object.getX() - ((imgSupport.getWidth() - imgSupport.getIcon().getIconWidth())/2),
                                          object.getY() - ((imgSupport.getHeight() - imgSupport.getIcon().getIconHeight())/2), 
                                          Integer.parseInt(infText.getText()), 
                                          Integer.parseInt(supText.getText()));
                    }
                    else{
                        trainning.addCell(imgSupport.convertScale(object.getX(), 
                                          imgSupport.getIcon().getIconWidth() + imgSupport.getScaleX(), 
                                          imgSupport.getIcon().getIconWidth()), 
                                          imgSupport.convertScale(object.getY(), 
                                          imgSupport.getIcon().getIconHeight()+ imgSupport.getScaleY(), 
                                          imgSupport.getIcon().getIconHeight()),
                                          Integer.parseInt(infText.getText()), 
                                          Integer.parseInt(supText.getText()));
                    }
                }
                else if(menuButton == GUITrainning.PARASITE_PRESSED){
                    if(imgSupport.getScaleX() == 0 && imgSupport.getScaleY() == 0){
                        trainning.addParasite(object.getX() - ((imgSupport.getWidth() - imgSupport.getIcon().getIconWidth())/2),
                                              object.getY() - ((imgSupport.getHeight() - imgSupport.getIcon().getIconHeight())/2), 
                                              Integer.parseInt(infText.getText()), 
                                              Integer.parseInt(supText.getText()));
                    }
                    else{
                        trainning.addParasite(imgSupport.convertScale(object.getX(), 
                                              imgSupport.getIcon().getIconWidth() + imgSupport.getScaleX(), 
                                              imgSupport.getIcon().getIconWidth()), 
                                              imgSupport.convertScale(object.getY(), 
                                              imgSupport.getIcon().getIconHeight()+ imgSupport.getScaleY(), 
                                              imgSupport.getIcon().getIconHeight()),
                                              Integer.parseInt(infText.getText()), 
                                              Integer.parseInt(supText.getText()));
                    }
                }
                else if(menuButton == GUITrainning.DEFECT_PRESSED){
                    if(imgSupport.getScaleX() == 0 && imgSupport.getScaleY() == 0){
                        trainning.addDefects(object.getX() - ((imgSupport.getWidth() - imgSupport.getIcon().getIconWidth())/2),
                                              object.getY() - ((imgSupport.getHeight() - imgSupport.getIcon().getIconHeight())/2), 
                                              Integer.parseInt(infText.getText()), 
                                              Integer.parseInt(supText.getText()));
                    }
                    else{
                        trainning.addDefects(imgSupport.convertScale(object.getX(), 
                                              imgSupport.getIcon().getIconWidth() + imgSupport.getScaleX(), 
                                              imgSupport.getIcon().getIconWidth()), 
                                              imgSupport.convertScale(object.getY(), 
                                              imgSupport.getIcon().getIconHeight()+ imgSupport.getScaleY(), 
                                              imgSupport.getIcon().getIconHeight()),
                                              Integer.parseInt(infText.getText()), 
                                              Integer.parseInt(supText.getText()));
                    }
                }      
                else if(menuButton == GUITrainning.AGLOMERACAO_PRESSED){
                    if(imgSupport.getScaleX() == 0 && imgSupport.getScaleY() == 0){
                        trainning.addAglomeracoes(object.getX() - ((imgSupport.getWidth() - imgSupport.getIcon().getIconWidth())/2),
                                              object.getY() - ((imgSupport.getHeight() - imgSupport.getIcon().getIconHeight())/2), 
                                              Integer.parseInt(infText.getText()), 
                                              Integer.parseInt(supText.getText()));
                    }
                    else{
                        trainning.addAglomeracoes(imgSupport.convertScale(object.getX(), 
                                              imgSupport.getIcon().getIconWidth() + imgSupport.getScaleX(), 
                                              imgSupport.getIcon().getIconWidth()), 
                                              imgSupport.convertScale(object.getY(), 
                                              imgSupport.getIcon().getIconHeight()+ imgSupport.getScaleY(), 
                                              imgSupport.getIcon().getIconHeight()),
                                              Integer.parseInt(infText.getText()), 
                                              Integer.parseInt(supText.getText()));
                    }
                }      

                imgIcon.setImage(image.getImage());
                imgSupport.repaint();
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
    
    private boolean setFrameSize(GUITrainning frame){
        Insets in = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());  
        Dimension dimensao = Toolkit.getDefaultToolkit().getScreenSize();
        
        int difference = CheckResolution();
        int height = dimensao.height - (in.top + in.bottom);
        int width = dimensao.width - difference;
        
        frame.setSize(width - 150,height - 150);                         
        frame.setLocation(((dimensao.width - difference) - frame.getSize().width)/2 , (dimensao.height - frame.getSize().height-60)/2); //Posição JFrame
                            
        return true;       
    }
}
