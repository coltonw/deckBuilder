package app

import CssSettings._

object Styles extends StyleSheet.Inline {
  import dsl._

  val mainFont = fontFace("mainFont")(
    _.src("local(Tahoma)").fontStretch.ultraCondensed.fontWeight._200
  )

  val appHeader = style(
    backgroundColor.rgb(34, 34, 34),
    height(50 px),
    padding(20 px),
    color.white
  )

  val appMatch = style(
    display.flex,
    flexDirection.row,
    justifyContent.spaceBetween,
    alignItems.flexStart,
    height(100 %%)
  )

  val appDeck = style(
    height(100 %%),
    overflowY.auto,
    flexGrow(1),
    flexShrink(1)
  )

  val appCard = style(
    fontSize.large,
    border(solid, gray, 2 px),
    backgroundColor.lightgray
  )

  val appCardUnselected = style(
    appCard,
    opacity(0.6)
  )

  val appAlchemist = style(
    borderColor.yellow,
    backgroundColor.lightyellow
  )

  val appBattleTech = style(
    borderColor.steelblue,
    backgroundColor.lightsteelblue
  )

  val appConjuror = style(
    borderColor.purple,
    backgroundColor.lavender
  )

  val appProphet = style(
    borderColor.red,
    backgroundColor.pink
  )

  val appWoodsman = style(
    borderColor.green,
    backgroundColor.lightgreen
  )

  val app = style(
    display.flex,
    flexDirection.column,
    textAlign.center,
    fontFamily(mainFont),
    justifyContent.flexStart,
    height(100 %%)
  )

  val body = style(
    margin(0 px),
    padding(0 px),
    height(100 %%),
    overflow.hidden
  )

  val html = style(body)
  val root = style(body)
}
