package org.jackcsv.i10n

import java.util.{Locale, ResourceBundle}
import scala.collection.mutable.ListBuffer
import scala.ref.WeakReference

trait Localization {

  Localization.localizedObjects += WeakReference(this)

  updateLocalizedStrings(Localization.resourceBundle)

  def updateLocalizedStrings(bundle:ResourceBundle)

}

object Localization {

  private var _resourceBundle:ResourceBundle = ResourceBundle.getBundle("i10n.messages", Locale.getDefault)

  private var _currentLocale:Locale = Locale.getDefault

  private val localizedObjects = new ListBuffer[WeakReference[Localization]]

  def resourceBundle = _resourceBundle

  def currentLocale = _currentLocale

  def localized(key:String) = resourceBundle.getString(key)

  def currentLocale_=(locale:Locale){
    if(_currentLocale == locale) return

    _currentLocale = locale

    _resourceBundle = ResourceBundle.getBundle("i10n.messages", locale)

    localizedObjects.toList.foreach {
      case WeakReference(o) => o updateLocalizedStrings _resourceBundle
      case any => localizedObjects -= any
    }
  }
}