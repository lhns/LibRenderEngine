package org.lolhens.renderengine.vector

final case class Vector3d private(override val x: Double,
                                  override val y: Double,
                                  override val z: Double) extends Vector3[Double](x, y, z) {
  override type Self = Vector3d

  override def Vector3(x: Double, y: Double, z: Double): Vector3d =
    if (x == this.x && y == this.y && z == this.z) this
    else Vector3d(x, y, z)

  override def isZero: Boolean = x == 0 && y == 0 && z == 0
  override def isOne: Boolean = x == 1 && y == 1 && z == 1

  override def unary_- : Vector3d = Vector3d(-x, -y, -z)

  override def +(x: Double, y: Double, z: Double): Vector3d = if (x == 0 && y == 0 && z == 0) this else Vector3d(this.x + x, this.y + y, this.z + z)
  override def -(x: Double, y: Double, z: Double): Vector3d = if (x == 0 && y == 0 && z == 0) this else Vector3d(this.x - x, this.y - y, this.z - z)
  override def *(x: Double, y: Double, z: Double): Vector3d = if (x == 1 && y == 1 && z == 1) this else Vector3d(this.x * x, this.y * y, this.z * z)
  override def /(x: Double, y: Double, z: Double): Vector3d = if (x == 1 && y == 1 && z == 1) this else Vector3d(this.x / x, this.y / y, this.z / z)

  override def `length²`: Double = x * x + y * y + z * z
  override def length: Double = Math.sqrt(`length²`)
}

object Vector3d {
  val Zero = new Vector3d(0, 0, 0)
  val One = new Vector3d(1, 1, 1)

  val X = new Vector3d(1, 0, 0)
  val Y = new Vector3d(0, 1, 0)
  val Z = new Vector3d(0, 0, 1)

  def apply(x: Double, y: Double, z: Double): Vector3d =
    if (x == 0 && y == 0 && z == 0) Zero
    else if (x == 1 && y == 1 && z == 1) One
    else new Vector3d(x, y, z)
}
