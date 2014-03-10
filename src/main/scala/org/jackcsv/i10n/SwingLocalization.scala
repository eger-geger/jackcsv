package org.jackcsv.i10n

import java.util.ResourceBundle
import scala.swing.TabbedPane.Page
import scala.swing._

object SwingLocalization {

  def createLocalizedMenuItem(titlePropertyKey: String)(action: => Unit) = {
    new MenuItem(Action(titlePropertyKey)(action)) with AbstractButtonLocalization {
      override def localizationPropertyKey: String = titlePropertyKey
    }
  }

  def createLocalizedRadioMenuItem(titlePropertyKey: String)(actionBody: => Unit) = {
    new RadioMenuItem(titlePropertyKey) with AbstractButtonLocalization {
      override def localizationPropertyKey: String = titlePropertyKey

      this.action = Action(titlePropertyKey)(actionBody)
    }
  }

  def createLocalizedButton(titlePropertyKey: String)(actionBody: => Unit) = {
    new Button(Action(titlePropertyKey)(actionBody)) with AbstractButtonLocalization {
      override def localizationPropertyKey: String = titlePropertyKey
    }
  }

  def createLocalizedPage(titlePropertyKey: String, content: Component) = {
    new Page(Localization.localized(titlePropertyKey), content) with Localization {
      override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
        if (parent == null || !parent.pages.contains(this)) return

        title = bundle.getString(titlePropertyKey)

        parent.repaint()
      }
    }
  }


}
