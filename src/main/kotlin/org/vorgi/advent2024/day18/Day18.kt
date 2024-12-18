package org.vorgi.org.vorgi.advent2024.day18

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point
import java.util.*

class Day18 {
  fun start() {
    val input1= Utils.readInput("day18_1")
    val result1=calculateShortestPath(input1,7,7,12)
    println("result1 = ${result1}")

    check(result1==22)

    val input2=Utils.readInput("day18_2")
    val result2=calculateShortestPath(input2,71,71,1024)
    println("result2 = ${result2}")

    val result3=findClosingBlock(input1,7,7)
    println("result3 = ${result3}")

    check(result3==Point(6,1))

    val result4=findClosingBlock(input2,71,71)
    println("result4 = $result4")
  }

  private fun floodFill(screen: CharGrid, startPoint: Point, c: Char='O') : CharGrid {
    val newScreen=screen.copy()
    newScreen.setAt(startPoint, c)

    val queue: Queue<Point> = LinkedList()
    queue.offer(startPoint)

    while(queue.isNotEmpty()) {
      val points= mutableListOf<Point>()

      while(queue.isNotEmpty()) {
        points+=queue.poll()
      }

      for(point in points) {
        for (direction in Direction.entries) {
          val newPoint = point + direction
          if (newScreen.getAt(newPoint) == '.') {
            newScreen.setAt(newPoint,c)
            queue.offer(newPoint)
          }
        }
      }
    }

    println(newScreen)

    return newScreen
  }

  private fun findClosingBlock(input: List<String>, width: Int, height: Int): Point {

    val points = input.map { line ->
      val split = line.split(",").map { it.toInt() }
      Point(split[0], split[1])
    }

    val screen=CharGrid(width,height)

    var index=0

    val blocked=false
    do {
      screen.setAt(points[index],'#')
      val filledScreen=floodFill(screen,Point(0,0),'O')
      if(filledScreen.getAt(filledScreen.width-1,filledScreen.height-1)=='.') {
        break
      }
      index++
    } while(!blocked)

    return points[index]

  }

  private fun calculateShortestPath(input: List<String>, width:Int, height:Int, falls: Int): Int {
    val screen=CharGrid(width,height)


    for(i in 0..<falls) {
      val line=input[i]
      val split=line.split(",").map { it.toInt() }
      screen.setAt(split[0],split[1],'#')
    }

    println(screen)

    screen.setAt(width-1,height-1,'E')

    val foundPaths = findPaths(screen, Point(0, 0), false) { points: List<Point> ->
      points.size
    }

    val result=foundPaths.minBy { it.size }

    result.forEach { point: Point ->
      screen.setAt(point,'O')
    }

    println(screen)

    return result.size-1
  }

  private fun findPaths(maze: CharGrid, startPoint: Point, addEqualPaths:Boolean=false, evaluatePath: (List<Point>) -> Int): List<List<Point>> {
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
  Day18().start()
}