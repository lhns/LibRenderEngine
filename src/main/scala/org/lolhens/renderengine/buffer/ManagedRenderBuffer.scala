package org.lolhens.renderengine.buffer

import java.nio.{ByteBuffer, ByteOrder}
import javax.media.opengl.{GL, GL2}

/**
 * Created by LolHens on 15.10.2014.
 */
class ManagedRenderBuffer(gl: GL2, byteBuffer: ByteBuffer) extends ManagedBuffer(byteBuffer) {
  def this(gl: GL2, size: Int) = this(gl, ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()))

  val vbo = new VBO(gl, byteBuffer.capacity(), (gl) => {
    val stride = 4 * 3
    gl.glVertexPointer(3, GL.GL_FLOAT, stride, 0)
    stride
  })

  def render = {
    vbo.bind
    vbo.put(this)
    vbo.render
    vbo.unbind
  }
}
