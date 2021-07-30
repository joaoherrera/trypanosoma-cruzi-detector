package Main_Package.GraphicalUserInterface;

import Adaptacao.SystemManager;
import Ferramentas_Extras.RockandRollComboBox;
import Ferramentas_Extras.RockandRollImageView;
import Ferramentas_Extras.RockandRollList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @date: 15/12/2013
 * @author João
 */

public class GUIPaginaInicial extends JPanel{
    
    private JTextField filePath;
    private JPanel backGrnd;
    private JPanel left;
    private JPanel right;
    private JPanel imgView;
    private RockandRollComboBox pathBox;
    private RockandRollList fileBox;
    private JScrollPane scroll;
    
    public GUIPaginaInicial(){      
        this.setLayout(new BorderLayout());
        this.createComponents();
    }
    
    private void createComponents(){
        JButton bckButton           = new JButton();
        JPanel addressPanel         = new JPanel(new GridBagLayout());
        JPanel listPanel            = new JPanel(new BorderLayout());
        this.imgView                = new JPanel(new BorderLayout());
        GridBagConstraints constr   = new GridBagConstraints();
        this.backGrnd               = new JPanel(new GridLayout());
        this.pathBox                = new RockandRollComboBox(System.getProperty("user.home"));
        this.fileBox                = new RockandRollList(this.pathBox.getCurrentPath(), RockandRollList.IMAGE_LABBELED);
        this.scroll                 = new JScrollPane(this.fileBox);
        
        this.pathBox.setEditable(true);
        this.pathBox.addComplementarList(this.fileBox);
        this.imgView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.imgView.setBackground(new Color(58, 57, 53, 0));
        
        this.left  = new JPanel(new BorderLayout());
        this.right = new JPanel(new BorderLayout());
        
        bckButton.setIcon(new ImageIcon(this.getClass().getResource("/Sprites/backButton_tryp.png")));
        bckButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("/Sprites/backButtonPressed_tryp.png")));
        bckButton.setFocusPainted(false);
        bckButton.setContentAreaFilled(false);
        bckButton.setBorderPainted(false);
        bckButton.setPreferredSize(new Dimension(bckButton.getIcon().getIconWidth(), bckButton.getIcon().getIconHeight()));
        
        //Eventos
        this.configBckButton(bckButton);
        this.configClickItem(this.fileBox);
        
        //Layout ComboBox + Botão
        constr.fill = GridBagConstraints.BOTH;
        addressPanel.add(bckButton, constr);
        constr.gridx = 1;
        constr.weightx = 3;
        addressPanel.add(pathBox, constr);
        this.left.add(addressPanel, BorderLayout.NORTH);
        
        //Layout Lista
        this.fileBox.setBorder(BorderFactory.createEtchedBorder());
        listPanel.setBorder(BorderFactory.createTitledBorder(""));
        listPanel.add(this.scroll, BorderLayout.CENTER);
        this.left.add(listPanel);
        
        //Layout View
        this.right.add(imgView);
        
        //Layout Pagina
        this.add(this.backGrnd);
        this.backGrnd.add(this.left);
        this.backGrnd.add(this.right);
        this.imgView.add(new RockandRollImageView(new ImageIcon(this.getClass().getResource("/Sprites/open_tryp.png"))));
    }
    
    private void configBckButton(JButton bckButton){
        bckButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae){
                pathBox.setSelectedItem((pathBox.getPreviousCurrentPath()));
            }
        });
    }
    
    private void configClickItem(RockandRollList list){
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                JLabel view;
                if(SystemManager.isArquivo(pathBox.getCurrentPath() + SystemManager.osSeparator() +fileBox.getSelectedValue())){
                    view = new RockandRollImageView(new ImageIcon(pathBox.getCurrentPath() + SystemManager.osSeparator() +fileBox.getSelectedValue()));
                }
                else{
                    view = new RockandRollImageView(new ImageIcon(this.getClass().getResource("/Sprites/noImg_tryp.png")));
                }
                imgView.removeAll();
                imgView.add(view);
                imgView.revalidate();
                
            }
        });
    }

    public JTextField getFilePath() {
        return this.filePath;
    }

    public void setFilePath(JTextField filePath) {
        this.filePath = filePath;
    }

    public RockandRollList getFileBox() {
        return this.fileBox;
    }

    public RockandRollComboBox getPathBox() {
        return this.pathBox;
    }

}
