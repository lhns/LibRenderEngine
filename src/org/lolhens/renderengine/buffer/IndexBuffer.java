package org.lolhens.renderengine.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class IndexBuffer extends VertexBuffer {
    protected int idIBO;
    protected int indexBufferSize;
    protected ByteBuffer indexBuffer;

    protected static final int INDEX_DATA_SIZE = INT_DATA_SIZE;

    public static final int INDICES_VERTEX = 1;
    public static final int INDICES_TRIANGLE_TRIANGLES = VERTICES_VERTEX * 3;
    public static final int INDICES_TRIANGLE_QUADS = 0;
    public static final int INDICES_QUAD_TRIANGLES = VERTICES_TRIANGLE_TRIANGLES * 2;
    public static final int INDICES_QUAD_QUADS = VERTICES_VERTEX * 4;
    public static final int INDICES_CUBE_TRIANGLES = VERTICES_QUAD_TRIANGLES * 6;
    public static final int INDICES_CUBE_QUADS = VERTICES_QUAD_QUADS * 6;

    public IndexBuffer(int mode, int indices) {
        super(mode, indices);
    }

    @Override
    protected void prepareBuffers(int indices) {
        super.prepareBuffers(indices);
        indexBuffer = ByteBuffer.allocateDirect(indices * STRIDE_DATA_SIZE).order(ByteOrder.nativeOrder());
        idIBO = GL15.glGenBuffers();
    }

    @Override
    public void clear() {
        super.clear();
        indexBuffer.clear();
        indexBufferSize = 0;
    }

    public void addIndices(int numIndices, int... indices) {
        for (int index : indices) {
            indexBuffer.putInt(indexBufferSize, lastVertex - numIndices + index);
            indexBufferSize += INDEX_DATA_SIZE;
        }
    }

    @Override
    public void addTriangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float nx,
            float ny, float nz, float u, float v, float r, float g, float b, float a) {
        if (drawMode == GL11.GL_QUADS) {
        } else {
            addVertex(x1, y1, z1, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x2, y2, z2, nx, ny, nz, u, v, r, g, b, a);
            addVertex(x3, y3, z3, nx, ny, nz, u, v, r, g, b, a);
            addIndices(3, 1, 2, 3);
        }
    }

    @Override
    public void addQuad(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4,
            float y4, float z4, float nx, float ny, float nz, float u, float v, float r, float g, float b, float a) {
        addVertex(x1, y1, z1, nx, ny, nz, u, v, r, g, b, a);
        addVertex(x2, y2, z2, nx, ny, nz, u, v, r, g, b, a);
        addVertex(x3, y3, z3, nx, ny, nz, u, v, r, g, b, a);
        addVertex(x4, y4, z4, nx, ny, nz, u, v, r, g, b, a);
        if (drawMode == GL11.GL_QUADS) {
            addIndices(4, 1, 2, 3, 4);
        } else {
            addIndices(4, 1, 2, 4, 2, 3, 4);
        }
    }

    public void addFastCube(float x1, float y1, float z1, float x2, float y2, float z2, float u, float v, float r, float g,
            float b, float a) {
        addVertex(x1, y1, z1, -0.57735f, -0.57735f, -0.57735f, u, v, r, g, b, a);
        addVertex(x2, y1, z1, 0.57735f, -0.57735f, -0.57735f, u, v, r, g, b, a);
        addVertex(x1, y2, z1, -0.57735f, 0.57735f, -0.57735f, u, v, r, g, b, a);
        addVertex(x2, y2, z1, 0.57735f, 0.57735f, -0.57735f, u, v, r, g, b, a);
        addVertex(x1, y1, z2, -0.57735f, -0.57735f, 0.57735f, u, v, r, g, b, a);
        addVertex(x2, y1, z2, 0.57735f, -0.57735f, 0.57735f, u, v, r, g, b, a);
        addVertex(x1, y2, z2, -0.57735f, 0.57735f, 0.57735f, u, v, r, g, b, a);
        addVertex(x2, y2, z2, 0.57735f, 0.57735f, 0.57735f, u, v, r, g, b, a);
        if (drawMode == GL11.GL_QUADS) {
            addIndices(8, 3, 7, 8, 4, 1, 3, 4, 2, 6, 8, 7, 5, 2, 4, 8, 6, 5, 7, 3, 1, 1, 2, 6, 5);
        } else {
            addIndices(8, 3, 7, 4, 7, 8, 4, 1, 3, 2, 3, 4, 2, 6, 8, 5, 8, 7, 5, 2, 4, 6, 4, 8, 6, 5, 7, 1, 7, 3, 1, 1, 2, 5, 2,
                    6, 5);
        }
    }

    /*
     * CALL BEFORE ADDING THE VERTEX ARRAY!!!
     */
    public void addIndexArray(byte[] array) {
        indexBuffer.put(array, indexBufferSize, array.length);
        int index;
        for (int i = 0; i < array.length / INDEX_DATA_SIZE; i++) {
            index = indexBufferSize + i * INDEX_DATA_SIZE;
            indexBuffer.putInt(index, indexBuffer.getInt(index) + lastVertex);
        }
        indexBufferSize += BYTE_DATA_SIZE * array.length;
    }

    public byte[] getIndexArray() {
        byte[] array = new byte[vertexBufferSize];
        indexBuffer.get(array, 0, vertexBufferSize);
        return array;
    }

    @Override
    public void draw() {
        if (indexBufferSize > 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, idVBO);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idIBO);
            if (dirty) {
                dirty = false;
                vertexBuffer.limit(vertexBufferSize);
                indexBuffer.limit(indexBufferSize);
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, usageMode);
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, usageMode);
                vertexBuffer.limit(vertexBuffer.capacity());
                indexBuffer.limit(indexBuffer.capacity());
            }
            setPointers();
            GL11.glDrawElements(drawMode, indexBufferSize / INT_DATA_SIZE, GL11.GL_UNSIGNED_INT, 0);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
    }
}
