package app.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import app.models._

import scalacss.ScalaCssReact._

object CardComponent {
  val component =
    ScalaComponent
      .builder[Card]("Card")
      .render_P(
        card =>
          <.div(
            app.Styles.appCard,
            <.div(card.basePower.toString),
            <.div(card.age.toString),
            <.div(card.profession.toString),
            <.div(card.race.toString),
            <.code(card.toString)
        )
      )
      .build

  def apply(card: Card) = component(card)
}
