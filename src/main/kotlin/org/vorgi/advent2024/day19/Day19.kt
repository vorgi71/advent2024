package org.vorgi.org.vorgi.advent2024.day19

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.memoize

class Day19 {
  fun start() {
    val input1 = Utils.readInput("day19_1")

    val result1 = countTowels(input1)

    println("result1 = ${result1}")

    check(result1 == 6L)

    val input2 = Utils.readInput("day19_2")
    val result2 = countTowels(input2)
    println("result2 = $result2")

    check(result2 == 267L)

    val result3 = countTowels(input1, false)
    println("result3 = ${result3}")

    check(result3 == 16L)

    val result4 = countTowels(input2, false)
    println("result4 = ${result4}")
  }

  private fun countTowels(input: List<String>, shortcut: Boolean = true): Long {
    val availableTowels = input[0].split(",").map { it.trim() }

    val requestedCombinations = input.subList(2, input.size)

    println("$availableTowels \n $requestedCombinations")

    if (shortcut) {
      return requestedCombinations.sumOf { combination ->
        checkCombination(combination, availableTowels, shortcut)
      }
    }

    memRecurseCombinations = ::recurseCombinations.memoize()

    val sum = requestedCombinations.sumOf { combination ->
      println("checking $combination")
      memRecurseCombinations(CombinationParameters(combination, availableTowels))
    }

    return sum

  }

  data class CombinationParameters(
    val combination: String,
    val availableTowels: List<String>
  )

  var memRecurseCombinations=::recurseCombinations.memoize()

  fun recurseCombinations(combinationParameters: CombinationParameters?): Long {
    if(combinationParameters==null)
      return 0L
    var sum = 0L
    for (towel in combinationParameters.availableTowels) {
      if (combinationParameters.combination.isBlank()) {
        return 1L
      }
      if (combinationParameters.combination.startsWith(towel)) {
        sum += memRecurseCombinations(
          CombinationParameters(
            combinationParameters.combination.substring(towel.length),
            combinationParameters.availableTowels
          )
        )
      }
    }

    memRecurseCombinations(null)

    return sum
  }

  private fun checkCombination(combination: String, availableTowels: List<String>, shortcut: Boolean = true): Long {
    println("checking $combination")

    val stack = if (shortcut) {
      mutableSetOf(0)
    } else {
      mutableListOf(0)
    }

    var combinations = 0L

    while (stack.isNotEmpty()) {
      val towelStackEntries = stack.toList()
      stack.clear()
      for (towelStackData in towelStackEntries) {
        for (towel in availableTowels) {
          if (combination.indexOf(towel, startIndex = towelStackData) == towelStackData) {
            val newTowelStackData = towelStackData + towel.length
            if (newTowelStackData == combination.length) {
              combinations++
              if (shortcut)
                break // shortcut
            } else {
              stack += newTowelStackData
            }
          }
        }
        if (shortcut && combinations > 0) {
          break // shortcut
        }
      }
      if (shortcut && combinations > 0) { // shortcut
        break
      }
    }

    return combinations
  }
}

fun main() {
  Day19().start()
}