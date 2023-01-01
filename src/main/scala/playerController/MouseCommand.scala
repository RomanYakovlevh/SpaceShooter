package playerController

import java.awt.event.MouseEvent

trait MouseCommand
object MouseCommand {
  final case class MouseMoved(mouseEvent: MouseEvent)    extends MouseCommand
  final case class MouseDragged(mouseEvent: MouseEvent)  extends MouseCommand
  final case class MouseClicked(mouseEvent: MouseEvent)  extends MouseCommand
  final case class MousePressed(mouseEvent: MouseEvent)  extends MouseCommand
  final case class MouseReleased(mouseEvent: MouseEvent) extends MouseCommand
}
