package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.*;
import java.util.Locale;

public class Cena implements GLEventListener{
    private GL2 gl;
    private int toning = GL2.GL_SMOOTH;
    private float aspect;
    private TextRenderer textRenderer;
    public int mode; // Menu
    public int op = 0; // Menu
    private float bolaX = 0;
    private float bolaY = 1f;
    private char direcaoX;
    private char direcaoY = 'd';
    private float velocidade = 0.02f;
    public float moveBarra = 0;
    public int score = 0;
    public int vidas = 5;
    public boolean pause = false;

    public void resetData() {
        bolaX = 0;
        bolaY = 1f;
        direcaoY = 'd';
        pause = false;
        op = 0;
        moveBarra = 0;
        score = 0;
        vidas = 5;
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
            case 0:
                menu();
                break;

            case 1:
                fase1();
                if (score == 40){
                    op = 2;
                }
                if (vidas < 1){
                    op = 3;
                }
                break;

            case 2:
                gl.glClearColor(0,0.3f,0.3f,1);
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
                fase1();
                gl.glPushMatrix();
                obstaculo();
                gl.glPopMatrix();
                velocidade += 0.00001f;

                if (vidas < 1){
                    op = 3;
                }
                break;

            case 3:
                gl.glClearColor(0, 0, 0, 1);
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
                gameOver();

                break;
            case 4:
                comandos();
                break;

            default:

                break;
        }

