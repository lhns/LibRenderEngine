package org.lolhens.renderengine.buffer

import java.nio.ByteBuffer
import javax.media.opengl.{GL, GL2}

/**
 * Created by LolHens on 15.10.2014.
 */
class ManagedRenderBuffer(gl: GL2, byteBuffer: ByteBuffer) extends ManagedBuffer(byteBuffer) {
  val vbo = new VBO(gl, byteBuffer.capacity(), () => {
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
