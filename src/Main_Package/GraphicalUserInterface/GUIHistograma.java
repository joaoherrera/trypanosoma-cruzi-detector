package Main_Package.GraphicalUserInterface;

import Adaptacao.SystemManager;
import Ferramentas_Extras.HistogramPanel;
import Ferramentas_Extras.RangeSlider;
import Ferramentas_Extras.RockandRollImageView;
import Main_Package.ImageProperties;
import Main_Package.PreProcessingTools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;

/**
 * @author João
 * @date 01/12/2013
 */

public class GUIHistograma extends JPanel{
    
    private ImageProperties      image;
    private JPanel         histFrame;
    private JPanel         histPanel;
    private JPanel         infoPanel;
    private JLabel         view;
    private HistogramPanel histRed;
    private HistogramPanel histGreen;
    private HistogramPanel histBlue;
    private ImageIcon      imgIcon;
    private RangeSlider    redSlider;
    private RangeSlider    greenSlider;
    private RangeSlider    blueSlider;
    private JButton        segm;
    private JButton        btnRemake;
    private JLabel         infThumbRed;
    private JLabel         supThumbRed;
    private JLabel         infThumbGreen;
    private JLabel         supThumbGreen;
    private JLabel         infThumbBlue;
    private JLabel         supThumbBlue;
    private JTextField     infTextRed;
    private JTextField     supTextRed;
    private JTextField     infTextGreen;
    private JTextField     supTextGreen;
    private JTextField     infTextBlue;
    private JTextField     supTextBlue;
    JScrollPane            scroll;
    private Thread         process;
    
    public GUIHistograma(ImageProperties image){
        this.image = image;
        this.setLayout(new GridLayout(1,2));
        this.initComponents();
    }
    
    private void initComponents(){
        this.imgIcon = new ImageIcon(image.getFilePath());
        this.view    = new RockandRollImageView(this.imgIcon);
        
        this.view.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.scroll = new JScrollPane(this.view);
        
        this.infoPanel = new JPanel(new MigLayout("insets 35 10 0 0"));
        this.histFrame = new JPanel(new GridLayout(1,3));
        this.histFrame.setBackground(Color.red);
        
        this.histPanel = new JPanel();
        this.histPanel.setLayout(new BorderLayout());
        this.histPanel.add(this.histFrame, BorderLayout.EAST);
        this.histPanel.add(this.infoPanel);
        
        this.add(this.scroll);
        this.add(this.histPanel);
  
        //Inicializar Histogramas      
        if(this.image.imageRead()){
            this.view.setIcon(new ImageIcon(image.getImageTools().getImageProcessing().getData().getImageBuffer()));
            this.histogramComponents();
            this.componentInfo();
        }
        
    }
    
