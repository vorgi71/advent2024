package org.vorgi.advent2024.day2

import org.vorgi.org.vorgi.Utils
import kotlin.math.abs
import kotlin.math.sign

class Day2 {
  fun start() {
    val input1 = """8 6 4 4 1
                    |1 2 7 8 9
                    |9 7 6 2 1
                    |1 3 2 4 5
                    |7 6 4 2 1
                    |1 3 6 7 9""".trimMargin().split("\n")

    val result1 = checkSafety(input1)
    println(result1)

    check(result1 == 2)

    val input2 = Utils.readInput("day2")

    val result2 = checkSafety(input2)
    println(result2)

    check(result2 == 257)

    val result3 = checkSafety2(input1)

    println(result3)

    check(result3 == 4) { " $result3 expected 4" }

    val result4 = checkSafety2(input2)

    println(result4)
  }

  private fun checkSafety(input1: List<String>): Int {
    var saveReports = 0

    input1.forEach { line ->
      print("line: $line")
      val split = line.split(" ").map { it.toInt() }

      val isSafe = checkSafety(split)
      if (isSafe) {
        println(" save")
        saveReports += 1
      } else {
        println(" unsave")
      }
    }

    return saveReports
  }

  private fun checkSafety2(input: List<String>): Int {
    var saveReports = 0

    input.forEach { line ->
      print("line: $line")
      val split = line.split(" ").map { it.toInt() }

      val isSafe = checkSafety(split)
      if (isSafe) {
        println(" save")
        saveReports += 1
      } else {
        val variants = createVariations(split)
        if (variants.any { checkSafety(it) }) {
          println(" save (variant)")
          saveReports += 1
        }
        println(" unsave")
      }
    }

    return saveReports
  }

  private fun createVariations(split: List<Int>): List<List<Int>> {
    val returnValue = ArrayList<List<Int>>()

    for (index in split.indices) {
      val variant = split.toMutableList()
      variant.removeAt(index)
      returnValue.add(variant)
    }

    return returnValue
  }

  private fun checkSafety(
    split: List<Int>,
  ): Boolean {
    var dir1 = 0

    for (index in 0..<split.size - 1) {
      val val1 = split[index]
      val val2 = split[index + 1]

      val diff = abs(val1 - val2)
      if (diff !in 1..3) {
        return false
      }

      // checkDirectory
      if (dir1 == 0) {
        dir1 = sign((val2 - val1).toDouble()).toInt()
      } else {
        if (dir1 != sign((val2 - val1).toDouble()).toInt()) {
          return false
        }
      }
    }
    return true
  }
}


fun main() {
  Day2().start()
}