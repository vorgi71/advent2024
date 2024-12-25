package org.vorgi.org.vorgi.advent2024.day25

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.permutations

class Day25 {
  fun start() {
    val input1= Utils.readInput("day25_1")
    val result1 = countFittingKeyLockPairs(input1)

    println("result1 = ${result1}")

    check(result1==3)

    val input2 = Utils.readInput("day25_2")
    val result2 = countFittingKeyLockPairs(input2)
    println("result2 = ${result2}")
  }

  private fun countFittingKeyLockPairs(input: List<String>): Int {
    val (locks, keys) = parseInput(input)

    println("Locks:")
    locks.forEach {
      println(it)
    }

    println("Keys:")
    keys.forEach {
      println(it)
    }

    var sum=0
    for(lock in locks) {
      for(key in keys) {
        if(checkFit(lock,key)) {
          sum++
        }
      }
    }


    return sum
  }

  private fun checkFit(lock: MutableList<Int>, key: MutableList<Int>): Boolean {
    for(colIndex in lock.indices) {
      if(lock[colIndex]+key[colIndex]>5) {
        return false
      }
    }
    return true
  }

  private fun parseInput(input: List<String>): Pair<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>> {
    val locks = mutableListOf<MutableList<Int>>()
    val keys = mutableListOf<MutableList<Int>>()

    for (lineIndex in 0..<input.size step 8) {
      val subString = input.subList(lineIndex, lineIndex + 7)
      val charGrid = CharGrid(subString)
      var listToFill = keys
      if (input[lineIndex] == "#####") {
        listToFill = locks
      }

      val newList = mutableListOf<Int>()
      listToFill.add(newList)
      for (x in 0..<5) {
        var sum = 0
        for (y in 0..<charGrid.height) {
          if (charGrid.getAt(x, y) == '#') {
            sum++
          }
        }
        newList += (sum - 1)
      }

    }
    return Pair(locks, keys)
  }
}

fun main() {
  Day25().start()
}