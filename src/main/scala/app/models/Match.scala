package app.models

case class Match(deck: Set[Card], enemyDeck: Set[Card]) {
  val myBreakdown    = Breakdown(deck)
  val enemyBreakdown = Breakdown(enemyDeck)
  val myFinalDeck    = Match.scoreDeck(deck, myBreakdown, enemyBreakdown)
  val enemyFinalDeck = Match.scoreDeck(enemyDeck, enemyBreakdown, myBreakdown)
  val myScore        = Match.score(myFinalDeck)
  val enemyScore     = Match.score(enemyFinalDeck)
  val win            = myScore > enemyScore
}

object Match {
  def scoreDeck(myDeck: Set[Card], myBreakdown: Breakdown, enemyBreakdown: Breakdown): Set[Card] =
    myDeck.map(c => c.copy(score = Some(c.power(myBreakdown.exclude(c), enemyBreakdown))))

  def score(myDeck: Set[Card]): Double = myDeck.foldLeft(0d) { (score, card) =>
    score + card.score.getOrElse(0.0)
  }
}
