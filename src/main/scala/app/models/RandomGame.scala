package app.models

import scala.util.Random

case class RandomGame(deckSize: Int) {
  val cardPool: Set[Card] = Card.pool(deckSize * 3)

  val enemy: Set[Card] =
    (for (_ <- 1 to 100) yield { Random.shuffle(cardPool.toVector).take(deckSize).toSet }).reduceLeft[Set[Card]] {
      case (champ: Set[Card], contender: Set[Card]) =>
        if (Match(contender, champ).win) {
          contender
        } else {
          champ
        }
    }
}
