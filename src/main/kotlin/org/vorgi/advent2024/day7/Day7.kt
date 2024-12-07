package org.vorgi.org.vorgi.advent2024.day7

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.combinations

class Day7 {

  class Equation(line: String) {
    val result:Long
    val values:List<Long>

    init {
      val split1=line.split(":")
      result=split1[0].toLong()
      val split2=split1[1].trim().split(" ")
      values=List(split2.size) { index -> split2[index].toLong() }
    }

    override fun toString(): String {
      return "$result ${values.joinToString(" ")}"
    }

    fun pow(e:Int,m:Int):Int {
      var result= 1
      for(i in 0..<m) {
        result*=e
      }
      return result
    }

    fun isValid(operators: List<Char>): Boolean {
      val combinations = operators.combinations(values.size-1)

      for(combination in combinations) {
        if(result == calculate(values,combination)) {
          return true
        }
      }

      return false
    }

    private fun calculate(values: List<Long>, combination: List<Char>): Long {


      var result=values[0]

      for(index in 1..<values.size) {
        when(combination[index-1]) {
          '+' -> result+=values[index]
          '*' -> result*=values[index]
          '|' -> result="$result${values[index]}".toLong()
        }
      }

      return result
    }
  }

  fun start() {
    val input1 = """190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20""".lines()

    val result1=checkEquations(input1,listOf('+','*'))

    println("result1 = ${result1}")

    check(result1 == 3749L)

    val input2= Utils.readInput("day7")

    val result2=checkEquations(input2, listOf('*','+'))

    println("result2 = ${result2}")

    check(result2 == 1582598718861L)

    val result3=checkEquations(input1,listOf('*','+','|'))

    println("result3 = ${result3}")

    check(result3 == 11387L)

    val result4=checkEquations((input2),listOf('*','+','|'))

    println("result4 = $result4")
  }

  private fun checkEquations(lines: List<String>, operators: List<Char>): Any {
    val equations= mutableListOf<Equation>()

    for(line in lines) {
      equations.add(Equation(line))
    }

    val equations1 = equations.filter { equation -> equation.isValid(operators) }

    return equations1.sumOf { it.result }
  }
}

fun main() {
  Day7().start()
}