    //Informações gerais
    private void componentInfo(){
        JPanel content     = new JPanel(new MigLayout("insets 10 10 10 10"));
        JPanel redPanel    = new JPanel(new MigLayout("insets 20 10 10 10"));
        JPanel greenPanel  = new JPanel(new MigLayout("insets 10 10 10 10"));
        JPanel bluePanel   = new JPanel(new MigLayout("insets 10 10 10 10"));
        JLabel option      = new JLabel("Opções");
        
        this.segm          = new JButton("Identificar");
        this.btnRemake     = new JButton("Refazer"); 
        this.infThumbRed   = new JLabel("Thumb X :");  this.infTextRed    = new JTextField("0");
        this.supThumbRed   = new JLabel("Thumb Y :");  this.supTextRed    = new JTextField("255");
        this.infThumbGreen = new JLabel("Thumb X :");  this.infTextGreen  = new JTextField("0");
        this.supThumbGreen = new JLabel("Thumb Y :");  this.supTextGreen  = new JTextField("255");
        this.infThumbBlue  = new JLabel("Thumb X :");  this.infTextBlue   = new JTextField("0");
        this.supThumbBlue  = new JLabel("Thumb Y :");  this.supTextBlue   = new JTextField("255");
        
        this.segm.setFocusable(false);
        this.btnRemake.setFocusable(false);
        
        option.setFont(new Font("Monospace", Font.ITALIC, 15));
        
        content.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        redPanel.setBorder(BorderFactory.createTitledBorder("Vermelho"));
        greenPanel.setBorder(BorderFactory.createTitledBorder("Verde"));
        bluePanel.setBorder(BorderFactory.createTitledBorder("Azul"));
        
        this.infTextRed.addActionListener(new ActionListener() {
     
            @Override
            public void actionPerformed(ActionEvent Txt) {
                JTextField Text = (JTextField) Txt.getSource();
                boolean nonDigit = false;
                
                for(int i=0; i<Text.getText().length(); i++){
                    if(!Character.isDigit(Text.getText().charAt(i))){
                        nonDigit = true;
                        break;
                    }                      
                }
             
                if(nonDigit == false && (Integer.parseInt(Text.getText()) < 0 || Integer.parseInt(Text.getText()) > 255)) {
                    JOptionPane.showMessageDialog(Text, "Valor deve estar entre 0 e 255", "Excedência de Range", JOptionPane.ERROR_MESSAGE, new ImageIcon(this.getClass().getResource("/Sprites/errorMsg_tryp.png")));
                    
                    if(Text == infTextRed)
                        Text.setText(Integer.toString(redSlider.getValue()));
                    
                    else if(Text == infTextGreen)
                        Text.setText(Integer.toString(greenSlider.getValue()));
                    
                    else if(Text == infTextBlue)
                        Text.setText(Integer.toString(blueSlider.getValue()));
                    
                    else if(Text == supTextRed)
                        Text.setText(Integer.toString(redSlider.getUpperValue()));
                    
                    else if(Text == supTextGreen)
                        Text.setText(Integer.toString(greenSlider.getUpperValue()));
                    
                    else if(Text == supTextBlue)
                        Text.setText(Integer.toString(blueSlider.getUpperValue()));
                }
                else if(nonDigit == false){          
                    
                    if(Text == infTextRed){
                        redSlider.setValue(Integer.parseInt(Text.getText()));
                        redSlider.revalidate();
                    }               
                    else if(Text == infTextGreen){
                        greenSlider.setValue(Integer.parseInt(Text.getText()));
                        greenSlider.revalidate();
                    }                   
                    else if(Text == infTextBlue){
                        blueSlider.setValue(Integer.parseInt(Text.getText()));
                        blueSlider.revalidate();
                    }
                    else if(Text == supTextRed){
                        redSlider.setUpperValue(Integer.parseInt(Text.getText()));
                        redSlider.revalidate();
                    }                   
                    else if(Text == supTextGreen){
                        greenSlider.setUpperValue(Integer.parseInt(Text.getText()));
                        greenSlider.revalidate();
                    }                    
                    else if(Text == supTextBlue){
                        blueSlider.setUpperValue(Integer.parseInt(Text.getText()));
                        blueSlider.revalidate();
                    }
                }
                
                if(nonDigit){
                    if(Text == infTextRed)
                        Text.setText(Integer.toString(redSlider.getValue()));
                    
                    else if(Text == infTextGreen)
                        Text.setText(Integer.toString(greenSlider.getValue()));
                    
                    else if(Text == infTextBlue)
                        Text.setText(Integer.toString(blueSlider.getValue()));
                    
                    else if(Text == supTextRed)
                        Text.setText(Integer.toString(redSlider.getUpperValue()));
                    
                    else if(Text == supTextGreen)
                        Text.setText(Integer.toString(greenSlider.getUpperValue()));
                    
                    else if(Text == supTextBlue)
                        Text.setText(Integer.toString(blueSlider.getUpperValue()));
                }
            }
        });
            
        this.supTextRed.addActionListener(this.infTextRed.getActionListeners()[0]);
        this.infTextGreen.addActionListener(this.infTextRed.getActionListeners()[0]);
        this.supTextGreen.addActionListener(this.infTextRed.getActionListeners()[0]);
        this.infTextBlue.addActionListener(this.infTextRed.getActionListeners()[0]);
        this.supTextBlue.addActionListener(this.infTextRed.getActionListeners()[0]);
        
        redPanel.add(this.infThumbRed);  
        redPanel.add(this.infTextRed, "wrap, width 40:40:40");   
        redPanel.add(this.supThumbRed);
        redPanel.add(this.supTextRed, "width 40:40:40");
        
        greenPanel.add(this.infThumbGreen);  
        greenPanel.add(this.infTextGreen, "wrap, width 40:40:40");   
        greenPanel.add(this.supThumbGreen);
        greenPanel.add(this.supTextGreen, "width 40:40:40");
        
        bluePanel.add(this.infThumbBlue);  
        bluePanel.add(this.infTextBlue, "wrap, width 40:40:40");   
        bluePanel.add(this.supThumbBlue);
        bluePanel.add(this.supTextBlue, "width 40:40:40");
        
        segm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent button) {
                JButton btnPressed;
                LinkedList imageList = new LinkedList();
                SystemManager sysMgr = new SystemManager();
                
                File baseDirectory = sysMgr.createBaseDirectory();
                
                imageList.add(new File(image.getFilePath()));
                PreProcessingTools ppTools = new PreProcessingTools(image, imageList);
                ppTools.setTempPath(baseDirectory.getAbsolutePath());
                
                process = new Thread(ppTools);
                process.start();
                
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized(process){
                            try{
                               process.wait();
                               
                                if(baseDirectory.exists() && baseDirectory.isDirectory() && baseDirectory.listFiles().length > 0){
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.setIcon(new ImageIcon((new SystemManager()).getFilesFromDirectory(baseDirectory).getFirst().getAbsolutePath()));
                                        }
                                    });             
                                }
                            }
                            catch (InterruptedException ex) {
                                Logger.getLogger(GUIAutoIdentificacao.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                })).start();
                //image.getImageTools().getImageProcessing().grayScale(image.getImage());
                //image.getImageTools().getImageDataStructure().setPixelBuffer(image.getImageTools().getImageProcessing().watershed(image.getImage()));               
                //image.getImageTools().getImageProcessing().identificarSegmentacao(image.getImageTools().getImageDataStructure().getPixelBuffer());
                  
               // image.setElementos(image.getImageTools().getImageDataStructure().transElemento(image.getImageTools().getImageDataStructure().encontraElemento()));
                
                /*
                redSlider.setValue(0);
                redSlider.setUpperValue(255);
                greenSlider.setValue(0);
                greenSlider.setUpperValue(255);
                blueSlider.setValue(0);
                blueSlider.setUpperValue(255);
                
                infTextRed.setText("0");
                supTextRed.setText("255");
                infTextGreen.setText("0");
                supTextGreen.setText("255");
                infTextBlue.setText("0");
                supTextBlue.setText("255");
                
                view.repaint();
                */
                btnPressed = (JButton) button.getSource();
                btnPressed.setEnabled(false);
            }
        });
        
        btnRemake.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent button) {
                image = new ImageProperties(image.getFilePath());
                image.imageRead();
                
                segm.setEnabled(true);
                view.repaint();
                histogramComponents();
            }
        });
        
        content.add(this.segm, "gapright 30");
        content.add(this.btnRemake, "span 2, wrap");
        content.add(redPanel, "wrap");
        content.add(greenPanel, "wrap");      
        content.add(bluePanel, "wrap");
        
        this.infoPanel.add(option, "wrap");
        this.infoPanel.add(content);/*, "height " + (this.histRed.getComprimento() + this.redSlider.getPreferredSize().height)
                                        + ":" + (this.histRed.getComprimento() + this.redSlider.getPreferredSize().height) 
                                        + ":" + (this.histRed.getComprimento() + this.redSlider.getPreferredSize().height));*/
    }
    
    //Configuração dos Histogramas R,G e B
    private void histogramComponents(){       
        this.histFrame.removeAll();
        this.histFrame.revalidate();
        
        this.histRed   = new HistogramPanel(image.getImageTools().createHistogram(image.getFilePath()), 0, "Histograma Vermelho");
        this.histGreen = new HistogramPanel(image.getImageTools().createHistogram(image.getFilePath()), 1, "Histograma Verde");
        this.histBlue  = new HistogramPanel(image.getImageTools().createHistogram(image.getFilePath()), 2, "Histograma Azul");
        
        JPanel rgbHistogram     = new JPanel(new GridLayout(3,1));
        JPanel redPanel         = new JPanel(new BorderLayout());
        JPanel greenPanel       = new JPanel(new BorderLayout());
        JPanel bluePanel        = new JPanel(new BorderLayout());     
        JPanel sliderRedPanel   = new JPanel(new MigLayout("insets 0 50 0 0"));
        JPanel sliderGreenPanel = new JPanel(new MigLayout("insets 0 50 0 0"));
        JPanel sliderBluePanel  = new JPanel(new MigLayout("insets 0 50 0 0"));

//        GridBagConstraints constr = new GridBagConstraints();
//        
//        constr.insets  = new Insets(0, 44, 0, 21);
//        constr.fill    = GridBagConstraints.BOTH;
//        constr.weightx = 0.3;
        
        this.redSlider   = new RangeSlider();
        this.greenSlider = new RangeSlider();
        this.blueSlider  = new RangeSlider();
        
        imgIcon.setImage(image.getImage());
        
        redSlider.setMinimum(0);
        redSlider.setMaximum(255);
        redSlider.setValue(0);
        redSlider.setUpperValue(255);
        redSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                RangeSlider slider =(RangeSlider) ce.getSource();
                image.getImageTools().getImageProcessing().manualRedPixels(slider.getValue(), slider.getUpperValue());
                infTextRed.setText(Integer.toString(redSlider.getValue()));
                supTextRed.setText(Integer.toString(redSlider.getUpperValue()));
                view.repaint();
            }
        });
        
        greenSlider.setMinimum(0);
        greenSlider.setMaximum(255);
        greenSlider.setValue(0);
        greenSlider.setUpperValue(255);
        greenSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                RangeSlider slider =(RangeSlider) ce.getSource();
                image.getImageTools().getImageProcessing().manualGreenPixels(slider.getValue(), slider.getUpperValue());
                infTextGreen.setText(Integer.toString(greenSlider.getValue()));
                supTextGreen.setText(Integer.toString(greenSlider.getUpperValue()));
                view.repaint();
            }
        });
        
        blueSlider.setMinimum(0);
        blueSlider.setMaximum(255);
        blueSlider.setValue(0);
        blueSlider.setUpperValue(255);
        blueSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                RangeSlider slider =(RangeSlider) ce.getSource();
                image.getImageTools().getImageProcessing().manualBluePixels(slider.getValue(), slider.getUpperValue());            
                infTextBlue.setText(Integer.toString(blueSlider.getValue()));
                supTextBlue.setText(Integer.toString(blueSlider.getUpperValue()));
                view.repaint();
            }
        });
        
        sliderRedPanel.add(redSlider,     "width "+ this.histRed.getComprimento() 
                                              +":"+ this.histRed.getComprimento() 
                                              +":"+ this.histRed.getComprimento());
        
        sliderGreenPanel.add(greenSlider, "width "+ this.histGreen.getComprimento() 
                                              +":"+ this.histGreen.getComprimento() 
                                              +":"+ this.histGreen.getComprimento());
        
        sliderBluePanel.add(blueSlider,   "width "+ this.histBlue.getComprimento() 
                                              +":"+ this.histBlue.getComprimento() 
                                              +":"+ this.histBlue.getComprimento());

        redPanel.add(BorderLayout.CENTER, histRed);
        greenPanel.add(BorderLayout.CENTER, histGreen);
        bluePanel.add(BorderLayout.CENTER, histBlue);
                
        redPanel.add(BorderLayout.SOUTH, sliderRedPanel);
        greenPanel.add(BorderLayout.SOUTH, sliderGreenPanel);
        bluePanel.add(BorderLayout.SOUTH, sliderBluePanel);
        
        rgbHistogram.add(redPanel);
        rgbHistogram.add(greenPanel);
        rgbHistogram.add(bluePanel);
        
        this.histFrame.add(rgbHistogram);
        this.histFrame.setSize(histRed.getComprimento(), this.histFrame.getHeight());
        image.getImageTools().getImageProcessing().AlocateBufferBinaries();
    }
    
}   
