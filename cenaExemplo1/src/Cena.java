package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.*;
import java.util.Locale;
import java.util.Random;

public class Cena implements GLEventListener{
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    GLU glu;
    public float translacao = 0;
    double limite = 2*Math.PI;
    private float cX = 0.0f, cY = 0.0f, rX = 0.1f, rY = 0.1f; // Posições da Bola
    public int op = 1; // Menu
    public int mode; // Menu

    // Randomiza a Bola
    private Random random;
    public Cena() {
        random = new Random();
    }
    private float limiteSuperior = 1.0f, limiteEsquerdo = -1.8f, limiteDireito = 1.8f, limiteInferior = -0.85f; // Limites da Bola na Tela
    private float velocidadeBola = 0.01f; // Velocidade da Bola
    private float direcaoBolaX = 0.9f, direcaoBolaY = -0.9f; // Direção da Bola
    public float plataformaY = -0.99f; // Posição inicial da plataforma
    public float userBarMove = 0;
    // Texto
    private TextRenderer textRenderer;
    private GL2 gl;

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        gl = drawable.getGL().getGL2();
        glu = new GLU();
        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -1;
        xMax = yMax = zMax = 1;
        textRenderer = new TextRenderer(new Font("Fixedsys Regular", Font.BOLD,50));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();
        // Define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);
        // Limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity(); // Lê a matriz identidade

        switch (op) {
            case 1:
                menu();
                break;
            case 2:
                fase1();
                break;

            default:

                break;
        }

        gl.glFlush();
    }

    public void menu() {
        gl.glPushMatrix();
            desenhaTexto(gl, 550, 900, Color.CYAN, "-=PONG=-");
            desenhaTexto(gl, 50, 600, Color.WHITE, "COMANDOS:");
            desenhaTexto(gl, 50, 550, Color.WHITE, "As teclas ← e → movem a barra!");
            desenhaTexto(gl, 50, 500, Color.WHITE, "Aperte S para iniciar!");
            desenhaTexto(gl, 50, 450, Color.WHITE, "Aperte M para voltar ao menu principal!");
        gl.glPopMatrix();
    }

    public void fase1() {
        // Desenha a Barra
        gl.glPushMatrix();
        gl.glTranslatef(translacao, 0.05f, 0);
        criaRetangulo(gl);
        gl.glPopMatrix();

        // Desenha a Bola
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 0);
        criaBola(gl);
        gl.glPopMatrix();
        bolaMove();
    }

    public void desenhaTexto(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase){
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, mode);
    }
    public void criaRetangulo(GL2 gl){
        gl.glPushMatrix();
            gl.glTranslatef(userBarMove, 0, 0);
            gl.glBegin(GL2.GL_QUADS);
                gl.glColor3f(0f, 1f, 1f);
                gl.glVertex2f(-0.2f, -0.8f);
                gl.glColor3f(0.0f, 0.0f, 0.0f);
                gl.glVertex2f(0.2f, -0.8f);
                gl.glColor3f(0.0f, 1f, 1f);
                gl.glVertex2f(0.2f, -0.9f);
                gl.glColor3f(0.0f, 0.0f, 0.0f);
                gl.glVertex2f(-0.2f, -0.9f);
            gl.glEnd();
        gl.glPopMatrix();
    }
    public void criaBola(GL2 gl) {
        gl.glColor3d(1, 1, 1);
        gl.glBegin(GL2.GL_POLYGON);
        for (double i = 0; i < limite; i += 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
        }
        gl.glEnd();
    }

    public void bolaMove() {
        cX += direcaoBolaX * velocidadeBola;
        cY += direcaoBolaY * velocidadeBola;

        float randomIncrement = random.nextFloat() * 0.02f - 0.01f; // Valor aleatório entre -0.01 e 0.01
        cY += (0.01 + randomIncrement) * direcaoBolaY;

        // Verifica colisão com os limites da tela e inverte a direção se necessário
        if (cX - rX < limiteEsquerdo || cX + rX > limiteDireito) {
            direcaoBolaX = -direcaoBolaX; // Inverte a direção horizontal
        }
        if (cY + rY > limiteSuperior) {
            direcaoBolaY = -direcaoBolaY; // Inverte a direção vertical
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width , int height) {
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();

        //evita a divisão por zero
        if(height == 0) height = 1;
        //calcula a proporção da janela (aspect ratio) da nova janela
        float aspect = (float) width / height;

        //seta o viewport para abranger a janela inteira
        gl.glViewport(0, 0, width, height);

        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade

        //Projeção ortogonal
        //true:   aspect >= 1 configura a altura de -1 para 1 : com largura maior
        //false:  aspect < 1 configura a largura de -1 para 1 : com altura maior
        if(width >= height)
            gl.glOrtho(xMin * aspect, xMax * aspect, yMin, yMax, zMin, zMax);
        else
            gl.glOrtho(xMin, xMax, yMin / aspect, yMax / aspect, zMin, zMax);

        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //lê a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}
}