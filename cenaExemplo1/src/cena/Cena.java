package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.*;
import java.util.Locale;
import com.jogamp.opengl.util.texture.Texture;

public class Cena implements GLEventListener{
    private GL2 gl;
    private int toning = GL2.GL_SMOOTH;
    private float aspect;
    private TextRenderer textRenderer;
    private Texture txtmenu; // Textura menu
    private Texture txtcj; // Textura como jogar
    private Texture txtgo; // Textura game over
    private Texture txtf1; // Textura fase 1
    private Texture txtf2; // Textura fase 2
    public int mode;
    private long tempoInicial;
    public int op = 0;
    private float bolaX = 0;
    private float bolaY = 1f;
    private char direcaoX;
    private char direcaoY = 'd';
    private float velocidade = 0.02f;
    public float moveBarra = 0;
    public int score = 0;
    public int vidas = 5;
    public boolean pause = false;
    private boolean textoVisivel = true;

    public void resetData() {
        bolaX = 0;
        bolaY = 1f;
        direcaoY = 'd';
        pause = false;
        moveBarra = 0;
        op = 0;
        velocidade = 0.02f;
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
                fundoF1();
                fase1();
                if (score == 40){
                    op = 2;
                }
                if (vidas < 1){
                    op = 3;
                }
                break;

            case 2:
                fundoF2();
                fase1();
//                gl.glPushMatrix();
//                gl.glPopMatrix();
                velocidade += 0.00001f;

                if (vidas < 1){
                    op = 3;
                }
                break;

            case 3:
                gameOver();
                break;
            case 4:
                fundoCJ();
                break;

            default:

                break;
        }

        gl.glFlush();
    }

    public void menu() {
        fundoMenu();
        gl.glPushMatrix();
        if(textoVisivel) {
            desenhaTextoSuave(gl, 470, 50, Color.WHITE, "Aperte S para INICIAR!");
        }
        gl.glPopMatrix();
        gl.glEnd();
    }

    public boolean gameOver(){
        if (vidas <= 0) {
            fundoGo();
            gl.glPushMatrix();
                desenhaTexto(gl, 480, 225, Color.WHITE, "Almas coletadas " + score);
            gl.glPopMatrix();
            gl.glEnd();
        }
        return true;
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

        if (!pause) {
            bolaMove();
        }
        else {
            desenhaTextoEspecifico(gl, 440, 500, Color.WHITE , "Jogo Pausado", 70);
            desenhaTexto(gl, 460, 350, Color.WHITE, "Aperte R para RECOMEÇAR");
            desenhaTexto(gl, 410, 300, Color.WHITE, "Aperte M para voltar ao MENU");
        }

        // Desenha barra de Vida
        gl.glPushMatrix();
        criaVida();
        gl.glPopMatrix();

        desenhaTexto(gl, 1110, 670, Color.WHITE, "Almas " + score);
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

    public void fundoMenu() {

        gl.glEnable(GL2.GL_TEXTURE_2D);
        txtmenu.enable(gl);
        txtmenu.bind(gl);

        // Desenhar o objeto
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(1.0f, 1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-1.0f, 1.0f);
        gl.glEnd();

        // Desativar a textura
        txtmenu.disable(gl);
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    public void fundoCJ() {

        gl.glEnable(GL2.GL_TEXTURE_2D);
        txtcj.enable(gl);
        txtcj.bind(gl);

        // Desenhar o objeto
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(1.0f, 1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-1.0f, 1.0f);
        gl.glEnd();

        // Desativar a textura
        txtcj.disable(gl);
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    public void fundoF1() {

        gl.glEnable(GL2.GL_TEXTURE_2D);
        txtf1.enable(gl);
        txtf1.bind(gl);

        // Desenhar o objeto
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(1.0f, 1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-1.0f, 1.0f);
        gl.glEnd();

        // Desativar a textura
        txtf1.disable(gl);
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    public void fundoF2() {

        gl.glEnable(GL2.GL_TEXTURE_2D);
        txtf2.enable(gl);
        txtf2.bind(gl);

        // Desenhar o objeto
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(1.0f, 1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-1.0f, 1.0f);
        gl.glEnd();

        // Desativar a textura
        txtf2.disable(gl);
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    public void fundoGo() {

        gl.glEnable(GL2.GL_TEXTURE_2D);
        txtgo.enable(gl);
        txtgo.bind(gl);

        // Desenhar o objeto
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(1.0f, 1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-1.0f, 1.0f);
        gl.glEnd();

        // Desativar a textura
        txtgo.disable(gl);
        gl.glDisable(GL2.GL_TEXTURE_2D);
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

    private void desenhaTextoSuave(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase) {
        long tempoAtual = System.currentTimeMillis();
        long tempoPassado = tempoAtual - tempoInicial;

        float alpha = (float) Math.abs(Math.sin(tempoPassado / 1300.0)); // Ajuste a frequência aqui

        // Configura a cor com a intensidade alpha
        cor = new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), (int) (alpha * 255));
        desenhaTexto(gl, xPosicao, yPosicao, cor, frase);

        gl.glColor4f(1f, 1f, 1f, 1f);
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

    // Função para mudar tamanho de um TEXTO ESPECÍFICOS
    public void desenhaTextoEspecifico(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase, int tamanhoFonte) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        TextRenderer textRendererEspecifico = new TextRenderer(new Font("OptimusPrinceps", Font.BOLD, tamanhoFonte));
        textRendererEspecifico.beginRendering(Renderer.screenWidth, Renderer.screenHeight);
        textRendererEspecifico.setColor(cor);
        textRendererEspecifico.draw(frase, xPosicao, yPosicao);
        textRendererEspecifico.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, mode);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Dados iniciais da cena
        gl = drawable.getGL().getGL2();
        textRenderer = new TextRenderer(new Font("Castellar", Font.BOLD,25));
        randomBola();

        // temporizador de texto
        tempoInicial = System.currentTimeMillis();

        // Carrega a textura usando a classe Textura
        txtmenu = Textura.loadTexture(gl, "src/texturas/pong_souls.jpg");
        txtcj = Textura.loadTexture(gl, "src/texturas/como_jogar.jpg");
        txtgo = Textura.loadTexture(gl, "src/texturas/gameover.jpg");
        txtf1 = Textura.loadTexture(gl, "src/texturas/fundo_fase_1.jpg");
        txtf2 = Textura.loadTexture(gl, "src/texturas/fundo_fase_2.jpg");
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}
}