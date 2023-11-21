package cena;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Renderer {
    private static GLWindow window = null;
    public static int screenWidth = 1280;  //1280
    public static int screenHeight = 960; //960

    // Cria a janela de renderização do JOGL
    public static void init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        window = GLWindow.create(caps);
        window.setSize(screenWidth, screenHeight);
        window.setResizable(false);

        Cena cena = new Cena();

        window.addGLEventListener(cena); // Adiciona a Cena à Janela
        // Habilita o teclado: cena
        window.addKeyListener(new KeyBoard(cena));

        // window.requestFocus();
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); // Inicia o loop de animação

        // Adiciona um adaptador de janela para tratar eventos de fechamento
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });

        // Define a janela para tela cheia
        window.setFullscreen(true);

        // Torna a janela visível
        window.setVisible(true);
    }

    public static void main(String[] args) {
        init();
    }
}
