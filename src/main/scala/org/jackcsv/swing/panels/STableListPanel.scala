package org.jackcsv.swing.panels

import org.jackcsv.table.STable
import scala.swing.{ScrollPane, Label, ListView, GridBagPanel}
import scala.swing.event.SelectionChanged
import org.jackcsv.i10n.Localization
import java.util.ResourceBundle
import org.jackcsv.ViewModel

class STableListPanel(private val tables:Seq[STable]) extends GridBagPanel {

  private val selectedTableLabel = new Label with Localization {
    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
      text = bundle.getString("stable_list_panel.selected_table_label")
    }
  }
  
  private val selectedTableNameLabel = new Label

  private val tableListView = new ListView(ViewModel.fromSeq[STable](tables, _.name)){
    selection.intervalMode = ListView.IntervalMode.Single
  }

  tableListView.selection.reactions += {
    case SelectionChanged(_) =>
      selectedTable.foreach(t => selectedTableNameLabel.text = t.name)
  }

  add(selectedTableLabel, new Constraints{
    grid = (0, 0)
    weightx = 1
    weighty = 1
  })
  
  add(selectedTableNameLabel, new Constraints{
    grid = (1, 0)
    weightx = 10
    weighty = 1
  })

  add(new ScrollPane(tableListView), new Constraints{
    grid = (0, 1)
    gridwidth = 2
    weightx = 1
    weighty = 10
    fill = GridBagPanel.Fill.Both
  })

  def selectedTable:Option[STable] = {
    tableListView.selection.items.headOption match {
      case Some(ViewModel(table)) => Some(table)
      case None => None
    }
  }

}
