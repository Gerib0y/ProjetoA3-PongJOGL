package cena;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyBoard implements KeyListener {
    private Cena cena;
    private float velocidade;
    private float fatorInterpolacao; // Valor entre 0 e 1 para controlar a suavização
    public KeyBoard(Cena cena) {
        this.cena = cena;
        this.velocidade = 0.3f;
        this.fatorInterpolacao = 0.0f;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyCode());

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        float destino = cena.translacao;

        switch (e.getKeyCode()) {
            case 151: // Seta direita
                destino += velocidade;
                break;
            case 149: // Seta esquerda
                destino -= velocidade;
                break;
        }
        switch (e.getKeyChar()){
            case 'm':
                cena.op = 1;
                break;
            case 's':
                cena.op = 2;
                break;
        }

        // Limita a Plataforma na tela
        float limiteEsquerdo = -1.6f;
        float limiteDireito = 1.6f;
        cena.translacao = Math.max(limiteEsquerdo, Math.min(destino, limiteDireito));

        // Aplica suavização usando interpolação linear
        cena.translacao = lerp(cena.translacao, cena.translacao, fatorInterpolacao);
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    // Método de interpolação linear
    private float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    // Método para limitar um valor dentro de um intervalo
    private float clamp(float valor, float minimo, float maximo) {
        return Math.max(minimo, Math.min(valor, maximo));
    }
}
