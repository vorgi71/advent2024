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

    check(result2==301)

    val result3 = countAntinodes(input1,AntinodeMethod.LINE)

    println("result3 = ${result3}")

    check(result3 == 34)

    val result4=countAntinodes(input2,AntinodeMethod.LINE)

    println(result4)
  }

  enum class AntinodeMethod {
    PAIR,
    LINE
  }

  private fun countAntinodes(input: List<String>,antinodeMethod: AntinodeMethod=AntinodeMethod.PAIR): Int {
    var grid=CharGrid8(input)

    val charSet= mutableSetOf<Char>()

    val antinodes= mutableSetOf<Pair<Char,Point>>()

    for(y in 0..< grid.height) {
      for(x in 0..< grid.width) {
        val c=grid.getAt(x,y)
        if(c!='.') {
          charSet.add(c)
        }
      }
    }

    for (c in charSet) {
      print("$c:")
      var charPositions = grid.findChars(c)

      println("${charPositions.size}")

      var charCombinations = charPositions.combinations(2)
      var uniqueCharCombinations = charCombinations.map { pointList ->
        pointList.sortedBy { point: Point ->
          point.x + grid.width*point.y
        }
      }.filter { pointList ->
        (pointList[0] != pointList[1])
      }.toSet()

      uniqueCharCombinations.forEach { pointList ->
        val newAntinodes = createAntinodes(pointList,grid,antinodeMethod)

        for(antinode in newAntinodes) {
          val c1 = grid.getAt(antinode.x, antinode.y)
          if (c1 == '.' || c1 == '#') {
            grid.setAt(antinode.x, antinode.y, '#')
          }
          if (grid.isInside(antinode.x, antinode.y)) {
            antinodes.add(Pair('#', antinode))
          }
        }
      }
    }

    println(grid)
    return antinodes.size
  }

  private fun createAntinodes(pointList: List<Point>,grid: CharGrid8,antinodeMethod: AntinodeMethod): List<Point> {
    val diff = pointList[1] - pointList[0]

    if(antinodeMethod==AntinodeMethod.PAIR) {
      val anti1 = pointList[0] - diff
      val anti2 = pointList[1] + diff
      return listOf(anti1, anti2)
    }

    val result= mutableListOf<Point>()

    var point=pointList[0]
    while(grid.isInside(point)) {
      result+=Point(point)
      point-=diff
    }

    point=pointList[1]
    while(grid.isInside(point)) {
      result+=Point(point)
      point+=diff
    }

    return result
  }
}

fun main() {
  Day8().start()
}