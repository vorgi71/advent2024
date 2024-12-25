package org.vorgi.org.vorgi.advent2024.day24

import org.vorgi.org.vorgi.Utils
import java.util.BitSet


class Day24 {
  class Program {
    fun executeUntilResolved() {
      do {
        instructions.forEach { it.execute() }
        val zVariables = variables.filter { it.key.startsWith("z") }
        println(variables)
        val zResolved= zVariables.all { it.value!=null }
      } while (!zResolved)
    }

    companion object {
      fun parseInput(input: List<String>): Program {
        val program = Program()

        for (line in input) {
          val split = line.split(" ")
          if (split.size == 2) {
            val name = split[0].substring(0, split[0].length - 1)
            val value = split[1] != "0"
            program.variables[name] = value
          } else if (split.size == 5) {
            val i1 = split[0]
            val i2 = split[2]
            val out = split[4]
            val operation = split[1]
            when (operation) {
              "AND" -> {
                program.instructions.add(program.And(i1, i2, out))
              }

              "OR" -> {
                program.instructions.add(program.Or(i1, i2, out))
              }

              "XOR" -> {
                program.instructions.add(program.Xor(i1, i2, out))
              }
            }
          }
        }

        return program
      }
    }

    abstract inner class Instruction(val i1: String, val i2: String, val out: String) {
      abstract fun execute(): Boolean
      fun performOperation(operation: (Boolean, Boolean) -> Boolean): Boolean {
        val i1Val = variables[i1]
        val i2Val = variables[i2]
        if (i1Val == null || i2Val == null) {
          variables[out] = null
          return false
        }
        val outValue = operation(i1Val, i2Val)

        variables[out] = outValue
        return true
      }
    }

    inner class And(i1: String, i2: String, out: String) : Instruction(i1, i2, out) {
      override fun execute(): Boolean {
        return performOperation { a, b -> a.and(b) }
      }

    }

    inner class Or(i1: String, i2: String, out: String) : Instruction(i1, i2, out) {
      override fun execute(): Boolean {
        return performOperation { a, b -> a.or(b) }
      }

    }

    inner class Xor(i1: String, i2: String, out: String) : Instruction(i1, i2, out) {
      override fun execute(): Boolean {
        return performOperation { a, b -> a.xor(b) }
      }

    }

    val instructions = mutableListOf<Instruction>()
    val variables = mutableMapOf<String, Boolean?>()
  }

  fun start() {
    val input1 = Utils.readInput("day24_1")
    val result1 = executeProgram(input1)

    println("result1 = ${result1}")

    val input2 = Utils.readInput("day24_2")
    val result2 = executeProgram(input2)

    println("result2 = ${result2}")
  }

  private fun executeProgram(input: List<String>): Any {
    val program = Program.parseInput(input)

    program.executeUntilResolved()

    val zVariables=program.variables.keys.filter { it.startsWith("z") }.sorted()
    val bitSet=BitSet()

    zVariables.forEachIndexed { index,name ->
      println("$name ${program.variables.get(name)}")
      if(program.variables.get(name)==true) {
        bitSet.set(index)
      }
    }



    return bitSet.toLongArray()[0]
  }
}

fun main() {
  Day24().start()
}