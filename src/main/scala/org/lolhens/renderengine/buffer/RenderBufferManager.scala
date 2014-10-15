package org.lolhens.renderengine.buffer

import javax.media.opengl.GL2

import scala.collection.mutable

/**
 * Created by LolHens on 12.10.2014.
 */
class RenderBufferManager(gl: GL2) {
  val buffers = mutable.MutableList[ManagedRenderBuffer]()
  val bufferSize = 4 * 3 * 3 * 1000

  def +=(kv: (Any, Array[Byte])): Boolean = {
    for (buffer <- buffers) {
      if (buffer += kv) return true
      else if (kv._2.length > buffer.bufferRegion.length) return false
    }
    val newBuffer = new ManagedRenderBuffer(gl, bufferSize)
    buffers += newBuffer
    newBuffer += kv
  }

  def -=(key: Any): Boolean = {
    for (buffer <- buffers) if (buffer -= key) return true
    false
  }

  def render = buffers.foreach(_.render)

  def foreach(func: ManagedBuffer => Unit) = buffers.foreach(func)
}
