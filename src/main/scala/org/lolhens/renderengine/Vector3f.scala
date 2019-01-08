package org.lolhens.renderengine

final case class Vector3f private(override val x: Float,
                                  override val y: Float,
                                  override val z: Float) extends Vector3[Float](x, y, z) {
  override type Self = Vector3f

  override def Vector3(x: Float, y: Float, z: Float): Vector3f =
    if (x == this.x && y == this.y && z == this.z) this
    else Vector3f(x, y, z)

  override def isZero: Boolean = x == 0 && y == 0 && z == 0
  override def isOne: Boolean = x == 1 && y == 1 && z == 1

  override def unary_- : Vector3f = Vector3f(-x, -y, -z)

  override def +(x: Float, y: Float, z: Float): Vector3f = if (x == 0 && y == 0 && z == 0) this else Vector3f(this.x + x, this.y + y, this.z + z)
  override def -(x: Float, y: Float, z: Float): Vector3f = if (x == 0 && y == 0 && z == 0) this else Vector3f(this.x - x, this.y - y, this.z - z)
  override def *(x: Float, y: Float, z: Float): Vector3f = if (x == 1 && y == 1 && z == 1) this else Vector3f(this.x * x, this.y * y, this.z * z)
  override def /(x: Float, y: Float, z: Float): Vector3f = if (x == 1 && y == 1 && z == 1) this else Vector3f(this.x / x, this.y / y, this.z / z)

  def max(vec: Vector3f): Vector3f = this.Vector3(
    if (vec.x > x) vec.x else x,
    if (vec.y > y) vec.y else y,
    if (vec.z > z) vec.z else z
  )

  def min(vec: Vector3f): Vector3f = this.Vector3(
    if (vec.x < x) vec.x else x,
    if (vec.y < y) vec.y else y,
    if (vec.z < z) vec.z else z
  )
}

object Vector3i {
  val Zero = new Vector3f(0, 0, 0)
  val One = new Vector3f(1, 1, 1)

  val X = new Vector3f(1, 0, 0)
  val Y = new Vector3f(0, 1, 0)
  val Z = new Vector3f(0, 0, 1)

  def apply(x: Float, y: Float, z: Float): Vector3f =
    if (x == 0 && y == 0 && z == 0) Zero
    else if (x == 1 && y == 1 && z == 1) One
    else new Vector3f(x, y, z)
}
