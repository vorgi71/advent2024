package org.vorgi.org.vorgi.advent2024.day20

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point
import kotlin.math.abs

class DistPoint(x:Int , y:Int ,var dist: Int) : Point(x,y) {

  constructor(point:Point ) : this(point,0)
  constructor(point:Point, dist:Int ) : this(point.x,point.y,dist)

  override operator fun plus(dir:Direction) :DistPoint {
    return DistPoint(dir.dx,dir.dy,1)+this
  }

  operator fun plus(otherPoint: DistPoint): DistPoint {
    return DistPoint(this.x+otherPoint.x,this.y+otherPoint.y,this.dist+otherPoint.dist)
  }

  override fun toString(): String {
    return super.toString()+",$dist"
  }
}

class Day20 {
  fun start() {
    val input1= Utils.readInput("day20_1")
    val result1=findCheats(input1, 2, 2)

    println("result1 = ${result1}")

    val input2 = Utils.readInput("day20_2")
    val result2 = findCheats(input2, 100, 2)

    println("result2 = ${result2.size}")

    val result3=findCheats(input1,50,20)

    val result3Map= mutableMapOf<Int,Int>()
    for(value in result3) {
      val count=result3Map.getOrDefault(value,0)+1
      result3Map[value] = count
    }
    println("result3Map = ${result3Map}")

  }

  private fun findCheats(input: List<String>, minShort: Int, cheatSize: Int=2): List<Int> {
    val maze=CharGrid(input)

    val startPoint = DistPoint(maze.find { x, y -> maze.getAt(x, y) == 'S' }!!)

    val paths = findPath(startPoint, maze)

    for(point in paths[0]) {
      maze.setAt(point,'O')
    }

    println(maze)

    val result=mutableListOf<Pair<DistPoint,DistPoint>>()

    for(point in paths[0]) {
      val distPoints = paths[0].filter { otherPoint ->
        abs(point.x - otherPoint.x) + abs(point.y - otherPoint.y) <= cheatSize && otherPoint.dist - point.dist >= minShort + cheatSize
      }
      if(distPoints.isNotEmpty()) {
        distPoints.forEach {
          result.add(Pair(point,it))
        }
      }
    }

    return result.map { it.second.dist - it.first.dist- cheatSize }
  }

  private fun findPath(startPoint: DistPoint, maze: CharGrid, visited: List<DistPoint> = listOf(startPoint)): List<List<DistPoint>> {

    val result= mutableListOf<List<DistPoint>>()

    for(direction in Direction.entries) {
      val newPoint = startPoint + direction
      val newVisited=visited+newPoint
      if(maze.getAt(newPoint)=='.' && !visited.contains(newPoint)) {
        result += findPath(newPoint,maze, newVisited)
      }
      if(maze.getAt(newPoint)=='E') {
        return listOf(newVisited)
      }
    }

    return result
  }
}

fun main() {
  Day20().start()
}
