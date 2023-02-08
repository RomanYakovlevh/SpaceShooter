import characters.{AsteroidSpawner, PlayableCharacter}
import components.Button
import playerController.PlayerController
import renderers.{SwingBridge, SwingState}

import java.awt.geom.AffineTransform
import java.awt.image.{AffineTransformOp, BufferedImage}
import java.awt.{Dimension, Graphics}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{JFrame, Timer, WindowConstants}
import scala.math.abs

object GameLoop {
  val preferredX                = 1000
  val preferredY                = 600
  val zoom                      = 3
  val konataChan: BufferedImage = {
    val before  = ImageIO.read(new File("Anime-Konata-Izumi-PNG-HD.png"))
    val w       = before.getWidth
    val h       = before.getHeight
    var after   = new BufferedImage((w * 0.05).toInt, (h * 0.05).toInt, BufferedImage.TYPE_INT_ARGB)
    val at      = new AffineTransform
    at.scale(0.05, 0.05)
    val scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR)
    after = scaleOp.filter(before, after)
    println(s"konata ${after.getWidth} ${after.getHeight}")
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
    var after   = new BufferedImage((w * 0.1).toInt, (h * 0.1).toInt, BufferedImage.TYPE_INT_ARGB)
    val at      = new AffineTransform
    at.scale(0.1, 0.1)
    val scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR)
    after = scaleOp.filter(before, after)
    println(s"asteroid ${after.getWidth} ${after.getHeight}")
    after
  }

  private def updateF(swingState: SwingState, g: Graphics): SwingState = {
    val commands =
      swingState.playerController.retrieveAndPerish // Its important to clean playerController from accumulating commands
    g.translate(preferredX / 2, preferredY / 2)
    val newSwingState =
      if (!swingState.hasEnded)
        SwingState( // TODO redo into passing state through components
          swingState.playerController,
          swingState.playableCharacter.tickState(commands._1, commands._2),
          swingState.asteroidSpawner.tickState(),
          swingState.asteroidSpawner.asteroids.foldLeft(false)((acc, a) => // Temp collision detection
            if (acc) acc
            else
              a.posX - swingState.playableCharacter.posX < 90 && a.posX - swingState.playableCharacter.posX > -60
              && a.posY - swingState.playableCharacter.posY < 130 && a.posY - swingState.playableCharacter.posY > -60
          ),
          swingState.survivalRate + 1,
          swingState.window
        )
      else swingState

    g.drawImage(milkyWay, -500, -300, null)
    g.setColor(java.awt.Color.WHITE)
    if (newSwingState.hasEnded) {
      g.setFont(new java.awt.Font("Aerial", java.awt.Font.BOLD, 30))
      g.drawString("Misfortune!!!", -150, -50)
      g.drawString("Konata-chan collided into asteroid and died!!!", -350, 0)
      g.drawString(s"Survival rate: ${newSwingState.survivalRate}", -150, 50)
      val b = Button(-150, 150, 200, 100, "Retry", g)
      b.tickComponent(commands._2, newSwingState)
    } else {
      g.setFont(new java.awt.Font("Aerial", java.awt.Font.BOLD, 20))
      g.drawImage(
        konataChan,
        newSwingState.playableCharacter.posX.toInt,
        newSwingState.playableCharacter.posY.toInt,
        null
      )
      newSwingState.asteroidSpawner.asteroids.foreach(a => g.drawImage(asteroid, a.posX.toInt, a.posY.toInt, null))
      g.drawString(s"Survival rate: ${newSwingState.survivalRate}", -450, -250)
      g.drawString(
        s"PosX: ${newSwingState.playableCharacter.posX}, PosY: ${newSwingState.playableCharacter.posY}",
        -450,
        -200
      )
      newSwingState
    }
  }

  def run = {
    import javax.swing.JFrame

    val window: JFrame = new JFrame("SpaceShooter")
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    val playerController = PlayerController.apply

    val swingBridge = SwingBridge(
      context => context.setPreferredSize(new Dimension(preferredX, preferredY)),
      updateF,
      SwingState(playerController, PlayableCharacter.default, AsteroidSpawner.default, hasEnded = false, 0, window)
    )
    val timer       = new Timer(50, swingBridge)
    timer.start()

    window.add(swingBridge)
    window.addKeyListener(playerController)
    window.addMouseMotionListener(playerController)
    window.addMouseListener(playerController)
    window.setResizable(false)
    window.pack()
    window.setLocationRelativeTo(null)
    window.setVisible(true)
  }
}
