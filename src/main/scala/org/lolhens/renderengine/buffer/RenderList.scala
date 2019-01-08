package org.lolhens.renderengine.buffer

import java.util
import java.util.Random

import com.jogamp.opengl.GL2
import org.lolhens.renderengine.model.{Model, Triangle}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by LolHens on 12.10.2014.
  */
class RenderList(gl: GL2, setPointers: GL2 => Int) {
  var bufferSize: Int = 4 * 3 * 3 * 1000
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
        buffer.close()
      }
      true
    } else false
  }

  def render(): Unit = {
    buffers.foreach(_.render())
  }

  def close(): Unit = buffers.foreach(_.close())

  def foreach(func: ManagedBuffer => Unit): Unit = buffers.foreach(func)

  def put(model: Model): Unit = {
    if (!model.removedChildren.isEmpty) model.removedChildren.asScala.foreach(removeAll)
    model.removedChildren.clear()
    if (!model.addedChildren.isEmpty) model.addedChildren.asScala.foreach(addAll)
    model.addedChildren.clear()
    if (!model.dirtyChildren.isEmpty) model.dirtyChildren.asScala.foreach(put)
    model.dirtyChildren.clear()
  }

  private def addAll(model: Model): Unit = model match {
    case triangle: Triangle => this += new Random().nextFloat() -> triangle.toByteVector.toArray
    case model => model.foreach(addAll)
  }

  private def removeAll(model: Model): Unit = model match {
    case triangle: Triangle => this -= triangle
    case model => model.foreach(addAll)
  }
}
