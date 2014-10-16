package org.lolhens.renderengine.buffer

import java.nio.{ByteBuffer, ByteOrder}
import javax.media.opengl.GL2

/**
 * Created by LolHens on 15.10.2014.
 */
class ManagedVBO(gl: GL2, setPointers: (GL2) => Int, byteBuffer: ByteBuffer) extends ManagedBuffer(byteBuffer) {
  def this(gl: GL2, setPointers: (GL2) => Int, size: Int) = this(gl, setPointers, ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()))

  val vbo = new VBO(gl, byteBuffer.capacity(), setPointers)

  def render = {
    VBO.enable(gl)
    vbo.bind
    vbo.put(this)
    vbo.render
    vbo.unbind
  }

  def close = vbo.close
}
