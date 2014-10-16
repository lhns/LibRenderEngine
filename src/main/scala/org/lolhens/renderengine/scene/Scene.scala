package org.lolhens.renderengine.scene

import java.lang.reflect.Modifier
import javax.media.opengl._

import com.jogamp.opengl.util.FPSAnimator

import scala.reflect.ClassTag
import scala.runtime.Nothing$

/**
 * Created by LolHens on 16.10.2014.
 */
final class Scene[Renderer <: SceneRenderer : ClassTag](drawable: GLAutoDrawable) {
  private var sceneRenderer: SceneRenderer = null

  drawable.addGLEventListener(SceneGLEventListener)
  val animator = new FPSAnimator(drawable, 60)
  animator.start

  object SceneGLEventListener extends GLEventListener {
    override def init(drawable: GLAutoDrawable): Unit = sceneRenderer = implicitly[ClassTag[Renderer]].runtimeClass match {
      case clazz if (clazz == classOf[Nothing$]) => throw new NullPointerException()
      case clazz if (Modifier.isAbstract(clazz.getModifiers)) => throw new IllegalArgumentException(s"SceneRenderer class [${clazz.getName}] must not be abstract")
      case clazz if (classOf[SceneRenderer].isAssignableFrom(clazz)) => clazz.getConstructor(classOf[GLAutoDrawable]).newInstance(drawable).asInstanceOf[SceneRenderer]
      case _ => throw new NullPointerException()
    }

    override def display(drawable: GLAutoDrawable): Unit = sceneRenderer.render

    override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = sceneRenderer.resizeDrawable(x, y, width, height)

    override def dispose(drawable: GLAutoDrawable): Unit = sceneRenderer.disposeDrawable
  }

}