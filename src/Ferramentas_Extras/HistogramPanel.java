package Ferramentas_Extras;

import Main_Package.GraphicalUserInterface.GUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.media.jai.Histogram;
import javax.swing.JComponent;

/**
 * @date 01/16/2013 * @author João Paulo Herrera
 *
 * Classe responsável por desenhar histogramas
 */
public class HistogramPanel extends JComponent implements MouseMotionListener {

    private String titulo;
    private Histogram histograma;
    private int banda;
    private int[] bins;
    private int maxBins;
    private Color backgroundColor;
    private Color graduacaoColor;
    private Color barraColor;
    private Font graduacaoFont;
    private int espVertical;
    private int espHorizontal;
    private int altura;
    private int comprimento;
    private double compGrad;
    private int minDivisao;

    /**
     * @param histograma Histogram - Classe Histograma encontrada no pacotr JAI
     * @param titulo String - Título para o Histograma
     * @param banda int - RGB representado pelo Histograma. 0-R,1-G,2-B
     */
    public HistogramPanel(Histogram histograma, int banda, String titulo) {
        this.titulo = titulo;
        this.histograma = histograma;
        this.banda = banda;

        backgroundColor = new Color(214, 217, 223); //Cor baseada no LookandFeel Nimbus
        graduacaoColor = new Color(0, 0, 0);
        barraColor = new Color(0, 0, 0);
        bins = histograma.getBins(banda); //Todas as posições existentes, pixel x pixel do histograma
        maxBins = getMaxBinValue(Integer.MIN_VALUE);
        graduacaoFont = new Font("monospaced", 0, 10);
        this.addMouseMotionListener(this);
        this.defineDimensions();
    }
    
    public HistogramPanel(Histogram histograma, String titulo){
        this.histograma = histograma;
        this.titulo = titulo;
        this.defineDimensions();
    }
    
    public HistogramPanel(){
        this.histograma = null;
        this.bins = null;
        this.defineDimensions();
    }

    /**
     * Encontra o maior valor de posição do histograma
     *
     * @param comparison int - menor valor possível para um inteiro.
     * @return int - Maior valor de posição encontrado no histograma
     */
    private int getMaxBinValue(int comparison) {
        for (int cont = 0; cont < bins.length; cont++) {
            comparison = Math.max(comparison, bins[cont]);
        }
        return comparison;
    }

