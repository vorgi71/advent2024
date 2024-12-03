package org.vorgi.org.vorgi.advent2024.day3

import org.vorgi.org.vorgi.Utils

class Day3 {
  fun start() {
    val input1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    val result1 = multiply(input1)
    println("result1=$result1")

    check(result1 == 161L)

    val input2 = Utils.readInputAsText("day3")
    val result2 = multiply(input2)
    println("result2=$result2")

    check(result2 == 174336360L)

    val input3 = """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""
    val result3 = doDontMultiply(input3)

    println("result3=$result3")

    check(result3 == 48L)

    val result4 = doDontMultiply(input2)
    println("result4=$result4")

  }

  private fun doDontMultiply(input: String): Long {
    val mulRegex = Regex("""(do\(\))|(don't\(\))|(mul\(([0-9]*),([0-9]*)\))""")

    var doMulitply = true

    var sum = 0L

    mulRegex.findAll(input).forEach { instruction ->
      println("${instruction.value}")
      when (instruction.value) {
        "do()" -> {
          doMulitply = true
        }

        "don't()" -> {
          doMulitply = false
        }

        else -> {
          println("${instruction.groups[4]} ${instruction.groups[5]}")
          if (doMulitply) {
            val a = instruction.groups[4]?.value?.toLong()
            val b = instruction.groups[5]?.value?.toLong()
            if (a != null && b != null) {
              sum += a * b
            } else {
              throw IllegalArgumentException("fail parsing $instruction")
            }
          }
        }
      }
    }

    return sum
  }

  private fun multiply(input1: String): Long {
    val mulRegex = Regex("""mul\(([0-9]*),([0-9]*)\)""")

    var sum = 0L

    mulRegex.findAll(input1).forEach { mulInstruction ->
      println("${mulInstruction.value} ${mulInstruction.groups[1]?.value}")
      var a = mulInstruction.groups[1]?.value?.toLong()
      var b = mulInstruction.groups[2]?.value?.toLong()
      if (a != null && b != null) {
        sum += a * b
      } else {
        throw IllegalArgumentException("fail parsing $mulInstruction")
      }
    }
    return sum

  }
}

fun main() {
  Day3().start()
}