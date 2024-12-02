package org.vorgi.org.vorgi.advent2024.day1

import org.vorgi.org.vorgi.Utils
import kotlin.math.abs

class Day1 {
  fun start() {
    val input1="""3   4
      |4   3
      |2   5
      |1   3
      |3   9
      |3   3""".trimMargin()
    val result1=findDistance(input1)
    println(result1)

    val input2= Utils.readInputAsText("day1")
    val result2=findDistance(input2)
    println(result2)
  }

  private fun findDistance(input1: String): Long {
    val list1= mutableListOf<Long>()
    val list2= mutableListOf<Long>()
    input1.lines().forEach {
      println("line: $it")
      val split=it.split("   ")
      list1.add(split[0].toLong())
      list2.add(split[1].toLong())
    }

    list1.sort()
    list2.sort()

    var sum=0L
    for(index in list1.indices){
      sum+=abs(list1[index]-list2[index])
    }
    return sum
  }
}

fun main() {
  Day1().start()
}