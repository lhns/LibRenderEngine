package org.lolhens.renderengine.vector

final case class Vector3l private(override val x: Long,
                                  override val y: Long,
                                  override val z: Long) extends Vector3[Long](x, y, z) {
  override type Self = Vector3l

  override def Vector3(x: Long, y: Long, z: Long): Vector3l =
    if (x == this.x && y == this.y && z == this.z) this
    else Vector3l(x, y, z)

  override def isZero: Boolean = x == 0 && y == 0 && z == 0
  override def isOne: Boolean = x == 1 && y == 1 && z == 1

  override def unary_- : Vector3l = Vector3l(-x, -y, -z)

  override def +(x: Long, y: Long, z: Long): Vector3l = if (x == 0 && y == 0 && z == 0) this else Vector3l(this.x + x, this.y + y, this.z + z)
  override def -(x: Long, y: Long, z: Long): Vector3l = if (x == 0 && y == 0 && z == 0) this else Vector3l(this.x - x, this.y - y, this.z - z)
  override def *(x: Long, y: Long, z: Long): Vector3l = if (x == 1 && y == 1 && z == 1) this else Vector3l(this.x * x, this.y * y, this.z * z)
  override def /(x: Long, y: Long, z: Long): Vector3l = if (x == 1 && y == 1 && z == 1) this else Vector3l(this.x / x, this.y / y, this.z / z)

  override def `length²`: Long = x * x + y * y + z * z
  override def length: Long = Math.sqrt(`length²`).toLong
}

object Vector3l {
  val Zero = new Vector3l(0, 0, 0)
  val One = new Vector3l(1, 1, 1)

  val X = new Vector3l(1, 0, 0)
  val Y = new Vector3l(0, 1, 0)
  val Z = new Vector3l(0, 0, 1)

  def apply(x: Long, y: Long, z: Long): Vector3l =
    if (x == 0 && y == 0 && z == 0) Zero
    else if (x == 1 && y == 1 && z == 1) One
    else new Vector3l(x, y, z)
}
