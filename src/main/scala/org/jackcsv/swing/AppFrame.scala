package org.jackcsv.swing

import java.util.Locale
import org.jackcsv.i10n.{Localization, SwingLocalization, AbstractButtonLocalization}
import org.jackcsv.swing.panels.WizardCanceled
import org.jackcsv.swing.panels.WizardFinished
import org.jackcsv.swing.panels._
import org.jackcsv.swing.wizards._
import org.jackcsv.table.STable
import scala.swing.TabbedPane.Page
import scala.swing._

class AppFrame extends MainFrame {

  private val tabbedPane = new XTabbedPane {
    reactions += {
      case e@(WizardCanceled(_) | WizardFinished(_)) =>
        pages.find(_.content == e.asInstanceOf[WizardPanelEvent].wizardPanel).foreach(pages -= _)
    }

    def +=(page: Page) {
      pages += page

      listenTo(page.content)
    }
  }

  contents = tabbedPane

  menuBar = new MenuBar {
    _contents += new Menu(Localization.localized("menu.tables")) with AbstractButtonLocalization {
      override def localizationPropertyKey: String = "menu.tables"

      _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.show") {
        tabbedPane += SwingLocalization.createLocalizedPage("page.show_table", new ShowTableWizard(displayTable))
      }

      _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.join") {
        tabbedPane += SwingLocalization.createLocalizedPage("page.join_tables", new JoinTablesWizard(displayTable))
      }

      _contents += new Menu(Localization.localized("menu.tables.import")) with AbstractButtonLocalization {
        override def localizationPropertyKey: String = "menu.tables.import"

        _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.import.csv") {
          tabbedPane += SwingLocalization.createLocalizedPage("page.import_table", new ImportTableWizard(displayTable))
        }

        _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.import.text") {
          tabbedPane += SwingLocalization.createLocalizedPage("page.import_table", new InputTableWizard(displayTable))
        }
      }

      _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.export") {
        tabbedPane += SwingLocalization.createLocalizedPage("page.export_table", new ExportTableWizard)
      }
    }

    _contents += new Menu(Localization.localized("menu.language")) with AbstractButtonLocalization {
      override def localizationPropertyKey: String = "menu.language"

      _contents ++= new ButtonGroup(
        SwingLocalization.createLocalizedRadioMenuItem("menu.language.en") {
          Localization.currentLocale = Locale.ENGLISH
        },

        SwingLocalization.createLocalizedRadioMenuItem("menu.language.ru") {
          Localization.currentLocale = Locale.forLanguageTag("ru-RU")
        }
      ).buttons
    }
  }

  private def displayTable(table: STable) {
    tabbedPane.pages += new Page(table.name, new TablePanel(table))
  }

}
