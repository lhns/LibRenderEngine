package org.lolhens.renderengine.texture

import java.nio.file.Path

import javax.imageio.ImageIO

case class Bitmap(width: Int,
                  height: Int,
                  data: Array[Array[Int]]) {
  require(data.length == width)
  require(width == 0 || data.head.length == height)

  def withData(data: Array[Array[Int]]): Bitmap =
    Bitmap(width, height, data)
}

object Bitmap {
  def fromImageFile(path: Path): Bitmap = {
    val bufferedImage = ImageIO.read(path.toFile)

    val width = bufferedImage.getWidth
    val height = bufferedImage.getHeight

    val data = Array.tabulate[Int](width, height) { (x, y) =>
      bufferedImage.getRGB(x, y)
    }

    Bitmap(width, height, data)
  }
}
