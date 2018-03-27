package app.models

import scala.util.Random

trait BasicGame {
  val cardPool: Set[Card]
  val deckSize: Int

  private val possibleEnemies: Seq[Set[Card]] = for (_ <- 1 to 5) yield { Random.shuffle(cardPool.toVector).take(deckSize).toSet }
  val enemy: Set[Card] = possibleEnemies.reduceLeft[Set[Card]] {
    case (champ: Set[Card], contender: Set[Card]) =>
      if (Match(contender, champ).win) {
        contender
      } else {
        champ
      }
  }
}
