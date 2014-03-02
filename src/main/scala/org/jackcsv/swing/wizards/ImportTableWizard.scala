package org.jackcsv.swing.wizards

import org.jackcsv.swing.panels._
import org.jackcsv.table.STable
import org.jackcsv.TableController

class ImportTableWizard(onFinish:STable => Unit) extends WizardPanel {

  private var table:STable = null

  private val fileChooserPanel = new CSVChooserPanel(FileDialogMode.Open)

  private val tableNamePanel = new TextFieldPanel {
    title = "Table Name:"
  }

  this += fileChooserPanel
  this += tableNamePanel

  reactions += {
    case ComponentLoaded(c:TextFieldPanel) =>
      c.content = table.name
  }

  controllers += {
    case `fileChooserPanel` =>
      table = TableController.importTable(fileChooserPanel.selectedFile, fileChooserPanel.selectedPref)

    case `tableNamePanel` =>
      TableController.renameTable(tableNamePanel.content, table)
      onFinish(table)
  }
}
