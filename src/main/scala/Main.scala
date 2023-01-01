object Main {
  def main(args: Array[String]): Unit = {
    import javax.swing.SwingUtilities
    SwingUtilities.invokeLater(new Runnable() {
      override def run(): Unit =
        GameLoop.run
    })
  }
}
