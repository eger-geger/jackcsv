package org.jackcsv

import java.util.{Locale, ResourceBundle}

trait Localization {

  def l(key:String): String = {
    Localization.localizedString(key)
  }

}

object Localization {

  private var _locale:Locale = Locale.ENGLISH

  private var _bundle:ResourceBundle = ResourceBundle.getBundle("i10n.messages", locale)

  def locale = _locale

  def locale_=(locale:Locale){
    _locale = locale

    _bundle = ResourceBundle.getBundle("i10n.messages", locale)
  }

  def localizedString(key:String) = _bundle.getString(key)
}