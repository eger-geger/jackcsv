package com.jackcsv.swing.wizards

import scala.swing.Component

trait WizardStep {self:Component=>

  def component:Component = this

  def isFinished:Boolean

  def description:String

  def title:String

}
