package org.jackcsv.swing.wizards

import org.jackcsv.Validation
import org.jackcsv.swing.panels.{STableListPanel, WizardPanel}
import org.jackcsv.table.STable

class ShowTableWizard(onFinish: STable => Unit) extends WizardPanel with Validation {

  private val tableListPanel = new STableListPanel

  this += tableListPanel

  controllers += {
    case `tableListPanel` =>
      require(tableListPanel.selectedTable.isDefined, l("errors.table_not_selected"))

      onFinish(tableListPanel.selectedTable.get)
  }

}
