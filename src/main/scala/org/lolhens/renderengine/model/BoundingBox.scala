package org.lolhens.renderengine.model

import org.lolhens.renderengine.vector.Vector3f

/**
  * Created by LolHens on 05.10.2014.
  */
class BoundingBox(val min: Vector3f, val max: Vector3f) {
  def contain(cube: BoundingBox): BoundingBox = new BoundingBox(min.min(cube.min), max.max(cube.max))
}

object NullBoundingBox extends BoundingBox(Vector3f.Zero, Vector3f.Zero) {}
