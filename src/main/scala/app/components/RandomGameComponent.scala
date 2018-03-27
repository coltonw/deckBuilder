package app.components

import app.models._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scalacss.ScalaCssReact._

object RandomGameComponent {
  case class State(chosenCards: Set[Card])

  class Backend($ : BackendScope[RandomGame, State]) {
    def chooseCard(card: Card): Callback = $.modState { state: State => State(state.chosenCards + card) }

    def render(game: RandomGame, s: State): VdomNode = {
      <.div(
        app.Styles.appMatch,
        <.div(
          app.Styles.appDeck,
          game.cardPool.map(c => CardComponent(c)).toVdomArray
        ),
        Some(ResultsComponent(Match(s.chosenCards, game.enemy))).filter(_ => s.chosenCards.size == game.deckSize),
        DeckComponent(game.enemy)
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
