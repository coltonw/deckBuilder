package app.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

object Header {
  val component =
    ScalaComponent.builder[Unit]("Header")
      .renderStatic(<.div(
        app.Styles.appHeader,
        <.h2("Deck Builder")
      ))
      .build
  def apply() = component()
}
