package org.jackcsv.swing.panels

import java.awt.Insets
import scala.swing.{Label, TextField, GridBagPanel}

class TextFieldPanel extends GridBagPanel {

  private val contentTxt = new TextField

  private val titleLabel = new Label

  add(titleLabel, new Constraints {
    grid = (0, 0)
    weightx = 0
    weighty = 1
    insets = new Insets(5, 5, 5, 0)
  })

  add(contentTxt, new Constraints {
    grid = (1, 0)
    weighty = 1
    weightx = 1
    insets = new Insets(5, 0, 5, 5)
    fill = GridBagPanel.Fill.Horizontal
  })

  def title = titleLabel.text
  def title_=(value: String) = titleLabel.text = value

  def content = contentTxt.text
  def content_=(value: String) = contentTxt.text = value

}