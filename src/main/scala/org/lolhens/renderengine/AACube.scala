package org.lolhens.renderengine

/**
 * Created by LolHens on 05.10.2014.
 */
class AACube(val min: Vector3f, val max: Vector3f) {
  def contain(cube: AACube): AACube = new AACube(min.min(cube.min), max.max(cube.max))
}

object NullAACube extends AACube(NullVector3f, NullVector3f) {}
