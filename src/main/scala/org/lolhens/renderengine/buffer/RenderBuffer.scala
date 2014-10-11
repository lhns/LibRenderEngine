package org.lolhens.renderengine.buffer

import java.nio.ByteBuffer

import org.lolhens.renderengine.{NullVector3f, Vector3f}

/**
 * Created by LolHens on 05.10.2014.
 */
class RenderBuffer {
  var buffer = ByteBuffer.allocateDirect(0)

  def update = {}

  def render(translation: Vector3f = NullVector3f, rotation: Vector3f = NullVector3f, rotationOrigin: Vector3f = NullVector3f) = {}
}
