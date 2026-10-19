package com.example.app5fe80a

import kotlin.random.Random

class TetrisEngine {
    companion object {
        const val WIDTH = 10
        const val HEIGHT = 20
        private val SHAPES = arrayOf(
            arrayOf(intArrayOf(0,1),intArrayOf(1,1),intArrayOf(2,1),intArrayOf(3,1)),
            arrayOf(intArrayOf(1,0),intArrayOf(2,0),intArrayOf(1,1),intArrayOf(2,1)),
            arrayOf(intArrayOf(1,0),intArrayOf(0,1),intArrayOf(1,1),intArrayOf(2,1)),
            arrayOf(intArrayOf(1,0),intArrayOf(2,0),intArrayOf(0,1),intArrayOf(1,1)),
            arrayOf(intArrayOf(0,0),intArrayOf(1,0),intArrayOf(1,1),intArrayOf(2,1)),
            arrayOf(intArrayOf(0,0),intArrayOf(0,1),intArrayOf(1,1),intArrayOf(2,1)),
            arrayOf(intArrayOf(2,0),intArrayOf(0,1),intArrayOf(1,1),intArrayOf(2,1))
        )
    }
    val board = Array(HEIGHT) { IntArray(WIDTH) }
    var score = 0; private set
    var lines = 0; private set
    var gameOver = false; private set
    var paused = false; private set
    var kind = 0; private set
    var nextKind = Random.nextInt(7); private set
    var x = 3; private set
    var y = 0; private set
    private var cells = SHAPES[0]

    init { restart() }
    fun restart() {
        board.forEach { it.fill(0) }; score = 0; lines = 0
        gameOver = false; paused = false; nextKind = Random.nextInt(7); spawn()
    }
    private fun spawn() {
        kind = nextKind; nextKind = Random.nextInt(7)
        cells = SHAPES[kind].map { it.clone() }.toTypedArray(); x = 3; y = 0
        if (!fits(cells, x, y)) gameOver = true
    }
    private fun fits(shape: Array<IntArray>, nx: Int, ny: Int): Boolean = shape.all {
        val cx = nx + it[0]; val cy = ny + it[1]
        cx in 0 until WIDTH && cy in 0 until HEIGHT && board[cy][cx] == 0
    }
    fun activeCells(): List<Pair<Int,Int>> = cells.map { Pair(x + it[0], y + it[1]) }
    fun move(dx: Int): Boolean {
        if (gameOver || paused || !fits(cells, x + dx, y)) return false
        x += dx; return true
    }
    fun rotate(): Boolean {
        if (gameOver || paused) return false
        if (kind == 1) return true
        val rotated = cells.map { intArrayOf(3 - it[1], it[0]) }.toTypedArray()
        for (shift in intArrayOf(0,-1,1,-2,2)) if (fits(rotated, x + shift, y)) {
            cells = rotated; x += shift; return true
        }
        return false
    }
    fun step(): Boolean {
        if (gameOver || paused) return false
        if (fits(cells, x, y + 1)) { y++; return true }
        lock(); return true
    }
    fun hardDrop() {
        if (gameOver || paused) return
        while (fits(cells, x, y + 1)) { y++; score += 2 }
        lock()
    }
    private fun lock() {
        cells.forEach { val cx=x+it[0]; val cy=y+it[1]; if(cx in 0 until WIDTH && cy in 0 until HEIGHT) board[cy][cx]=kind+1 }
        var row=HEIGHT-1; var cleared=0
        while(row>=0) {
            if(board[row].all { it!=0 }) {
                for(r in row downTo 1) board[r] = board[r-1].clone()
                board[0] = IntArray(WIDTH); cleared++
            } else row--
        }
        if(cleared>0) { lines+=cleared; score+=intArrayOf(0,100,300,500,800)[cleared] }
        spawn()
    }
    fun togglePause() { if(!gameOver) paused=!paused }
}
