package org.vorgi.org.vorgi.advent2024.day8

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Point
import org.vorgi.org.vorgi.combinations

class CharGrid8(input: List<String>) : CharGrid(input) {
  fun findChars(c:Char) : List<Point> {
    val result:MutableList<Point> = mutableListOf()

    for(y in 0..<height) {
      for(x in 0 ..<width) {
        if(getAt(x,y)==c) {
          result += Point(x,y)
        }
      }
    }

    return result
  }
}

class Day8 {
  fun start() {
    val input1="""............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............""".lines()

    val result1 = countAntinodes(input1)

    println("result1 = ${result1}")

    check(result1==14)

    val input2 = Utils.readInput("day8")
    val result2=countAntinodes(input2)

    println("result2 = ${result2}")
  }

  private fun countAntinodes(input: List<String>): Int {
    var grid=CharGrid8(input)

    val charSet= mutableSetOf<Char>()

    for(y in 0..< grid.height) {
      for(x in 0..< grid.width) {
        val c=grid.getAt(x,y)
        if(c!='.') {
          charSet.add(c)
        }
      }
    }

    var sum=0

    for (c in charSet) {
      var charPositions = grid.findChars(c)
      var charCombinations = charPositions.combinations(2)
      var uniqueCharCombinations = charCombinations.map { pointList ->
        pointList.sortedBy { point: Point ->
          point.x + grid.width*point.y
        }
      }.filter { pointList ->
        (pointList[0] != pointList[1])
      }.toSet()

      println("$c:")
      uniqueCharCombinations.forEach { pointList ->
        val diff=pointList[1]-pointList[0]
        val anti1=pointList[0]-diff
        val anti2=pointList[1]+diff
        val c1 = grid.getAt(anti1.x, anti1.y)
        if(c1 =='.' || c1 == '#') {
          grid.setAt(anti1.x,anti1.y,'#')
          sum++
        }
        val c2 = grid.getAt(anti2.x, anti2.y)
        if(c2 =='.' || c2 == '#') {
          grid.setAt(anti2.x,anti2.y,'#')
          sum++
        }
      }
    }

    println(grid)
    return sum
  }
}

fun main() {
  Day8().start()
}