package org.lolhens.renderengine.buffer

import java.nio.ByteBuffer
import java.util

import org.lolhens.renderengine.buffer.ManagedBuffer._

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * Created by LolHens on 05.10.2014.
 */
class ManagedBuffer(buffer: ByteBuffer) {
  private val mapped = mutable.Map[Any, Region]()
  private val empty = new RegionList()
  private val dirty = new RegionList()

  empty += new Region(0, buffer.capacity())

  def +=(key: Any, bytes: Array[Byte]): Boolean = {
    if (mapped contains key) this -= key

    val region = empty.getSuitableRegion(bytes.length)
    if (region == null) return false

    val bytesRegion = new Region(region.offset, bytes.length)

    buffer.put(bytes, bytesRegion.offset, bytesRegion.length)

    mapped += key -> bytesRegion
    empty -= bytesRegion
    dirty += bytesRegion

    true
  }

  def -=(key: Any): Boolean = {
    val region = mapped(key)
    if (region == null) return false

    for (i <- region.offset until region.end) buffer.put(i, 0)

    mapped.remove(key)
    empty += region
    dirty += region

    true
  }
}

object ManagedBuffer {

  case class Region(offset: Int, length: Int) {
    def end = offset + length

    def touches(other: Region): Boolean = other != null && ((other.offset <= end && other.end >= offset) || (other.end >= offset && other.offset <= end))

    def intersects(other: Region): Boolean = other != null && ((other.offset < end && other.end > offset) || (other.end > offset && other.offset < end))

    def contains(other: Region): Boolean = other != null && other.offset >= offset && other.end <= end

    def +=(other: Region): Region = {
      if (!touches(other)) return null
      val newOffset = if (other.offset < offset) other.offset else offset
      val newLength = (if (other.end > end) other.end else end) - newOffset
      if (newOffset == offset && newLength == length) return this
      new Region(newOffset, newLength)
    }

    def -=(other: Region): Array[Region] = {
      if (!intersects(other)) return null
      if (other contains this) return Array()

      var pre: Region = null
      if (other.offset > offset) pre = new Region(offset, other.offset - offset)
      var post: Region = null
      if (other.end < end) post = new Region(other.end, end - other.end)

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
              iterator.remove
            }
            merged = newRegion += last
            if (merged != null) {
              newRegion = merged
              iterator.previous
              iterator.remove
            }
            iterator.add(newRegion)
            return
          }
          last = current
        }
        val merged = region += list.getLast
        if (merged != null) {
          newRegion = merged
          list.removeLast
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
          iterator.remove
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

    override def toString = {
      var string = ""
      for (region <- list) string = if (string == "") region.toString else s"$string, $region"
      string
    }
  }

}