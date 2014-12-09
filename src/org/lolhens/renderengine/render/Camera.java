package org.lolhens.renderengine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.dafttech.math.Vector;

public class Camera {
    private Window window;
    private Vector axis = new Vector(0, 1, 0);
    private Vector pos = new Vector(0, 0, 0);
    private Vector at = new Vector(0, 0, 0);
    private float fov, aspect, zNear, zFar;

    public Camera(Window window, float fov, float zNear, float zFar) {
        this.window = window;
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
        updateAspect();
    }

    private void updatePerspective() {
        GL11.glViewport(0, 0, (int) window.getWidth(), (int) window.getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(fov, aspect, zNear, zFar);
    }

    public void updateAspect() {
        setAspect(window.getWidth() / window.getHeight());
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
        updatePerspective();
    }

    public void setAxis(Vector vec) {
        axis.set(vec);
        update();
    }

    public void lookAt(Vector vec) {
        at.set(vec);
        update();
    }

    public void setPos(Vector vec) {
        pos.set(vec);
        update();
    }

    public void rotateAround(Vector rot, Vector origin) {
        pos.rotate(rot, origin);
        update();
    }

    public void update() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GLU.gluLookAt(pos.x, pos.y, pos.z, at.x, at.y, at.z, axis.x, axis.y, axis.z);
    }
}
