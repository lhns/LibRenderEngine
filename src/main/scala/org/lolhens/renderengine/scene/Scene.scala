package org.lolhens.renderengine.scene

import java.lang.reflect.Modifier
import java.util
import javax.media.opengl._

import com.jogamp.opengl.util.FPSAnimator
import org.lolhens.renderengine.model.Model

import scala.reflect.ClassTag

/**
 * Created by LolHens on 16.10.2014.
 */
final class Scene[Renderer <: SceneRenderer : ClassTag](drawable: GLAutoDrawable) {
  drawable.addGLEventListener(SceneGLEventListener)

  private var sceneRenderer: SceneRenderer = null
  private val modelList = new util.ArrayList[Model]()
  val animator = new FPSAnimator(drawable, 60)

  animator.start

  def +=(model: Model): Boolean = modelList.add(model)

  def -=(model: Model): Boolean = modelList.remove(model)

  def contains(model: Model): Boolean = modelList.contains(model)

  object SceneGLEventListener extends GLEventListener {
    override def init(drawable: GLAutoDrawable): Unit = sceneRenderer = implicitly[ClassTag[Renderer]].runtimeClass match {
      case clazz if (!classOf[SceneRenderer].isAssignableFrom(clazz)) => throw new NullPointerException(s"Scene must have generic args!")
      case clazz if (Modifier.isAbstract(clazz.getModifiers)) => throw new IllegalArgumentException(s"SceneRenderer class [${clazz.getName}] must not be abstract!")
      case clazz => clazz.getConstructor(classOf[GLAutoDrawable]).newInstance(drawable).asInstanceOf[SceneRenderer]
    }

    override def display(drawable: GLAutoDrawable): Unit = {
      val iterator = modelList.iterator()
      while (iterator.hasNext) sceneRenderer.renderList.put(iterator.next)
      sceneRenderer.render
    }

    override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = sceneRenderer.resizeDrawable(x, y, width, height)

    override def dispose(drawable: GLAutoDrawable): Unit = sceneRenderer.disposeDrawable
  }

}