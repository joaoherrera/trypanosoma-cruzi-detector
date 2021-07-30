package Main_Package.GraphicalUserInterface;

import Adaptacao.SystemManager;
import Main_Package.Modelling.IOFunctions;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.miginfocom.swing.MigLayout;

/**
 * @date 18/07/2014
 * @author João
 * 
 * Classe que apresenta os resultados do estudo tão como os valores definidos como base de conhecimento da rede neural.
 */

public class GUIResultados extends JPanel{
    private IOFunctions sdyGroup;
    private IOFunctions sdyFile;
 
    private JComboBox<String> comboFile;
    private JComboBox<String> comboGroup;
    
    private LinkedList<File> files;
    private LinkedList<File> groups;
    
    private JPanel wholePage; 
    private JPanel leftPage; 
    private JPanel leftSup; 
    private JPanel leftSupL; 
    private JPanel leftSupR; 
    private JPanel leftSupRB; 
    private JPanel leftSupRB2; 
    private JPanel leftSupRB3; 
    private JPanel leftSupRBC; 
    private JPanel leftInf;  
    
    private JButton addItem;
    private JButton createGroup;
    private JButton deleteGroup;
    private JButton compute;
    
    private JTable tableResultados;
    private DefaultTableModel dtmodel;
    private JScrollPane scroll;
    
    private JDialog dialogAdd;
    
    private boolean groupActivated;
    
    public GUIResultados(){
      this.setLayout(new BorderLayout());
      this.createComponents();
      this.initData();
    }
    
    private void createComponents(){
        this.wholePage  = new JPanel(new BorderLayout());
        this.leftPage   = new JPanel(new BorderLayout());
        this.leftSup    = new JPanel(new MigLayout("insets 10 0 20 0"));
        this.leftSupL   = new JPanel(new BorderLayout());
        this.leftSupR   = new JPanel(new BorderLayout());
        this.leftSupRB  = new JPanel(new MigLayout("insets 0 10 0 0"));
        this.leftSupRB2 = new JPanel(new MigLayout("insets 0 10 0 0"));
        this.leftSupRB3 = new JPanel(new MigLayout("insets 0 10 0 0"));
        this.leftSupRBC = new JPanel(new MigLayout("insets 0 10 0 0"));
        this.leftInf    = new JPanel(new BorderLayout());
        
        this.addItem = new JButton("Adicionar Info");
        this.addItem.setFocusable(false);
        this.createGroup = new JButton(new ImageIcon(this.getClass().getResource("/Sprites/add_tryp.png")));
        this.createGroup.setFocusable(false);
        this.deleteGroup = new JButton(new ImageIcon(this.getClass().getResource("/Sprites/delete_tryp.png")));
        this.deleteGroup.setFocusable(false);
        this.compute = new JButton(new ImageIcon(this.getClass().getResource("/Sprites/comp_tryp.png")));
        this.compute.setFocusable(false);
        this.compute.setToolTipText("Calcular parasita/celula");
        
        this.comboFile   = new JComboBox<>();
        this.comboGroup  = new JComboBox<>();
        
        this.comboFile.setFocusable(false);
        this.comboGroup.setFocusable(false);
                
        this.dtmodel = new DefaultTableModel(100, 4);
        this.tableResultados = new JTable(this.dtmodel);
        this.scroll = new JScrollPane(this.tableResultados);
                
        TableCellRenderer tableCellRenderer = this.tableResultados.getDefaultRenderer(this.tableResultados.getClass());
        ((JLabel)tableCellRenderer).setHorizontalAlignment(JLabel.CENTER);
                
        this.leftInf.add(this.scroll);
        
        this.leftSupL.add(this.comboFile);
        this.leftSupRB2.add(this.comboGroup, "width "+ 0.223 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width +":"+ 0.223 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width  +":"+ 0.223 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width );
        this.leftSupRB3.add(this.createGroup);
        this.leftSupRB3.add(this.deleteGroup);
        this.leftSupR.add(this.leftSupRB2, BorderLayout.WEST);
        this.leftSupR.add(this.leftSupRB3, BorderLayout.CENTER);
        this.leftSupRB.add(this.addItem);
        this.leftSupRBC.add(this.compute);
        
        this.leftSupRB.add(this.leftSupRBC);
        this.leftSupR.add(this.leftSupRB, BorderLayout.EAST);
        
        this.leftSup.add(this.leftSupL, "width "+ 0.3 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width +":"+ 0.3 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width  +":"+ 0.3 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width );
        this.leftSup.add(this.leftSupR, "width "+ 0.4 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width +":"+ 0.4 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width  +":"+ 0.4 * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width );
 
        this.leftPage.add(this.leftSup, BorderLayout.NORTH);
        this.leftPage.add(this.leftInf);
        this.wholePage.add(this.leftPage);

        this.add(this.wholePage);
        
       // this.defaultColumnsName();
        this.buttonActions();
        this.comboActions();
    }
    
