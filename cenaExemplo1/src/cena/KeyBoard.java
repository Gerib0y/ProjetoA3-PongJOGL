package cena;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public abstract class KeyBoard implements KeyListener {
    private Cena cena;
    public KeyBoard(Cena cena){
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyCode());

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if (cena.moveBarra < 0.8) {
                    cena.moveBarra = cena.moveBarra + 0.2f;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (cena.moveBarra > -0.8) {
                    cena.moveBarra = cena.moveBarra - 0.2f;
                }
                break;
        }
        switch (e.getKeyChar()){
            case 'm':
                cena.resetData();
                cena.op = 0;
                break;
            case 's':
                cena.op = 1;
                break;
            case 'r':
                cena.resetData();
                cena.op = 1;
                break;
        }
    }

    // Método para limitar um valor dentro de um intervalo
    private float clamp(float valor, float minimo, float maximo) {
        return Math.max(minimo, Math.min(valor, maximo));
    }
}
