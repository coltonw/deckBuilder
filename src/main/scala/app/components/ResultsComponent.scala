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
          <.code(matchVal.myBreakdown.profession.toString),
          <.br,
          <.code(matchVal.enemyBreakdown.profession.toString),
          <.div("My score:", matchVal.myScore),
          <.div("Enemy Score:", matchVal.enemyScore)
        )
      })
      .build

  def apply(matchVal: Match) = component(matchVal)
}