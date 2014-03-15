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
import scala.collection.mutable.ListBuffer

class AppFrame extends MainFrame {

  private val tables = new ListBuffer[STable]

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
        tabbedPane += SwingLocalization.createLocalizedPage("page.show_table", new ShowTableWizard(tables, displayTable))
      }

      _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.join") {
        tabbedPane += SwingLocalization.createLocalizedPage("page.join_tables", new JoinTablesWizard(tables, nTable =>{
          tables += nTable; displayTable(nTable)
        }))
      }

      _contents += new Menu(Localization.localized("menu.tables.import")) with AbstractButtonLocalization {
        override def localizationPropertyKey: String = "menu.tables.import"

        _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.import.csv") {
          tabbedPane += SwingLocalization.createLocalizedPage("page.import_table", new ImportTableWizard(sTable => {
            tables += sTable; displayTable(sTable)
          }))
        }

        _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.import.text") {
          tabbedPane += SwingLocalization.createLocalizedPage("page.import_table", new InputTableWizard(sTable => {
            tables += sTable; displayTable(sTable)
          }))
        }
      }

      _contents += SwingLocalization.createLocalizedMenuItem("menu.tables.export") {
        tabbedPane += SwingLocalization.createLocalizedPage("page.export_table", new ExportTableWizard(tables))
      }
    }

    _contents += new Menu(Localization.localized("menu.language")) with AbstractButtonLocalization {
      override def localizationPropertyKey: String = "menu.language"

      private val enRadioButton = SwingLocalization.createLocalizedRadioMenuItem("menu.language.en") {
        Localization.currentLocale = Locale.ENGLISH
      }

      private val ruRadioButton = SwingLocalization.createLocalizedRadioMenuItem("menu.language.ru") {
        Localization.currentLocale = Locale.forLanguageTag("ru-RU")
      }

      enRadioButton.selected = Localization.currentLocale.getLanguage == "en"
      ruRadioButton.selected = Localization.currentLocale.getLanguage == "ru"

      _contents ++= new ButtonGroup(enRadioButton, ruRadioButton).buttons
    }
  }

  private def displayTable(table: STable) {
    tabbedPane.pages += new Page(table.name, new TablePanel(table))
  }

}
