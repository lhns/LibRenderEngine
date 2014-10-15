package org.lolhens.renderengine.buffer

import java.nio.ByteBuffer

import scala.collection.mutable

/**
 * Created by LolHens on 12.10.2014.
 */
class RenderBufferManager(createBuffer: () => ByteBuffer, updateBuffer: (ManagedBuffer) => Unit, drawBuffer: (ManagedBuffer) => Unit) {
  val buffers = mutable.MutableList[ManagedBuffer]()

  private def getManagedBuffer(byteBuffer: ByteBuffer) = new ManagedBuffer(byteBuffer)

  def +=(kv: (Any, Array[Byte])): Boolean = {
    var possible = false
    for (buffer <- buffers) {
      if (buffer += kv) return true
      if (kv._2.length < buffer.bufferRegion.length) possible = true
    }
    if (!possible) return false
    val newBuffer = getManagedBuffer(createBuffer())
    buffers += newBuffer
    newBuffer += kv
  }

  def -=(key: Any): Boolean = {
    for (buffer <- buffers) if (buffer -= key) return true
    false
  }

  def foreach(func: ManagedBuffer => Unit) = buffers.foreach(func)
}
