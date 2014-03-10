package org.jackcsv

case class ViewModel[A <: AnyRef](model: A) {

  private var toStringDelegate: Function[A, String] = null

  override def toString = {
    if (toStringDelegate == null) {
      model.toString
    } else {
      toStringDelegate(model)
    }
  }

  override def equals(obj: scala.Any): Boolean = obj match {
    case ViewModel(o) => o == model
    case _ => false
  }
}

object ViewModel {

  private val _empty = new ViewModel(null) {
    override def toString = ""
  }

  def empty[A <: AnyRef] = _empty

  def apply[A <:AnyRef](a:A, toString: =>String):ViewModel[A] = {
    val viewModel = new ViewModel[A](a)
    viewModel.toStringDelegate = _ => toString
    viewModel
  }

  def apply[A <: AnyRef](a: A, toStringDelegate: Function[A, String] = null):ViewModel[A] = {
    val viewModel = new ViewModel[A](a)

    if (toStringDelegate != null) {
      viewModel.toStringDelegate = toStringDelegate
    }

    viewModel
  }

  def fromSeq[A <: AnyRef](seq: Seq[A], toStringDelegate: Function[A, String] = null) = {
    seq.map(ViewModel(_, toStringDelegate))
  }

}