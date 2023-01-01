import characters.{AsteroidSpawner, PlayableCharacter}
import playerController.PlayerController
import renderers.SwingBridge

import java.awt.image.BufferedImage
import java.awt.{Dimension, Graphics}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{Timer, WindowConstants}
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import math.abs

object GameLoop {
  val preferredX                = 1000
  val preferredY                = 600
  val zoom                      = 3
  val konataChan: BufferedImage = {
    val before  = ImageIO.read(new File("Anime-Konata-Izumi-PNG-HD.png"))
    val w       = before.getWidth
    val h       = before.getHeight
    var after   = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val at      = new AffineTransform
    at.scale(0.05, 0.05)
    val scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR)
    after = scaleOp.filter(before, after)
    after
  }
  val milkyWay: BufferedImage   = {
    val before  = ImageIO.read(new File("milky-way-2695569__480.jpg"))
    val w       = before.getWidth
    val h       = before.getHeight
    var after   = new BufferedImage(preferredX, preferredY, BufferedImage.TYPE_INT_ARGB)
    val at      = new AffineTransform
    at.scale(preferredX.toFloat / w, preferredY.toFloat / h)
    val scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR)
    after = scaleOp.filter(before, after)
    after
  }
  val kagamiChan: BufferedImage = {
    val before  = ImageIO.read(new File("Kagami_Render.png"))
    val w       = before.getWidth
    val h       = before.getHeight
    var after   = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val at      = new AffineTransform
    at.scale(0.05, 0.05)
    val scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR)
    after = scaleOp.filter(before, after)
    after
  }
  val asteroid: BufferedImage   = {
    val before  = ImageIO.read(new File("asteroid-transparent-2.png"))
    val w       = before.getWidth
    val h       = before.getHeight
    var after   = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val at      = new AffineTransform
    at.scale(0.1, 0.1)
    val scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR)
    after = scaleOp.filter(before, after)
    after
  }

  final private case class SwingState(
    playerController:  PlayerController,
    playableCharacter: PlayableCharacter,
    asteroidSpawner:   AsteroidSpawner,
    hasEnded:          Boolean
  )

  private def updateF(swingState: SwingState, g: Graphics): SwingState = {
    g.translate(preferredX / 2, preferredY / 2)
    val commands      = swingState.playerController.retrieveAndPerish
    val newSwingState =
      if (swingState.hasEnded) swingState
      else SwingState(
        swingState.playerController,
        swingState.playableCharacter.tickState(commands._1, commands._2),
        swingState.asteroidSpawner.tickState(),
        swingState.asteroidSpawner.asteroids.foldLeft(false)((acc, a) => // Temp collision detection
          if (acc) acc
          else
            abs(a.posX - swingState.playableCharacter.posX) < 50 &&
            abs(a.posY - swingState.playableCharacter.posY) < 50
        )
      )

    g.drawImage(milkyWay, -500, -300, null)
    g.setColor(java.awt.Color.WHITE)
    if (newSwingState.hasEnded) {
      g.setFont(new java.awt.Font("Aerial", java.awt.Font.BOLD, 30))
      g.drawString("Misfortune!!!", -100, 0)
      g.drawString("Konata-chan collided into asteroid and died!!!", -300, 50)
    } else {
      g.setFont(new java.awt.Font("Aerial", java.awt.Font.BOLD, 20))
      g.drawImage(
        konataChan,
        newSwingState.playableCharacter.posX.toInt,
        newSwingState.playableCharacter.posY.toInt,
        null
      )
      newSwingState.asteroidSpawner.asteroids.foreach(a => g.drawImage(asteroid, a.posX.toInt, a.posY.toInt, null))
      g.drawString(s"posX: ${newSwingState.playableCharacter.posX}", -450, -250)
      g.drawString(s"posY: ${newSwingState.playableCharacter.posY}", -450, -200)
      g.drawString(s"Asteroids: ${newSwingState.asteroidSpawner.asteroids.length}", -450, -150)
    }
    newSwingState

  }

  def run = {
    import javax.swing.JFrame

    val window: JFrame = new JFrame("SpaceShooter")
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    val playerController = PlayerController.apply

    val swingBridge = SwingBridge(
      context => context.setPreferredSize(new Dimension(preferredX, preferredY)),
      updateF,
      SwingState(playerController, PlayableCharacter.default, AsteroidSpawner.default, hasEnded = false)
    )
    val timer       = new Timer(50, swingBridge)
    timer.start()

    window.add(swingBridge)
    window.addKeyListener(playerController)
    window.addMouseMotionListener(playerController)
    window.setResizable(false)
    window.pack()
    window.setLocationRelativeTo(null)
    window.setVisible(true)
  }
}
