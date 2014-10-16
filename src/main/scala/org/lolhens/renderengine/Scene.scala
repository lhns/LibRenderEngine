package org.lolhens.renderengine

import javax.media.opengl._
import javax.media.opengl.fixedfunc.{GLLightingFunc, GLMatrixFunc}
import javax.media.opengl.glu.GLU

import com.jogamp.opengl.util.FPSAnimator
import org.lolhens.renderengine.buffer.RenderList
import org.lolhens.renderengine.util.ToByteArray

/**
 * Created by LolHens on 16.10.2014.
 */
class Scene(drawable: GLAutoDrawable) {
  drawable.addGLEventListener(SceneGLEventListener)
  val animator = new FPSAnimator(drawable, 60)
  animator.start

  object SceneGLEventListener extends GLEventListener {
    private var sceneRenderer: SceneRenderer = null

    override def init(drawable: GLAutoDrawable): Unit = sceneRenderer = new SceneRenderer(drawable)

    override def display(drawable: GLAutoDrawable): Unit = sceneRenderer.render

    override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, _height: Int): Unit = {
      val gl = drawable.getGL.getGL2

      var height = _height
      if (height <= 0) height = 1

      val h = width.asInstanceOf[Float] / height.asInstanceOf[Float]

      gl.glViewport(0, 0, width, height)
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
      gl.glLoadIdentity()
      sceneRenderer.glu.gluPerspective(45.0f, h, 1.0, 20.0)
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
      gl.glLoadIdentity()
    }

    override def dispose(drawable: GLAutoDrawable): Unit = {
      sceneRenderer.renderList.close
    }
  }

  class SceneRenderer(drawable: GLAutoDrawable) {
    val gl = drawable.getGL.getGL2
    val glu = new GLU()
    val renderList = new RenderList(gl, (gl) => {
      val stride = 4 * 3
      gl.glVertexPointer(3, GL.GL_FLOAT, stride, 0)
      stride
    })

    gl.glEnable(GL.GL_DEPTH_TEST)
    gl.glDepthFunc(GL.GL_LEQUAL)
    gl.glShadeModel(GLLightingFunc.GL_SMOOTH)
    gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST)
    gl.glClearColor(1f, 0f, 0f, 1f)

    val off = 0
    //(new Random().nextInt(1000) - 500).asInstanceOf[Float] / 100f
    val data = Array(0.0f + off, 1.0f + off, 0.0f + off, -1.0f + off, -1.0f + off, 0.0f + off, 1.0f + off, -1.0f + off, 0.0f + off)

    //mb += bufData -> bufData
    renderList += data -> ToByteArray(data)

    def render = {
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
      gl.glLoadIdentity
      gl.glTranslatef(0, 0.0f, -3f)
      renderList.render
      gl.glFlush
    }
  }

}