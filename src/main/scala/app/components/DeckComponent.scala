package app.components

import app.models._
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scalacss.ScalaCssReact._

object DeckComponent {
  case class Props(main: Set[Card], unused: Set[Card])
  val component =
    ScalaComponent
      .builder[Props]("Deck")
      .render_P(
        p =>
          <.div(
            app.Styles.appDeck,
            Card.sort(p.main).map(c => CardComponent(c)).toVdomArray,
            Card.sort(p.unused).map(c => CardComponent(c, desaturate = true)).toVdomArray,
          )
      )
      .build

  def apply(deck: Set[Card], unusedCards: Set[Card] = Set.empty) = component(Props(deck, unusedCards))
}
