package org.vorgi.org.vorgi

import java.io.File


object Utils {
  fun readInput(name: String) = File("src/test/resources", "$name.txt")
    .readLines()

  fun readInputAsText(name: String) = File("src/test/resources", "$name.txt")
    .readText()
}
