import java.awt.Graphics
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JPanel

class SwingBridge extends JPanel with ActionListener {

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)

  override def actionPerformed(e: ActionEvent): Unit =
    repaint() // Calls .tickComponent()

  def init(): Unit = ???
}

object SwingBridge {
  def apply[A](initF: JPanel => Unit, updateF: (A, Graphics) => A, zeroState: A): SwingBridge = {
    val swingBridge = new SwingBridge {
      private var state = zeroState

      override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        state = updateF(state, g)
      }

      override def init(): Unit = initF(this)
    }
    swingBridge.init()
    swingBridge
  }
}
