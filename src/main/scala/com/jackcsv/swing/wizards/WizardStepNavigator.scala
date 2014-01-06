package com.jackcsv.swing.wizards

trait WizardStepNavigator {self:Seq[WizardStep]=>

  private var currIndex = 0

  def current = this(currIndex)

  def moveNext() = currIndex += 1

  def movePrev() = currIndex -= 1

  def hasNext = currIndex < size

  def hasPrev = currIndex > 0

  def isFinished = currIndex >= size

  def isCanceled = currIndex < 0
}
