package org.lolhens.renderengine.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.dafttech.math.Vector;

public class Window {
    private volatile String title;
    private volatile Vector size = new Vector();
    private final RenderThread renderThread;

    public Window(String title, float width, float height) {
        setTitle(title);
        size.set(width, height);
        renderThread = new RenderThread();
    }

    public final Window start() {
        renderThread.start();
        return this;
    }

    public final void setTitle(String title) {
        this.title = title;
        Display.setTitle(title);
    }

    public final void close() {
        renderThread.closed = true;
    }

    public final String getTitle() {
        return title;
    }

    public final float getWidth() {
        return size.x;
    }

    public final float getHeight() {
        return size.y;
    }

    public final boolean isAlive() {
        return !renderThread.closed;
    }

    public void render(float delta) {

    }

    public void setup() {

    }

    public boolean onClose() {
        return true;
    }

    private class RenderThread extends Thread {
        private volatile boolean closed = false;
        private volatile long lastTime, delta;

        @Override
        public void run() {
            try {
                Display.setDisplayMode(new DisplayMode(size.xi(), size.yi()));
                Display.create();
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
            setup();
            lastTime = System.nanoTime();
            while (!closed) {
                delta = System.nanoTime() - lastTime;
                lastTime += delta;
                render(delta / 1000000000f);
                Display.update();
                if (Display.isCloseRequested() && onClose()) closed = true;
            }
            Display.destroy();
        }
    }
}
