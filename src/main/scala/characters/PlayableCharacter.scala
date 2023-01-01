package characters

import playerController.{KeyCommand, MouseCommand, PlayerControllerState}

import java.awt.event.KeyEvent
import javax.swing.event.MenuKeyEvent

case class PlayableCharacter(posX: Float, posY: Float) {
  def tickState(commandsKey: List[KeyCommand], commandMouse: List[MouseCommand]) = {
    val (newPosX, newPosY) = commandsKey.foldLeft((posX, posY))((acc, kc) =>
      kc match {
        case KeyCommand.KeyPressed(keyEvent) => keyEvent.getKeyCode match {
            case KeyEvent.VK_W => (acc._1, acc._2 - 15)
            case KeyEvent.VK_S => (acc._1, acc._2 + 15)
            case KeyEvent.VK_A => (acc._1 - 15, acc._2)
            case KeyEvent.VK_D => (acc._1 + 15, acc._2)
            case _             => acc
          }
        case _                               => acc
      }
    )
    PlayableCharacter(newPosX, newPosY)
  }
}

object PlayableCharacter {
  def default: PlayableCharacter = PlayableCharacter(0, 0)
}
