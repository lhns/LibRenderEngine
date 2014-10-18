package org.lolhens.renderengine.scene

import javax.media.opengl.fixedfunc.{GLLightingFunc, GLMatrixFunc}
import javax.media.opengl.{GL, GL2, GL2ES1, GLAutoDrawable}

/**
 * Created by LolHens on 16.10.2014.
 */
class DefaultSceneRenderer(drawable: GLAutoDrawable) extends SceneRenderer(drawable) {
  gl.glEnable(GL.GL_DEPTH_TEST)
  gl.glDepthFunc(GL.GL_LEQUAL)
  gl.glShadeModel(GLLightingFunc.GL_SMOOTH)
  gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST)
  gl.glClearColor(1f, 0f, 0f, 1f)

  override def render = {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
    gl.glLoadIdentity
    gl.glTranslatef(0, 0.0f, -3f)
    renderList.render
  }

  override def setVBOPointers(gl: GL2): Int = {
    val stride = 4 * 3
    gl.glVertexPointer(3, GL.GL_FLOAT, stride, 0)
    stride
  }

  override def resizeDrawable(x: Int, y: Int, width: Int, _height: Int): Unit = {
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
