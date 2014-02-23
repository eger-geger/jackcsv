package org.jackcsv.swing

import akka.actor.ActorSystem
import org.jackcsv.swing.panels.{WizardPanelEvent, WizardFinished, WizardCanceled}
import org.jackcsv.swing.utils.MenuBuilder._
import scala.swing._

object SwingApp extends SimpleSwingApplication {

  AppFrame.title = "Swing Table Utility"
  AppFrame.minimumSize = new Dimension(400, 400)
  AppFrame.preferredSize = new Dimension(400, 400)

  override def top: Frame = AppFrame

}

object AppFrame extends MainFrame {

  private implicit val actorSystem = ActorSystem("UI")

  private object TabbedPane extends XTabbedPane {
    reactions += {
      case e @ (WizardCanceled(_) | WizardFinished(_)) =>
        pages.find(_.content == e.asInstanceOf[WizardPanelEvent].wizardPanel).foreach(pages -= _)
    }
  }

  contents = TabbedPane

  menuBar = new MenuBar {
    _contents += new Menu("Tables") {
      _contents += "Combine" --> {}

      _contents += "Test" --> {}

      _contents += new Menu("Import") {
        _contents += "...from CSV" --> {}

        _contents += "...from text" --> {}
      }

      _contents += "Export to CSV" --> {}
    }
  }
}
