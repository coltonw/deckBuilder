package app.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._
import app.models._

object Content {
  val deckSize = 20
  val matchVal = Match(1.to(deckSize).map(_ => Card.random).toSet, 1.to(deckSize).map(_ => Card.random).toSet)
  val component =
    ScalaComponent
      .builder[Unit]("Content")
      .renderStatic(
          MatchComponent(matchVal)
      )
      .build
  def apply() = component()
}
