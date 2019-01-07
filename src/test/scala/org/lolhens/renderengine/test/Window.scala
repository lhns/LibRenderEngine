package org.lolhens.renderengine.test

import java.awt.BorderLayout
import java.awt.event.{WindowAdapter, WindowEvent}
import com.jogamp.opengl.awt.GLCanvas
import javax.swing.JFrame

import org.lolhens.renderengine.model.Model
import org.lolhens.renderengine.scene.{DefaultSceneRenderer, Scene}

/**
 * Created by LolHens on 12.10.2014.
 */
class Window extends JFrame {
  val canvas = new GLCanvas()
  val model = new Model()
  val scene = new Scene[DefaultSceneRenderer](canvas)

  addWindowListener(new WindowAdapter() {
    override def windowClosing(event: WindowEvent) = System.exit(0)
  })

  setSize(500, 500)
  setTitle("TEST WINDOW")

  getContentPane().add(canvas, BorderLayout.CENTER)

  /*
  val off = 0
  //(new Random().nextInt(1000) - 500).asInstanceOf[Float] / 100f
  val data = Array(0.0f + off, 1.0f + off, 0.0f + off, -1.0f + off, -1.0f + off, 0.0f + off, 1.0f + off, -1.0f + off, 0.0f + off)

  renderList += data -> ToByteArray(data)

   */
}
