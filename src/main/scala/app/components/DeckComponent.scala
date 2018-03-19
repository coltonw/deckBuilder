package app.components

import app.models._
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scalacss.ScalaCssReact._

object DeckComponent {
  val component =
    ScalaComponent
      .builder[Set[Card]]("Deck")
      .render_P(
        deck =>
          <.div(
            app.Styles.appDeck,
            deck.map(c => CardComponent(c)).toVdomArray
          )
      )
      .build

  def apply(deck: Set[Card]) = component(deck)
}
