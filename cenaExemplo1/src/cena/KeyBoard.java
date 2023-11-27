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

        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

            case KeyEvent.VK_RIGHT:
                if (cena.moveBarra < 0.7 && !cena.pause) {
                    cena.moveBarra = cena.moveBarra + 0.2f;
                }
                break;

            case KeyEvent.VK_LEFT:
                if (cena.moveBarra > -0.7 && !cena.pause) {
                    cena.moveBarra = cena.moveBarra - 0.2f;
                }
                break;

            case KeyEvent.VK_M:
                if (cena.pause || cena.op == 4 || cena.op == 3) {
                    cena.resetData();
                    cena.op = 0;
                }
                break;

            case KeyEvent.VK_S:
                if (cena.op != 4 && cena.op != 3) {
                    cena.op = 1;
                }
                break;

            case KeyEvent.VK_R:
                if (cena.op !=0 && cena.op != 4) {
                    cena.resetData();
                    cena.op = 1;
                }
                break;

            case KeyEvent.VK_1:
                if (cena.op == 0) {
                cena.op = 4;
            }
            break;

            case KeyEvent.VK_P:
                if (cena.op !=4 && cena.op !=0 ){
                    cena.pause = !cena.pause;
                }
        }
    }

    // Método para limitar um valor dentro de um intervalo
    private float clamp(float valor, float minimo, float maximo) {
        return Math.max(minimo, Math.min(valor, maximo));
    }
}
