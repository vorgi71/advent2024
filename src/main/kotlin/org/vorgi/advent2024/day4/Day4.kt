package org.vorgi.org.vorgi.advent2024.day4

import org.vorgi.org.vorgi.Utils

class Day4 {
  fun start() {
    val input1="""MMMSXXMASM
                |MSAMXMSMSA
                |AMXSXMAAMM
                |MSAMASMSMX
                |XMASAMXAMM
                |XXAMMXXAMA
                |SMSMSASXSS
                |SAXAMASAAA
                |MAMMMXMMMM
                |MXMXAXMASX""".trimMargin()

    val result1=findXmas(input1)
    println(result1)

    check(result1==18)

    val input2= Utils.readInputAsText("day4")

    val result2=findXmas(input2)

    println(result2)

    val result3=findX(input1)

    println(result3)

    check(result3==9)

    val result4=findX(input2)
    println(result4)
  }



  class Puzzle(val puzzle:List<String> ) {
    var width:Int=0
    var height:Int=0

    init {
      height=puzzle.size
      width=puzzle[0].length
    }

    fun getAt(x:Int,y:Int):Char {
      if(x in 0 until width && y in 0 until height) {
        return puzzle[x][y]
      } else {
        return '.'
      }
    }

    fun isXmas(x:Int, y:Int, dir:Pair<Int,Int>, string:String="XMAS"):Boolean {
      var cx=x
      var cy=y
      for(element in string) {
        if(getAt(cx,cy)!= element) {
          return false
        }
        cx+=dir.first
        cy+=dir.second
      }
      return true
    }

    fun isX(x: Int, y: Int): Boolean {
      if(getAt(x,y)!='A') {
        return false
      }

      var line1:String="${getAt(x-1,y-1)}${getAt(x+1,y+1)}"
      var line2:String="${getAt(x+1,y-1)}${getAt(x-1,y+1)}"

      if((line1=="MS" || line1=="SM") && (line2=="SM" || line2=="MS")) {
        return true
      }

      return false
    }
  }

  private fun findX(input: String): Int {
    var sum=0

    val puzzle = Puzzle(input.lines())

    for(y in 0..<puzzle.height) {
      for (x in 0 until puzzle.width) {
        if (puzzle.isX(x, y)) {
          sum++
        }
      }
    }

    return sum
  }

  private fun findXmas(input: String): Int {
    var sum=0

    val puzzle = Puzzle(input.lines())

    val directions=listOf(Pair(0,1),Pair(0,-1),Pair(1,0),Pair(-1,0),Pair(1,1),Pair(-1,-1),Pair(1,-1),Pair(-1,1))

    for(y in 0..<puzzle.height) {
      for(x in 0 until puzzle.width) {
        for(direction in directions) {
          if(puzzle.isXmas(x,y,direction)) {
            sum+=1
          }
        }
      }
    }

    return sum
  }
}

fun main() {
  Day4().start()
}