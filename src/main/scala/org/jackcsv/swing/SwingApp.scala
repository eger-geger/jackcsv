package org.jackcsv.swing

import java.util.ResourceBundle
import org.jackcsv.i10n.Localization
import scala.swing._

object SwingApp extends SimpleSwingApplication {

  override def top: Frame = new AppFrame with Localization {
    minimumSize = new Dimension(400, 400)
    preferredSize = new Dimension(400, 400)

    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
      title = bundle.getString("app.title")

      this.repaint()
    }
  }
}