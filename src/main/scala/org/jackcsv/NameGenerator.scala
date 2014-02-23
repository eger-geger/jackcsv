package org.jackcsv

import scala.reflect.ClassTag

object NameGenerator {

  private var index = 1

  def generatedName[T](implicit classTag: ClassTag[T]): String = {
    try {
      s"${classTag.runtimeClass.getSimpleName} $index"
    } finally {
      index += 1
    }
  }

}
