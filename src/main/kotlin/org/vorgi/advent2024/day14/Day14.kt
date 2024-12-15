package org.vorgi.org.vorgi.advent2024.day14

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.Point

class Day14 {
  class Robot(val room: RobotRoom, var pos:Point, val vel:Point) {
    fun move() {
      pos+=vel

      if(pos.x<0) {
        pos.x += room.width
      }
      if(pos.x>= room.width) {
        pos.x -= room.width
      }
      if(pos.y<0) {
        pos.y += room.height
      }
      if(pos.y>=room.height) {
        pos.y -= room.height
      }
    }
  }

  class RobotRoom(val width: Int, val height: Int, input:List<String>) {
    val robots: MutableList<Robot> = mutableListOf()

    init {
      for(line in input) {
        val split=line.split(" ")
        val pos=split[0].substring(2).split(",")
        val vel=split[1].substring(2).split(",")
        val posPoint=Point(pos[0].toInt(),pos[1].toInt())
        val velPoint=Point(vel[0].toInt(),vel[1].toInt())
        robots+=Robot(this,posPoint,velPoint)
      }
    }

    fun move(times:Int) {
      for(i in 1..times) {
        for (robot in robots) {
          robot.move()
        }
      }
    }

    override fun toString(): String {
      return buildString {
        for(y in 0..<height) {
          for(x in 0..<width) {
            val count=robots.count { robot -> robot.pos.x==x && robot.pos.y==y }
            if(count>0) {
              append(count)
            } else {
              append('.')
            }
          }
          append("\n")
        }
      }
    }

    fun countSegments() : List<Int> {
      val segments:MutableList<Int> = mutableListOf(0,0,0,0)

      val halfWidth = width/2
      val halfHeight = height/2

      for(robot in robots) {
        if(robot.pos.x<halfWidth && robot.pos.y < halfHeight) {
          segments[0]++
        }
        if(robot.pos.x>halfWidth && robot.pos.y < halfHeight) {
          segments[1]++
        }
        if(robot.pos.x<halfWidth && robot.pos.y > halfHeight) {
          segments[2]++
        }
        if(robot.pos.x>halfWidth && robot.pos.y > halfHeight) {
          segments[3]++
        }

      }

      return segments
    }

    fun moveUntilLine() {
      for (count in 1..<10_000) {
        move(1)
        robots.sortBy { it.pos.y * width + it.pos.x }

        var lineCount = 0

        var lastRobot = robots[0]
        for (i in 1..<robots.size) {
          val robot = robots[i]
          if (robot.pos.x == lastRobot.pos.x + 1) {
            lineCount++
            if (lineCount > 10) {
              break
            }
          } else {
            lineCount = 0
          }
          lastRobot = robot
        }
        if (lineCount > 10) {
          println(count)
          println(this)
        }
      }
    }
  }

  fun start() {
    val input1=RobotRoom(11,7,"""p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3""".lines())


    input1.move(100)

    println(input1)

    val segments1=input1.countSegments()
    val result1 = segments1.reduce { mul, it -> it * mul }
    println("result1 = $result1")
    check(result1==12)

    val input2=RobotRoom(101,103, Utils.readInput("day14"))
    input2.move(100)
    val segments2=input2.countSegments()
    val result2 = segments2.reduce {mul,it -> it*mul}
    println("result2 = ${result2}")

    val input3=RobotRoom(101,103, Utils.readInput("day14"))

    input3.moveUntilLine()

  }
}

fun main() {
  Day14().start()
}