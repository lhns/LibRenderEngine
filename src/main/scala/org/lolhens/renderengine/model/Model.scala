package org.lolhens.renderengine.model

import java.util

/**
 * Created by LolHens on 05.10.2014.
 */
class Model {
  private var _dirty = true

  def dirty: Boolean = _dirty

  def dirty_=(value: Boolean): Unit = {
    _dirty // TODO!!!!!!!
    _dirty = value
    if (value) parents.foreach(_.dirty = value)
  }

  private val parents = new util.ArrayList[Model]()
  private val children = new util.ArrayList[Model]()
  private val dirtyChildren = new util.LinkedList[Model]()

  var bounds = NullBoundingBox

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
}
