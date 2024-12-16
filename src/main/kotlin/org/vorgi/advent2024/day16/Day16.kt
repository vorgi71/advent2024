package org.vorgi.org.vorgi.advent2024.day16

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point
import kotlin.math.abs

class Day16 {

  val directionMap = mapOf(Direction.Left to '<', Direction.Right to '>', Direction.Up to '^', Direction.Down to 'v')

  fun start() {
    var input1 = Utils.readInput("day16_1")
    var result1 = findCheapestPath(input1)

    println(result1)

    check(result1 == 7036)

    val input2 = Utils.readInput("day16_2")
    val result2 = findCheapestPath(input2)
    println("result2 = ${result2}")

    check(result2==11048)

    val input3 = Utils.readInput("day16_3")
    val result3 = findCheapestPath(input3)
    println("result3 = ${result3}")
  }

  private fun findCheapestPath(input: List<String>): Int {
    var maze = CharGrid(input)

    val startPoint = maze.find { x, y -> maze.getAt(x, y) == 'S' }

    val endPoint = maze.find { x, y -> maze.getAt(x, y) == 'E' }

    if (startPoint == null || endPoint == null) {
      throw UnsupportedOperationException("fail ")
    }

    val visitedPoints = mutableListOf<Point>()

    val result = findPaths(maze, startPoint, visitedPoints).toSet()

    val min = result.minBy(evaluatePath())

    println(maze)

    for (i in 0..<min.size - 1) {
      val p1 = min[i]
      val p2 = min[i + 1]
      val dir = p1.getDirection(p2)
      val c = directionMap[dir]
      maze.setAt(p1, c ?: '?')
    }

    println(maze)

    return evaluatePath().invoke(min)
  }

  private fun evaluatePath() = { points: List<Point> ->
    var direction = Direction.Right
    var sum = 0

    for (i in 0..<points.size - 1) {
      val newDirection = points[i].getDirection(points[i + 1]) ?: throw UnsupportedOperationException("crash")
      if (newDirection != direction) {
        var turns = abs(newDirection.ordinal - direction.ordinal)
        if (turns > 2) turns = 4 - turns
        sum += 1000 * turns
        direction = newDirection
      }
      sum += 1
    }

    sum
  }

  private fun findPaths(maze: CharGrid, startPoint: Point, visitedPoints: MutableList<Point>): List<List<Point>> {
    val result = mutableListOf<List<Point>>()

    visitedPoints += startPoint

    for (direction in Direction.entries) {
      val newPoint = startPoint + direction
      if (maze.getAt(newPoint) == '.' && !visitedPoints.contains(newPoint)) {
        val newVisitedPoints = visitedPoints.toMutableList()
        result += findPaths(maze, newPoint, newVisitedPoints)
      }
      if (maze.getAt(newPoint) == 'E') {
        visitedPoints += newPoint
        result += visitedPoints
        return result
      }
    }
    return result
  }
}

fun main() {
  Day16().start()
}