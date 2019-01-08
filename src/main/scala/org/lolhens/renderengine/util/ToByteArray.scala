package org.lolhens.renderengine.util

import java.nio.{ByteBuffer, ByteOrder}


/**
  * Created by LolHens on 16.10.2014.
  */
object ToByteArray {
  private val localBuffer: ThreadLocal[ByteBuffer] = ThreadLocal.withInitial { () =>
    ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder())
  }

  def apply[T <: AnyVal](values: Array[T]): Array[Byte] = {
    if (values == null || values.length == 0) new Array[Byte](0)
    else if (values(0).isInstanceOf[Byte]) values.asInstanceOf[Array[Byte]]
    else {
      val valueSize = values(0) match {
        case _: Float => 4
      }

      val byteArray = new Array[Byte](values.length * valueSize)
      val buffer = localBuffer.get()

      for (i <- values.indices) {
        values(i) match {
          case value: Float => buffer.putFloat(0, value)
        }
        buffer.rewind
        buffer.get(byteArray, i * valueSize, valueSize)
      }

      byteArray
    }
  }
}
