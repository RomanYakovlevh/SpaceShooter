package playerController

import java.awt.event.KeyEvent

trait KeyCommand
object KeyCommand {
  final case class KeyPressed(keyEvent: KeyEvent)  extends KeyCommand
  final case class KeyReleased(keyEvent: KeyEvent) extends KeyCommand
  final case class KeyTyped(keyEvent: KeyEvent)    extends KeyCommand
}
