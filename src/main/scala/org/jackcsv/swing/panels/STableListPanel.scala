package org.jackcsv.swing.panels

import org.jackcsv.table.STable
import scala.swing.{ScrollPane, Label, ListView, GridBagPanel}
import scala.swing.event.SelectionChanged

class STableListPanel extends GridBagPanel {

  private val selectedTableLabel = new Label
  
  private val tableListView = new ListView[STable](STable.tables.toSeq){
    selection.intervalMode = ListView.IntervalMode.Single
  }

  tableListView.selection.reactions += {
    case SelectionChanged(_) =>
      selectedTable.foreach(t => selectedTableLabel.text = t.name)
  }

  add(new Label("Selected Table:"), new Constraints{
    grid = (0, 0)
    weightx = 1
    weighty = 0
  })
  
  add(selectedTableLabel, new Constraints{
    grid = (1, 0)
    weightx = 10
    weighty = 0
  })

  add(new ScrollPane(tableListView), new Constraints{
    grid = (0, 1)
    gridwidth = 2
    weightx = 1
    weighty = 10
    fill = GridBagPanel.Fill.Both
  })

  def selectedTable:Option[STable] = tableListView.selection.items.headOption

}
