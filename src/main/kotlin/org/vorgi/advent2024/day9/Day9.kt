package org.vorgi.org.vorgi.advent2024.day9

import org.vorgi.org.vorgi.Utils

class Day9 {

  data class DiskFile(var id: Int, var size: Int) {

  }

  fun scanInput(input: String): MutableList<DiskFile> {
    val result = mutableListOf<DiskFile>()

    for (i in 0 until input.length step 2) {
      val lenght = "${input[i]}".toInt()
      result += DiskFile(i / 2, lenght)
      if (i + 1 < input.length) {
        val space = "${input[i + 1]}".toInt()
        result += DiskFile(-1, space)
      }
    }

    return result
  }

  fun prettyPrint(files: List<DiskFile>) {
    for (file in files) {
      for (i in 0..<file.size) {
        print(if (file.id == -1) '.' else file.id)
      }
    }
    println()
  }

  fun organizeDisc2(input: String): Long {
    val files = scanInput(input)

    prettyPrint(files)

    val lastId = files.findLast { it.id != -1 }!!.id

    for (id in lastId downTo 0) {
      val currentFile = files.find { it.id == id }!!
      val indexOfCurrentFile = files.indexOf(currentFile)
      val nextFreeFile =
        files.find { it.id == -1 && it.size >= currentFile.size && files.indexOf(it) < indexOfCurrentFile }
      val indexOfNextFile = files.indexOf(nextFreeFile)
      if (nextFreeFile != null) {
        val remainingSize = nextFreeFile.size - currentFile.size
        nextFreeFile.id = currentFile.id
        nextFreeFile.size = currentFile.size
        if (remainingSize > 0) {
          files.add(indexOfNextFile + 1, DiskFile(-1, remainingSize))
        }
        currentFile.id = -1
      }
      // prettyPrint(files)
    }

    return calculateChecksum(files)
  }

  fun organizeDisc(input: String): Long {
    val files = scanInput(input)

    while (files.any { it.id == -1 }) {
      // take last file
      val lastFile = files.last()
      if (lastFile.id == -1) {
        files.removeLast()
      } else {
        val firstEmpty = files.find { it.id == -1 }
        if (firstEmpty == null) {
          break
        } else {
          val firstEmptyIndex = files.indexOf(firstEmpty)
          if (lastFile.size <= firstEmpty.size) {
            files.removeLast()
            firstEmpty.id = lastFile.id
            val remainingSize = firstEmpty.size - lastFile.size
            firstEmpty.size = lastFile.size
            if (remainingSize > 0) {
              val newEmpty = DiskFile(-1, remainingSize)
              files.add(firstEmptyIndex + 1, newEmpty)
            }
          } else {
            firstEmpty.id = lastFile.id
            lastFile.size -= firstEmpty.size
          }
        }
      }
    }

    return calculateChecksum(files)
  }

  fun calculateChecksum(input: List<DiskFile>): Long {
    var result = 0L

    var index = 0
    input.forEach { discFile ->
      for (i in 0..<discFile.size) {
        if (discFile.id != -1) {
          result += discFile.id * index
        }
        index++
      }

    }

    return result
  }

  fun start() {
    val input1 = "2333133121414131402"

    val result1 = organizeDisc(input1)
    println(result1)

    check(result1 == 1928L)

    val input2 = Utils.readInputAsText("day9")

    val result2 = organizeDisc(input2)

    check(result2 == 6291146824486L)

    println(result2)

    val result3 = organizeDisc2(input1)

    println(result3)

    val result4 = organizeDisc2(input2)

    println(result4)
  }
}

fun main() {
  Day9().start()
}