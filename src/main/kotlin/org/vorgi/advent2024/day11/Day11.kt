package org.vorgi.org.vorgi.advent2024.day11

import kotlin.math.pow

class Day11 {
  fun start() {
    val input1="125 17"

    val result1=blink(input1,25)
    println(result1)

    check(result1==55312)

    val input2="41078 18 7 0 4785508 535256 8154 447"
    val result2=blink(input2,25)

    check(result2==217443)

    println(result2)

    val result4=blink(input2,75)
  }

  val resultMap= mutableMapOf<Long,List<Long>>()

  fun processStone(stone:Long): List<Long> {
    val result=resultMap.computeIfAbsent(stone) {
      if (stone == 0L) {
        return@computeIfAbsent listOf(1L)
      } else if (countDigits(stone) % 2 == 0) {
        val splitStone = splitLong(stone)
        return@computeIfAbsent listOf(splitStone.first, splitStone.second)
      } else {
        return@computeIfAbsent listOf(stone * 2024)
      }
    }

    return result
  }

  fun splitLong(n: Long): Pair<Long, Long> {
    if (n == 0L) return Pair(0, 0)  // Handle 0

    val numDigits = countDigits(n)
    val firstHalfDigits = (numDigits + 1) / 2 // Integer division for correct rounding
    val secondHalfDigits = numDigits - firstHalfDigits
    val divisor = 10.0.pow(secondHalfDigits).toLong()


    val firstHalf = n / divisor
    val secondHalf = n % divisor

    return Pair(firstHalf, secondHalf)
  }



  fun countDigits(n: Long): Int { // Previous digit counting function
    if (n == 0L) return 1
    var num = if (n < 0) -n else n
    var count = 0
    while (num > 0) {
      num /= 10
      count++
    }
    return count
  }

  private fun blink(input: String, iterations: Int): Int {
    var stones = input.split(" ").toList().map(String::toLong)

    for(i in 1..iterations) {
      val newStones= mutableListOf<Long>()

      for(stone in stones) {
        newStones.addAll(processStone(stone))

      }
      stones=newStones
      println("$i: ${stones.size} ${resultMap.size}")
    }

    return stones.size
  }
}

fun main() {
  Day11().start()
}