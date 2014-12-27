package org.lolhens.renderengine.model

import java.util

import scala.collection.JavaConversions._

/**
 * Created by LolHens on 05.10.2014.
 */
class Model {
  private val parents = new util.ArrayList[Model]()
  private val children = new util.ArrayList[Model]()
  val dirtyChildren = new util.LinkedList[Model]()
  val addedChildren = new util.LinkedList[Model]()
  val removedChildren = new util.LinkedList[Model]()

  var bounds = NullBoundingBox

  private def update(model: Model): Unit = {
    dirtyChildren.add(model)
    parents.foreach(_.update(this))
  }

  def dirty: Boolean = !dirtyChildren.isEmpty || !addedChildren.isEmpty || !removedChildren.isEmpty

  def +=(model: Model): Unit = {
    if (children.add(model)) {
      model.parents.add(this)
      addedChildren.add(model)
      if (model.dirty) dirtyChildren.add(model)
      parents.foreach(_.update(this))
    }
  }

  def -=(model: Model): Unit = {
    if (children.remove(model)) {
      model.parents.remove(this)
      removedChildren.add(model)
      if (model.dirty) dirtyChildren.add(model)
      parents.foreach(_.update(this))
    }
  }

  def foreach(func: Model => Unit) = {
    val iterator = children.iterator()
    while (iterator.hasNext) func(iterator.next)
  }

  override def clone: Model = ???
}