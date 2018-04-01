package app.components

import app.models._
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scalacss.ScalaCssReact._

object ResultsComponent {
  val component =
    ScalaComponent
      .builder[Match]("Results")
      .render_P(matchVal => {
        val winDiv = if (matchVal.win) { <.div("YOU WIN!!!") } else { <.div("YOU SUCK!!!") }
        <.div(
          winDiv,
          <.div(f"My score: ${matchVal.myScores.getOrElse(matchVal.finalAge, 0d)}%.1f"),
          <.div(f"Enemy Score: ${matchVal.enemyScores.getOrElse(matchVal.finalAge, 0d)}%.1f")
        )
      })
      .build

  def apply(matchVal: Match) = component(matchVal)
}
