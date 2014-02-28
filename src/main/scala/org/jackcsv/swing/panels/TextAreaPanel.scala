package org.jackcsv.swing.panels

import scala.swing._

class TextAreaPanel extends GridBagPanel {

  private val titleLabel = new Label

  private val contentTxt = new TextArea

  add(new Label("Table Source:"), new Constraints {
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

  def title = titleLabel.text

  def title_=(value: String) = titleLabel.text = value

  def content = contentTxt.text

  def content_=(value: String) = contentTxt.text = value

}