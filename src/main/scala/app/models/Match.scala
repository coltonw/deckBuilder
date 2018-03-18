package app.models

case class Match(deck: Set[Card], enemyDeck: Set[Card]) {
  val myBreakdown    = Breakdown(deck)
  val enemyBreakdown = Breakdown(enemyDeck)
  val myScore        = Match.score(deck, myBreakdown, enemyBreakdown)
  val enemyScore     = Match.score(enemyDeck, enemyBreakdown, myBreakdown)
  val win            = myScore > enemyScore
}

object Match {
  def score(myDeck: Set[Card], myBreakdown: Breakdown, enemyBreakdown: Breakdown): Double = myDeck.foldLeft(0d) {
    (score, card) =>
      score + card.power(myBreakdown.exclude(card), enemyBreakdown)
  }
}
