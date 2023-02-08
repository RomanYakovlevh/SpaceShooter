package renderers

import characters.{AsteroidSpawner, PlayableCharacter}
import playerController.PlayerController

import javax.swing.JFrame

final case class SwingState(
  playerController:  PlayerController,
  playableCharacter: PlayableCharacter,
  asteroidSpawner:   AsteroidSpawner,
  var hasEnded:      Boolean,
  survivalRate:      Int,
  window:            JFrame
)
