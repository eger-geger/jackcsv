package com.jackcsv.table

object NameGenerator {

  private var index = 1

  def generatedName[T](implicit typeTag:TypeTag[T]):String = {
    try{
      s"${classOf[T].getSimpleName} $index"
    } finally {
      index += 1
    }
  }

}
