package org.jackcsv

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class NavigableBuffer[A](private val _seq: Seq[A])
  extends mutable.Buffer[A] with mutable.Seq[A] with PartialFunction[Int, A] {

  private val listBuffer = new ListBuffer[A] ++= _seq

  private var currentIndex = 0

  def this() = this(Nil)

  def moveNext() { currentIndex += 1 }

  def movePrev() { currentIndex -= 1 }

  def hasNext = currentIndex + 1 < listBuffer.size

  def hasPrev = currentIndex > 0

  def curr: Option[A] = lift(currentIndex)

  def next: Option[A] = { moveNext(); lift(currentIndex) }

  def prev: Option[A] = { movePrev(); lift(currentIndex) }

  override def length: Int = listBuffer.length

  override def iterator: Iterator[A] = listBuffer.iterator

  override def apply(n: Int): A = listBuffer(n)

  override def update(n: Int, newelem: A): Unit = listBuffer.update(n, newelem)

  override def +=(elem: A): this.type = { listBuffer += elem; this }

  override def +=:(elem: A): this.type = { listBuffer.+=:(elem); this }

  override def insertAll(n: Int, elems: Traversable[A]): Unit = listBuffer.insertAll(n, elems)

  override def remove(n: Int): A = listBuffer.remove(n)

  override def clear(): Unit = listBuffer.clear()

}
