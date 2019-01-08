package org.lolhens.renderengine.vector

final case class Vector2i private(override val x: Int,
                                  override val y: Int) extends Vector2[Int](x, y) {
  override type Self = Vector2i

  override def Vector2(x: Int, y: Int): Vector2i =
    if (x == this.x && y == this.y) this
    else Vector2i(x, y)

  override def isZero: Boolean = x == 0 && y == 0
  override def isOne: Boolean = x == 1 && y == 1

  override def unary_- : Vector2i = Vector2i(-x, -y)

  override def +(x: Int, y: Int): Vector2i = if (x == 0 && y == 0) this else Vector2i(this.x + x, this.y + y)
  override def -(x: Int, y: Int): Vector2i = if (x == 0 && y == 0) this else Vector2i(this.x - x, this.y - y)
  override def *(x: Int, y: Int): Vector2i = if (x == 1 && y == 1) this else Vector2i(this.x * x, this.y * y)
  override def /(x: Int, y: Int): Vector2i = if (x == 1 && y == 1) this else Vector2i(this.x / x, this.y / y)
}

object Vector2i {
  val Zero = new Vector2i(0, 0)
  val One = new Vector2i(1, 1)

  val X = new Vector2i(1, 0)
  val Y = new Vector2i(0, 1)

  def apply(x: Int, y: Int): Vector2i =
    if (x == 0 && y == 0) Zero
    else if (x == 1 && y == 1) One
    else new Vector2i(x, y)
}