    private void comboActions(){
        this.comboFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboFile.getSelectedItem() != null){
                    int index = findTRSTIndex(comboFile.getSelectedItem().toString() + ".trst", files);

                    defaultColumnsName();
                    configColumnWidth();

                    if(files.size() > 0){
                        interpretarFile(files.get(index));
                    }

                    configTableResize();
                    groupActivated = false;
                }
            }
        });
        
        this.comboGroup.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboGroup.getSelectedItem() != null){
                    int index = findTRSTIndex(comboGroup.getSelectedItem().toString() + ".tgrp", groups);

                    defaultColumnsName();
                    configColumnWidth();

                    if(groups.size() > 0){
                        interpretarFile(groups.get(index));
                    }

                    configTableResize();
                    groupActivated = true;
                }
            }
        });
    }
    
    private String hasData(String fileContent, String groupContent){
        String fileName = "";
        
        while(fileContent.length() > 0){
            fileName    = fileContent.substring(0, fileContent.indexOf(","));
            fileContent = fileContent.substring(fileContent.indexOf("\n")+1);
            
            if(groupContent.contains(fileName)){
                return fileName;
            }
        }
        
        return null;
    }
    
    private void buttonActions(){
        this.addItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int decision = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja adicionar o conteúdo de " + comboFile.getSelectedItem().toString() + " para o grupo "+ comboGroup.getSelectedItem().toString() +"?", "Resposta do Sistema", JOptionPane.YES_NO_OPTION);
                
                if(decision == JOptionPane.YES_OPTION){
                    int indexFile        = findTRSTIndex(comboFile.getSelectedItem().toString() + ".trst", files);
                    int indexGroup       = findTRSTIndex(comboGroup.getSelectedItem().toString() + ".tgrp", groups);
                    
                    String content       = (sdyFile = new IOFunctions(files.get(indexFile))).ler();
                    String repeat        = hasData(sdyFile.ler(), sdyGroup.ler());
                    sdyGroup             = new IOFunctions(groups.get(indexGroup));
                    
                    if(repeat != null){
                        JOptionPane.showMessageDialog(null, "O arquivo " + sdyGroup.getFile().getName() + " já contem a imagem " + repeat, "Imagem já contida", JOptionPane.OK_OPTION);
                        return;                       
                    }
                    
                    if((sdyGroup.count() + sdyFile.count()) > 100){
                        if(JOptionPane.showConfirmDialog(null, "O arquivo " + sdyGroup.getFile().getName() + " já atingiu o máximo "
                                                          + "de 100 valores, deseja adicionar mesmo assim?", "Limite de valores", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION){
                            return;
                        }
                    }
                    
                    sdyGroup.gravar(sdyGroup.ler().concat(content));
                    comboFile.removeItemAt(indexFile);
                    sdyFile.getFile().delete();
                }
            }
        });
        
        this.createGroup.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel               = new JPanel(new MigLayout());
                JPanel panelB              = new JPanel(new MigLayout("insets 0 30 0 0"));
                JButton okButton           = new JButton("Ok"); 
                final JTextField textField = new JTextField();
                
                panel.add(new JLabel("Nome do grupo"));
                panel.add(textField, "width 200:200:200");         
                panelB.add(okButton);
                panel.add(panelB);
                
                okButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!textField.getText().equals("")){
                            IOFunctions newFile = new IOFunctions(new File("rst/"+ textField.getText() +".tgrp"));
                            newFile.gravar("");
                            dialogAdd.dispose();
                            dialogAdd = null;
                            updateAvailableFiles();
                        }
                    }
                });
                
                if(dialogAdd == null){
                    dialogAdd = new JDialog(new JFrame(), "Novo grupo");
                    dialogAdd.add(panel);
                    dialogAdd.pack();
                    dialogAdd.setLocationRelativeTo(comboGroup);
                    dialogAdd.setResizable(false);
                    dialogAdd.setVisible(true);
                }
            }
        });
        
        this.deleteGroup.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selected;
                
                selected = groupActivated == true ? comboGroup.getSelectedItem().toString() : comboFile.getSelectedItem().toString();
                
                int result = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o grupo \"" + selected + "\" ?", "Excluir grupo", JOptionPane.OK_CANCEL_OPTION);
                
                if(result == JOptionPane.YES_OPTION){
                    File file;
                    int index;
                    
                    if(groupActivated){
                        index = findTRSTIndex(comboGroup.getSelectedItem().toString() + ".tgrp", groups);
                        file  = groups.remove(index);
                    }
                    else{
                        index = findTRSTIndex(comboFile.getSelectedItem().toString() + ".trst", files);
                        file  = files.remove(index);
                    }
                    
                    file.delete();
                    updateAvailableFiles();
                }
            }
        });
        
        this.compute.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LinkedList indexes = new LinkedList<Integer>();
                Scanner sc;
                BigDecimal resDec;
                        
                for(int row : tableResultados.getSelectedRows()){
                    if(dtmodel.getValueAt(row, 1) != null){
                        sc = new Scanner(dtmodel.getValueAt(row, 1).toString());

                        if(sc.hasNextInt()){
                            indexes.add(row);
                        }
                    }
                }
                
                resDec = new BigDecimal(calculate(indexes));
                resDec = resDec.setScale(2, BigDecimal.ROUND_HALF_UP);
                
                JOptionPane.showMessageDialog(null, "parasita/célula = " + resDec, "Média parasita/célula", JOptionPane.OK_OPTION, new ImageIcon(this.getClass().getResource("/Sprites/compBig_tryp.png")));               
            }
        });
    }
    
    private float calculate(LinkedList<Integer> indexes){
        float somaCelula = 0;
        float somaParasita = 0;
        
        for(int index : indexes){
            somaCelula   += (int) dtmodel.getValueAt(index, 1);
            somaParasita += (int) dtmodel.getValueAt(index, 2);
        } 
        
        return somaParasita/somaCelula;
    }
    
    private int findTRSTIndex(String selectedFile, LinkedList<File> source){
        for(int i=0; i<source.size(); i++){
            if(source.get(i).getName().equals(selectedFile)){
               return i;
            }
        }
        
        return 0;
    }
    
    private void defaultColumnsName(){
        String[] heading = {"Imagem", "Nº Células", "Nº Parasitas"};
        this.dtmodel.setColumnIdentifiers(heading);
    }
    
    private void initData(){
        SystemManager sysMgr = new SystemManager();
        this.sdyGroup = new IOFunctions("rst/Default.tgrp");
        
        sysMgr.addExtensionToFilter(".trst");
        sysMgr.addExtensionToFilter(".tgrp");
        
        this.files  = sysMgr.getFilesWExtension(new File("rst"), ".trst");
        this.groups = sysMgr.getFilesWExtension(new File("rst"), ".tgrp");      
        
        if(this.groups.isEmpty()){
            this.sdyGroup = new IOFunctions("rst/Default.tgrp");
            this.comboGroup.addItem(SystemManager.formatName(this.sdyGroup.getFile().getName()));
        }
        else{
            sysMgr.sortByLastModifield(this.groups);
            
            for(File filesElem : this.groups){
                this.comboGroup.addItem(SystemManager.formatName(filesElem.getName()));
            }
        }
        
        if(!this.groups.isEmpty()){
            sysMgr.sortByLastModifield(this.files);
            
            for(File filesElem : this.files){
                this.comboFile.addItem(SystemManager.formatName(filesElem.getName()));
            }
        }
        
        if(this.comboFile.getSelectedItem() == null){
            this.showMsgEmptyCombo(this.comboFile);
        }
        
        if(this.comboGroup.getSelectedItem() == null){
            this.showMsgEmptyCombo(this.comboGroup);
        }
    }
    
    private void updateAvailableFiles(){
        SystemManager sysMgr = new SystemManager();
        
        sysMgr.addExtensionToFilter(".trst");
        sysMgr.addExtensionToFilter(".tgrp");
        
        this.files  = sysMgr.getFilesWExtension(new File("rst"), ".trst");
        this.groups = sysMgr.getFilesWExtension(new File("rst"), ".tgrp"); 
        
        sysMgr.sortByLastModifield(this.groups);
        sysMgr.sortByLastModifield(this.files);
        
        this.comboGroup.removeAllItems();
        this.comboFile.removeAllItems();
        
        for(File filesElem : this.groups){
            this.comboGroup.addItem(SystemManager.formatName(filesElem.getName()));
        }
        
        for(File filesElem : this.files){
            this.comboFile.addItem(SystemManager.formatName(filesElem.getName()));
        }
        
        if(this.comboFile.getSelectedItem() == null){
            this.showMsgEmptyCombo(this.comboFile);
        }
        
        if(this.comboGroup.getSelectedItem() == null){
            this.showMsgEmptyCombo(this.comboGroup);
        }
    }
    
    private void interpretarFile(File file){
        SystemManager sysMgr = new SystemManager();
        String content = "";
        
        if(file.isFile()){
            if(sysMgr.getExtension(file).equals(".trst")){
                this.sdyFile = new IOFunctions(file);
                content = this.sdyFile.ler();
            }
            else if(sysMgr.getExtension(file).equals(".tgrp")){
                this.sdyGroup = new IOFunctions(file);
                content = this.sdyGroup.ler();
            }
        }
        
        int maxColumn = this.maxNumberOfColumns(content)+4;
        
        for(int i=0; i<maxColumn-3; i++){
            this.dtmodel.addColumn(String.valueOf(i));
        }
        //this.dtmodel.addColumn("Processamento");
        this.dtmodel.setRowCount(0); //zerar numero de linha
        this.fillRows(content, maxColumn);
        this.addStatistic(maxColumn);
    }
    
    private void addStatistic(int maxColumn){
        Object[] rowData     = new Object[maxColumn];
        Object[] rowDataInfo = new Object[maxColumn];
        
        //Passo 1: Definir o título de cada campo que seja preenchido na linha abaixo.
        rowData[0] = "Total Imagens";
        rowData[1] = "Total Células";
        rowData[2] = "Total Parasitas";
        
        for(int i=3; i<maxColumn; i++){
            rowData[i] = String.valueOf(i-3);
        }

        rowDataInfo = new Object[maxColumn];
        int somatoria;
        
        rowDataInfo[0] = this.dtmodel.getRowCount();
        
        for(int i=1; i<this.dtmodel.getColumnCount();i++){
            somatoria = 0;
            for(int j=0; j<this.dtmodel.getRowCount(); j++){
                somatoria += Integer.parseInt(this.dtmodel.getValueAt(j,i).toString());
            }
            
            rowDataInfo[i] = somatoria;
        }
        
        this.dtmodel.addRow(new Object[maxColumn]);
        this.dtmodel.addRow(rowData);
        this.dtmodel.addRow(rowDataInfo);
        
    }
    
    private void fillRows(String content, int maxColumn){
        String contentCpy = content;
        Object[] rowData  = new Object[maxColumn];
        int parasitasSoma = 0;
        int celulasSoma   = 0;
        
        while(!contentCpy.isEmpty()){
            
            for(int i=0; i<maxColumn; i++){
                rowData[i] = "0";
            }
            
            rowData[0] = contentCpy.substring(0, contentCpy.indexOf(","));
            contentCpy = contentCpy.substring(contentCpy.indexOf(",")+1);
            //rowData[maxColumn] = contentCpy.substring(0, contentCpy.indexOf("$"));
            contentCpy = contentCpy.substring(contentCpy.indexOf("$")+1);
            
            while(contentCpy.indexOf("\n") != 0){
                rowData[Integer.parseInt(contentCpy.substring(0, contentCpy.indexOf(","))) + 3] = String.valueOf(Integer.parseInt((String) rowData[Integer.parseInt(contentCpy.substring(0, contentCpy.indexOf(","))) + 3]) + 1);//contentCpy.substring(0, contentCpy.indexOf(","));
                parasitasSoma += Integer.parseInt(contentCpy.substring(0, contentCpy.indexOf(",")));
                celulasSoma++;
                contentCpy = contentCpy.substring(contentCpy.indexOf(",")+1);
            }
            contentCpy = contentCpy.substring(contentCpy.indexOf("\n")+1);
            
            rowData[1] = celulasSoma;
            rowData[2] = parasitasSoma;
            
            celulasSoma = 0;
            parasitasSoma = 0;
            
            this.dtmodel.addRow(rowData); 
        }
//        
//        int rowFilled = this.dtmodel.getRowCount();
//        rowData = new Object[maxColumn];
//        
//        while(rowFilled < 100){
//            this.dtmodel.addRow(rowData);
//            rowFilled++;
//        }
    }
    
   private int maxNumberOfColumns(String content){
        String contentCopy = content;
        int value = 0;

        while(!contentCopy.isEmpty() && contentCopy.indexOf("\n") != 0){
            
            contentCopy = contentCopy.substring(contentCopy.indexOf("$")+1, contentCopy.length());
            
            while(contentCopy.indexOf("\n") != 0){
                if(Integer.parseInt(contentCopy.substring(0, contentCopy.indexOf(","))) > value){
                    value = Integer.parseInt(contentCopy.substring(0,contentCopy.indexOf(",")));
                }
                
                contentCopy = contentCopy.substring(contentCopy.indexOf(",")+1);
            }
            
            contentCopy = contentCopy.substring(contentCopy.indexOf(",")+1);
        } 
        return value;
    }
    
    private void configTableResize(){
        if(this.tableResultados.getPreferredSize().width < this.leftInf.getPreferredSize().width){
            this.tableResultados.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        else{
            this.tableResultados.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }
    
    private void configColumnWidth(){
        for(int i=0; i<this.tableResultados.getColumnCount(); i++){
            this.tableResultados.getColumnModel().getColumn(i).setPreferredWidth(this.dtmodel.getColumnName(i).length());
        }
    }
    
    private void showMsgEmptyCombo(JComboBox<String> comboBox){
        comboBox.addItem("< Vazio >");
    }
}
