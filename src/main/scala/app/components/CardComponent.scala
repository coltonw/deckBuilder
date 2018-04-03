package app.components

import japgolly.scalajs.react.{Callback, _}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import app.models._

import scalacss.ScalaCssReact._
import scalacss.internal.Compose

object CardComponent {
  private implicit def compose = Compose.safe
  private val noOp             = (_: Card) => Callback {}
  case class Props(card: Card,
                   selectable: Boolean = false,
                   onSelect: Card => Callback = noOp,
                   onUnselect: Card => Callback = noOp,
                   desaturate: Boolean = false)

  class Backend($ : BackendScope[Props, Boolean]) {

    val cardClicked: ReactEvent => Callback =
      _ => {
        (for {
          p        <- $.props
          s        <- $.state
          callback <- if (!s) p.onSelect(p.card) else p.onUnselect(p.card)
        } yield callback) >> $.modState(s => !s)
      }

    def render(p: Props, s: Boolean): VdomNode = {
      val baseStyle = if ((p.selectable && !s) || p.desaturate) app.Styles.appCardUnselected else app.Styles.appCard
      val fullStyle =
        if (p.card.profession.contains(Alchemist)) {
          baseStyle + app.Styles.appAlchemist
        } else if (p.card.profession.contains(BattleTech)) {
          baseStyle + app.Styles.appBattleTech
        } else if (p.card.profession.contains(Conjuror)) {
          baseStyle + app.Styles.appConjuror
        } else if (p.card.profession.contains(Prophet)) {
          baseStyle + app.Styles.appProphet
        } else if (p.card.profession.contains(Woodsman)) {
          baseStyle + app.Styles.appWoodsman
        } else {
          baseStyle
        }
      <.div(
        ^.onClick ==> cardClicked,
        fullStyle,
        Some("SELECTED")
          .filter(_ => p.selectable && s),
        <.div(p.card.basePower.toString),
        <.div(p.card.age.toString),
        p.card.profession.map(p => <.div(p.toString)),
        p.card.race.map(r => <.div(r.toString)),
        p.card.score.map(s => <.div(f"$s%.1f")),
        // <.code(p.card.toString)
      )
    }
  }

  val component =
    ScalaComponent
      .builder[Props]("Card")
      .initialState(false)
      .renderBackend[Backend]
      .build

  def apply(card: Card, desaturate: Boolean = false) = component(Props(card, desaturate = desaturate))
  def apply(props: Props)                            = component(props)
}
