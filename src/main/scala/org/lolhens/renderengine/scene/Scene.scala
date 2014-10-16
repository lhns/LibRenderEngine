package org.lolhens.renderengine.scene

import javax.media.opengl._
import javax.media.opengl.glu.GLU

import com.jogamp.opengl.util.FPSAnimator
import org.lolhens.renderengine.buffer.RenderList
import org.lolhens.renderengine.util.ToByteArray

/**
 * Created by LolHens on 16.10.2014.
 */
abstract class Scene(drawable: GLAutoDrawable) {
  drawable.addGLEventListener(SceneGLEventListener)
  val animator = new FPSAnimator(drawable, 60)
  animator.start

  object SceneGLEventListener extends GLEventListener {
    private var sceneRenderer: SceneRenderer = null

    override def init(drawable: GLAutoDrawable): Unit = sceneRenderer = new SceneRenderer(drawable)

    override def display(drawable: GLAutoDrawable): Unit = sceneRenderer.render

    override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = resize(drawable, x, y, width, height, sceneRenderer.glu)

    override def dispose(drawable: GLAutoDrawable): Unit = {
      sceneRenderer.renderList.close
    }
  }

  class SceneRenderer(drawable: GLAutoDrawable) {
    val gl = drawable.getGL.getGL2
    val glu = new GLU()
    val renderList = new RenderList(gl, setPointers)

    setupGL(gl, glu)

    val off = 0
    //(new Random().nextInt(1000) - 500).asInstanceOf[Float] / 100f
    val data = Array(0.0f + off, 1.0f + off, 0.0f + off, -1.0f + off, -1.0f + off, 0.0f + off, 1.0f + off, -1.0f + off, 0.0f + off)

    renderList += data -> ToByteArray(data)

    def render = {
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
      gl.glLoadIdentity
      gl.glTranslatef(0, 0.0f, -3f)
      renderList.render
      gl.glFlush
    }
  }

  def setupGL(gl: GL2, glu: GLU): Unit

  def setPointers(gl: GL2): Int

  def resize(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int, glu: GLU): Unit
}