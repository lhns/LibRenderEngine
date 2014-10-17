package org.lolhens.renderengine.util

import java.util

/**
 * Created by LolHens on 17.10.2014.
 */
class JavaForeach[T](val list: util.List[T]) extends AnyVal {
  def foreach(func: T => Unit) = {
    val iterator = list.iterator()
    while (iterator.hasNext) func(iterator.next)
  }
}

object JavaForeach {
  implicit def toJavaForeach[T](list: util.List[T]): JavaForeach[T] = new JavaForeach[T](list)
}