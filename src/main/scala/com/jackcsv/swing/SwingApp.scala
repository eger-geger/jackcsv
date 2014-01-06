package com.jackcsv.swing

import scala.swing._
import javax.swing.{JPopupMenu, AbstractAction}
import java.awt.event.ActionEvent
import org.supercsv.prefs.CsvPreference
import java.io.File
import com.jackcsv.swing.wizards.{WizardCanceled, WizardFinished, WizardPanel}
import scala.swing.TabbedPane.Page
import com.jackcsv.table.STable

object SwingApp extends SimpleSwingApplication {

  AppFrame.title = "Swing Table Utility"
  AppFrame.minimumSize = new Dimension(400, 400)
  AppFrame.preferredSize = new Dimension(400, 400)

  override def top: Frame = AppFrame

}

object AppFrame extends MainFrame {

  val tabbedPane = new TabbedPane {

    private object RemoveCurrentTabAction extends AbstractAction("Remove Current Tab") {
      override def actionPerformed(e: ActionEvent) {
        pages -= selection.page
      }
    }

    peer.setComponentPopupMenu(new JPopupMenu() {
      add(RemoveCurrentTabAction)
    })

    def addWizardPanelAsPage[A](wizardPanel: WizardPanel[A]) {
      val panelPage = new Page(wizardPanel.title, wizardPanel)

      wizardPanel.reactions += {
        case WizardFinished(_) => pages -= panelPage
        case WizardCanceled(_) => pages -= panelPage
      }

      pages += panelPage

      wizardPanel.repaint()

      AppFrame.this.listenTo(wizardPanel)
    }
  }


  reactions += {
    case WizardFinished(table: STable) => {
      tabbedPane.pages += new Page(table.name, new TablePanel(table))
    }
  }

  contents = tabbedPane

  menuBar = new MenuBar {
    _contents += new Menu("Tables") {
      _contents += createMenuItem("Combine") {}

      _contents += new Menu("Import") {
        _contents += createMenuItem("...from CSV") {}
        _contents += createMenuItem("...from text") {
          tabbedPane.addWizardPanelAsPage(new EnterTableWizardPanel)
        }
      }

      _contents += createMenuItem("Export to CSV") {}
    }

    def createMenuItem(title: String)(action: => Unit): MenuItem =
      new MenuItem(new Action(title) {
        def apply() = action
      })
  }

}

case class ImportTable(var file: File, var name: String, var preference: CsvPreference)

case class ExportTable(var file: File, var table: STable, var preference: CsvPreference)

