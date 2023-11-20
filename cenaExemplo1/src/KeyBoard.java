package cena;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;
/**
 *
 * @author Kakugawa
 */
public class KeyBoard implements KeyListener{
    private Cena cena;

    public KeyBoard(Cena cena){
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);

        if(e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == 151 )
            cena.translacao = cena.translacao + (0.3f);
        if(e.getKeyChar() == 'a'|| e.getKeyChar() == 'A' || e.getKeyCode() == 149)
            cena.translacao = cena.translacao - (0.3f) ;
    }

    @Override
    public void keyReleased(KeyEvent e) { }

}