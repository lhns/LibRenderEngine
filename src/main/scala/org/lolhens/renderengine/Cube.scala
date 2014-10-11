package org.lolhens.renderengine

/**
 * Created by LolHens on 05.10.2014.
 */
class Cube(val min: Vector3f, val max: Vector3f) {
  def contain(cube: Cube): Cube = new Cube(min.min(cube.min), max.max(cube.max))
}

object NullCube extends Cube(NullVector3f, NullVector3f) {}
