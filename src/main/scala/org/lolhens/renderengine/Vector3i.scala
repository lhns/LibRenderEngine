package org.lolhens.renderengine

final case class Vector3i private(override val x: Int,
                                  override val y: Int,
                                  override val z: Int) extends Vector3[Int](x, y, z) {
  override type Self = Vector3i

  override def Vector3(x: Int, y: Int, z: Int): Vector3i =
    if (x == this.x && y == this.y && z == this.z) this
    else Vector3i(x, y, z)

  override def isZero: Boolean = x == 0 && y == 0 && z == 0
  override def isOne: Boolean = x == 1 && y == 1 && z == 1

  override def unary_- : Vector3i = Vector3i(-x, -y, -z)

  override def +(x: Int, y: Int, z: Int): Vector3i = if (x == 0 && y == 0 && z == 0) this else Vector3i(this.x + x, this.y + y, this.z + z)
  override def -(x: Int, y: Int, z: Int): Vector3i = if (x == 0 && y == 0 && z == 0) this else Vector3i(this.x - x, this.y - y, this.z - z)
  override def *(x: Int, y: Int, z: Int): Vector3i = if (x == 1 && y == 1 && z == 1) this else Vector3i(this.x * x, this.y * y, this.z * z)
  override def /(x: Int, y: Int, z: Int): Vector3i = if (x == 1 && y == 1 && z == 1) this else Vector3i(this.x / x, this.y / y, this.z / z)
}

object Vector3i {
  val Zero = new Vector3i(0, 0, 0)
  val One = new Vector3i(1, 1, 1)

  val X = new Vector3i(1, 0, 0)
  val Y = new Vector3i(0, 1, 0)
  val Z = new Vector3i(0, 0, 1)

  def apply(x: Int, y: Int, z: Int): Vector3i =
    if (x == 0 && y == 0 && z == 0) Zero
    else if (x == 1 && y == 1 && z == 1) One
    else new Vector3i(x, y, z)
}
