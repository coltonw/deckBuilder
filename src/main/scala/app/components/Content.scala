package app.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._
import app.models._

object Content {
  val matchVal = Match(Set(Card(4d)), Set(Card(3d)))
  val component =
    ScalaComponent
      .builder[Unit]("Content")
      .renderStatic(
        <.div(MatchComponent(matchVal))
      )
      .build
  def apply() = component()
}
