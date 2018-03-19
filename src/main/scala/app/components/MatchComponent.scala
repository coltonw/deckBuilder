package app.components

import app.models._
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scalacss.ScalaCssReact._

object MatchComponent {
  val component =
    ScalaComponent
      .builder[Match]("Match")
      .render_P(matchVal => {
        val winDiv = if (matchVal.win) { Some(<.div("YOU WIN!!!")) } else { None }
        <.div(
          app.Styles.appMatch,
          DeckComponent(matchVal.deck),
          winDiv,
          DeckComponent(matchVal.enemyDeck)
        )
      })
      .build

  def apply(matchVal: Match) = component(matchVal)
}
