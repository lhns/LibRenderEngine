package org.lolhens.renderengine.vector

abstract class Vector3[@specialized(Int, Long, Float, Double) T] protected(val x: T,
                                                                           val y: T,
                                                                           val z: T) {
  type Self <: Vector3[T]

  def Vector3(x: T, y: T, z: T): Self

  def withX(x: T): Self = Vector3(x, y, z)
  def withY(y: T): Self = Vector3(x, y, z)
  def withZ(z: T): Self = Vector3(x, y, z)

  def mapX(f: T => T): Self = withX(f(x))
  def mapY(f: T => T): Self = withY(f(y))
  def mapZ(f: T => T): Self = withZ(f(z))

  def isZero: Boolean
  def isOne: Boolean

  def unary_- : Self

  def +(x: T, y: T, z: T): Self
  def -(x: T, y: T, z: T): Self
  def *(x: T, y: T, z: T): Self
  def /(x: T, y: T, z: T): Self

  def +(vec: Self): Self = if (isZero) vec else this + (vec.x, vec.y, vec.z)
  def -(vec: Self): Self = this - (vec.x, vec.y, vec.z)
  def *(vec: Self): Self = if (isOne) vec else this * (vec.x, vec.y, vec.z)
  def /(vec: Self): Self = this / (vec.x, vec.y, vec.z)

  def +(value: T): Self = this + (value, value, value)
  def -(value: T): Self = this - (value, value, value)
  def *(value: T): Self = this * (value, value, value)
  def /(value: T): Self = this / (value, value, value)
}
