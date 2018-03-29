package app.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._
import app.models._

object Content {
  val deckSize = 20
  val randomGame = RandomGame(deckSize)
  val component =
    ScalaComponent
      .builder[Unit]("Content")
      .renderStatic(
          RandomGameComponent(randomGame)
      )
      .build
  def apply() = component()
}
