package org.vorgi.org.vorgi.advent2024.day20

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point

class Day20 {
  fun start() {
    val input1= Utils.readInput("day20_1")
    val result1=findCheats(input1,20)

    println("result1 = ${result1}")

    val input2 = Utils.readInput("day20_2")
    val result2 = findCheats(input2,100)

    println("result2 = ${result2}")
  }

  private fun findCheats(input: List<String>, maxShort: Int): List<Int> {
    val maze=CharGrid(input)

    val startPoint = maze.find { x, y -> maze.getAt(x, y) == 'S' }
    val endPoint = maze.find {x,y -> maze.getAt(x,y)=='E'}

    if(startPoint==null) {
      throw UnsupportedOperationException("no start")
    }

    val paths = findPath(startPoint, maze)

    for(point in paths[0]) {
      maze.setAt(point,'O')
    }

    println(maze)

    return paths.map { it.size }
  }

  private fun findPath(startPoint: Point, maze: CharGrid): List<List<Point>> {
    val queue: MutableList<Pair<Point, List<Point>>> = mutableListOf()
    queue.add(Pair(startPoint, listOf(startPoint)))
    val results = mutableListOf<List<Point>>()

    while (queue.isNotEmpty()) {
      val currentList = queue.toList()
      queue.clear()
      for(element in currentList) {
        val (currentPoint, currentPath) = element

        if (maze.getAt(currentPoint) == 'E') {
          results.add(currentPath)
          continue
        }

        for (direction in Direction.entries) {
          val newPoint = currentPoint + direction
          val newPath = currentPath + newPoint
          if (maze.getAt(newPoint) == '.' && !currentPath.contains(newPoint)) {
            queue.add(Pair(newPoint, newPath))
          }
        }
      }
    }

    return results
  }
}

fun main() {
  Day20().start()
}
