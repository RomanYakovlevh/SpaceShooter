package components

import characters.{AsteroidSpawner, PlayableCharacter}
import playerController.MouseCommand
import renderers.SwingState

import java.awt.Graphics

class Button(x: Int, y: Int, width: Int, height: Int, text: String, g: Graphics) {
  def tickComponent(mouseCommands: List[MouseCommand], state: SwingState): SwingState = {
    val b = mouseCommands.exists { mc =>
      mc match {
        case MouseCommand.MousePressed(mouseEvent) =>
          val mx = mouseEvent.getX - 500
          val my = mouseEvent.getY - 300
          x <= mx && mx <= x + width && y <= my && my <= y + height
        case _                                     => false
      }
    }
    if (b) {
      SwingState(
        state.playerController,
        PlayableCharacter.default,
        AsteroidSpawner.default,
        hasEnded = false,
        0,
        state.window
      )
    } else state
  }
}
object Button                                                                    {
  def apply(x: Int, y: Int, width: Int, height: Int, text: String, g: Graphics): Button = {
    g.drawRoundRect(x, y, width, height, 3, 3)
    g.drawString(text, x + 50, y + 50)
    new Button(x, y, width, height, text, g)
  }
}
