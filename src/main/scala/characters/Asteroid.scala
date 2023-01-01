package characters

import java.awt.geom.AffineTransform
import java.awt.image.{AffineTransformOp, BufferedImage}
import java.io.File
import javax.imageio.ImageIO

final case class Asteroid(posX: Float, posY: Float, vectorX: Float, vectorY: Float, lifetime: Int) {
  def tickState(): Asteroid =
    Asteroid(posX + vectorX, posY + vectorY, vectorX, vectorY, lifetime + 1)
}

object Asteroid {
  def apply(posX: Float, posY: Float, vectorX: Float, vectorY: Float) =
    new Asteroid(posX, posY, vectorX, vectorY, 0)
}
