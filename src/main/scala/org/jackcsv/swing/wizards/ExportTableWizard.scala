package org.jackcsv.swing.wizards

import org.jackcsv.swing.panels.{FileDialogMode, CSVChooserPanel, STableListPanel, WizardPanel}
import org.jackcsv.table.STable
import org.jackcsv.{Validation, TableController}

class ExportTableWizard extends WizardPanel with Validation {

  private var table: STable = null

  private val tableListPanel = new STableListPanel

  private val fileChooserPanel = new CSVChooserPanel(FileDialogMode.Save)

  this += tableListPanel
  this += fileChooserPanel

  controllers += {
    case `tableListPanel` =>
      require(tableListPanel.selectedTable.isDefined, "Table not selected")
      table = tableListPanel.selectedTable.get

    case `fileChooserPanel` =>
      TableController.exportTable(fileChooserPanel.selectedFile, fileChooserPanel.selectedPref, table)
  }

}
