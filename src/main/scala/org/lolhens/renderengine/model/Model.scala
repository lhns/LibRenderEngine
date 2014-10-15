package org.lolhens.renderengine.model

import org.lolhens.renderengine.{NullCube, NullVector3f, Vector3f}

import scala.collection.mutable

/**
 * Created by LolHens on 05.10.2014.
 */
class Model {
  //private val buffer = new RenderBuffer()

  private var _dirty = true

  def dirty: Boolean = _dirty

  def dirty_=(value: Boolean): Unit = {
    _dirty = value
    if (value) parents.foreach(_.dirty = value)
  }

  private val parents = mutable.MutableList[Model]()
  private val children = mutable.MutableList[Model]()

  var bounds = NullCube

  def +=(model: Model): Unit = {
    dirty = true
    children += model
    model.parents += this
  }

  def update: Unit = {
    dirty = false

    bounds = null
    children.foreach((child: Model) => {
      if (child.dirty) update
      if (bounds == null) bounds = child.bounds else bounds.contain(child.bounds)
      // collect the children's buffers
    })

    //buffer.update
  }

  def render(translation: Vector3f = NullVector3f, rotation: Vector3f = NullVector3f, rotationOrigin: Vector3f = NullVector3f): Unit = {
    if (dirty) update
    //buffer.render(translation, rotation, rotationOrigin)
  }
}
