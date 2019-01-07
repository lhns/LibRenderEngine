package org.lolhens.renderengine.buffer

import java.nio.{ByteBuffer, ByteOrder}
import java.util

import org.lolhens.renderengine.buffer.ManagedBuffer._
import org.lolhens.renderengine.util.NullByteArray


/**
  * Created by LolHens on 05.10.2014.
  */
class ManagedBuffer(val buffer: ByteBuffer) {
  def this(size: Int) = this(ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()))

  private val mapped = new util.HashMap[Any, Region]()
  private val empty = new RegionList()
  private val dirty = new RegionList()
  val bufferRegion = Region(this, 0, buffer.capacity())

  empty += bufferRegion
  dirty += bufferRegion

  def +=(kv: (Any, Array[Byte])): Boolean = {
    val key = kv._1
    val bytes = kv._2

    if (mapped.containsKey(key)) if (ignoreDuplicates) return true else this -= key

    val region = empty.getSuitableRegion(bytes.length)
    if (region == null) return false

    val bytesRegion = Region(this, region.offset, bytes.length)

    buffer.position(bytesRegion.offset)
    buffer.put(bytes, 0, bytesRegion.length)

    mapped.put(key, bytesRegion)
    empty -= bytesRegion
    dirty += bytesRegion

    true
  }

  def -=(key: Any): Boolean = {
    val region = mapped.get(key)
    if (region == null) return false

    buffer.position(region.offset)
    buffer.put(NullByteArray(region.length), 0, region.length)

    mapped.remove(key)
    empty += region
    dirty += region

    true
  }

  def isEmpty: Boolean = empty.getFirstRegion == bufferRegion

  def foreach(func: Region => Unit): Unit = {
    dirty.foreach(func)
    dirty.remove()
  }
}

object ManagedBuffer {
  val ignoreDuplicates = true

  case class Region(buffer: ManagedBuffer, offset: Int, length: Int) {
    def end: Int = offset + length

    def touches(other: Region): Boolean = other != null && ((other.offset <= end && other.end >= offset) || (other.end >= offset && other.offset <= end))

    def intersects(other: Region): Boolean = other != null && ((other.offset < end && other.end > offset) || (other.end > offset && other.offset < end))

    def contains(other: Region): Boolean = other != null && other.offset >= offset && other.end <= end

    def +=(other: Region): Region = {
      if (!touches(other)) return null
      val newOffset = if (other.offset < offset) other.offset else offset
      val newLength = (if (other.end > end) other.end else end) - newOffset
      if (newOffset == offset && newLength == length) return this
      Region(buffer, newOffset, newLength)
    }

    def -=(other: Region): Array[Region] = {
      if (!intersects(other)) return null
      if (other contains this) return Array()

      var pre: Region = null
      if (other.offset > offset) pre = Region(buffer, offset, other.offset - offset)
      var post: Region = null
      if (other.end < end) post = Region(buffer, other.end, end - other.end)

      if (pre != null && post != null)
        Array(pre, post)
      else
        Array(if (pre != null) pre else post)
    }

    override def toString = s"$offset-$end"
  }

  class RegionList {
    private val list = new util.LinkedList[Region]()

    def +=(region: Region): Unit = {
      if (list.size == 0) list.add(region)
      else {
        var newRegion = region
        val iterator = list.listIterator()
        var last: Region = null
        while (iterator.hasNext) {
          val current = iterator.next
          if (current.offset > region.offset) {
            iterator.previous
            var merged = newRegion += current
            if (merged != null) {
              newRegion = merged
              iterator.remove()
            }
            merged = newRegion += last
            if (merged != null) {
              newRegion = merged
              iterator.previous
              iterator.remove()
            }
            iterator.add(newRegion)
            return
          }
          last = current
        }
        val merged = region += list.getLast
        if (merged != null) {
          newRegion = merged
          list.removeLast()
        }
        list.add(newRegion)
      }
    }

    def -=(region: Region): Unit = {
      val iterator = list.listIterator()
      while (iterator.hasNext) {
        val current = iterator.next
        if (current intersects region) {
          iterator.previous
          iterator.remove()
          for (region <- current -= region) iterator.add(region)
        }
      }
    }

    def getSuitableRegion(length: Int): Region = {
      val iterator = list.iterator
      var suitable: Region = null
      while (iterator.hasNext) {
        val current = iterator.next
        if (current.length == length)
          return current
        else if (current.length > length && (suitable == null || current.length < suitable.length))
          suitable = current
      }
      suitable
    }

    private var foreachIterator: util.Iterator[Region] = null

    def foreach(func: Region => Unit): Unit = {
      val iterator = list.iterator
      foreachIterator = iterator
      while (iterator.hasNext) func(iterator.next)
      foreachIterator = null
    }

    private[ManagedBuffer] def remove(): Unit = if (foreachIterator != null) foreachIterator.remove()

    private[ManagedBuffer] def getFirstRegion = if (list.size > 0) list.getFirst else null

    override def toString: String = {
      var string = ""
      val iterator = list.iterator()
      while (iterator.hasNext) {
        val region = iterator.next
        string = if (string == "") region.toString else s"$string, $region"
      }
      string
    }
  }

}