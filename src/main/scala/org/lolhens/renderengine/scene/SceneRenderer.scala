package org.lolhens.renderengine.scene

import com.jogamp.opengl.glu.GLU
import com.jogamp.opengl.{GL2, GLAutoDrawable}
import org.lolhens.renderengine.buffer.RenderList

/**
  * Created by LolHens on 16.10.2014.
  */
abstract class SceneRenderer(drawable: GLAutoDrawable) {
  val gl = drawable.getGL.getGL2
  val glu = new GLU()
  val renderList = new RenderList(gl, setVBOPointers)

  def render: Unit

  def setVBOPointers(gl: GL2): Int

  def resizeDrawable(x: Int, y: Int, width: Int, height: Int): Unit

  def disposeDrawable: Unit = renderList.close
}