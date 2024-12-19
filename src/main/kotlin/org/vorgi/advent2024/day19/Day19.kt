package org.vorgi.org.vorgi.advent2024.day19

import org.vorgi.org.vorgi.Utils

class Day19 {
  fun start() {
    val input1= Utils.readInput("day19_1")

    val result1 = countTowels(input1)

    println("result1 = ${result1}")

    check(result1==6)

    val input2 = Utils.readInput("day19_2")
    val result2 = countTowels(input2)
    println("result2 = $result2")
  }

  private fun countTowels(input: List<String>): Int {
    val availableTowels=input[0].split(",").map { it.trim() }

    val requestedCombinations=input.subList(2,input.size)

    println("$availableTowels \n $requestedCombinations")

    val sum= requestedCombinations.sumOf { combination ->
      val result=if(checkCombination(combination,availableTowels)==0) 0 else 1
      result
    }

    return sum

  }

  private fun checkCombination(combination: String, availableTowels: List<String>) : Int {
    println("checking $combination")

    val stack= mutableSetOf(0)

    var combinations=0

    while(stack.isNotEmpty()){
      val towelStackEntries=stack.toList()
      stack.clear()
      for(towelStackData in towelStackEntries) {
        for(towel in availableTowels) {
          if(combination.indexOf(towel, startIndex = towelStackData)==towelStackData) {
            val newTowelStackData=towelStackData+towel.length
            if(newTowelStackData==combination.length) {
              combinations++
              break // shortcut
            } else {
              stack += newTowelStackData
            }
          }
        }
        if(combinations>0) {
          break // shortcut
        }
      }
      if(combinations>0) { // shortcut
        break
      }
    }

    println("$combination $combinations")

    return combinations
  }
}

fun main() {
  Day19().start()
}