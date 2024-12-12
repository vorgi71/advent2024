package org.vorgi.org.vorgi.advent2024.day11

import org.vorgi.org.vorgi.pow
import kotlin.system.measureTimeMillis

class Day11 {
  fun start() {
    val timeMilis=measureTimeMillis {
      val input1 = "125 17"

      val result1 = blink(input1, 25)
      println(result1)

      check(result1 == 55312L)

      val input2 = "41078 18 7 0 4785508 535256 8154 447"
      val result2 = blink(input2, 25)

      check(result2 == 217443L)

      println(result2)

      val result4 = blink(input2, 75)

      println(result4)
    }
    println("$timeMilis ms")
  }

  val resultMap= mutableMapOf<Long,List<Long>>()

  fun processStone(stones:Stones): List<Stones> {
    val result=resultMap.computeIfAbsent(stones.value) { stoneValue ->
      if (stoneValue == 0L) {
        return@computeIfAbsent listOf(1L)
      } else if (countDigits(stoneValue) % 2 == 0) {
        val splitStone = splitLong(stoneValue)
        return@computeIfAbsent listOf(splitStone.first, splitStone.second)
      } else {
        return@computeIfAbsent listOf(stoneValue * 2024)
      }
    }

    val returnValue=List(result.size) { index ->
      return@List Stones(result[index],stones.count)
    }

    return returnValue
  }

  fun splitLong(n: Long): Pair<Long, Long> {
    if (n == 0L) return Pair(0, 0)  // Handle 0

    val numDigits = countDigits(n)
    val firstHalfDigits = (numDigits + 1) / 2 // Integer division for correct rounding
    val secondHalfDigits:Long = (numDigits - firstHalfDigits).toLong()
    val divisor = 10L.pow(secondHalfDigits)

    val firstHalf = n / divisor
    val secondHalf = n % divisor

    return Pair(firstHalf, secondHalf)
  }

  fun countDigits(n: Long): Int {
    if (n == 0L) return 1
    var num = if (n < 0) -n else n
    var count = 0
    while (num > 0) {
      num /= 10
      count++
    }
    return count
  }

  data class Stones(val value:Long, var count:Long)

  private fun blink(input: String, iterations: Int): Long {
    var stonesList = input.split(" ").toList().map {Stones(it.toLong(),1L) }

    for(i in 1..iterations) {
      val newStones= mutableListOf<Stones>()

      for(stone in stonesList) {
        newStones.addAll(processStone(stone))
      }

      stonesList=optimizeStonesList(newStones)
    }

    return countStones(stonesList)
  }

  private fun optimizeStonesList(stonesList: List<Day11.Stones>): List<Day11.Stones> {
    val newStonesList= mutableListOf<Stones>()
    for(stones in stonesList) {
      val sameStone=newStonesList.find { it.value==stones.value }
      if(sameStone!=null) {
        sameStone.count+=stones.count
      } else {
        newStonesList.add(stones)
      }
    }
    return newStonesList
  }

  private fun countStones(stonesList: List<Stones>): Long {
    var sum=0L
    for(stones in stonesList) {
      sum+=stones.count
    }
    return sum
  }
}

fun main() {
  Day11().start()
}