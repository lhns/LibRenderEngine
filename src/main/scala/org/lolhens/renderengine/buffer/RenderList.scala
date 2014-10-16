package org.lolhens.renderengine.buffer

import java.util
import javax.media.opengl.GL2

import org.lolhens.renderengine.model.Model

import scala.collection.mutable.ArrayBuffer

/**
 * Created by LolHens on 12.10.2014.
 */
class RenderList(gl: GL2, setPointers: (GL2) => Int) {
  var bufferSize = 4 * 3 * 3 * 1000
  private val buffers = ArrayBuffer[ManagedVBO]()
  private val keys = new util.HashMap[Any, ManagedVBO]()

  def +=(kv: (Any, Array[Byte])): Boolean = {
    for (buffer <- buffers) {
      if (addToBuffer(kv, buffer)) return true
      else if (kv._2.length > buffer.bufferRegion.length) return false
    }
    val newBuffer = new ManagedVBO(gl, setPointers, bufferSize)
    buffers += newBuffer
    addToBuffer(kv, newBuffer)
  }

  private def addToBuffer(kv: (Any, Array[Byte]), buffer: ManagedVBO): Boolean =
    if (buffer += kv) {
      keys.put(kv._1, buffer)
      true
    } else false

  def -=(key: Any): Boolean = {
    val buffer = keys.get(key)
    if (buffer != null && (buffer -= key)) {
      keys.remove(key)
      if (buffer.isEmpty) {
        buffers -= buffer
        buffer.close
      }
      true
    } else false
  }

  def render = {
    buffers.foreach(_.render)
  }

  def close = buffers.foreach(_.close)

  def foreach(func: ManagedBuffer => Unit) = buffers.foreach(func)

  def put(model: Model): Unit = {

  }
}
