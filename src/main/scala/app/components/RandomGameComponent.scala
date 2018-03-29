package app.components

import app.models._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scalacss.ScalaCssReact._

object RandomGameComponent {
  case class State(chosenCards: Set[Card])

  class Backend($ : BackendScope[RandomGame, State]) {
    def chooseCard(card: Card): Callback = $.modState { state: State =>
      State(state.chosenCards + card)
    }
    def removeCard(card: Card): Callback = $.modState { state: State =>
      State(state.chosenCards - card)
    }

    def render(p: RandomGame, s: State): VdomNode = {
      val matchVal: Option[Match] = if (s.chosenCards.size == p.deckSize) {
        Some(Match(s.chosenCards, p.enemy))
      } else {
        None
      }
      val enemyDeck = matchVal.map(m => DeckComponent(m.enemyFinalDeck)) getOrElse DeckComponent(p.enemy)
      <.div(
        app.Styles.appMatch,
        <.div(
          app.Styles.appDeck,
          Card
            .sort(p.cardPool)
            .map(c => CardComponent(CardComponent.Props(c, true, chooseCard, removeCard)))
            .toVdomArray
        ),
        matchVal.map(m => ResultsComponent(m)),
        Some(<.div(
          s"${p.deckSize - s.chosenCards.size} cards left to choose"
        )).filter(_ => matchVal.isEmpty),
        enemyDeck
      )
    }
  }

  val component =
    ScalaComponent
      .builder[RandomGame]("RandomGame")
      .initialState(State(Set.empty[Card]))
      .renderBackend[Backend]
      .build

  def apply(randomGame: RandomGame) = component(randomGame)
}
