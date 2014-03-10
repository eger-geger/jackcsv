package org.jackcsv.swing.panels

import scala.swing._
import org.jackcsv.i10n.Localization
import java.util.ResourceBundle

class TableSourcePanel extends GridBagPanel {

  private val titleLabel = new Label with Localization {
    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
      text = bundle.getString("table_source_panel.title")
    }
  }

  private val contentTxt = new TextArea

  add(titleLabel, new Constraints {
    grid = (0, 0)
    weighty = 0
    weightx = 0
    insets = new Insets(5, 5, 5, 0)
    fill = GridBagPanel.Fill.Horizontal
  })

  add(new ScrollPane(contentTxt), new Constraints {
    grid = (0, 1)
    weighty = 1
    weightx = 1
    insets = new Insets(5, 5, 5, 5)
    fill = GridBagPanel.Fill.Both
  })

  def content = contentTxt.text

  def content_=(value: String) = contentTxt.text = value

}