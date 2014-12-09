package org.lolhens.renderengine.texture;

import com.dafttech.math.Vector;

public class Texture {
    private TextureFile texFile;
    private Vector texCoord;

    public Texture(TextureFile texFile, float u, float v) {
        this.texFile = texFile;
        texCoord = new Vector(u, v);
    }

    public TextureFile getTextureFile() {
        return texFile;
    }

    public float getU() {
        return texCoord.x;
    }

    public float getV() {
        return texCoord.y;
    }
}
