package org.lolhens.renderengine.scene

import javax.media.opengl.fixedfunc.{GLLightingFunc, GLMatrixFunc}
import javax.media.opengl.glu.GLU
import javax.media.opengl.{GL, GL2, GL2ES1, GLAutoDrawable}

/**
 * Created by LolHens on 16.10.2014.
 */
class DefaultScene(drawable: GLAutoDrawable) extends Scene(drawable) {
  override def setupGL(gl: GL2, glu: GLU): Unit = {
    gl.glEnable(GL.GL_DEPTH_TEST)
    gl.glDepthFunc(GL.GL_LEQUAL)
    gl.glShadeModel(GLLightingFunc.GL_SMOOTH)
    gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST)
    gl.glClearColor(1f, 0f, 0f, 1f)
  }

  override def setPointers(gl: GL2): Int = {
    val stride = 4 * 3
    gl.glVertexPointer(3, GL.GL_FLOAT, stride, 0)
    stride
  }

  override def resize(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, _height: Int, glu: GLU): Unit = {
    val gl = drawable.getGL.getGL2

    var height = _height
    if (height <= 0) height = 1

    val h = width.asInstanceOf[Float] / height.asInstanceOf[Float]

    gl.glViewport(0, 0, width, height)
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
    gl.glLoadIdentity()
    glu.gluPerspective(45.0f, h, 1.0, 20.0)
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
    gl.glLoadIdentity()
  }
}
