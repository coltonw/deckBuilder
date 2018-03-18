package app.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._
import app.models._

object Content {
  val card = Card(4d)
  val component =
    ScalaComponent
      .builder[Unit]("Content")
      .renderStatic(
        <.div(CardComponent(card))
      )
      .build
  def apply() = component()
}
