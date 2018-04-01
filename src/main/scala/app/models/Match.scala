package app.models

case class Match(deck: Set[Card], enemyDeck: Set[Card]) {
  val myPhaseDecks: Map[Age, Set[Card]]    = Age.phases.map(age => age -> deck.filter(age.inPhase)).toMap
  val enemyPhaseDecks: Map[Age, Set[Card]] = Age.phases.map(age => age -> enemyDeck.filter(age.inPhase)).toMap
  val myBreakdowns: Map[Age, Breakdown]    = myPhaseDecks.mapValues(d => Breakdown(d))
  val enemyBreakdowns: Map[Age, Breakdown] = enemyPhaseDecks.mapValues(d => Breakdown(d))
  val myScoredPhaseDecks: Map[Age, Set[Card]] = myPhaseDecks.map {
    case (age, d) =>
      age -> Match.scoreDeck(d,
                             myBreakdowns.getOrElse(age, Breakdown.empty),
                             enemyBreakdowns.getOrElse(age, Breakdown.empty))
  }
  val enemyScoredPhaseDecks: Map[Age, Set[Card]] = enemyPhaseDecks.map {
    case (age, d) =>
      age -> Match.scoreDeck(d,
                             enemyBreakdowns.getOrElse(age, Breakdown.empty),
                             myBreakdowns.getOrElse(age, Breakdown.empty))
  }
  val myScores: Map[Age, Double]    = myScoredPhaseDecks.mapValues(d => Match.score(d))
  val enemyScores: Map[Age, Double] = enemyScoredPhaseDecks.mapValues(d => Match.score(d))

  val (result: Option[Match.Result], finalAge: Age) = Age.phases
    .foldLeft[(Option[Match.Result], Age)](None, Ancient) {
      case ((None, _), Ancient) =>
        (Match.subMatch(myScores.getOrElse(Ancient, 0d), enemyScores.getOrElse(Ancient, 0d), 0.0d), Ancient)
      case ((None, _), age) =>
        (Match.subMatch(myScores.getOrElse(age, 0d), enemyScores.getOrElse(age, 0d)), age)
      case (r, _) => r
    }
  val myFinalDeck: Set[Card] = myScoredPhaseDecks.getOrElse(finalAge, Set.empty)
  val enemyFinalDeck: Set[Card] = enemyScoredPhaseDecks.getOrElse(finalAge, Set.empty)
  val myUnusedCards: Set[Card] = deck.filter(c => !finalAge.inPhase(c))
  val enemyUnusedCards: Set[Card] = enemyDeck.filter(c => !finalAge.inPhase(c))
  val win = result.contains(Match.Win)

}

object Match {
  trait Result
  case object Win  extends Result
  case object Loss extends Result

  def scoreDeck(myDeck: Set[Card], myBreakdown: Breakdown, enemyBreakdown: Breakdown): Set[Card] =
    myDeck.map(c => c.copy(score = Some(c.power(myBreakdown.exclude(c), enemyBreakdown))))

  def score(myDeck: Set[Card]): Double = myDeck.foldLeft(0d) { (score, card) =>
    score + card.score.getOrElse(0.0)
  }

  def subMatch(subScore: Double, enemySubScore: Double, threshold: Double = 20d): Option[Result] = {
    if (enemySubScore >= subScore + threshold) {
      Some(Loss)
    } else if (subScore >= enemySubScore + threshold) {
      Some(Win)
    } else {
      None
    }
  }
}