        gl.glFlush();
    }

    public void menu() {
        gl.glPushMatrix();
            desenhaTextoEspecifico(gl, 500, 650, Color.CYAN, "-=PONG=-",60);
            desenhaTexto(gl, 50, 450, Color.WHITE, "Aperte 1 para saber COMO JOGAR");
            desenhaTexto(gl, 50, 350, Color.WHITE, "Aperte 2 para acessar os CRÉDITOS");
            desenhaTextoEspecifico(gl, 410, 150, Color.GREEN, "Aperte S para INICIAR!", 50);
        gl.glPopMatrix();
            gl.glLineWidth(10.0f);
            gl.glColor3f(0f, 1f, 1f);
            gl.glBegin(GL2.GL_LINE_LOOP);
            gl.glVertex2f(-1f, -0.9999f);
            gl.glVertex2f(1, -1);
            gl.glVertex2f(1, 1);
            gl.glVertex2f(-0.9999f, 1);
        gl.glEnd();
    }

    public void comandos() {
        gl.glPushMatrix();
        desenhaTextoEspecifico(gl, 450, 650, Color.WHITE, "COMO JOGAR", 60);
            desenhaTexto(gl, 50, 450, Color.WHITE, "← - Move a barra para esquerda");
            desenhaTexto(gl, 50, 400, Color.WHITE, "→ - Move a barra para direita");
            desenhaTexto(gl, 50, 350, Color.WHITE, "R - Recomeça o JOGO");
            desenhaTextoEspecifico(gl, 50, 150, Color.CYAN, "Aperte M para voltar ao MENU PRINCIPAl!", 50);
        gl.glPopMatrix();
    }

    public boolean gameOver(){
        if (vidas <= 0) {
            gl.glPushMatrix();
                desenhaTexto(gl, 500, 600, Color.RED, "GAME OVER!");
                desenhaTexto(gl, 320, 450, Color.WHITE, "Seu Score total foi de: " + score);
                desenhaTexto(gl, 320, 350, Color.WHITE, "Aperte R para RECOMEÇAR!");
                desenhaTexto(gl, 320, 300, Color.WHITE, "Aperte M para voltar ao MENU!");
            gl.glPopMatrix();
                gl.glLineWidth(10.0f);
                gl.glColor3f(1f, 0f, 0f);
            gl.glBegin(GL2.GL_LINE_LOOP);
                gl.glVertex2f(-1f, -0.9999f);
                gl.glVertex2f(1, -1);
                gl.glVertex2f(1, 1);
                gl.glVertex2f(-0.9999f, 1);
            gl.glEnd();
        }
        return true;
    }

    public void fase1() {
        if (!pause) {
            bolaMove();
        }
        else {
            desenhaTexto(gl, 500, 600, Color.WHITE , "JOGO PAUSADO");
        }

        // Desenha a Barra
        gl.glPushMatrix();
        criaRetangulo();
        gl.glPopMatrix();

        // Desenha a Bola
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 0);
        criaBola();
        gl.glPopMatrix();

        // Desenha barra de Vida
        gl.glPushMatrix();
        criaVida();
        gl.glPopMatrix();

        desenhaTexto(gl, 1120, 670, Color.WHITE, "Almas " + score);
        //desenhaTexto(gl, 20, 670, Color.WHITE, "Vidas:" + vidas);
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

    public void obstaculo(){
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_TRIANGLES);
            gl.glColor3f(1, 1, 1);
            gl.glVertex2f(-0.7f,0.7f );
            gl.glVertex2f(-0.8f,0.5f);
            gl.glVertex2f(-0.6f,0.5f);
            gl.glVertex2f(0.7f, 0.7f);
            gl.glVertex2f(0.8f,0.5f);
            gl.glVertex2f(0.6f, 0.5f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void criaRetangulo(){
        gl.glPushMatrix();
            gl.glTranslatef(moveBarra, 0, 0);
            gl.glBegin(GL2.GL_QUADS);
                gl.glColor3f(0.5f, 0.5f, 0.5f);
                gl.glVertex2f(-0.2f, -0.8f);
                gl.glColor3f(0f, 0f, 0f);
                gl.glVertex2f(0.2f, -0.8f);
                gl.glColor3f(0.5f, 0.5f, 0.5f);
                gl.glVertex2f(0.2f, -0.9f);
                gl.glColor3f(0.0f, 0.0f, 0.0f);
                gl.glVertex2f(-0.2f, -0.9f);
            gl.glEnd();
        gl.glPopMatrix();
    }

    public void criaBola() {
        gl.glPushMatrix();
        gl.glTranslatef(bolaX, bolaY, 0);

        double limit = 2 * Math.PI;
        double i;
        double cX = 0;
        double cY = 0;
        double rX = 0.1f / aspect;
        double rY = 0.1f;

        gl.glBegin(GL2.GL_POLYGON);
        for (i = 0; i < limit; i += 0.01) {
            double x = cX + rX * Math.cos(i);
            double y = cY + rY * Math.sin(i);
            gl.glColor3f(0f, 0f, 0f);
            gl.glVertex2d(x, y);
        }
        gl.glEnd();

        // Contorno Laranja
        gl.glColor3f(1.0f, 0.5f, 0.0f);
        gl.glBegin(GL2.GL_LINE_LOOP);
        for (i = 0; i < limit; i += 0.01) {
            double x = cX + rX * Math.cos(i);
            double y = cY + rY * Math.sin(i);
            gl.glVertex2d(x, y);
        }
        gl.glEnd();

        gl.glPopMatrix();
    }

    public void criaVida(){
        for (int j = 0; j < vidas; j++) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(0.255f,0,0);
                gl.glVertex2f(-1, 0.95f);
                gl.glVertex2f(-1, 0.85f);
                gl.glVertex2f(-0.9f,0.85f);
                gl.glVertex2f(-0.9f,0.95f);
            gl.glEnd();

            // Tira uma parte da barra da vida
            gl.glTranslatef(0.1f, 0, 0);
        }
    }

    public void randomBola() {
        double xRandom = -0.8f + Math.random() * 1.6f;
        if (xRandom > 0) {
            direcaoX = 'r';
        }
        else {
            direcaoX = 'l';
        }

        bolaX = Float.valueOf(String.format(Locale.US, "%.2f", xRandom));
    }

    public boolean colisaoBolaBarra(float xTranslatedBallFixed) {
        float leftBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", moveBarra - 0.2f));
        float rightBarLimit = Float.valueOf(String.format(Locale.US, "%.1f", moveBarra + 0.2f));

        if (leftBarLimit <= xTranslatedBallFixed && rightBarLimit >= xTranslatedBallFixed) {
            return true;
        }

        return false;
    }

    public boolean colisaoBolaY(float xObj, float yObj, float bLimit, float tLimit, float xPoint) {
        if (tLimit >= yObj && bLimit <= yObj && xObj == xPoint) {
            return true;
        }

        return false;
    }

    public boolean colisaoBolaX(float xObj, float heightObj, float lLimit, float rLimit, float tLimit) {
        if (lLimit <= xObj && rLimit >= xObj && heightObj == tLimit) {
            return true;
        }

        return false;
    }

    public void bolaMove() {
        float xTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", bolaX));
        float yTransBallFixed = Float.valueOf(String.format(Locale.US, "%.1f", bolaY));

        // Colisão com objeto da fase 2
//        if (op == 2 && direcaoX == 'l'
//                && colisaoBolaY(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, 0.2f)) {
//            direcaoX = 'r';
//        }
//        if (op == 2 && direcaoX == 'r'
//                && colisaoBolaY(xTransBallFixed, yTransBallFixed, -0.1f, 0.5f, -0.2f)) {
//            direcaoX = 'l';
//        }
        // Colisão com a plataforma
        if (xTransBallFixed > -1f && direcaoX == 'l') {
            bolaX -= velocidade/2;
        }
        else if (xTransBallFixed == -1f && direcaoX == 'l') {
            direcaoX = 'r';
        }
        else if (xTransBallFixed < 1f && direcaoX == 'r') {
            bolaX += velocidade/2;
        }
        else if (xTransBallFixed == 1f && direcaoX == 'r') {
            direcaoX = 'l';
        }

        // Colisão com objeto da fase 2
//        if (op == 2 && direcaoY == 'u'
//                && colisaoBolaX(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, -0.2f)) {
//            direcaoY = 'd';
//        }
//        else if (op == 2 && direcaoY == 'd'
//                && colisaoBolaX(xTransBallFixed, yTransBallFixed, -0.2f, 0.2f, 0.6f)) {
//            direcaoY = 'u';
//        }
        // Colisão com a plataforma
        if (yTransBallFixed == -0.7f && direcaoY == 'd' && colisaoBolaBarra(xTransBallFixed)) {
            direcaoY = 'u';
            //lightOn = false;
            toning = toning == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
            score += 10;
        }
        else if (yTransBallFixed < 0.9f && direcaoY == 'u') {
            bolaY += velocidade;
        }
        else if (yTransBallFixed == 0.9f && direcaoY == 'u') {
            direcaoY = 'd';
        }
        else if (yTransBallFixed < -1f) {
            bolaY = 1f;
            bolaX = 0;
            vidas--;
            randomBola();
        }
        else {
            bolaY -= velocidade;
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
        System.out.println("Reshape: " + width + ", " + height);
    }

    // Funçao para mudar tamanho de TEXTO ESPECÍFICOS
    public void desenhaTextoEspecifico(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase, int tamanhoFonte) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        TextRenderer textRendererEspecifico = new TextRenderer(new Font("Fixedsys Regular", Font.BOLD, tamanhoFonte));
        textRendererEspecifico.beginRendering(Renderer.screenWidth, Renderer.screenHeight);
        textRendererEspecifico.setColor(cor);
        textRendererEspecifico.draw(frase, xPosicao, yPosicao);
        textRendererEspecifico.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, mode);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        gl = drawable.getGL().getGL2();
        textRenderer = new TextRenderer(new Font("Fixedsys Regular", Font.BOLD,30));
        randomBola();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}
}