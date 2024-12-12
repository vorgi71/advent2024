package org.vorgi.org.vorgi.advent2024.day12

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point

class Day12 {

  fun start() {
    val input1 = CharGrid(
      """AAAA
BBCD
BBCC
EEEC""".lines()
    )
    val result1 = calculateCost(input1)

    println("result1 = ${result1}")

    check(result1 == 140)

    val input2 = CharGrid(
      """RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE""".lines()
    )
    val result2 = calculateCost(input2)
    println("result2 = ${result2}")

    check(result2 == 1930)

    val input3 = CharGrid(Utils.readInput("day12"))

    val result3 = calculateCost(input3)
    println("result3 = ${result3}")

  }

  fun calculateSize(c: Char, x: Int, y: Int, input: CharGrid, handled: CharGrid): Pair<Int, Int> {
    val point = Point(x, y)

    var area = 1
    var diameter = 0

    handled.setAt(point, 'x')
    Direction.entries.forEach { direction ->
      val newPoint = point + direction
      if (handled.getAt(newPoint) != 'x') {
        if (input.getAt(newPoint) == c) {
          val calc = calculateSize(c, newPoint.x, newPoint.y, input, handled)
          area += calc.first
          diameter += calc.second
        }
      }
      if (input.getAt(newPoint) != c) {
        diameter++
      }
    }
    return Pair(area, diameter)
  }

  private fun calculateCost(input: CharGrid, calcMode: Int =0): Int {
    val handledListLines = List(input.height) { ".".repeat(input.width) }

    val handledGrid = CharGrid(handledListLines)

    val sizes = mutableListOf<Pair<Int, Int>>()

    for (y in 0..<input.height) {
      for (x in 0..<input.width) {
        if (handledGrid.getAt(x, y) == '.') {
          val size = calculateSize(input.getAt(x, y), x, y, input, handledGrid)
          sizes += size
        }
      }
    }

    println("sizes = ${sizes}")

    val sum=if(calcMode == 0) {
      sizes.sumOf { it.first * it.second }
    } else {
      5
    }

    return sum
  }
}

fun main() {
  Day12().start()
}