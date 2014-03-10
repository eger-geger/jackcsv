package org.jackcsv.swing.wizards

import org.jackcsv.Validation
import org.jackcsv.swing.panels.{STableListPanel, WizardPanel}
import org.jackcsv.table.STable
import org.jackcsv.i10n.Localization

class ShowTableWizard(onFinish: STable => Unit) extends WizardPanel with Validation with Localization {

  private val tableListPanel = new STableListPanel

  this += tableListPanel

  controllers += {
    case `tableListPanel` =>
      require(tableListPanel.selectedTable.isDefined, Localization.localized("errors.table_not_selected"))

      onFinish(tableListPanel.selectedTable.get)
  }

}
