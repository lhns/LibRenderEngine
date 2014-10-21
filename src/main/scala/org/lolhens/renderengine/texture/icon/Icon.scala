package org.lolhens.renderengine.texture.icon

import javax.media.opengl.GL

import com.jogamp.opengl.util.texture.Texture

/**
 * Created by LolHens on 21.10.2014.
 */
class Icon(texture: Texture,
           private val argMinU: Float,
           private val argMinV: Float,
           private val argMaxU: Float,
           private val argMaxV: Float) extends IIcon {
  //def this(texture: Texture, )

  override def minU: Float = argMinU

  override def minV: Float = argMinV

  override def maxU: Float = argMaxU

  override def maxV: Float = argMaxV

  override def bind(gl: GL): Unit = texture.bind(gl)

}
