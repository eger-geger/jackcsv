package org.jackcsv.i10n

import java.util.ResourceBundle
import scala.swing.AbstractButton
import org.jackcsv.Logger


trait AbstractButtonLocalization extends Localization {
  self: AbstractButton =>

  def localizationPropertyKey: String

  override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
    self.text = bundle.getString(localizationPropertyKey)

    self.repaint()
  }
}
