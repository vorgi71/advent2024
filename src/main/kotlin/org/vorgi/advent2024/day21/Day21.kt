package org.vorgi.org.vorgi.advent2024.day21

import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point

class Day21 {
  val numPad = mapOf(
    '7' to Point(0, 0),
    '8' to Point(1, 0),
    '9' to Point(2, 0),
    '4' to Point(0, 1),
    '5' to Point(1, 1),
    '6' to Point(2, 1),
    '1' to Point(0, 2),
    '2' to Point(1, 2),
    '3' to Point(2, 2),
    'X' to Point(0,3),
    '0' to Point(1, 3),
    'A' to Point(2, 3),
  )

  val dirPad = mapOf(
    'X' to Point(0,0),
    '^' to Point(1, 0),
    'A' to Point(2, 0),
    '<' to Point(0, 1),
    'v' to Point(1, 1),
    '>' to Point(2, 1),
  )

  private fun translateKeyPresses(input: String, inputPad: Map<Char, Point> = numPad): String {
    var out = ""
    var debugOut=""

    var currentPoint = inputPad['A']!!

    for (nextChar in input) {
      debugOut+="($nextChar)"
      val nextPoint = inputPad[nextChar]
      if (nextPoint == null) {
        throw IllegalStateException("$nextPoint not found")
      }
      var xDiff = nextPoint.x - currentPoint.x
      var yDiff = nextPoint.y - currentPoint.y
      while (yDiff != 0) {
        if (yDiff > 0) {
          out += 'v';debugOut+='v'
          yDiff--
        } else {
          out += '^';debugOut+='^'
          yDiff++
        }
      }
      while (xDiff != 0) {
        if (xDiff > 0) {
          out += ">";debugOut += ">"
          xDiff--
        } else {
          out += "<";debugOut += "<"
          xDiff++
        }
      }
      out += "A";debugOut+="A"
      currentPoint = nextPoint
    }

    println(debugOut)
    return out
  }

  fun untranslateKeyPresses(input:String,inputPad: Map<Char, Point> = numPad) : String {
    val outMap:MutableMap<Point,Char> = mutableMapOf()

    var output=""

    inputPad.forEach { (key, value) ->
      outMap[value] = key
    }

    var currentPoint=inputPad['A']!!

    for(char in input) {
      when(char) {
        'v' -> currentPoint+=Direction.Down
        '^' -> currentPoint+=Direction.Up
        '<' -> currentPoint+=Direction.Left
        '>' -> currentPoint+=Direction.Right
        'A' -> {
          val outChar=outMap[currentPoint]?:throw IllegalStateException("fail")
          output+=outChar
        }
        'X' -> throw IllegalStateException("we reached the X")
      }
    }
    return output
  }

  fun start() {
    val input1 = """029A
980A
179A
456A
379A"""

    val input2 = """638A
965A
780A
803A
246A"""

    val result01 = translateKeyPresses("379A")

    println("result01 = ${result01}")

    val computed01 = "v<<A^>>AvA^Av<<A^>>AAv<A<A^>>AA<Av>AA^Av<A^>AA<A>Av<A<A^>>AAA<Av>A^A"

    val uncomputed01=untranslateKeyPresses(computed01,dirPad)
    val uncomputed02=untranslateKeyPresses(uncomputed01,dirPad)
    val uncomputed03=untranslateKeyPresses(uncomputed02)

    println("uncomputed01 = ${uncomputed01}")
    println("uncomputed02 = ${uncomputed02}")
    println("uncomputed03 = ${uncomputed03}")

    val result02 = translateKeyPresses(result01, dirPad)
    println("result02 = ${result02}")

    val result03 = translateKeyPresses(result02, dirPad)
    println("result03 = $result03")

    val result04 = remoteRemoteRemote("379A")
    println("result04 = ${result04}")

    val result1 = input2.lines().sumOf { line ->
      val result = remoteRemoteRemote(line)
      println("$line: $result")
      result
    }

    println("result1 = ${result1}")
  }

  private fun remoteRemoteRemote(input: String): Long {
    val numToDir = translateKeyPresses(input)
    val dirToDir = translateKeyPresses(numToDir, dirPad)
    val dirToDir2 = translateKeyPresses(dirToDir, dirPad)

    val numValue = input.substring(0, 3).toLong()
    val length = dirToDir2.length
    println("$input: $numValue * $length")
    val complexity = input.substring(0, 3).toLong() * dirToDir2.length

    return complexity
  }
}

fun main() {
  Day21().start()
}

// 3:<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A     (solution)
// 3:v<<A^>>AvA^Av<<A^>>AAv<A<A^>>AA<Av>AA^Av<A^>AA<A>Av<A<A^>>AAA<Av>A^A (computed)

// <A>A<AAv<AA^>>AvAA^Av<AAA^>A (solution)
// <A>A<AAv<AA^>>AvAA^Av<AAA^>A (computed)

// ^A^^<<A>>AvvvA (solution)
// ^A^^<<A>>AvvvA (computed)