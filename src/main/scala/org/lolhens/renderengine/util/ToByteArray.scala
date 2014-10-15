package org.lolhens.renderengine.util

import java.nio.{ByteBuffer, ByteOrder}


/**
 * Created by LolHens on 16.10.2014.
 */
object ToByteArray {
  private val tmpBuffer = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder())

  def apply[T <: AnyVal](values: Array[T]): Array[Byte] = {
    if (values == null || values.length == 0) return new Array[Byte](0)

    val valueSize = values(0) match {
      case _: Byte => return values.asInstanceOf[Array[Byte]]
      case _: Float => 4
    }
    val byteArray = new Array[Byte](values.length * valueSize)
    for (i <- 0 until values.length) {
      values(i) match {
        case value: Float => tmpBuffer.putFloat(0, value)
      }
      tmpBuffer.rewind
      tmpBuffer.get(byteArray, i * valueSize, valueSize)
    }
    byteArray
  }
}
