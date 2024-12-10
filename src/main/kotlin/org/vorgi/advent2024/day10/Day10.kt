package org.vorgi.org.vorgi.advent2024.day10

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point

class CharGrid10(input:List<String>) : CharGrid(input) {
  fun findTrail(point:Point, char: Char) : List<Point> {
    val currentChar = char+1
    val sum= mutableListOf<Point>()
    val foundDirections = Direction.entries.filter { direction -> getAt(point + Point(direction.dx, direction.dy)) == currentChar }

    if(currentChar=='9') {
      foundDirections.forEach { direction ->
        sum+=point+direction
      }
    } else {
      foundDirections.forEach { direction ->
        sum += findTrail(point + Point(direction.dx, direction.dy), currentChar)
      }
    }
    return sum
  }
}


class Day10 {
  fun start() {
    val input1=CharGrid10("""89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732""".lines())

    val result1=findTrails(input1)
    println("result1 = $result1")

    val input2= CharGrid10(Utils.readInput("day10"))
    val result2=findTrails(input2)

    println("result2 = $result2")

  }

  private fun findTrails(input1: CharGrid10): Pair<Int,Int> {
    var uniqueSum=0
    var sum=0
    for(y in 0..<input1.height) {
      for(x in 0..<input1.width) {
        if(input1.getAt(x,y)=='0') {
          val value=input1.findTrail(Point(x,y),'0')
          println("value: $value")
          uniqueSum+=value.toSet().size
          sum+=value.size
        }
      }
    }
    return Pair(uniqueSum,sum)
  }
}

fun main() {
  Day10().start()
}