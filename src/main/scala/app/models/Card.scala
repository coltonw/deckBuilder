package app.models

import scala.util.Random

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
sealed trait Profession {
  val beats: Profession
}

case object Alchemists extends Profession {
  val beats = Woodsmen
}
case object Techs extends Profession {
  val beats = Alchemists
}
case object Conjurors extends Profession {
  val beats = Techs
}
case object Prophets extends Profession {
  val beats = Conjurors
}
case object Woodsmen extends Profession {
  val beats = Prophets
}

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
    val newCount         = count - 1
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
        profession.mapValues(_ * count / newCount)
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
        race.mapValues(_ * count / newCount)
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
          val profUpdated: Map[Profession, Int] = prof ++ card.profession
            .map(p => (p -> (1 + prof.getOrElse(p, 0))))
            .toMap
          val raceUpdated: Map[Race, Int] = race ++ card.race.map(r => (r -> (1 + race.getOrElse(r, 0)))).toMap
          (ageUpdated, profUpdated, raceUpdated)
      }
    Breakdown(deck.size,
              ageCount.mapValues(_ / deck.size.toDouble),
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
                synergies: Set[Synergy] = Set.empty,
                counters: Set[Synergy] = Set.empty,
                score: Option[Double] = None) {
  def power: (Breakdown, Breakdown) => Double =
    (allyBreakdown: Breakdown, enemyBreakdown: Breakdown) => {
      val professionMulti = profession match {
        case Some(prof) =>
          1.0 + 1.0 * enemyBreakdown.profession.getOrElse(prof.beats, 0.0)
        case None =>
          1.0
      }
      (basePower +
        synergies.foldLeft(0.0) { (pow, s) =>
          pow + s.power(allyBreakdown)
        } +
        counters.foldLeft(0.0) { (pow, c) =>
          pow + c.power(enemyBreakdown)
        }) * professionMulti
    }
}

object Card {
  val rand = new Random()
  def randomOption[A](list: Vector[A], chanceOfNone: Double = 0.0): Option[A] = {
    val randNum = rand.nextDouble()
    if (1.0 - randNum < chanceOfNone) {
      None
    } else {
      var i = 0
      while (i < list.size - 1 && randNum > (1 - chanceOfNone) / list.size.toDouble * (i + 1)) {
        i += 1
      }
      Some(list(i))
    }
  }
  def random: Card = {
    val randForAge = rand.nextDouble()
    val (age, agePower) = if (randForAge < 0.3) {
      Young -> 10
    } else if (randForAge < 0.7) {
      Adult -> 6
    } else if (randForAge < 0.9) {
      Old -> 3
    } else {
      Ancient -> 0
    }
    val profession = randomOption[Profession](
      Vector(Alchemists, Techs, Conjurors, Prophets, Woodsmen),
      0.1
    )
    val race = randomOption[Race](
      Vector(Human, Beastman, Undead)
    )

    Card(
      basePower = rand.nextInt(6) + rand.nextInt(6) + 6 - agePower,
      age = age,
      profession = profession,
      race = race
    )
  }
}
