package org.lolhens.renderengine

/**
  * Created by LolHens on 05.10.2014.
  */
case class Vector3f private(x: Float, y: Float, z: Float) {
  def +(vec: Vector3f): Vector3f = Vector3f(x + vec.x, y + vec.y, z + vec.z)

  def -(vec: Vector3f): Vector3f = Vector3f(x - vec.x, y - vec.y, z - vec.z)

  def *(vec: Vector3f): Vector3f = Vector3f(x * vec.x, y * vec.y, z * vec.z)

  def /(vec: Vector3f): Vector3f = Vector3f(x / vec.x, y / vec.y, z / vec.z)

  def max(vec: Vector3f): Vector3f = Vector3f(if (vec.x > x) vec.x else x, if (vec.y > y) vec.y else y, if (vec.z > z) vec.z else z)

  def min(vec: Vector3f): Vector3f = Vector3f(if (vec.x < x) vec.x else x, if (vec.y < y) vec.y else y, if (vec.z < z) vec.z else z)
}

object Vector3f {
  val Zero = new Vector3f(0, 0, 0)

  val One = new Vector3f(1, 1, 1)

  val X = new Vector3f(1, 0, 0)

  val Y = new Vector3f(0, 1, 0)

  val Z = new Vector3f(0, 0, 1)

  @inline
  def apply(x: Float, y: Float, z: Float): Vector3f =
    if (x == 0 && y == 0 && z == 0) Zero
    else if (x == 1 && y == 1 && z == 1) One
    else new Vector3f(x, y, z)

  @inline
  def apply(x: Float, y: Float, z: Float, reuse: Vector3f): Vector3f =
    if (x == reuse.x && y == reuse.y && z == reuse.z) reuse
    else Vector3f(x, y, z)
}
