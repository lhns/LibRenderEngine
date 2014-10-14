package org.lolhens.renderengine.buffer

import java.nio.ByteBuffer

import scala.collection.mutable

/**
 * Created by LolHens on 12.10.2014.
 */
class BufferManager(createBuffer: () => ByteBuffer, updateBuffer: (ManagedBuffer) => Unit, drawBuffer: (ManagedBuffer) => Unit) {
  val buffers = mutable.MutableList[ManagedBuffer]()

  def +=(kv: (Any, Array[Byte])): Boolean = {
    var possible = false
    for (buffer <- buffers) {
      if (buffer += kv) return true
      if (kv._2.length < buffer.maxCapacity) possible = true
    }
    if (!possible) return false
    val newBuffer = new ManagedBuffer(createBuffer())
    buffers += newBuffer
    newBuffer += kv
  }

  def -=(key: Any): Boolean = {
    for (buffer <- buffers) if (buffer -= key) return true
    false
  }

  def update = for (buffer <- buffers) updateBuffer(buffer)

  def draw = for (buffer <- buffers) drawBuffer(buffer)
}
