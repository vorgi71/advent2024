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

    val result3 = countSimilarities(input1)
    println(result3)

    val result4 = countSimilarities(input2)
    println(result4)
  }

  private fun countSimilarities(input1: String): Long {
    val (list1, list2) = readLists(input1)

    var sum=0L
    for(element in list1) {
      val count = list2.count { it == element }
      sum+=element*count
    }
    return sum
  }

  private fun findDistance(input1: String): Long {
    val (list1, list2) = readLists(input1)

    list1.sort()
    list2.sort()

    var sum=0L
    for(index in list1.indices){
      sum+=abs(list1[index]-list2[index])
    }
    return sum
  }

  private fun readLists(input1: String): Pair<MutableList<Long>, MutableList<Long>> {
    val list1 = mutableListOf<Long>()
    val list2 = mutableListOf<Long>()
    input1.lines().forEach {
      println("line: $it")
      val split = it.split("   ")
      list1.add(split[0].toLong())
      list2.add(split[1].toLong())
    }
    return Pair(list1, list2)
  }
}

fun main() {
  Day1().start()
}