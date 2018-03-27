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
        <.div(
          app.Styles.appMatch,
          DeckComponent(matchVal.myFinalDeck),
          ResultsComponent(matchVal),
          DeckComponent(matchVal.enemyFinalDeck)
        )
      })
      .build

  def apply(matchVal: Match) = component(matchVal)
}
