package org.vorgi.org.vorgi.advent2024.day6

import org.vorgi.org.vorgi.Utils

data class Point(var x: Int, var y: Int) {
  constructor(p: Point) : this(p.x,p.y)

  operator fun minus(point: Point): Point {
    return Point(this.x-point.x,this.y-point.y)
  }

  operator fun plus(point: Point): Point {
    return Point(this.x+point.x,this.y+point.y)
  }

  operator fun plus(dir:Direction) :Point {
    return Point(dir.dx,dir.dy)+this
  }

  fun getDirection(otherPoint:Point) : Direction? {
    if(this.x==otherPoint.x && this.y==otherPoint.y+1) {
      return Direction.Up
    }
    if(this.x==otherPoint.x && this.y==otherPoint.y-1) {
      return Direction.Down
    }
    if(this.y==otherPoint.y && this.x==otherPoint.x-1) {
      return Direction.Right
    }
    if(this.y==otherPoint.y && this.x==otherPoint.x+1) {
      return Direction.Left
    }

    return null
  }
}

open class CharGrid(input: List<String>) {
  val width: Int = input[0].length
  val height: Int = input.size
  private val data: MutableList<MutableList<Char>> = MutableList(height) { y ->
    MutableList(width) { x -> input[y][x] }
  }

  constructor(width:Int,height:Int,fillChar:Char='.') : this(
    List(height) { "$fillChar".repeat(width)}
  )

  fun isInside(x:Int,y: Int) : Boolean {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      return false
    }
    return true
  }

  fun isInside(p:Point) : Boolean {
    return isInside(p.x,p.y)
  }

  fun getAt(x: Int, y: Int): Char {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      return '?'
    }
    return data[y][x]
  }

  fun getAt(point:Point) : Char {
    return getAt(point.x,point.y)
  }

  fun setAt(x: Int, y: Int, char: Char) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      return
    }
    data[y][x] = char
  }

  fun setAt(p: Point,char: Char) {
    setAt(p.x,p.y,char)
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

  fun find(searchFunction: (x: Int, y: Int) -> Boolean) : Point? {
    for(y in 0..<height) {
      for(x in 0..<width) {
        if(searchFunction(x,y)) {
          return Point(x,y)
        }
      }
    }
    return null
  }

  fun findAll(searchFunction: (x: Int, y: Int) -> Boolean) : List<Point> {
    val result= mutableListOf<Point>()
    for(y in 0..<height) {
      for(x in 0..<width) {
        if(searchFunction(x,y)) {
          result+= Point(x,y)
        }
      }
    }
    return result
  }
}

enum class Direction(val dx: Int, val dy: Int) {
  Up(0, -1), Right(1, 0), Down(0, 1), Left(-1, 0),;

  operator fun plus(point: Point) :Point {
    return Point(this.dx,this.dy)+point
  }
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