package Ferramentas_Extras;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @date 24/10/2014
 * @author João
 */

public class RockandRollButton extends JButton{
    private boolean pressed;
    
    public RockandRollButton(ImageIcon icon){
        super(icon);
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
