package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import java.util.Random;

public class Cena implements GLEventListener{
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    GLU glu;
    public float translacao = 0;
    double limite = 2*Math.PI;
    private float cX = 0.0f; // Posição x da bola
    private float cY = 0.0f; // Posição y da bola
    private float rX = 0.1f; // Raio x da bola
    private float rY = 0.1f; // Raio y da bola

    // Randomiza a Bola
    private Random random;
    public Cena() {
        random = new Random();
    }
    // Limita a Bola
    private float limiteSuperior = 1.0f;
    private float limiteEsquerdo = -1.8f;
    private float limiteDireito = 1.8f;
    // Velocidade + Direção da Bola
    private float velocidadeBola = 0.01f;
    private float direcaoBolaX = 0.9f;// 1.0 para direita, -1.0 para esquerda
    private float direcaoBolaY = -0.9f;
    // Colisão com a plataforma
    private float plataformaX = 0.0f; // Posição inicial da plataforma no eixo X
    private float plataformaY = -0.9f; // Altura constante da plataforma no eixo Y
    private float larguraPlataforma = 0.6f; // Largura da plataforma
    private float alturaPlataforma = 0.1f; // Ajuste conforme necessário

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        glu = new GLU();
        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -1;
        xMax = yMax = zMax = 1;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();
        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);
        //limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity(); //lê a matriz identidade

        // Desenha a plataforma
        gl.glPushMatrix();
            gl.glTranslatef(translacao,0.05f, 0);
            criaRetangulo(gl);
        gl.glPopMatrix();

        // Desenha a Bola
        gl.glPushMatrix();
            gl.glTranslatef(0,0,0);
            criaBola(gl);
        gl.glPopMatrix();

        gl.glEnd();

        gl.glFlush();

        cX += direcaoBolaX * velocidadeBola;
        cY += direcaoBolaY * velocidadeBola;

        float randomIncrement = random.nextFloat() * 0.02f - 0.01f; // Valor aleatório entre -0.01 e 0.01
        cY += (0.01 + randomIncrement) * direcaoBolaY;
        //cX += (0.01 + randomIncrement) * direcaoBolaX;// Valor constante ajustado conforme necessário

        // Verifica colisão com os limites da tela e inverte a direção se necessário
        if (cX - rX < limiteEsquerdo || cX + rX > limiteDireito) {
            direcaoBolaX = -1.0f; // Inverte a direção horizontal
        }
        if (cY + rY > limiteSuperior) {
            direcaoBolaY = -1.0f; // Inverte a direção vertical
        }
        // Verifica colisão com a plataforma
        if (verificaColisaoBolaComPlataforma()) {
            // Inverte a direção vertical da bola
            direcaoBolaY = -direcaoBolaY;
            //direcaoBolaX = -direcaoBolaX;
        }

    }

    private boolean verificaColisaoBolaComPlataforma() {
        return (cX + rX > plataformaX - larguraPlataforma  &&
                cX - rX < plataformaX + larguraPlataforma  &&
                cY + rY > plataformaY - alturaPlataforma &&
                cY - rY < plataformaY + alturaPlataforma );
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
    public void criaRetangulo(GL2 gl){
        gl.glColor3f(0, 1, 1f); // Cor verde
        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glVertex2f(plataformaX - larguraPlataforma / 2, plataformaY); // Canto inferior esquerdo
        gl.glVertex2f(plataformaX + larguraPlataforma / 2, plataformaY); // Canto inferior direito
        gl.glVertex2f(plataformaX - larguraPlataforma / 2, plataformaY + 0.1f); // Canto superior esquerdo
        gl.glVertex2f(plataformaX + larguraPlataforma / 2, plataformaY + 0.1f); // Canto superior direito
        gl.glEnd();
    }
    public void criaBola(GL2 gl) {
        gl.glColor3d(1, 1, 1);
        gl.glBegin(GL2.GL_POLYGON);
        for (double i = 0; i < limite; i += 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
        }
        gl.glEnd();
    }



    @Override
    public void dispose(GLAutoDrawable drawable) {}
}