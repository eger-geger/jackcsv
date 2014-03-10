package org.jackcsv.swing.wizards

import org.jackcsv.swing.panels.{FileDialogMode, CSVChooserPanel, STableListPanel, WizardPanel}
import org.jackcsv.table.STable
import org.jackcsv.{Validation, TableController}
import org.jackcsv.i10n.Localization

class ExportTableWizard extends WizardPanel with Validation with Localization {

  private var table: STable = null

  private val tableListPanel = new STableListPanel

  private val fileChooserPanel = new CSVChooserPanel(FileDialogMode.Save)

  this += tableListPanel
  this += fileChooserPanel

  controllers += {
    case `tableListPanel` =>
      require(tableListPanel.selectedTable.isDefined, Localization.localized("errors.table_not_selected"))
      table = tableListPanel.selectedTable.get

    case `fileChooserPanel` =>
      TableController.exportTable(fileChooserPanel.selectedFile, fileChooserPanel.selectedPref, table)
  }

}
