package Ferramentas_Extras;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @date 02/02/2014
 * @author João
 */

public class RockandRollImageView extends JLabel{
    private ImageIcon imgIcon;
    private int scaleX;
    private int scaleY;
    private int scaleXImg;
    private int scaleYImg;
    
    public RockandRollImageView(ImageIcon image){
        super(image);
        this.imgIcon = image;
        
        this.scaleX = 0;
        this.scaleY = 0;
    }
    
    public void zoom(int scaleX, int scaleY){
        this.scaleX    = scaleX;
        this.scaleY    = scaleY;
        this.scaleXImg = this.imgIcon.getImage().getWidth(null)  + scaleX;
        this.scaleYImg = this.imgIcon.getImage().getHeight(null) + scaleY;

        this.paintComponent(this.getGraphics());
    }
    
    public void resizedImg(int imgType){
        BufferedImage resizedImg = new BufferedImage(this.scaleX, this.scaleY, imgType);
        Graphics2D g2d = resizedImg.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(this.imgIcon.getImage(), 0, 0, this.scaleX, this.scaleY, null);
        
        g2d.dispose();
        
        this.imgIcon.setImage(resizedImg);
    }
    
    public int convertScale(int fator, int scaleX, int correspX){
        return (fator*correspX)/scaleX;
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        if(this.scaleX > 0 && this.scaleY > 0){
            this.setPreferredSize(new Dimension(this.scaleXImg, this.scaleYImg));
            g.drawImage(this.imgIcon.getImage(), 0, 0, this.scaleXImg, this.scaleYImg, null); 
        }
        else{
            this.setPreferredSize(new Dimension(this.imgIcon.getIconWidth(), this.imgIcon.getIconHeight()));
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public int getScaleX() {
        return this.scaleX;
    }

    public int getScaleY() {
        return this.scaleY;
    }

}
