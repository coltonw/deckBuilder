package app.components

import japgolly.scalajs.react.{Callback, _}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import app.models._
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

import scalacss.ScalaCssReact._
import scalacss.internal.Compose

object CardComponent {
  private implicit def compose = Compose.safe
  private val noOp = (_: Card) => Callback {}
  case class Props(card: Card,
                   selectable: Boolean = false,
                   onSelect: Card => Callback = noOp,
                   onUnselect: Card => Callback = noOp)

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
      val baseStyle = if (p.selectable && !s) app.Styles.appCardUnselected else app.Styles.appCard
      val fullStyle =
        if (p.card.profession.contains(Alchemists)) {
          baseStyle + app.Styles.appAlchemists
        } else if (p.card.profession.contains(Techs)) {
          baseStyle + app.Styles.appTechs
        } else if (p.card.profession.contains(Conjurors)) {
          baseStyle + app.Styles.appConjurors
        } else if (p.card.profession.contains(Prophets)) {
          baseStyle + app.Styles.appProphets
        } else if (p.card.profession.contains(Woodsmen)) {
          baseStyle + app.Styles.appWoodsmen
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
        p.card.score.map(s => <.div(s)),
        <.code(p.card.toString)
      )
    }
  }

  val component =
    ScalaComponent
      .builder[Props]("Card")
      .initialState(false)
      .renderBackend[Backend]
      .build

  def apply(card: Card)   = component(Props(card))
  def apply(props: Props) = component(props)
}
