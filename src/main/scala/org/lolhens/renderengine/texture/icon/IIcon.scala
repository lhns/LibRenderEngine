package org.lolhens.renderengine.texture.icon

import javax.media.opengl.GL

/**
 * Created by LolHens on 21.10.2014.
 */
trait IIcon {
  def minU: Float

  def minV: Float

  def maxU: Float

  def maxV: Float

  def bind(gl: GL): Unit
}
