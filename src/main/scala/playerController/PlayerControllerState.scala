package demo_V2.playerController

class PlayerControllerState(
  @volatile private var keyCommands:   List[KeyCommand],
  @volatile private var mouseCommands: List[MouseCommand]
) {
  private def addOrPerishToList[A](elem: Option[A], addTo: List[A]) =
    elem match { // Do I actually want to solve it like that? Maybe there is a way to use
      // different synchronized blocks synchronosly to each other?
      case Some(value) => (addTo.appended(value), List())
      case None        => (List(), addTo)
    }

  private def addOrPerishKeyCommand(maybeKeyCommand: Option[KeyCommand]): List[KeyCommand] =
    synchronized {
      val r = addOrPerishToList(maybeKeyCommand, keyCommands)
      keyCommands = r._1
      val perishedList = r._2
      return perishedList
    }

  private def addOrPerishMouseCommand(maybeMouseCommand: Option[MouseCommand]): List[MouseCommand] =
    synchronized {
      val r = addOrPerishToList(maybeMouseCommand, mouseCommands)
      mouseCommands = r._1
      val perishedList = r._2
      return perishedList
    }

  def addKeyCommand(keyCommand: KeyCommand): Unit =
    addOrPerishKeyCommand(Some(keyCommand))

  def addMouseCommand(mouseCommand: MouseCommand): Unit =
    addOrPerishMouseCommand(Some(mouseCommand))

  def retrieveAndPerishCommands: (List[KeyCommand], List[MouseCommand]) =
    (addOrPerishKeyCommand(None), addOrPerishMouseCommand(None))
}

object PlayerControllerState {
  def empty = new PlayerControllerState(List(), List())
}
