package org.vorgi.org.vorgi.advent2024.day5

import org.vorgi.org.vorgi.Utils

class Day5 {
  fun start() {
    val input1 = """47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47"""

    val result1 = countValidInstructions(input1.lines())

    println(result1)

    check(result1 == 143)

    val input2 = Utils.readInput("day5")
    val result2 = countValidInstructions(input2)
    println(result2)

    check(result2 == 5087)

    val result3=fixSequences(input1.lines())
    println(result3)

    check(result3 == 123)

    val result4=fixSequences(input2)

    println(result4)
  }

  private fun countValidInstructions(input: List<String>): Int {
    val (orders, sequences) = readInput(input)

    var sum = 0

    for (sequence in sequences) {
      if (checkSequence(sequence, orders)) {
        println("sequence: $sequence is valid")

        sum += sequence[sequence.size / 2]
      } else {
        println("sequence: $sequence is not valid")
      }
    }

    return sum
  }

  private fun readInput(input: List<String>): Pair<MutableList<Pair<Int, Int>>, MutableList<List<Int>>> {
    val orders: MutableList<Pair<Int, Int>> = mutableListOf()
    val sequences: MutableList<List<Int>> = mutableListOf()

    for (line in input) {
      if (line.contains("|")) {
        val split = line.split("|")
        orders.add(Pair(split[0].toInt(), split[1].toInt()))
      } else if (line.contains(",")) {
        val split = line.split(",")
        val sequence = split.map { entry -> entry.toInt() }
        sequences.add(sequence)
      }
    }
    return Pair(orders, sequences)
  }

  private fun checkSequence(sequence: List<Int>, orders: List<Pair<Int, Int>>): Boolean {
    for (i in 0..<sequence.size - 1) {
      val first = sequence[i]
      val second = sequence[i + 1]
      orders.forEach { order ->
        if (first == order.second && second == order.first) {
          return false
        }
      }
    }
    return true
  }

  private fun fixSequences(input: List<String>): Int {
    val (orders, sequences) = readInput(input)
    var sum = 0
    for(sequence in sequences) {
      val fixedSequence = fixSequence(sequence, orders)

      val wasFixed=fixedSequence != sequence

      println("<$sequence> <$fixedSequence> $<<$wasFixed>>")

      if(wasFixed) {
        sum += fixedSequence[fixedSequence.size / 2]
      }
    }

    return sum
  }

  private fun fixSequence(sequence: List<Int>, orders: List<Pair<Int, Int>>): List<Int> {
    val newSequence = sequence.toMutableList()
    while(!checkSequence(newSequence, orders)) {
      var swap=false
      for(i in 0..<newSequence.size - 1) {
        val first = newSequence[i]
        val second = newSequence[i + 1]
        orders.forEach { order ->
          if (first == order.second && second == order.first) {
            newSequence[i] = second
            newSequence[i+1] = first
            swap=true
          }
        }
      }
      if (!swap) {
        break
      }
    }
    if(checkSequence(newSequence, orders)) {
      return newSequence
    } else {
      throw UnsupportedOperationException("cannot fix sequence $newSequence")
    }
  }
}

fun main() {
  Day5().start()
}