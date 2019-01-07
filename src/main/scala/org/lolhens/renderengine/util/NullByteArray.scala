package org.lolhens.renderengine.util

/**
  * Created by LolHens on 15.10.2014.
  */
object NullByteArray {
  private var nullByteArray = new Array[Byte](0)

  def apply(length: Int): Array[Byte] = {
    if (length > nullByteArray.length) nullByteArray = new Array[Byte](length)
    nullByteArray
  }
}
