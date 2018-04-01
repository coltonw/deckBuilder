package app.models

import scala.util.Random

case class RandomGame(deckSize: Int) {
  val cardPool: Set[Card] = (for (_ <- 1 to deckSize * 3) yield {
    Card.random
  }).toSet

  val enemy: Set[Card] =
    (for (_ <- 1 to 500) yield { Random.shuffle(cardPool.toVector).take(deckSize).toSet }).reduceLeft[Set[Card]] {
      case (champ: Set[Card], contender: Set[Card]) =>
        if (Match(contender, champ).win) {
          contender
        } else {
          champ
        }
    }
}
