package Main_Package.GraphicalUserInterface;

/**
 * @date 08/26/2012
 * @author João Paulo Herrera
 */

import Adaptacao.SystemManager;
import Main_Package.ImageProperties;
import Sistema.SysMenuFileOperation;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel;


public class GUI extends JFrame implements ActionListener{
    
    private JMenuBar mainMenu;
    private JToolBar mainToolBar;
    private final Font standardFont;
    private ImageProperties image;
    private JPanel histograma;
    private JPanel estrutura;
    private GUIPaginaInicial pagInicial;
    private GUITrainning trainning;
    private GUIResultados resultados;
    private JFrame picFrame;
    private JButton homeButton;
    private JButton resultButton;
    private JButton trainningButton;
    private JButton analiseButton;
    private JButton identAuto;
    private JTabbedPane aba;
    
    public GUI(){
        this.setTitle("Trypanosoma cruze Detector");
        //this.setFrameSize(0, this); //Caso não queira fullscreen
        
        //this.gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        //gd.setFullScreenWindow(this); //fullscreen ativado
        this.setSize(800, 600);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setIconImage((new ImageIcon(this.getClass().getResource("/Sprites/logo_tryp.png"))).getImage());
    
        this.standardFont = new Font("calibri",Font.LAYOUT_LEFT_TO_RIGHT,14);  
        this.createComponents();
    } 
    
    private void menuBar(){
        mainMenu = new JMenuBar();
        JMenu file = new JMenu("Arquivo");
        file.setFont(standardFont);
        
        //--------------------------------- Componentes file.
        JMenuItem open = new JMenuItem("Abrir");
        open.setFont(standardFont);
        open.addActionListener(this);
        
        JMenuItem save = new JMenuItem("Salvar");
        save.setFont(standardFont);
        save.setEnabled(false);
        save.addActionListener(this);
        
        JMenuItem saveAs = new JMenuItem("Salvar como...");
        saveAs.setFont(standardFont);
        saveAs.setEnabled(false);
        saveAs.addActionListener(this);
        
        JMenuItem exit = new JMenuItem("Sair");
        exit.setFont(standardFont);
        exit.addActionListener(this);
        
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(new JSeparator());
        file.add(exit);
        file.setVisible(false);
        
        mainMenu.add(file);
    }
    
    private void toolBar(){
        this.mainToolBar = new JToolBar();
        this.mainToolBar.setPreferredSize(new Dimension(500, 100));
        this.mainToolBar.setBorderPainted(false);
        this.mainToolBar.setLayout(null);
        
        this.homeButton = new JButton("Home");
        this.homeButton.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/home_tryp.png")));
        this.homeButton.setVerticalTextPosition(JButton.BOTTOM);
        this.homeButton.setHorizontalTextPosition(JButton.CENTER);
        this.homeButton.setFocusable(false);
        this.homeButton.setBounds(0, 0, 150, 100);
        
        this.analiseButton = new JButton("Análise");
        this.analiseButton.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/hist_tryp.png")));
        this.analiseButton.setVerticalTextPosition(JButton.BOTTOM);
        this.analiseButton.setHorizontalTextPosition(JButton.CENTER);
        this.analiseButton.setFocusable(false);
        this.analiseButton.setEnabled(false);
        this.analiseButton.setBounds(150, 0, 150, 100);
        
        this.resultButton = new JButton("Resultados");
        this.resultButton.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/result_tryp.png")));
        this.resultButton.setVerticalTextPosition(JButton.BOTTOM);
        this.resultButton.setHorizontalTextPosition(JButton.CENTER);
        this.resultButton.setFocusable(false);
        this.resultButton.setEnabled(true);
        this.resultButton.setBounds(300, 0, 150, 100);
        
        this.trainningButton = new JButton("Treinamento");
        this.trainningButton.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/train_tryp.png")));
        this.trainningButton.setVerticalTextPosition(JButton.BOTTOM);
        this.trainningButton.setHorizontalTextPosition(JButton.CENTER);
        this.trainningButton.setFocusable(false);
        this.trainningButton.setEnabled(false);
        this.trainningButton.setBounds(750, 0, 150, 100);
        
        this.identAuto = new JButton("Processar");
        this.identAuto.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/iden_tryp.png")));
        this.identAuto.setVerticalTextPosition(JButton.BOTTOM);
        this.identAuto.setHorizontalTextPosition(JButton.CENTER);
        this.identAuto.setFocusable(false);
        this.identAuto.setEnabled(false);
        this.identAuto.setBounds(450, 0, 150, 100);
        
        this.actionButtonsManager();
        this.mainToolBar.add(this.homeButton);
        this.mainToolBar.add(this.analiseButton);
        this.mainToolBar.add(this.resultButton);
        this.mainToolBar.add(this.identAuto);
        this.mainToolBar.add(this.trainningButton);
    }