    /**
     * Desenha o histograma no JPanel. O metodo é chamado apenas pelo sistema
     * quando um frame é atualizado
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graph2D = (Graphics2D) graphics;
        
        retanguloHistograma(graph2D);
        
        if(histograma != null){
            Point2D[] pontoHistograma = new Point2D[histograma.getNumBins(banda)];
            FontMetrics gradMetric = graph2D.getFontMetrics(); //Tamanho da fonte selecionada para a classe de desenho.
            drawHorizontalValues(graph2D, gradMetric, histograma.getNumBins(banda));
            drawVerticalValues(graph2D, gradMetric);           
            drawHistogramData(graph2D, pontoHistograma);
            paintHistogram(graph2D, pontoHistograma);
            titleHistogram(graph2D, gradMetric);
        }
        else{
            FontMetrics gradMetric = graph2D.getFontMetrics();
            drawHorizontalValues(graph2D, gradMetric, 256);
        }
            
    }

    @Override
    public void mouseDragged(MouseEvent e) {/*Metodo não utilizado*/

    }

    @Override
    public void mouseMoved(MouseEvent posicaoMouse) {
        double X = posicaoMouse.getX(); //coordenada X da posicao do mouse
        int Y = posicaoMouse.getY(); //coordenada Y da posicao do mouse

        if ((X > espHorizontal) && (X < espHorizontal + comprimento) && (Y > espVertical) && (Y < espVertical + altura)) {
            X = (X - espHorizontal) / compGrad;
            Y = bins[(int) X];
            setToolTipText((int) X + " : " + Y);
        } else {
            setToolTipText(null);
        }
    }

    private void retanguloHistograma(Graphics2D graph2D) {
        graph2D.setColor(graduacaoColor);
        graph2D.setFont(graduacaoFont);

        if(this.comprimento <= 0){
            this.defineDimensions();
        }
        
        //Desenhando o retângulo base para o histograma de acordo com a resolução
         graph2D.drawRect(this.espHorizontal, this.espVertical, this.comprimento, this.altura);
               
    }
    
    private void defineDimensions(){
        int screen = GUI.CheckResolution();
        
        switch(screen){
            case 650:
                this.espVertical = 40;
                this.espHorizontal = 55;
                this.altura = 180;
                this.comprimento = 510;
                this.compGrad = 2;
                this.minDivisao = 8;
                break;

            case 450:
                this.espVertical = 35;
                this.espHorizontal = 50;
                this.altura = 140;
                this.comprimento = 320;
                this.compGrad = 1.255;
                this.minDivisao = 10;
                break;

            case 425:                
                this.espVertical = 20;
                this.espHorizontal = 50;
                this.altura = 130;
                this.comprimento = 300;
                this.compGrad = 1.175;
                this.minDivisao = 15;
                break;

            case 350:
                this.espVertical = 20;
                this.espHorizontal = 35;
                this.altura = 90;
                this.comprimento = 260;
                break;
        }
    }

    private void drawHorizontalValues(Graphics2D graph2D, FontMetrics gradMetric, int teste) {
        int meiaFonte = gradMetric.getHeight() / 2;
        
        for (int bin = 0; bin <= teste; bin++) { // histograma.getNumBins(banda) == 255
            if (bin % minDivisao == 0) {                           // graduação dividida pelo valor mínimo (5 em 5, 10 em 10...)
                String label = "" + bin;                                // passando o valor da graduacao para o label
                int labelHeight = gradMetric.stringWidth(label);     // largura do valor da graduacao que sera altura, pois havera rotacao

                graph2D.translate(espHorizontal + bin * compGrad + meiaFonte, espVertical + altura + labelHeight + 2.4);

                // translate() vai posicionar o cursor na coordenada x,y 
                // desejada, com isso é possível desenhar a graduacao 
                // distribuida no histograma.
                //
                // Exemplo:
                // translate(40+0+5,40+250+10+2)
                //
                //299|
                //   |
                //302|   x  x  x  x  x  x  x  x  x  x
                //   |_________________________________
                //305|40    50    60    70    80    90

                graph2D.rotate(-Math.PI / 2);                     // Porque -Math.PI/2??
                graph2D.drawString(label, 0, 0);                  // rotate(theta) == [cos(theta)   -sen(theta)        0  ]
                graph2D.rotate(Math.PI / 2);                      //                  [sen(theta)   -cos(theta)        0  ]
                //                  [     0            0             1  ]

                graph2D.translate(-(espHorizontal + bin * compGrad + meiaFonte), -(espVertical + altura + labelHeight + 2.4));
            }
        }
    }

    private void drawVerticalValues(Graphics2D graph2D, FontMetrics gradMetric) {
        double step = (int) (maxBins / 10);                      //numero a ser multiplicado pelo fator bin
        String label;
        for (int bin = 0; bin < 10; bin++) {
            if (bin == 10) {
                label = String.format("%7d", maxBins);
            } else {
                label = String.format("%7d", (int) (bin * step));
            }
            int labelHeight = gradMetric.stringWidth(label);
            graph2D.drawString(label, espHorizontal - 2 - labelHeight, espVertical + altura - bin * (altura / 10));
        }
    }

    private void drawHistogramData(Graphics2D graph2D, Point2D[] pontoHistograma) {
        double CooX; // Coordenada X do ponto representando 1/TOTAL da amostra
        double CooY; // Coordenada Y do ponto representando 1/TOTAL da amostra 

        for (int bin = 0; bin < histograma.getNumBins(banda); bin++) {
            CooX = espHorizontal + bin * compGrad;
            CooY = espVertical + altura * (maxBins - bins[bin]) / (1. * maxBins);
            pontoHistograma[bin] = new Point2D.Double(CooX, CooY);
        }

        // ligando os pontos através de retas
        for (int bin = 0; bin < histograma.getNumBins(banda); bin++) {
            if (bin + 1 != histograma.getNumBins(banda)) {
                graph2D.draw(new Line2D.Double(pontoHistograma[bin], pontoHistograma[bin + 1]));
                // System.out.println(pontoHistograma[bin]+" "+ pontoHistograma[bin + 1]);
            }
        }
    }
    
    private void paintHistogram(Graphics2D graph2D, Point2D[] pontoHistograma){
         Color histogramaColor;

        //Cor relacionada ao valor da banda
        switch (banda) {
            case -1:
                histogramaColor = new Color(128, 128, 128, 70);
                break;
            case 0:
                histogramaColor = new Color(255, 0, 0, 70);
                break;
            case 1:
                histogramaColor = new Color(0, 255, 0, 70);
                break;
            case 2:
                histogramaColor = new Color(0, 0, 255, 70);
                break;
            default:
                histogramaColor = barraColor;

                break;
        }
        graph2D.setColor(histogramaColor);
        Point2D pontoX;
        Point2D pontoY;
        for (int paint = 0; paint < histograma.getNumBins(banda); paint++) {
            pontoX = new Point2D.Double(pontoHistograma[paint].getX(), pontoHistograma[paint].getY() + 1.0); //coordenada X da linha colorida
            pontoY = new Point2D.Double(pontoHistograma[paint].getX(), espVertical + altura - 1); //coordenada Y da linha colorida
            if (pontoY.getY() - pontoX.getY() >= -1.5) { //artifício usado para correção de retas traçadas em espaços muito pequenos, que não há necessidade
                graph2D.draw(new Line2D.Double(pontoX, pontoY));
            }
        }
        int diferenca = 0;
        for (int paint = 0; paint < histograma.getNumBins(banda); paint++) { //Melhora no preenchimento de cores
            if (paint == 0) {                                            //pois restaram espaços sem cor (valores onde não há pontos)
                diferenca = (int) pontoHistograma[0].getX();
            }
            if (diferenca++ != (int) pontoHistograma[paint].getX() && paint + 1 != histograma.getNumBins(banda)) {
                pontoX = new Point2D.Double((pontoHistograma[paint].getX() + pontoHistograma[paint + 1].getX()) / 2, (pontoHistograma[paint].getY() + pontoHistograma[paint + 1].getY()) / 2);
                pontoY = new Point2D.Double((pontoHistograma[paint].getX() + pontoHistograma[paint + 1].getX()) / 2, espVertical + altura - 1);
                graph2D.draw(new Line2D.Double(pontoX, pontoY));
            }
        }
    }
    
    private void titleHistogram(Graphics2D graph2D, FontMetrics gradMetric){
        graph2D.setColor(barraColor);
        graph2D.setFont(new Font("Monospace", Font.ITALIC, 15));

        gradMetric = graph2D.getFontMetrics();
        graph2D.drawString(titulo, espHorizontal + 2, espVertical - 2);
    }

    public int getComprimento() {
        return this.comprimento + 20; //Constante encontrada para deixar o slider do tamanho do histograma
    }
}
