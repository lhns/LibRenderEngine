package org.lolhens.renderengine.model

import java.nio.{ByteBuffer, ByteOrder}

import org.lolhens.renderengine.vector.Vector3f
import scodec.bits.ByteVector

/**
  * Created by LolHens on 05.10.2014.
  */
class Triangle(val a: Vector3f,
               val b: Vector3f,
               val c: Vector3f) extends Model {
  val toByteVector: ByteVector = {
    val buffer = ByteBuffer.allocate(4 * 3 * 3).order(ByteOrder.BIG_ENDIAN)
    buffer.putFloat(a.x)
    buffer.putFloat(a.y)
    buffer.putFloat(a.z)
    buffer.putFloat(b.x)
    buffer.putFloat(b.y)
    buffer.putFloat(b.z)
    buffer.putFloat(c.x)
    buffer.putFloat(c.y)
    buffer.putFloat(c.z)
    buffer.flip()
    ByteVector.view(buffer)
  }
}