    private void createComponents(){
        this.menuBar();
        this.toolBar();
        
        JPanel imagem     = new JPanel();
        JPanel menuPanel  = new JPanel(new BorderLayout());
        this.aba          = new JTabbedPane();       
        
        this.pagInicial = new GUIPaginaInicial();
        this.estrutura  = new JPanel(new BorderLayout());
        //this.histograma = new GUIHistograma();
                
        this.implementRockAndRollListener();
        //this.histograma.setBorder(BorderFactory.createTitledBorder("Histograma"));
        this.estrutura.setBorder(BorderFactory.createTitledBorder("Estrutura"));
        imagem.setBorder(BorderFactory.createEtchedBorder());
        
       // this.pagInicial.add(new RockandRollFileChooser(), BorderLayout.WEST);
        
        this.aba.add(this.pagInicial,"Pagina Inicial");
       // this.activateTabbedPaneListener(this.aba);
        
        menuPanel.add(this.mainMenu, BorderLayout.NORTH);
        menuPanel.add(this.mainToolBar, BorderLayout.SOUTH);
        
        this.definirMnemonico();
        
        this.add(imagem);
        this.add(this.aba);
        this.add(menuPanel,BorderLayout.NORTH);
    }
    
    private void implementRockAndRollListener(){
        this.pagInicial.getFileBox().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2){        
                    if(SystemManager.isDiretorio(pagInicial.getFileBox().getCurrentPath().getAbsolutePath() + SystemManager.osSeparator() + pagInicial.getFileBox().getSelectedValue())){
                        pagInicial.getPathBox().setSelectedItem(pagInicial.getFileBox().getSelectedValue());
                    }
                }
                else if(me.getClickCount() == 1){
                    if(SystemManager.isDiretorio(pagInicial.getFileBox().getCurrentPath().getAbsolutePath() + SystemManager.osSeparator() + pagInicial.getFileBox().getSelectedValue())){
                        identAuto.setEnabled(true);
                        analiseButton.setEnabled(false);
                        trainningButton.setEnabled(false);
                    }
                    else if(SystemManager.isArquivo(pagInicial.getFileBox().getCurrentPath().getAbsolutePath() + SystemManager.osSeparator() + pagInicial.getFileBox().getSelectedValue())){
                        image = new ImageProperties(pagInicial.getFileBox().getCurrentPath().getAbsolutePath() + SystemManager.osSeparator() + pagInicial.getFileBox().getSelectedValue());
                        analiseButton.setEnabled(true);
                        trainningButton.setEnabled(true);
                        identAuto.setEnabled(false);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent me){}
            @Override
            public void mouseReleased(MouseEvent me){}
            @Override
            public void mouseEntered(MouseEvent me){}
            @Override
            public void mouseExited(MouseEvent me){}
        });
    }
    
    //Listener para chamar a área de histograma.
    private void actionButtonsManager(){
        this.homeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aba.setSelectedIndex(0);
            }
        });
        
        this.analiseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent){
                if(image != null){
                   histograma = new GUIHistograma(image);
                   aba.add(histograma, "Análise" + " " +image.getFileName());
                   aba.setSelectedComponent(histograma);
                }
                else{
                   //Implementar erro
                }                    
            }
        });
        
        this.trainningButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               if(trainning != null){
                   trainning.dispose();
                   trainning = null;
               }
               
               if(image != null)
                  trainning = new GUITrainning(image);
               else
                  JOptionPane.showInternalMessageDialog(null, "Erro ao abrir uma nova instancia \n Imagem nao definida", "Erro - Treinamento", JOptionPane.ERROR_MESSAGE, new ImageIcon(this.getClass().getResource("/Sprites/errorMsg_tryp.png")));        
            }
        });
        
        this.resultButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultados == null){
                    resultados = new GUIResultados();
                    aba.add(resultados, "Resultados");
                    aba.setSelectedComponent(resultados);
                }
                else
                    aba.setSelectedComponent(resultados);
            }
        });
        
        this.identAuto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String absPath = pagInicial.getFileBox().getCurrentPath().getAbsolutePath();
                String identPath = pagInicial.getFileBox().getSelectedValue() == null ? absPath : absPath + SystemManager.osSeparator() + pagInicial.getFileBox().getSelectedValue();
                GUIAutoIdentificacao newGUI =  new GUIAutoIdentificacao(new File(identPath));
                
                if(!newGUI.getArquivos().isEmpty()){
                    aba.add(newGUI, "Propriedades - " + pagInicial.getFileBox().getSelectedValue());
                    aba.setSelectedComponent(newGUI);
                }
            }
        });
    }
    
    private void definirMnemonico(){
            this.aba.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent e){}

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    if(aba.getSelectedIndex() != 0 && JOptionPane.showConfirmDialog(null, "Tem certeza que deseja fechar esta aba?", "Atenção", JOptionPane.OK_CANCEL_OPTION) == 0){
                        if(aba.getSelectedComponent() == resultados){
                            resultados = null;
                        }
                        else if(aba.getSelectedComponent() == histograma){
                            histograma = null;
                        }
                        
                        aba.remove(aba.getSelectedIndex());
                    }
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e){}
        });
    }
       
    //Inicia o sistema em uma Thread distinta, onde pode ser aplicado novos estilos de Look and Feel
    //Data: 01/12/2013
    public static void Initialize(){
       SwingUtilities.invokeLater(new Runnable(){
           @Override
           public void run(){
               try{
                   UIManager.setLookAndFeel(new SubstanceDustLookAndFeel()); //Look and Feel externo           
               }
               catch (UnsupportedLookAndFeelException e){
                    JOptionPane.showMessageDialog(null, "Erro ao estabelecer um ambiente gráfico. Verifique seu driver de vídeo e reinicie o sistema");
               }
               finally{
                   GUI winGui = new GUI();
                   winGui.setVisible(true);
               }    
           }
       });
    }
    
    @Deprecated
    private void openImage(){
        picFrame = new JFrame(image.getFileName());
        JLabel picLabel = new JLabel(new ImageIcon(image.getImage()));
        JScrollPane scroll = new JScrollPane(picLabel);

        setFrameSize(1,picFrame);
        picFrame.add(scroll);
               
        picFrame.setVisible(true);
        //this.histogramComponents();
    }
    
    /** @Date 01/12/2013 -- Obsoleto **/
    @Deprecated
    public boolean setFrameSize(int frameID, JFrame frame){
        Insets in = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());  
        Dimension dimensao = Toolkit.getDefaultToolkit().getScreenSize();
        
        int difference = CheckResolution();
        int height = dimensao.height - (in.top + in.bottom);
        int width = dimensao.width - difference;
                
        switch(frameID){
            case 0: //Painel a direita.
                frame.setSize(difference,height);
                frame.setLocation(width,in.top); //Width: nº pixels(horiz) - 650; in.top: posicação acessível de cima.
                //this.setResizable(false);

                return true;
                
            case 1:  // quando uma imagem for aberta                
                if(image.getImage().getWidth() > width && image.getImage().getHeight() > height){
                    frame.setSize(width - 150,height - 150);
                }
                else if(image.getImage().getWidth() > width && image.getImage().getHeight() <= height){
                    frame.setSize(width,image.getImage().getHeight());
                }
                else if(image.getImage().getWidth() <= width && image.getImage().getHeight() > height){
                    frame.setSize(image.getImage().getWidth(), height);
                }
                else{
                    frame.setSize(image.getImage().getWidth()+28, image.getImage().getHeight()+58);
                }
                              
                frame.setLocation(((dimensao.width - difference) - frame.getSize().width)/2 , (dimensao.height - frame.getSize().height)/2); //Posição JFrame
                            
                return true;

            default:
                return false;
        }
    }
    
    /* =============================================================================== *
     * 13/01/2013 - Executado o programa em um computador com uma resolução inferior à *
     * 2048x1536 (full HD). O método CheckResolution verifica a melhor dimensão do     *
     * software para determinadas resoluções.                                          *
     * O valor retornado é a quantidade de pixels de largura e também o valor a ser    *
     * subtraído para posiciona-lo na extremidade da tela.                             *
     * =============================================================================== */
    
    static public int CheckResolution(){
        Dimension dimensao = Toolkit.getDefaultToolkit().getScreenSize();
        
        if(dimensao.getWidth() == 1920 && dimensao.getHeight() == 1080) {
            return 650;
        }
        else if(dimensao.getWidth() == 1152 && dimensao.getHeight() == 864) {
            return 450;
        }
        else if(dimensao.getWidth() == 1024 && dimensao.getHeight() == 768) {
            return 425;
        }       
        else if(dimensao.getWidth() == 800 && dimensao.getHeight() == 600) {
            return 350;
        }
        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent eventAction){
        JMenuItem item = (JMenuItem) eventAction.getSource();
        SysMenuFileOperation fileOp = new SysMenuFileOperation();
        String pathWay;
        String extensao;
        
        switch (item.getText()){
            case "Abrir":
                    pathWay = fileOp.openFile(SysMenuFileOperation.PICTURE);
              
                    image = new ImageProperties(pathWay);
                    image.imageRead();

                    if(!image.getFilePath().equals("")){
                        mainMenu.getMenu(0).getItem(1).setEnabled(true);
                        mainMenu.getMenu(0).getItem(2).setEnabled(true);
                    }
                    
                    this.openImage();               
                    break;
                
            case "Salvar como...":
                    extensao = fileOp.saveAsFile(SysMenuFileOperation.PICTURE, image.getFileName());
                    
                    if(!extensao.equals("")){
                        image.setFileExtension(extensao); //salvando no filtro selecionado.
                        image.setFilePath(fileOp.getPathWay());
                        image.imageWrite();
                    }
              
                    break;
                
            case "Salvar":
                image.imageWrite();  //Adicionar message box substituição arquivo
                break;
                
            case "Sair":
                int opcao = JOptionPane.showConfirmDialog(GUI.this, "Deseja encerrar o aplicativo?", "", JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
        }
    }
}
