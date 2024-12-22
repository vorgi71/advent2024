package org.vorgi.org.vorgi.advent2024.day22

import org.vorgi.org.vorgi.Utils

class Day22 {
  fun start() {
    val input1 = """1
10
100
2024""".lines()
    val result1 = calculateSum(input1)
    println("result1 = $result1")
    check(result1 == 37327623L)

    val input2 = Utils.readInput("day22")
    val result2 = calculateSum(input2)
    println("result2 = $result2")

    val input3 = """1
2
3
2024""".lines()

    val result3 = calculateSequence(input3)
  }

  private fun calculateSequence(input: List<String>): Pair<Long,List<Int>> {
    val rngs:List<MutableList<Int>> = List(input.size) { mutableListOf() }
    for (j in 0..<input.size) {
      val line=input[j]
      var intVal = line.toInt()
      for (i in 0..<2000) {
        rngs[j].add(intVal%10)
        intVal=rng(intVal)
      }
    }



    return Pair(0,listOf(0))
  }

  private fun calculateSum(input1: List<String>): Long {
    var sum = 0L

    for (line in input1) {
      var intVal = line.toInt()
      for (i in 1..2000) {
        intVal = rng(intVal)
      }
      sum += intVal
    }

    return sum
  }

  fun rng(input: Int): Int {
    var i = input.shl(6)
    i = input.xor(i)
    i = i.and(0xFFFFFF)

    val i2 = i.shr(5)
    i = i2.xor(i)
    i = i.and(0xFFFFFF)

    val i3 = i.shl(11)
    i = i3.xor(i)
    i = i.and(0xFFFFFF)

    return i
  }
}

fun main() {
  Day22().start()
}