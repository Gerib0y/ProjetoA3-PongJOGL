package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.*;
import java.util.Locale;

public class Cena implements GLEventListener{
    private float aspect;
    private float xTranslateBall = 0;
    private float yTranslateBall = 1f;
    private char xDirection;
    private char yDirection = 'd';
    private int toning = GL2.GL_SMOOTH;
    public int op = 0; // Menu
    private float gameSpeed = 0.02f;
    public int mode; // Menu
    public float userBarMove = 0;
    private TextRenderer textRenderer;
    private GL2 gl;
    private GLUT glut;
    public int score = 0;

    public void resetData() {
        xTranslateBall = 0;
        yTranslateBall = 1f;
        yDirection = 'd';

        //isGamePaused = false;
        //gameFase = 0;

        userBarMove = 0;
        score = 0;
        //userLives = 5;
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
        glut = new GLUT();

        switch (op) {
            case 0:
                menu();
                break;
            case 1:
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
        criaRetangulo();
        gl.glPopMatrix();

        // Desenha a Bola
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 0);
        criaBola();
        gl.glPopMatrix();
        bolaMove();
        desenhaTexto(gl, 25, 900, Color.WHITE, "Score:" + score);
        op = 1;
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
    public void criaRetangulo(){
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
    public void criaBola() {
        gl.glPushMatrix();
        gl.glTranslatef(xTranslateBall, yTranslateBall, 0);
        gl.glColor3f(1, 1, 1);

        double limit = 2 * Math.PI;
        double i;
        double cX = 0;
        double cY = 0;
        double rX = 0.1f / aspect;
        double rY = 0.1f;

        gl.glBegin(GL2.GL_POLYGON);
        for (i = 0; i < limit; i += 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
        }
        gl.glEnd();

        gl.glPopMatrix();
    }

    public void randomRunBall() {
        double xRandom = -0.8f + Math.random() * 1.6f;
        if (xRandom > 0) {
            xDirection = 'r';
        } else {
            xDirection = 'l';
        }
        xTranslateBall = Float.valueOf(String.format(Locale.US, "%.2f", xRandom));
    }

    public boolean isBallInRangeOfBar(float xTranslatedBallFixed) {
        float leftBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", userBarMove - 0.2f));
        float rightBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", userBarMove + 0.2f));

        if (leftBarLimit <= xTranslatedBallFixed && rightBarLimit >= xTranslatedBallFixed) {
            return true;
        }
        return false;
    }

    public boolean isObjectInYRange(float xObj, float yObj, float bLimit, float tLimit, float xPoint) {
        if (tLimit >= yObj && bLimit <= yObj && xObj == xPoint) {
            return true;
        }
        return false;
    }

    public boolean isObjectInXRange(float xObj, float heightObj, float lLimit, float rLimit, float tLimit) {
        if (lLimit <= xObj && rLimit >= xObj && heightObj == tLimit) {
            return true;
        }
        return false;
    }

    public void bolaMove() {
        float xTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", xTranslateBall));
        float yTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", yTranslateBall));

        if (op == 2 && xDirection == 'l'
                && isObjectInYRange(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, 0.2f)) {
            xDirection = 'r';
        }
        if (op == 2 && xDirection == 'r'
                && isObjectInYRange(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, -0.2f)) {
            xDirection = 'l';
        } else if (xTransBallFixed > -1f && xDirection == 'l') {
            xTranslateBall -= gameSpeed/2;
        } else if (xTransBallFixed == -1f && xDirection == 'l') {
            xDirection = 'r';
        } else if (xTransBallFixed < 1f && xDirection == 'r') {
            xTranslateBall += gameSpeed/2;
        } else if (xTransBallFixed == 1f && xDirection == 'r') {
            xDirection = 'l';
        }

        if (op == 2 && yDirection == 'u'
                && isObjectInXRange(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, -0.2f)) {
            yDirection = 'd';
        } else if (op == 2 && yDirection == 'd'
                && isObjectInXRange(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, 0.6f)) {
            yDirection = 'u';
        } else if (yTransBallFixed == -0.7f && yDirection == 'd'
                && isBallInRangeOfBar(xTransBallFixed)) {
            yDirection = 'u';
            //lightOn = false;
            toning = toning == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
            score += 10;
        } else if (yTransBallFixed < 0.9f && yDirection == 'u') {
            yTranslateBall += gameSpeed;
        } else if (yTransBallFixed == 0.9f && yDirection == 'u') {
            yDirection = 'd';
        } else if (yTransBallFixed < -1f) {
            yTranslateBall = 1f;
            xTranslateBall = 0;
            //userLives--;
            randomRunBall();
        } else {
            yTranslateBall -= gameSpeed;
            //lightOn = true;
            toning = toning == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width , int height) {
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();
        //evita a divisão por zero
        if(height == 0) height = 1;
        //calcula a proporção da janela (aspect ratio) da nova janela
        aspect = (float) width / height;
        //seta o viewport para abranger a janela inteira
        gl.glOrtho(-1, 1, -1, 1, -1, 1);
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //lê a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        gl = drawable.getGL().getGL2();
        //glu = new GLU();
        textRenderer = new TextRenderer(new Font("Fixedsys Regular", Font.BOLD,50));
        randomRunBall();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}
}