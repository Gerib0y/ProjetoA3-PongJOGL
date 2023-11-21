package cena;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyBoard implements KeyListener {
    private Cena cena;
    private float velocidade;
    private float fatorInterpolacao; // Valor entre 0 e 1 para controlar a suavização

    public KeyBoard(Cena cena) {
        this.cena = cena;
        this.velocidade = 0.5f;
        this.fatorInterpolacao = 0.1f; // Ajuste conforme necessário
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        float destino = cena.translacao;

        if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == 151) {
            destino += velocidade;
        } else if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A' || e.getKeyCode() == 149) {
            destino -= velocidade;
        }

        // Limita a posição nos limites da tela
        float limiteEsquerdo = -1.5f; // Ajuste conforme necessário
        float limiteDireito = 1.5f;  // Ajuste conforme necessário
        cena.translacao = clamp(destino, limiteEsquerdo, limiteDireito);

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
