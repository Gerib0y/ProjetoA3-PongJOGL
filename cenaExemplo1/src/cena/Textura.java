package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;

public class Textura {
    public static Texture loadTexture(GL2 gl, String filePath) {
        try {
            // Carrega a textura a partir do arquivo
            File textureFile = new File(filePath);
            Texture texture = TextureIO.newTexture(textureFile, true);

            // Configurações de filtragem e repetição da textura
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

            return texture;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
