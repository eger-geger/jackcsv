package org.jackcsv.swing

import org.jackcsv.Localization
import org.jackcsv.swing.panels._
import org.jackcsv.swing.utils.MenuBuilder._
import org.jackcsv.swing.wizards._
import org.jackcsv.table.STable
import scala.swing.TabbedPane.Page
import scala.swing._
import java.util.Locale

object SwingApp extends SimpleSwingApplication with Localization {

  AppFrame.title = l("app.title")
  AppFrame.minimumSize = new Dimension(400, 400)
  AppFrame.preferredSize = new Dimension(400, 400)

  override def top: Frame = AppFrame

}

object AppFrame extends MainFrame with Localization {

  private object TabbedPane extends XTabbedPane {
    reactions += {
      case e@(WizardCanceled(_) | WizardFinished(_)) =>
        pages.find(_.content == e.asInstanceOf[WizardPanelEvent].wizardPanel).foreach(pages -= _)
    }

    def +=(title: String, component: Component) {
      pages += new Page(title, component)

      listenTo(component)
    }
  }

  contents = TabbedPane

  menuBar = new MenuBar {
    _contents += new Menu(l("menu.tables")) {
      _contents += l("menu.tables.show") --> {
        TabbedPane +=(l("page.show_table"), new ShowTableWizard(displayTable))
      }

      _contents += l("menu.tables.join") --> {
        TabbedPane +=(l("page.join_tables"), new JoinTablesWizard(displayTable))
      }

      _contents += new Menu(l("menu.tables.import")) {
        _contents += l("menu.tables.import.csv") --> {
          TabbedPane +=(l("page.import_table"), new ImportTableWizard(displayTable))
        }

        _contents += l("menu.tables.import.text") --> {
          TabbedPane +=(l("page.import_table"), new InputTableWizard(displayTable))
        }
      }

      _contents += l("menu.tables.export") --> {
        TabbedPane +=(l("page.export_table"), new ExportTableWizard)
      }
    }

    _contents += new Menu(l("menu.language")){
      _contents ++= new ButtonGroup(
        new RadioMenuItem(l("menu.language.en")){
          Localization.locale = Locale.ENGLISH
          AppFrame.this.repaint()
        },

        new RadioMenuItem(l("menu.language.ru")) {
          Localization.locale = Locale.forLanguageTag("ru")
          AppFrame.this.repaint()
        }
      ).buttons
    }
  }

  private def displayTable(table: STable) {
    TabbedPane.pages += new Page(table.name, new TablePanel(table))
  }
}
