package characters

import java.awt.Graphics
import scala.util.Random

case class AsteroidSpawner(counter: Int, asteroids: List[Asteroid]) {
  def tickState(): AsteroidSpawner = {
    val newAsteroids = (if (counter % 10 == 0 && counter > 60) {
                          val px = Random.between(-500, 500)
                          val py = if (Random.between(-300, 300) > 0) 300 else -300
                          val vx = Random.between(math.max(-500, px - 50), math.min(500, px + 50)) - px
                          val vy = -py / 20
                          asteroids.appended(Asteroid(px, py, vx, vy))
                        } else asteroids).filter(a => a.lifetime < 100).map(a => a.tickState())

    AsteroidSpawner(counter + 1, newAsteroids)
  }
}

object AsteroidSpawner {
  def default: AsteroidSpawner = AsteroidSpawner(0, List())
}
