package org.jackcsv.swing.panels

import org.jackcsv.table.{NTable, STable}
import scala.collection.mutable.ListBuffer
import scala.swing._

class NTableListPanel extends FlowPanel {

  private val tablePanels = new ListBuffer[NTablePanel]

  contents += new GridBagPanel {
    private val addButton = Button("Add Panel") {
      addTablePanel(new NTablePanel(STable.tables.toSeq))
    }

    add(addButton, new Constraints {
      grid = (0, 0)
      fill = GridBagPanel.Fill.Both
      insets = new Insets(10, 10, 10, 10)
    })

    border = NTablePanel.defaultBorder
    minimumSize = NTablePanel.defaultSize
    preferredSize = NTablePanel.defaultSize
  }

  private def addTablePanel(panel: NTablePanel) {
    tablePanels += panel

    contents.insert(contents.size - 1, panel)

    revalidate()
  }

  def tables = tablePanels.map(p => p.table).filter(t => t != null).toSeq

  def tables_=(value: Seq[NTable]) {
    tablePanels.foreach(p => contents -= p)
    tablePanels.clear()
    tablePanels ++= value.map(t => new NTablePanel(STable.tables.toSeq) {table = t})
    tablePanels.foreach(addTablePanel)
  }

}


