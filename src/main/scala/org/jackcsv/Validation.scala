package org.jackcsv


trait Validation {

  /**
   * Throws IllegalArgumentException if given condition yields to false
   *
   * @param requirement condition
   * @param message exception message
   * @throws IllegalArgumentException
   */
  def require(requirement:Boolean, message: =>String){
    if(!requirement){
      throw new IllegalArgumentException(message)
    }
  }

}
