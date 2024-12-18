package org.vorgi.org.vorgi.advent2024.day17

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.pow
import javax.annotation.processing.Processor

class Day17 {

  class Processor(
    var a: UInt,
    var b: UInt,
    var c: UInt,
    var program: List<UInt>,
    var pc: UInt
  ) {
    companion object {
      fun fromInput(input: List<String>): Processor {
        val a = input[0].substring("Register A: ".length).toUInt()
        val b = input[1].substring("Register B: ".length).toUInt()
        val c = input[2].substring("Register C: ".length).toUInt()

        val programStrings = input[4].substring("Program: ".length).split(",")
        val program = programStrings.map { it.toUInt() }
        return Processor(a, b, c, program, 0U)
      }
    }

    override fun toString(): String {
      return "A: $a\nB: $b\nC: $c\nProgram: $program\n          ${"   ".repeat(pc.toInt())}^"
    }

    fun step(): UInt? {
      var output: UInt? = null
      val opcode = program[pc.toInt()]
      val literal = program[pc.toInt() + 1]

      val literalValue = processLiteral(literal)

      var jumped = false
      when (opcode) {
        0U -> {
          val divisor = 2U.pow(literalValue)
          val result = a / divisor
          a = result
        }

        1U -> {
          val result = b.xor(literalValue)
          b = result
        }

        2U -> {
          val result = literalValue.mod(8U)
          b = result
        }

        3U -> {
          if (a != 0U) {
            pc = literalValue
            jumped = true
          }
        }

        4U -> {
          b = b.xor(c)
        }

        5U -> {
          output = literalValue.mod(8U)
        }

        6U -> {
          val divisor = 2U.pow(literalValue)
          val result = a / divisor
          b = result
        }

        7U -> {
          val divisor = 2U.pow(literalValue)
          val result = a / divisor
          c = result
        }
      }

      if (!jumped) {
        pc += 2U
      }

      return output
    }

    private fun processLiteral(literal: UInt): UInt {
      return when (literal) {
        in 0U..3U -> literal
        4U -> a
        5U -> b
        6U -> c
        else -> throw UnsupportedOperationException("literal $literal invalid")
      }
    }
  }

  private fun computeInput(processor: Processor, aValue: UInt? = null): List<UInt> {
    val output = mutableListOf<UInt>()

    if (aValue != null) {
      processor.a = aValue
    }

    processor.pc = 0U

    while (processor.pc < processor.program.size.toUInt()) {
      val stepResult = processor.step()
      if (stepResult != null) {
        output += stepResult
      }
    }

    return output
  }

  private fun computeInput(input: List<String>, aValue: UInt? = null): List<UInt> {
    val processor = Processor.fromInput(input)
    return computeInput(processor, aValue)
  }

  fun start() {
    val input1 = Utils.readInput("day17_1")
    val result1 = computeInput(input1)

    println("result1: $result1")

    check(result1.joinToString(",") == "4,6,3,5,6,3,5,2,1,0")

    val input2 = Utils.readInput("day17_2")
    val result2 = computeInput(input2)

    println("result2: $result2")

    val input3 = Utils.readInput("day17_3")
    val result3 = findAToCopy(input3)
    println("result3 = ${result3}")

    val result4=findAToCopy(input2)
    println("result4 = ${result4}")
  }

  private fun findAToCopy(input: List<String>): UInt {
    val processor=Processor.fromInput(input)

    var counter=0U
    while(true) {
      val output = computeInput(processor, counter)

      println("output $output")

      if(output==processor.program) {
        break
      }
      counter++

      if(counter%100_000U==0U) {
        println("counter: $counter")
      }
    }
    return counter
  }

}

fun main() {
  Day17().start()
}