package org.lolhens.renderengine.test

import javax.swing.SwingUtilities

/**
  * Created by LolHens on 12.10.2014.
  */
object Main {
  def main(args: Array[String]): Unit = {
    SwingUtilities.invokeLater { () =>
      new Window().setVisible(true)
    }
  }
}
