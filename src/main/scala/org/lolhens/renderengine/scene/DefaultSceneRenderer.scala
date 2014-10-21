package org.lolhens.renderengine.scene

import javax.media.opengl._
import javax.media.opengl.fixedfunc.{GLLightingFunc, GLMatrixFunc}

import com.jogamp.opengl.util.texture.TextureIO
import org.lolhens.renderengine.util.ToByteArray

/**
 * Created by LolHens on 16.10.2014.
 */
class DefaultSceneRenderer(drawable: GLAutoDrawable) extends SceneRenderer(drawable) {
  gl.glEnable(GL.GL_DEPTH_TEST)
  gl.glDepthFunc(GL.GL_LEQUAL)
  gl.glEnable(GL.GL_TEXTURE_2D)
  //gl.glEnable(GL.GL_CULL_FACE);
  //gl.glCullFace(GL.GL_BACK);
  //gl.glFrontFace(GL.GL_CW);
  gl.glShadeModel(GLLightingFunc.GL_SMOOTH)
  gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST)
  gl.glClearColor(0, 0, 0, 1)


  val texture = TextureIO.newTexture(getClass.getClassLoader.getResourceAsStream("icons/pear16_32.png"), true, "png")
  texture.setTexParameteri(gl, GL2ES3.GL_TEXTURE_MAX_LEVEL, 7)
  texture.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST_MIPMAP_LINEAR)
  texture.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR)
  texture.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE)
  texture.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE)

  texture.updateImage(gl, TextureIO.newTextureData(gl.getGLProfile, getClass.getClassLoader.getResourceAsStream("icons/pear16_32.png"), true, "png"))

  val off = 0
  val data = Array[Float](
    0 + off, 1 + off, 0 + off, 0, 0, 1, /**/ 1, 1, 1, 1, /**/ 0, 1,
    1 + off, 0 + off, 0 + off, 0, 0, 1, /**/ 1, 1, 1, 1, /**/ 1, 0,
    0 + off, 0 + off, 0 + off, 0, 0, 1, /**/ 1, 1, 1, 1, /**/ 0, 0,

    0 + off, 1 + off, 0 + off, 0, 0, 1, /**/ 1, 1, 1, 1, /**/ 0, 1,
    1 + off, 1 + off, 0 + off, 0, 0, 1, /**/ 1, 1, 1, 1, /**/ 1, 1,
    1 + off, 0 + off, 0 + off, 0, 0, 1, /**/ 1, 1, 1, 1, /**/ 1, 0
  )
  renderList += data -> ToByteArray(data)

  var rot = 0f

  override def render = {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
    gl.glLoadIdentity
    gl.glTranslatef(-0.5f, -0.5f, -3f)
    rot += 0.6f
    gl.glRotatef(rot, 1, 1, 0)
    renderList.render
  }

  override def setVBOPointers(gl: GL2): Int = {
    val stride = 4 * (3 * 2 + 4 + 2)
    var offset = 0
    gl.glVertexPointer(3, GL.GL_FLOAT, stride, offset)
    offset += 4 * 3
    gl.glNormalPointer(GL.GL_FLOAT, stride, offset)
    offset += 4 * 3
    gl.glColorPointer(4, GL.GL_FLOAT, stride, offset)
    offset += 4 * 4
    gl.glTexCoordPointer(2, GL.GL_FLOAT, stride, offset)
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
