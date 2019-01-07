package org.lolhens.renderengine.texture

import java.nio.Buffer

import com.jogamp.opengl.{GL, GL2, GL2ES3, GL2GL3}

/**
  * Created by LolHens on 21.10.2014.
  */
class Texture(gl: GL2) {
  private var timesBound = 0

  val textureId: Int = gen
  allocate(1, 1, GL2GL3.GL_RGB12, GL.GL_UNSIGNED_BYTE, null)

  private def gen: Int = {
    //val pngReaderByte = new PngReaderByte()
    val textureIds = new Array[Int](1)
    gl.glGenTextures(1, textureIds, 0)
    textureIds(0)
  }

  def allocate(width: Int, height: Int, format: Int, tpe: Int, buffer: Buffer): Unit = {
    bind()
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, format, tpe, buffer)
    unbind()
  }


  def put(xOff: Int, yOff: Int, width: Int, height: Int, format: Int, tpe: Int, buffer: Buffer): Unit = {
    bind()
    gl.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, xOff, yOff, width, height, format, tpe, buffer)
    unbind()
  }

  def bind(): Unit = {
    timesBound += 1
    if (timesBound == 1) gl.glBindTexture(GL.GL_TEXTURE_2D, textureId)
  }

  def unbind(): Unit = {
    timesBound -= 1
    if (timesBound == 0) gl.glBindTexture(GL.GL_TEXTURE_2D, 0)
  }
}

object Texture {
  private var enabled = false

  def setup(gl: GL2): Unit = {
    if (!enabled) {
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2ES3.GL_TEXTURE_MAX_LEVEL, 7)
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST_MIPMAP_LINEAR)
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR)
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE)
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE)
      enabled = true
    }
  }
}
