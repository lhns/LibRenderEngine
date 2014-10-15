package org.lolhens.renderengine.test

import java.awt.BorderLayout
import java.awt.event.{WindowAdapter, WindowEvent}
import java.util.Random
import javax.media.opengl._
import javax.media.opengl.awt.GLCanvas
import javax.media.opengl.fixedfunc.{GLLightingFunc, GLMatrixFunc}
import javax.media.opengl.glu.GLU
import javax.swing.JFrame

import org.lolhens.renderengine.buffer.{RenderBufferManager, VBO}
import org.lolhens.renderengine.util.ToByteArray

/**
 * Created by LolHens on 12.10.2014.
 */
class Window extends JFrame with GLEventListener {
  val canvas = new GLCanvas()
  canvas.addGLEventListener(this)

  val glu = new GLU()

  addWindowListener(new WindowAdapter() {
    override def windowClosing(event: WindowEvent) = System.exit(0)
  })

  setSize(500, 500)
  setTitle("TEST WINDOW")

  getContentPane().add(canvas, BorderLayout.CENTER)

  var mb: RenderBufferManager = null

  override def init(drawable: GLAutoDrawable): Unit = {
    val gl = drawable.getGL.getGL2
    drawable.setGL(new DebugGL2(gl))

    VBO.enable(gl)
    gl.glEnable(GL.GL_DEPTH_TEST)
    gl.glDepthFunc(GL.GL_LEQUAL)
    gl.glShadeModel(GLLightingFunc.GL_SMOOTH)
    gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST)
    gl.glClearColor(1f, 0f, 0f, 1f)

    mb = new RenderBufferManager(gl)
  }

  override def display(drawable: GLAutoDrawable): Unit = {

    val gl = drawable.getGL.getGL2

    val off = (new Random().nextInt(1000) - 500).asInstanceOf[Float] / 100f
    val data = Array(0.0f + off, 1.0f + off, 0.0f + off, -1.0f + off, -1.0f + off, 0.0f + off, 1.0f + off, -1.0f + off, 0.0f + off)

    //mb += bufData -> bufData
    mb += data -> ToByteArray(data)
    //mb -= bufData

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
    gl.glLoadIdentity()
    gl.glTranslatef(-1.5f, 0.0f, -6.0f)

    mb.render

    gl.glTranslatef(3.0f, 0.0f, 0.0f)
    gl.glBegin(GL2GL3.GL_QUADS)
    gl.glVertex3f(-1.0f, 1.0f, new Random().nextFloat())
    gl.glVertex3f(1.0f, 1.0f, 0.0f)
    gl.glVertex3f(1.0f, -1.0f, 0.0f)
    gl.glVertex3f(-1.0f, -1.0f, 0.0f)
    gl.glEnd()
    gl.glFlush()
  }

  override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, _height: Int): Unit = {
    val gl = drawable.getGL.getGL2

    var height = _height
    if (height <= 0) height = 1

    val h = width.asInstanceOf[Float] / height.asInstanceOf[Float]

    gl.glViewport(0, 0, width, height)
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
    gl.glLoadIdentity()
    glu.gluPerspective(45.0f, h, 1.0, 20.0)
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
    gl.glLoadIdentity()
  }

  override def dispose(drawable: GLAutoDrawable): Unit = {}
}
