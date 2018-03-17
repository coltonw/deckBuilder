package app

import app.components.Main

import org.scalajs.dom.document
import CssSettings._
import scalacss.ScalaCssReact._

object App {
  def main(args: Array[String]): Unit = {
    Styles.addToDocument()
    Main().renderIntoDOM(document.getElementById("root"))
  }
}
