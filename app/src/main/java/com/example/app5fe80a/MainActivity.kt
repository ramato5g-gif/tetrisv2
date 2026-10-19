package com.example.app5fe80a

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val game = TetrisEngine()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var boardView: BoardView
    private lateinit var status: TextView
    private lateinit var pauseButton: Button
    private val tick = object : Runnable {
        override fun run() {
            game.step(); refresh()
            handler.postDelayed(this, (650L - game.lines * 12L).coerceAtLeast(130L))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12,16,31)
        window.navigationBarColor = Color.rgb(12,16,31)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16.dp, 12.dp, 16.dp, 12.dp)
            setBackgroundColor(Color.rgb(12,16,31))
        }
        val heading = TextView(this).apply {
            text = "TETRIS"; textSize = 29f; setTextColor(Color.WHITE)
            gravity = Gravity.CENTER; setPadding(0, 0, 0, 6.dp)
        }
        layout.addView(heading)
        status = TextView(this).apply {
            textSize = 17f; setTextColor(Color.rgb(169,221,244)); gravity = Gravity.CENTER
            setPadding(0, 0, 0, 10.dp)
        }
        layout.addView(status)
        boardView = BoardView(this)
        layout.addView(boardView, LinearLayout.LayoutParams(-1, 0, 1f))
        fun button(label: String, action: () -> Unit): Button = Button(this).apply {
            text = label; setOnClickListener { action(); refresh() }
        }
        val controls = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        fun add(label: String, action: () -> Unit) {
            controls.addView(button(label, action), LinearLayout.LayoutParams(0, -2, 1f))
        }
        add("◀") { game.move(-1) }
        add("⟳") { game.rotate() }
        add("▶") { game.move(1) }
        add("▼") { game.step() }
        layout.addView(controls)
        val bottom = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        bottom.addView(button("DROP") { game.hardDrop() }, LinearLayout.LayoutParams(0,-2,1f))
        pauseButton = button("PAUSA") { game.togglePause() }
        bottom.addView(pauseButton, LinearLayout.LayoutParams(0,-2,1f))
        bottom.addView(button("NUOVA") { game.restart() }, LinearLayout.LayoutParams(0,-2,1f))
        layout.addView(bottom)
        setContentView(layout); refresh()
    }
    private fun refresh() {
        status.text = "PUNTI ${game.score}   •   RIGHE ${game.lines}" +
            when { game.gameOver -> "   •   GAME OVER"; game.paused -> "   •   IN PAUSA"; else -> "" }
        pauseButton.text = if (game.paused) "RIPRENDI" else "PAUSA"
        boardView.invalidate()
    }
    override fun onResume() { super.onResume(); handler.removeCallbacks(tick); handler.postDelayed(tick,650) }
    override fun onPause() { handler.removeCallbacks(tick); super.onPause() }
    private val Int.dp: Int get() = (this * resources.displayMetrics.density).toInt()

    inner class BoardView(context: Context) : View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val colors = intArrayOf(Color.TRANSPARENT, 0xff35c9e8.toInt(), 0xfff4d35e.toInt(),
            0xffb47cf5.toInt(), 0xff61d6a2.toInt(), 0xffef6c8b.toInt(), 0xff6a9cff.toInt(), 0xffffa85c.toInt())
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val size = minOf(width / TetrisEngine.WIDTH.toFloat(), height / TetrisEngine.HEIGHT.toFloat())
            val ox = (width - size*TetrisEngine.WIDTH)/2f
            val oy = (height - size*TetrisEngine.HEIGHT)/2f
            paint.color = 0xff1b2440.toInt()
            canvas.drawRoundRect(ox,oy,ox+size*10,oy+size*20,12f,12f,paint)
            for (r in 0 until TetrisEngine.HEIGHT) for (c in 0 until TetrisEngine.WIDTH) {
                drawCell(canvas,ox,oy,size,c,r,game.board[r][c])
            }
            if(!game.gameOver) game.activeCells().forEach { (c,r) ->
                if(c in 0 until 10 && r in 0 until 20) drawCell(canvas,ox,oy,size,c,r,game.kind+1)
            }
        }
        private fun drawCell(canvas: Canvas,ox:Float,oy:Float,s:Float,c:Int,r:Int,value:Int) {
            paint.color = if(value==0) 0xff2a3555.toInt() else colors[value]
            val gap=if(value==0) 1.3f else 1.8f
            canvas.drawRoundRect(ox+c*s+gap,oy+r*s+gap,ox+(c+1)*s-gap,oy+(r+1)*s-gap,3f,3f,paint)
        }
    }
}
