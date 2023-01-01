package demo_V2.playerController

import java.awt.event.{KeyEvent, KeyListener, MouseEvent, MouseListener, MouseMotionListener}

class PlayerController(pcState: PlayerControllerState) extends KeyListener with MouseMotionListener with MouseListener {
  override def keyTyped(e: KeyEvent): Unit = pcState.addKeyCommand(KeyCommand.KeyTyped(e))

  override def keyPressed(e: KeyEvent): Unit = pcState.addKeyCommand(KeyCommand.KeyPressed(e))

  override def keyReleased(e: KeyEvent): Unit = pcState.addKeyCommand(KeyCommand.KeyReleased(e))

  override def mouseDragged(e: MouseEvent): Unit = pcState.addMouseCommand(MouseCommand.MouseDragged(e))

  override def mouseMoved(e: MouseEvent): Unit = pcState.addMouseCommand(MouseCommand.MouseMoved(e))

  override def mouseClicked(e: MouseEvent): Unit = pcState.addMouseCommand(MouseCommand.MouseClicked(e))

  override def mousePressed(e: MouseEvent): Unit = pcState.addMouseCommand(MouseCommand.MousePressed(e))

  override def mouseReleased(e: MouseEvent): Unit = pcState.addMouseCommand(MouseCommand.MouseReleased(e))

  override def mouseEntered(e: MouseEvent): Unit = ()

  override def mouseExited(e: MouseEvent): Unit = ()

  def retrieveAndPerish: (List[KeyCommand], List[MouseCommand]) = pcState.retrieveAndPerishCommands
}

object PlayerController {
  def apply = new PlayerController(PlayerControllerState.empty)
}
