package org.lolhens.renderengine.test

import java.awt.BorderLayout
import java.awt.event.{WindowAdapter, WindowEvent}
import java.nio.{ByteOrder, ByteBuffer}
import java.util.Random
import javax.media.opengl._
import javax.media.opengl.awt.GLCanvas
import javax.media.opengl.fixedfunc.{GLPointerFunc, GLLightingFunc, GLMatrixFunc}
import javax.media.opengl.glu.GLU
import javax.swing.JFrame

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

  override def init(drawable: GLAutoDrawable): Unit = {
    val gl = drawable.getGL.getGL2
    drawable.setGL(new DebugGL2(gl))

    gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY)
    gl.glEnable(GL.GL_DEPTH_TEST)
    gl.glDepthFunc(GL.GL_LEQUAL)
    gl.glShadeModel(GLLightingFunc.GL_SMOOTH)
    gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST)
    gl.glClearColor(1f, 0f, 0f, 1f)
  }

  override def display(drawable: GLAutoDrawable): Unit = {

    val gl = drawable.getGL.getGL2
    val vboIds = new Array[Int](1)
    gl.glGenBuffers(1, vboIds, 0)
    val vboId = vboIds(0)
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboId)

println(vboId)

    val byteBuffer = ByteBuffer.allocateDirect(3 * 3 * 4).order(ByteOrder.nativeOrder())
    byteBuffer.putFloat(0.0f)
    byteBuffer.putFloat(1.0f)
    byteBuffer.putFloat(0.0f)
    byteBuffer.putFloat(-1.0f)
    byteBuffer.putFloat(-1.0f)
    byteBuffer.putFloat(0.0f)
    byteBuffer.putFloat(1.0f)
    byteBuffer.putFloat(-1.0f)
    byteBuffer.putFloat(0.0f)
    byteBuffer.rewind()
    println(byteBuffer)
    gl.glBufferData(GL.GL_ARRAY_BUFFER, 3 * 3 * 4, byteBuffer, GL.GL_DYNAMIC_DRAW)
    //gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, 3 * 3 * 4, byteBuffer)
    //while (true) {
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
      gl.glLoadIdentity()
      gl.glTranslatef(-1.5f, 0.0f, -6.0f)
      /*gl.glBegin(GL.GL_TRIANGLES)
      gl.glVertex3f(0.0f, 1.0f, 0.0f)
      gl.glVertex3f(-1.0f, -1.0f, 0.0f)
      gl.glVertex3f(1.0f, -1.0f, 0.0f)
      gl.glEnd()*/
    //gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboId)
    gl.glVertexPointer(3, GL.GL_FLOAT, 4*3, 0)
      gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)
      gl.glTranslatef(3.0f, 0.0f, 0.0f)
      gl.glBegin(GL2GL3.GL_QUADS)
      gl.glVertex3f(-1.0f, 1.0f, new Random().nextFloat())
      gl.glVertex3f(1.0f, 1.0f, 0.0f)
      gl.glVertex3f(1.0f, -1.0f, 0.0f)
      gl.glVertex3f(-1.0f, -1.0f, 0.0f)
      gl.glEnd()
      gl.glFlush()
      //drawable.swapBuffers()
    //}
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
