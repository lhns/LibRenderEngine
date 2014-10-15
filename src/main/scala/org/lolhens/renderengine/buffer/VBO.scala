package org.lolhens.renderengine.buffer

import java.nio.Buffer
import javax.media.opengl.fixedfunc.GLPointerFunc
import javax.media.opengl.{GL, GL2}

/**
 * Created by LolHens on 15.10.2014.
 */
class VBO(gl: GL2, size: Int, setPointers: () => Int) {
  private var timesBound = 0

  val vboId = gen
  allocate(size)

  private def gen: Int = {
    val vboIds = new Array[Int](1)
    gl.glGenBuffers(1, vboIds, 0)
    vboIds(0)
  }

  private def allocate(size: Int) = {
    bind
    gl.glBufferData(GL.GL_ARRAY_BUFFER, size, null, GL.GL_DYNAMIC_DRAW)
    unbind
  }

  def put(managedBuffer: ManagedBuffer): Unit = {
    for (dirty <- managedBuffer) {
      managedBuffer.buffer.position(dirty.offset)
      put(managedBuffer.buffer, dirty.offset, dirty.length)
    }
  }

  def put(buffer: Buffer, offset: Int, length: Int): Unit = {
    bind
    gl.glBufferSubData(GL.GL_ARRAY_BUFFER, offset, length, buffer)
    unbind
  }

  def bind: Unit = {
    timesBound += 1
    if (timesBound == 1) gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboId)
  }

  def unbind: Unit = {
    timesBound -= 1
    if (timesBound == 0) gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)
  }

  def render: Unit = render(0, size)

  def render(offset: Int, length: Int): Unit = {
    bind
    val stride = setPointers()
    gl.glDrawArrays(GL.GL_TRIANGLES, offset, length / stride)
    unbind
  }
}

object VBO {
  def enable(gl: GL2) = {
    gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY)
  }
}
