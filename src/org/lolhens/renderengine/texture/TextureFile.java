package org.lolhens.renderengine.texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.dafttech.math.Vector;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureFile {
    private ByteBuffer pixels;
    private Vector size;
    private int unit, id;

    public TextureFile(File file, int unit) throws IOException {
        InputStream fileStream = new FileInputStream(file);
        PNGDecoder pngDecoder = new PNGDecoder(fileStream);
        size = new Vector(pngDecoder.getWidth(), pngDecoder.getHeight());
        pixels = ByteBuffer.allocateDirect(size.xi() * size.yi() * 4);
        pngDecoder.decode(pixels, size.xi() * 4, Format.RGBA);
        fileStream.close();
        pixels.flip();
        this.unit = unit;
        id = GL11.glGenTextures();
        setup();
    }

    public TextureFile(File file) throws IOException {
        this(file, GL13.GL_TEXTURE0);
    }

    private void setup() {
        bind();
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, size.xi(), size.yi(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                pixels);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        unbind();
    }

    public void bind() {
        GL13.glActiveTexture(unit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

    }

    public void unbind() {
        GL13.glActiveTexture(unit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
