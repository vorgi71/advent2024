package org.vorgi.org.vorgi.advent2024.day6

import processing.core.PApplet

class Day6 : PApplet() {
  private lateinit var grid: Array<IntArray>
  private var gridSize:Int = 0
  private val cellSize = 60f

  override fun settings() {
    gridSize=args[0].toInt()
    size((gridSize * cellSize).toInt(), (gridSize * cellSize).toInt())
  }

  override fun setup() {
    // Initialize your grid with all cells as 0 (empty)
    grid = Array(gridSize) { IntArray(gridSize) { 0 } }
    background(220)
    // Prevent drawing multiple times per frame
    noLoop()
  }

  override fun draw() {
    background(220)
    // Draw grid
    for (x in 0 until gridSize) {
      for (y in 0 until gridSize) {
        // Different colors based on grid value
        when (grid[x][y]) {
          0 -> fill(255) // White for empty
          1 -> fill(0) // Black for filled
          2 -> fill(200F, 0F, 0F) // Red for special
        }
        stroke(150) // Grid line color
        rect(x * cellSize, y * cellSize, cellSize, cellSize)
      }
    }
  }

  override fun mousePressed() {
    // Calculate which grid cell was clicked
    val gridX = (mouseX / cellSize).toInt()
    val gridY = (mouseY / cellSize).toInt()

    // Ensure click is within grid
    if (gridX in 0 until gridSize && gridY in 0 until gridSize) {
      // Toggle the cell state (0 -> 1 -> 0)
      grid[gridX][gridY] = (grid[gridX][gridY] + 1) % 2

      // Redraw the grid
      redraw()
    }
  }
}

fun main() {
  PApplet.main(Day6::class.java,"20")
}