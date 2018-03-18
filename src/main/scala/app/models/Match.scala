package app.models

case class Match(deck: Set[Card], enemyDeck: Set[Card]) {
  val myBreakdown = Breakdown(deck)
  val enemyBreakdown = Breakdown(enemyDeck)
  val myScore = deck.foldLeft(0d) { (score, card) =>
    score + card.power(myBreakdown.exclude(card), enemyBreakdown)
  }
}