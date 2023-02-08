package characters

import playerController.{KeyCommand, MouseCommand, PlayerControllerState}

import java.awt.event.KeyEvent
import javax.swing.event.MenuKeyEvent

case class PlayableCharacter(posX: Float, posY: Float, vecX: Float, vecY: Float) {
  def tickState(commandsKey: List[KeyCommand], commandMouse: List[MouseCommand]) = {
    val speed              = 25
    val (newVecX, newVecY) = commandsKey.foldLeft((vecX, vecY))((acc, kc) =>
      kc match {
        case KeyCommand.KeyPressed(keyEvent)  => keyEvent.getKeyCode match {
            case KeyEvent.VK_W => (acc._1, -speed)
            case KeyEvent.VK_S => (acc._1, speed)
            case KeyEvent.VK_A => (-speed, acc._2)
            case KeyEvent.VK_D => (speed, acc._2)
            case _             => acc
          }
        case KeyCommand.KeyReleased(keyEvent) => keyEvent.getKeyCode match {
            case KeyEvent.VK_W => (acc._1, 0)
            case KeyEvent.VK_S => (acc._1, 0)
            case KeyEvent.VK_A => (0, acc._2)
            case KeyEvent.VK_D => (0, acc._2)
            case _             => acc
          }
        case _                                => acc
      }
    )
    PlayableCharacter(posX + newVecX, posY + newVecY, newVecX, newVecY)
  }
}

object PlayableCharacter {
  def default: PlayableCharacter = PlayableCharacter(0, 0, 0, 0)
}
