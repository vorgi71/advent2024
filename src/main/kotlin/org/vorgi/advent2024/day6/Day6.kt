package org.vorgi.org.vorgi.advent2024.day6

import org.vorgi.org.vorgi.Utils

data class Point(val x: Int, val y: Int) {
  operator fun minus(point: Point): Point {
    return Point(this.x-point.x,this.y-point.y)
  }

  operator fun plus(point: Point): Point {
    return Point(this.x+point.x,this.y+point.y)
  }
}

open class CharGrid(input: List<String>) {
  val width: Int = input[0].length
  val height: Int = input.size
  private val data: MutableList<MutableList<Char>> = MutableList(height) { y ->
    MutableList(width) { x -> input[y][x] }
  }

  fun isInside(x:Int,y: Int) : Boolean {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      return false
    }
    return true
  }

  fun getAt(x: Int, y: Int): Char {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      return '?'
    }
    return data[y][x]
  }

  fun setAt(x: Int, y: Int, char: Char) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      return
    }
    data[y][x] = char
  }

  override fun toString(): String {
    val returnValue= buildString {
      for(line in data) {
        for(char in line) {
          append(char)
        }
        append('\n')
      }

    }
    return returnValue
  }
}

enum class Direction(val dx: Int, val dy: Int) {
  Up(0, -1), Down(0, 1), Left(-1, 0), Right(1, 0);
}

class Day6 {
  fun start() {
    val input1 = """....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...""".lines()

    val result1 = countSteps(input1)
    println(result1)

    check(result1 == 41)

    val input2 = Utils.readInput("day6")

    val result2 = countSteps(input2)
    println(result2)

    val result3 = findLoopWalls(input1)

    println(result3)

    val result4 = findLoopWalls(input2)

    println(result4)
  }

  private fun countSteps(input: List<String>): Int {
    val grid = CharGrid(input)

    var pos: Point = findGuardPosition(grid)
    var dir: Direction = Direction.Up

    val positionSet = mutableSetOf(pos)

    while (pos.x in 0..<grid.width && pos.y in 0..<grid.height) {
      val newPos = Point(pos.x + dir.dx, pos.y + dir.dy)
      if (grid.getAt(newPos.x, newPos.y) == '#') {
        dir = turnRight(dir)
      } else {
        pos = newPos
        if (pos.x in 0..<grid.width && pos.y in 0..<grid.height) positionSet.add(newPos)
      }
    }

    return positionSet.size
  }

  private fun isLoop(grid: CharGrid): Boolean {
    var pos: Point = findGuardPosition(grid)

    var dir: Direction = Direction.Up

    val posDirSet = mutableSetOf(Pair(pos, dir))

    while (pos.x in 0..<grid.width && pos.y in 0..<grid.height) {
      val newPos = Point(pos.x + dir.dx, pos.y + dir.dy)
      if (grid.getAt(newPos.x, newPos.y) == '#') {
        dir = turnRight(dir)
      } else {
        pos = newPos
        val posDir = Pair(pos, dir)
        if (posDirSet.contains(posDir)) {
          return true
        }
        posDirSet.add(posDir)
      }
    }

    return false
  }

  private fun turnRight(dir: Direction) = when (dir) {
    Direction.Up -> Direction.Right
    Direction.Right -> Direction.Down
    Direction.Down -> Direction.Left
    Direction.Left -> Direction.Up
  }

  private fun findGuardPosition(grid: CharGrid): Point {

    var foundx: Int = -1
    var foundy: Int = -1

    for (y in 0..<grid.height) {
      for (x in 0..<grid.width) {
        if (grid.getAt(x, y) == '^') {
          foundx = x
          foundy = y
          break
        }
      }
    }
    return Point(foundx, foundy)
  }

  fun findLoopWalls(input: List<String>): Int {
    val grid = CharGrid(input)
    val loopWalls = mutableSetOf<Point>()

    for (y in 0..<grid.height) {
      for (x in 0..<grid.width) {
        if (grid.getAt(x, y) == '.') { // Only consider empty spaces
          grid.setAt(x, y, '#') // Place a temporary wall
          if (isLoop(grid)) {   //  Careful. Needs to operate on data structure grid
            loopWalls.add(Point(x, y))
          }
          grid.setAt(x, y, '.')   // Remove the temporary wall
        }
      }
    }
    return loopWalls.size
  }

}

fun main() {
  Day6().start()
}