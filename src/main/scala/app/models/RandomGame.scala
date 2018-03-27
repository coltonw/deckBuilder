package app.models

case class RandomGame(deckSize: Int) extends BasicGame {
  val cardPool: Set[Card] = (for (_ <- 1 to deckSize * 3) yield {
    Card.random
  }).toSet
}
