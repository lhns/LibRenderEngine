package org.lolhens.renderengine.vector

final case class Vector2d private(override val x: Double,
                                  override val y: Double) extends Vector2[Double](x, y) {
  override type Self = Vector2d

  override def Vector2(x: Double, y: Double): Vector2d =
    if (x == this.x && y == this.y) this
    else Vector2d(x, y)

  override def isZero: Boolean = x == 0 && y == 0
  override def isOne: Boolean = x == 1 && y == 1

  override def unary_- : Vector2d = Vector2d(-x, -y)

  override def +(x: Double, y: Double): Vector2d = if (x == 0 && y == 0) this else Vector2d(this.x + x, this.y + y)
  override def -(x: Double, y: Double): Vector2d = if (x == 0 && y == 0) this else Vector2d(this.x - x, this.y - y)
  override def *(x: Double, y: Double): Vector2d = if (x == 1 && y == 1) this else Vector2d(this.x * x, this.y * y)
  override def /(x: Double, y: Double): Vector2d = if (x == 1 && y == 1) this else Vector2d(this.x / x, this.y / y)
}

object Vector2d {
  val Zero = new Vector2d(0, 0)
  val One = new Vector2d(1, 1)

  val X = new Vector2d(1, 0)
  val Y = new Vector2d(0, 1)

  def apply(x: Double, y: Double): Vector2d =
    if (x == 0 && y == 0) Zero
    else if (x == 1 && y == 1) One
    else new Vector2d(x, y)
}
