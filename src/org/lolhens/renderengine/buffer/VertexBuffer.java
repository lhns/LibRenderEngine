package org.lolhens.renderengine.buffer;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class VertexBuffer {
    protected int idVBO;
    protected boolean dirty;
    protected int vertexBufferSize;
    protected ByteBuffer vertexBuffer;
    protected int drawMode, usageMode = GL15.GL_DYNAMIC_DRAW;
    protected float transX = 0, transY = 0, transZ = 0;
    protected float rotX = 0, rotY = 0, rotZ = 0, rotOrX = 0, rotOrY = 0, rotOrZ = 0;
    protected int lastVertex;
    protected int extend = 0;

    protected static final int BYTE_DATA_SIZE = 1;
    protected static final int SHORT_DATA_SIZE = BYTE_DATA_SIZE * 2;
    protected static final int FLOAT_DATA_SIZE = BYTE_DATA_SIZE * 4;
    protected static final int INT_DATA_SIZE = BYTE_DATA_SIZE * 4;
    protected static final int POSITION_DATA_SIZE = FLOAT_DATA_SIZE * 3;
    protected static final int NORMAL_DATA_SIZE = FLOAT_DATA_SIZE * 3;
    protected static final int TEXCOORD_DATA_SIZE = FLOAT_DATA_SIZE * 2;
    protected static final int COLOR_DATA_SIZE = BYTE_DATA_SIZE * 4;
    protected static final int STRIDE_DATA_SIZE = POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXCOORD_DATA_SIZE + COLOR_DATA_SIZE;

    public static final int VERTICES_VERTEX = 1;
    public static final int VERTICES_TRIANGLE_TRIANGLES = VERTICES_VERTEX * 3;
    public static final int VERTICES_TRIANGLE_QUADS = 0;
    public static final int VERTICES_QUAD_TRIANGLES = VERTICES_TRIANGLE_TRIANGLES * 2;
    public static final int VERTICES_QUAD_QUADS = VERTICES_VERTEX * 4;
    public static final int VERTICES_CUBE_TRIANGLES = VERTICES_QUAD_TRIANGLES * 6;
    public static final int VERTICES_CUBE_QUADS = VERTICES_QUAD_QUADS * 6;

    public VertexBuffer(int drawMode, int vertices) {
        this.drawMode = drawMode;
        prepareBuffers(vertices);
        clear();
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    }

    protected void prepareBuffers(int vertices) {
        vertexBuffer = ByteBuffer.allocateDirect(vertices * STRIDE_DATA_SIZE).order(ByteOrder.nativeOrder());
        idVBO = GL15.glGenBuffers();
    }

    public void clear() {
        dirty = true;
        lastVertex = -1;
        vertexBuffer.clear();
        vertexBufferSize = 0;
    }

    public void addVertex(float x, float y, float z, float nx, float ny, float nz, float u, float v, float r, float g, float b,
            float a) {
        dirty = true;
        if (rotX != 0 || rotY != 0 || rotZ != 0) {
            float transX = x - rotOrX, transY = y - rotOrY, transZ = z - rotOrZ, oldNX = nx, oldNY = ny, oldNZ = nz;
            x = (float) (Math.cos(rotZ)
                    * (Math.cos(rotY) * transX + Math.sin(rotY) * (Math.sin(rotX) * transY + Math.cos(rotX) * transZ)) - Math
                    .sin(rotZ) * (Math.cos(rotX) * transY - Math.sin(rotX) * transZ))
                    + rotOrX;
            y = (float) (Math.sin(rotZ)
                    * (Math.cos(rotY) * transX + Math.sin(rotY) * (Math.sin(rotX) * transY + Math.cos(rotX) * transZ)) + Math
                    .cos(rotZ) * (Math.cos(rotX) * transY - Math.sin(rotX) * transZ))
                    + rotOrY;
            z = (float) (-Math.sin(rotY) * transX + Math.cos(rotY) * (Math.sin(rotX) * transY + Math.cos(rotX) * transZ))
                    + rotOrZ;
            nx = (float) (Math.cos(rotZ)
                    * (Math.cos(rotY) * oldNX + Math.sin(rotY) * (Math.sin(rotX) * oldNY + Math.cos(rotX) * oldNZ)) - Math
                    .sin(rotZ) * (Math.cos(rotX) * oldNY - Math.sin(rotX) * oldNZ));
            ny = (float) (Math.sin(rotZ)
                    * (Math.cos(rotY) * oldNX + Math.sin(rotY) * (Math.sin(rotX) * oldNY + Math.cos(rotX) * oldNZ)) + Math
                    .cos(rotZ) * (Math.cos(rotX) * oldNY - Math.sin(rotX) * oldNZ));
            nz = (float) (-Math.sin(rotY) * oldNX + Math.cos(rotY) * (Math.sin(rotX) * oldNY + Math.cos(rotX) * oldNZ));
        }
        if (transX != 0 || transY != 0 || transZ != 0) {
            x += transX;
            y += transY;
            z += transZ;
        }
        if (vertexBufferSize + STRIDE_DATA_SIZE > vertexBuffer.capacity()) extend();
        vertexBuffer.putFloat(vertexBufferSize, x);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.putFloat(vertexBufferSize, y);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.putFloat(vertexBufferSize, z);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.putFloat(vertexBufferSize, nx);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.putFloat(vertexBufferSize, ny);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.putFloat(vertexBufferSize, nz);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.putFloat(vertexBufferSize, u);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.putFloat(vertexBufferSize, v);
        vertexBufferSize += FLOAT_DATA_SIZE;
        vertexBuffer.put(vertexBufferSize, (byte) (r * 255));
        vertexBufferSize += BYTE_DATA_SIZE;
        vertexBuffer.put(vertexBufferSize, (byte) (g * 255));
        vertexBufferSize += BYTE_DATA_SIZE;
        vertexBuffer.put(vertexBufferSize, (byte) (b * 255));
        vertexBufferSize += BYTE_DATA_SIZE;
        vertexBuffer.put(vertexBufferSize, (byte) (a * 255));
        vertexBufferSize += BYTE_DATA_SIZE;
        lastVertex++;
    }

    protected void extend() {
        if (extend > 0) {
            vertexBuffer = ByteBuffer.allocateDirect(vertexBuffer.capacity() + extend * STRIDE_DATA_SIZE).order(
                    ByteOrder.nativeOrder());
        } else {
            throw new BufferOverflowException();
        }
    }

    public void addTriangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float nx,
            float ny, float nz, float u, float v, float r, float g, float b, float a) {
        if (drawMode == GL11.GL_TRIANGLES) {
            addVertex(x1, y1, z1, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x2, y2, z2, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x3, y3, z3, nx, ny, nz, u, v, r, g, b, a);
        }
    }

    public void addQuad(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4,
            float y4, float z4, float nx, float ny, float nz, float u, float v, float r, float g, float b, float a) {
        if (drawMode == GL11.GL_TRIANGLES) {
            addVertex(x1, y1, z1, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x2, y2, z2, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x4, y4, z4, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x2, y2, z2, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x3, y3, z3, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x4, y4, z4, nx, ny, nz, u, v, r, g, b, a);
        } else if (drawMode == GL11.GL_QUADS) {
            addVertex(x1, y1, z1, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x2, y2, z2, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x3, y3, z3, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x4, y4, z4, nx, ny, nz, u, v, r, g, b, a);
        }
    }

    public void addCube(float x1, float y1, float z1, float x2, float y2, float z2, float u, float v, float r, float g, float b,
            float a) {
        // TOP
        addQuad(x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1, 0, 1, 0, u, v, r, g, b, a);
        // FRONT
        addQuad(x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, 0, 0, -1, u, v, r, g, b, a);
        // BACK
        addQuad(x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2, 0, 0, 1, u, v, r, g, b, a);
        // RIGHT
        addQuad(x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, 1, 0, 0, u, v, r, g, b, a);
        // LEFT
        addQuad(x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1, -1, 0, 0, u, v, r, g, b, a);
        // BOTTOM
        addQuad(x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, 0, -1, 0, u, v, r, g, b, a);
    }

    public void setRotation(float rotX, float rotY, float rotZ, float rotOrX, float rotOrY, float rotOrZ) {
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.rotOrX = rotOrX;
        this.rotOrY = rotOrY;
        this.rotOrZ = rotOrZ;
    }

    public void setUsageMode(int usageMode) {
        this.usageMode = usageMode;
    }

    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }

    public void addVertexArray(byte[] array) {
        vertexBuffer.put(array, vertexBufferSize, array.length);
        vertexBufferSize += BYTE_DATA_SIZE * array.length;
        lastVertex += BYTE_DATA_SIZE * array.length / STRIDE_DATA_SIZE;
    }

    public byte[] getVertexArray() {
        byte[] array = new byte[vertexBufferSize];
        vertexBuffer.get(array, 0, vertexBufferSize);
        return array;
    }

    public void draw() {
        if (vertexBufferSize > 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, idVBO);
            if (dirty) {
                dirty = false;
                vertexBuffer.limit(vertexBufferSize);
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, usageMode);
                vertexBuffer.limit(vertexBuffer.capacity());
            }
            setPointers();
            GL11.glDrawArrays(drawMode, 0, vertexBufferSize / STRIDE_DATA_SIZE);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
    }

    protected void setPointers() {
        int offset = 0;
        GL11.glVertexPointer(3, GL11.GL_FLOAT, STRIDE_DATA_SIZE, offset);
        offset += POSITION_DATA_SIZE;
        GL11.glNormalPointer(GL11.GL_FLOAT, STRIDE_DATA_SIZE, offset);
        offset += NORMAL_DATA_SIZE;
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, STRIDE_DATA_SIZE, offset);
        offset += TEXCOORD_DATA_SIZE;
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, STRIDE_DATA_SIZE, offset);
    }
}
