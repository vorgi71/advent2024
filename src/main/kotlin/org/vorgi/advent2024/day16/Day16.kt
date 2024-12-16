package org.vorgi.org.vorgi.advent2024.day16

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point
import java.util.LinkedList
import java.util.Queue
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

    check(result2 == 11048)

//    val input3 = Utils.readInput("day16_3")
//    val result3 = findCheapestPath(input3)
//    println("result3 = ${result3}")

    val result4 = findBestPaths(input1)

    println("result4 $result4")
  }


  private fun findBestPaths(input: List<String>): Int {
    val maze = CharGrid(input)

    val startPoint = maze.find { x, y -> maze.getAt(x, y) == 'S' }

    val endPoint = maze.find { x, y -> maze.getAt(x, y) == 'E' }

    if (startPoint == null || endPoint == null) {
      throw UnsupportedOperationException("fail ")
    }

    val results = findPaths(maze, startPoint,true,evaluatePath())

    val min=evaluatePath().invoke(results.minBy(evaluatePath()))

    val lowestResults=results.filter { evaluatePath().invoke(it)==min }

    lowestResults.forEach { it ->
      println("${evaluatePath().invoke(it)} ${it}")
      val newMaze = CharGrid(input)
      for (point in it) {
        newMaze.setAt(point, 'O')
      }
      println(newMaze)
    }

    return 0
  }

  private fun findCheapestPath(input: List<String>): Int {
    val maze = CharGrid(input)

    val startPoint = maze.find { x, y -> maze.getAt(x, y) == 'S' }

    val endPoint = maze.find { x, y -> maze.getAt(x, y) == 'E' }

    if (startPoint == null || endPoint == null) {
      throw UnsupportedOperationException("fail ")
    }

    val result = findPaths(maze, startPoint, false, evaluatePath()).toSet()

    println("results:")
    result.forEach {
      println("$it: ${evaluatePath().invoke(it)}")
    }

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

  private fun findPaths(maze: CharGrid, startPoint: Point,addEqualPaths:Boolean=false, evaluatePath: (List<Point>) -> Int): List<List<Point>> {
    val queue: Queue<MutableList<Point>> = LinkedList()
    queue.offer(listOf(startPoint).toMutableList())
    val result = mutableListOf<List<Point>>()

    while (queue.isNotEmpty()) {
      val currentPath = queue.poll()
      val currentPoint = currentPath.last()

      for (direction in Direction.entries) {
        val newPoint = currentPoint + direction

        if (maze.getAt(newPoint) == 'E') {
          currentPath.add(newPoint)
          result.add(currentPath)
          // If you only need to find the *first* path, you can return here:
          // return result
          continue //Continue to find all paths
        }

        if (maze.getAt(newPoint) == '.' && !currentPath.contains(newPoint)) {
          val newPath = currentPath.toMutableList()
          newPath.add(newPoint)
          queue.offer(newPath)
        }
      }

      val endpoints = mutableMapOf<Point, MutableList<Point>>()

      val equalPathes = mutableListOf<MutableList<Point>>()

      queue.forEach { points ->
        val lastPoint = points.last()
        if (!endpoints.contains(lastPoint)) {
          endpoints[lastPoint] = points
        } else {
          val storedValue = evaluatePath.invoke(endpoints[lastPoint]!!)
          val newValue = evaluatePath.invoke(points)
          if (newValue == storedValue && addEqualPaths) {
            equalPathes += points
          } else if (newValue < storedValue) {
            endpoints[lastPoint] = points
          }
        }
      }

      queue.clear()
      endpoints.values.forEach { points ->
        queue.add(points)
      }

      if(addEqualPaths) {
        equalPathes.forEach { points ->
          queue.add(points)
        }
      }

    }
    return result
  }
}

fun main() {
  Day16().start()
}