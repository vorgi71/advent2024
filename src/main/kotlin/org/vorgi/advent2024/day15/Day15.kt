package org.vorgi.org.vorgi.advent2024.day15

import org.vorgi.org.vorgi.Utils
import org.vorgi.org.vorgi.advent2024.day6.CharGrid
import org.vorgi.org.vorgi.advent2024.day6.Direction
import org.vorgi.org.vorgi.advent2024.day6.Point
import kotlin.math.max
import kotlin.math.min

class Day15 {

  val moveMap = mapOf('<' to Direction.Left, '>' to Direction.Right, '^' to Direction.Up, 'v' to Direction.Down)

  fun start() {
    val input1 = Utils.readInput("day15_1")

    val result1 = moveBoxes(input1)
    println("result1 = ${result1}")

    check(result1 == 2028L)

    val input2 = Utils.readInput("day15_2")
    val result2 = moveBoxes(input2)
    println("result2: $result2")
    check(result2 == 10092L)

    val input3 = Utils.readInput("day15_3")
    val result3 = moveBoxes(input3)
    println("result3: $result3")

    val result1_2 = moveBoxes2(Utils.readInput("day15_4"))
  }

  private fun moveBoxes2(input: List<String>): Long {
    val newInput = input.map { line ->
      line.
      replace("#", "##").
      replace(".", "..").
      replace("O", "[]").
      replace("@", "@.")
    }
    var (box, movements, robotPos) = parseInput(newInput)

    println(box)

    for (c in movements) {
      robotPos = moveRobot(c, box, robotPos)

      println(box)
    }

    val score = countScore(box)

    return score
  }

  private fun moveBoxes(input: List<String>): Long {

    var (box, movements, robotPos) = parseInput(input)

    for (c in movements) {
      robotPos = moveRobot(c, box, robotPos)

      println(box)
    }

    val score = countScore(box)

    return score
  }

  private fun countScore(box: CharGrid): Long {
    var sum = 0L

    for (y in 0..<box.height) {
      for (x in 0..<box.width) {
        if (box.getAt(x, y) == 'O') {
          sum += y * 100 + x
        }
      }
    }

    return sum
  }

  private fun moveRobot(dir: Char, box: CharGrid, robotPos: Point): Point {
    println(dir)

    val direction = moveMap[dir] ?: Direction.Up
    var nextPos = robotPos + direction
    val nextChar = box.getAt(nextPos)
    when (nextChar) {
      '.' -> {
        box.setAt(robotPos, '.')
        box.setAt(nextPos, '@')
      }

      '#' -> {
        nextPos = robotPos
      }

      'O' -> {
        var behindCrateChar:Char
        var behindCratePos = nextPos
        do {
          behindCratePos += direction
          behindCrateChar = box.getAt(behindCratePos)
        } while (behindCrateChar == 'O')
        if (behindCrateChar == '#') {
          nextPos = robotPos
        } else if (behindCrateChar == '.') {
          box.setAt(robotPos, '.')
          box.setAt(behindCratePos, 'O')
          box.setAt(robotPos + direction, '@')
        }
      }

      '[',']' -> {
        when(direction) {
          Direction.Left,Direction.Right -> {
            var behindCrateChar:Char
            var behindCratePos = nextPos
            do {
              behindCratePos += direction
              behindCrateChar = box.getAt(behindCratePos)
            } while (behindCrateChar == '[' || behindCrateChar == ']')
            if (behindCrateChar == '#') {
              nextPos = robotPos
            } else if(behindCrateChar == '.') {
              box.setAt(robotPos, '.')
              box.setAt(robotPos + direction, '@')
              box.setAt(behindCratePos, 'O')
              val startX= min(behindCratePos.x,(robotPos+direction).x)
              val endX= max(behindCratePos.x,(robotPos+direction).x)
              for(x in startX..<endX step 2) {
                box.setAt(x,robotPos.y,'[')
                box.setAt(x+1,robotPos.y,']')
              }
            }
          }
          Direction.Up,Direction.Down -> {
            val otherChar=if(nextChar=='[') ']' else '['
            val behindCrateChars:Array<Char> = arrayOf('[',']')
            var behindCratePos = nextPos
            do {
              behindCratePos += direction
              behindCrateChars[0] = box.getAt(behindCratePos)
              val otherPos = if(nextChar=='[') behindCratePos+Direction.Right else behindCratePos+Direction.Left
              behindCrateChars[1] = box.getAt(otherPos)
            } while (behindCrateChars[0] == nextChar && behindCrateChars[1] == otherChar)

            if (behindCrateChars[0] == '.' && behindCrateChars[1] == '.') {
              val otherRobotPos=if(nextChar=='[') robotPos+direction+Direction.Right else robotPos+direction+Direction.Left
              box.setAt(robotPos, '.')
              box.setAt(otherRobotPos, '.')
              box.setAt(behindCratePos, nextChar)
              val otherPos = if(nextChar=='[') behindCratePos+Direction.Right else behindCratePos+Direction.Left
              box.setAt(otherPos,otherChar)
              box.setAt(robotPos + direction, '@')
            } else {
              nextPos = robotPos
            }
          }
        }
      }

      else -> {
        nextPos = robotPos
      }
    }

    return nextPos
  }

  private fun parseInput(input: List<String>): Triple<CharGrid, String, Point> {
    val gridIndex = input.indexOfFirst { line -> line.isBlank() }

    val box = CharGrid(input.subList(0, gridIndex))
    val movements = input.subList(gridIndex + 1, input.size).joinToString(separator = "")

    var roboPos: Point? = null

    for (y in 0..<box.height) {
      for (x in 0..<box.width) {
        if (box.getAt(x, y) == '@') {
          roboPos = Point(x, y)
          break
        }
      }
      if (roboPos != null) break
    }

    if (roboPos == null) {
      throw ArrayIndexOutOfBoundsException("fail to get Robot position")
    }

    return Triple(box, movements, roboPos)
  }

}

fun main() {
  Day15().start()
}