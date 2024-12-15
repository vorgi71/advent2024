package org.vorgi.org.vorgi.advent2024.day13

import org.vorgi.org.vorgi.Utils

class Day13 {

  data class GameMachine(val a: Pair<Long,Long>, val b: Pair<Long,Long>, val price: Pair<Long,Long>)

  fun start() {
    val input1 = """Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279""".lines()

    val result1 = findWins(input1)

    println(result1)

    check(result1==480L)

    val input2 = Utils.readInput("day13")
    val result2 = findWins(input2)
    println(result2)

    check(result2==25629L)

    val result3= findWins(input2,10000000000000)
    println(result3)

  }

  private fun findWins(input: List<String>,limit: Long=100): Long {
    var a = Pair(0L, 0L)
    var b = Pair(0L, 0L)
    var price: Pair<Long,Long>

    val gameMachines = mutableListOf<GameMachine>()

    for (line in input) {
      if (line.startsWith("Button A:")) {
        val split = line.substring(10).trim().split(", ")
        a = Pair(split[0].substring(1).toLong(), split[1].substring(1).toLong())
      }
      if (line.startsWith("Button B:")) {
        val split = line.substring(10).trim().split(", ")
        b = Pair(split[0].substring(1).toLong(), split[1].substring(1).toLong())
      }
      if (line.startsWith("Prize:")) {
        val split = line.substring(7).trim().split(", ")
        price = Pair(split[0].substring(2).toLong(), split[1].substring(2).toLong())
        if(limit==10000000000000L) {
          price=Pair(price.first+10000000000000, price.second+10000000000000)
        }
        gameMachines += GameMachine(a, b, price)

      }
    }


    var sum = 0L

    for (gameMachine in gameMachines) {
      val cheapestWin = findCheapestWin(gameMachine,limit)
      sum += cheapestWin.first * 3 + cheapestWin.second
    }


    return sum
  }

  private fun findCheapestWin(gameMachine: GameMachine, limit:Long=100): Pair<Long,Long> {
    println(gameMachine)

    val py = gameMachine.price.second.toDouble()
    val by = gameMachine.b.second.toDouble()
    val px = gameMachine.price.first.toDouble()

    val bx = gameMachine.b.first.toDouble()


    val ax = gameMachine.a.first.toDouble()
    val ay = gameMachine.a.second.toDouble()

    val a = (py - (by * px / bx)) / (ay - (ax * by / bx))
    val b = (px - a * ax) / bx


    if (a < 0 || b < 0) {
      return Pair(0L, 0L)
    }

    if(limit>0 &&( a > limit || b > limit)) {
      return Pair(0L, 0L)
    }

    val ai=Math.round(a)
    val bi = Math.round(b)

    if(ai*ax+bi*bx!=px) {
      println("alarm x $a $b  $ai $bi")
      return Pair(0L, 0L)
    }
    if(ai*ay+bi*by!=py) {
      println("alarm y $a $b $ai $bi")
      return Pair(0L, 0L)
    }

    return Pair(ai, bi)
  }
}

fun main() {
  Day13().start()
}