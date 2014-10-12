package org.lolhens.renderengine.buffer

import java.nio.ByteBuffer
import java.util.LinkedList

import org.lolhens.renderengine.buffer.ManagedBuffer._

import scala.collection.JavaConversions._
import scala.collection.mutable.Map
import scala.util.control.Breaks

/**
 * Created by LolHens on 05.10.2014.
 */
class ManagedBuffer(buffer: ByteBuffer) {
  private val mapped = Map[Any, Region]()
  private val empty = new LinkedList[Region]()

  empty.add(new Region(0, buffer.capacity()))

  private def availableRegion(length: Int): Region = {
    if (empty.size() == 0 || length > empty.get(empty.length - 1).length) return null
    var startIndex = 0
    var endIndex = empty.length - 1
    var midIndex = 0
    while (endIndex != startIndex) {
      midIndex = (startIndex + endIndex) / 2
      if (length > empty.get(midIndex).length) startIndex = midIndex + 1
      else endIndex = midIndex
    }
    empty.get(startIndex)
  }

  private def addEmptyRegion(region: Region): Unit = {
    val length = region.length
    if (length < empty.get(0).length) {
      empty.add(0, region)
    } else if (length > empty.get(empty.length - 1).length) {
      empty.add(region)
    } else {
      var startIndex = 0
      var endIndex = empty.length - 1
      var midIndex = 0
      while (endIndex != startIndex) {
        midIndex = (startIndex + endIndex) / 2
        if (length > empty.get(midIndex).length) startIndex = midIndex + 1
        else endIndex = midIndex
      }
      empty.add(startIndex, region)
    }
  }

  private def mergeEmptyRegion(region: Region) = {
    var pre: Region = null
    var post: Region = null

    val break = new Breaks
    break.breakable {
      for (emptyRegion <- empty) {
        if (emptyRegion.offset + emptyRegion.length == region.offset) pre = emptyRegion
        else if (emptyRegion.offset == region.offset + region.length) post = emptyRegion
        if (pre != null && post != null) break.break()
      }
    }

    val offset = if (pre != null) pre.offset else region.offset
    val length = (if (post != null) post.offset + post.length else region.offset + region.length) - offset

    if (pre != null) empty.remove(pre)
    if (post != null) empty.remove(post)
    empty.remove(region)

    addEmptyRegion(new Region(offset, length))
  }

  def add(key: Any, bytes: Array[Byte]): Boolean = {
    val region = availableRegion(bytes.length)
    if (region == null) return false

    val newRegion = new Region(region.offset, bytes.length)

    buffer.put(bytes, newRegion.offset, newRegion.length)
    mapped += key -> newRegion

    empty.remove(region)
    if (region.length > newRegion.length) addEmptyRegion(new Region(region.offset + newRegion.length, region.length - newRegion.length))

    true
  }

  def remove(key: Any): Boolean = {
    val region = mapped(key)
    if (region == null) return false

    mapped.remove(key)
    for (i <- region.offset until region.offset + region.length) buffer.put(i, 0)

    addEmptyRegion(region)
    mergeEmptyRegion(region)

    true
  }

  // def defrag() = {}
}

object ManagedBuffer {

  case class Region(offset: Int, length: Int) {}

}