package app.models

/**
 * Ages Young, Adult, Old, Ancient; older means lower frequency
 * First have Young face all young, then young and grown face young and adult etc.
 * Where each phase if one team wins by more than a certain threshold then the game is over and that team won.
 */
sealed trait Age {
  val phaseIndex: Int
}

case object Young extends Age {
  val phaseIndex = 0
}

case object Adult extends Age {
  val phaseIndex = 1
}

case object Old extends Age {
  val phaseIndex = 2
}

case object Ancient extends Age {
  val phaseIndex = 3
}

object Age {
  val phases: Vector[Age] = Vector(Young, Adult, Old, Ancient)
}

/**
 * Alchemists < Techs < Conjurors < Prophets < Woodsmen < Alchemists
 * Professions have one other they are strong against (the two to the left with looping)
 * Could have multiple professions etc count as both for all pruposes
 * if you are strong against something you get double strength reduced to the ratio of opponents cards you are strong against
 */
sealed trait Profession

case object Alchemists extends Profession
case object Techs      extends Profession
case object Conjurors  extends Profession
case object Prophets   extends Profession
case object Woodsmen   extends Profession

// Add races purely for synergy
// Beastman, Undead, Human
sealed trait Race

case object Beastman extends Race
case object Undead   extends Race
case object Human    extends Race

// synergies or anti-synergies:
// can synergize with or against age, profession, race, or power level
// flat boost with or against
// flat weakness with or against
// change age (younger or older) with synergies

case class Breakdown(count: Int, age: Map[Age, Double], profession: Map[Profession, Double], race: Map[Race, Double]) {
  def exclude(card: Card): Breakdown = {
    val newCount = count - 1
    val newAgeCount: Int = Math.round(age.getOrElse(card.age, 0d) * count - 1).toInt
    val ageUpdated: Map[Age, Double] = if (newAgeCount > 0) {
      age.mapValues(_ * count / newCount) + (card.age -> newAgeCount / newCount.toDouble)
    } else {
      age.mapValues(_ * count / newCount) - card.age
    }
    val profUpdated: Map[Profession, Double] = card.profession match {
      case Some(profVal) =>
        val newProfCount: Int = Math.round(profession.getOrElse(profVal, 0d) * count - 1).toInt
        if (newProfCount > 0) {
          profession.mapValues(_ * count / newCount) + (profVal -> newProfCount / newCount.toDouble)
        } else {
          profession.mapValues(_ * count / newCount) - profVal
        }
      case None =>
    }
    val raceUpdated: Map[Race, Double] = card.race match {
      case Some(raceVal) =>
        val newRaceCount: Int = Math.round(race.getOrElse(raceVal, 0d) * count - 1).toInt
        if (newRaceCount > 0) {
          race.mapValues(_ * count / newCount) + (raceVal -> newRaceCount / newCount.toDouble)
        } else {
          race.mapValues(_ * count / newCount) - raceVal
        }
      case None =>
    }
    Breakdown(newCount, ageUpdated, profUpdated, raceUpdated)
  }
}

object Breakdown {
  def apply(deck: Set[Card]): Breakdown = {
    val (ageCount, profCount, raceCount): (Map[Age, Int], Map[Profession, Int], Map[Race, Int]) =
      deck.foldLeft((Map[Age, Int](), Map[Profession, Int](), Map[Race, Int]())) {
        case ((age: Map[Age, Int], prof: Map[Profession, Int], race: Map[Race, Int]), card: Card) =>
          val ageUpdated: Map[Age, Int] = age + (card.age -> (1 + age.getOrElse(card.age, 0)))
          val profUpdated: Map[Profession, Int] = prof ++ card.profession.map(p => (p -> (1 + prof.getOrElse(p, 0)))).toMap
          val raceUpdated: Map[Race, Int] = race ++ card.race.map(r => (r -> (1 + race.getOrElse(r, 0)))).toMap
          (ageUpdated, profUpdated, raceUpdated)
      }
    Breakdown(deck.size, ageCount.mapValues(_ / deck.size.toDouble),
              profCount.mapValues(_ / deck.size.toDouble),
              raceCount.mapValues(_ / deck.size.toDouble))
  }
}

// Synergies can be with allies or against enemies. Synergies against enemies are called "counters".
// Negative synergies can also exist and are referred to as anti-synergies and weaknesses respectively.
sealed trait Synergy {
  def power: Breakdown => Double
}

case class AgeSynergy(basePower: Double, age: Age) extends Synergy {
  def power: Breakdown => Double =
    (breakdown: Breakdown) => breakdown.age.getOrElse(age, 0.0d) * basePower
}

case class Card(basePower: Double = 5.0,
                age: Age = Young,
                profession: Option[Profession] = None,
                race: Option[Race] = None,
                synergies: Set[Synergy],
                counters: Set[Synergy]) {
  def power: (Breakdown, Breakdown) => Double =
    (allyBreakdown: Breakdown, enemyBreakdown: Breakdown) => {
      basePower +
        synergies.foldLeft(0.0) { (pow, s) =>
          pow + s.power(allyBreakdown)
        } +
        counters.foldLeft(0.0) { (pow, c) =>
          pow + c.power(enemyBreakdown)
        }
    }
}
