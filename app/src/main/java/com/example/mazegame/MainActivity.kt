package com.example.mazegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.mazegame.ui.theme.MazeGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MazeGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    MazeGame(Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun MazeGame(modifier: Modifier = Modifier) {
    val maze = listOf(
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1),
        listOf(1, 0, 1, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 1, 1, 1, 0, 1, 0, 1),
        listOf(1, 0, 1, 0, 0, 0, 1, 0, 1),
        listOf(1, 0, 1, 0, 1, 1, 1, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 1, 0, 1),
        listOf(1, 1, 1, 1, 1, 1, 1, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 1, 1, 1, 0, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 1, 1, 1, 1, 1, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 1, 0, 1),
        listOf(1, 1, 1, 1, 1, 0, 1, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 1, 0, 1),
        listOf(1, 0, 1, 1, 1, 1, 1, 0, 1),
        listOf(1, 0, 1, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 1, 1, 1, 1, 1, 1, 1)
    )

    var playerX by remember { mutableStateOf(1) }
    var playerY by remember { mutableStateOf(1) }
    var isGameWon by remember { mutableStateOf(false) }

    if (playerX == 1 && playerY == 14) {
        isGameWon = true
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Top black bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.Black)
        )

        // Maze canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        val (dx, dy) = dragAmount
                        change.consume()
                        when {
                            dx > 50 -> if (maze[playerY][playerX + 1] == 0) playerX += 1
                            dx < -50 -> if (maze[playerY][playerX - 1] == 0) playerX -= 1
                            dy > 50 -> if (maze[playerY + 1][playerX] == 0) playerY += 1
                            dy < -50 -> if (maze[playerY - 1][playerX] == 0) playerY -= 1
                        }
                    }
                }
        ) {
            val cellSize = size.width / maze[0].size

            for (i in maze.indices) {
                for (j in maze[i].indices) {
                    val color = when {
                        i == playerY && j == playerX -> Color.Red
                        maze[i][j] == 1 -> Color.Black
                        else -> Color.White
                    }
                    drawRect(
                        color = color,
                        topLeft = Offset(j * cellSize, i * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }

            if (isGameWon) {
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.GREEN
                        textSize = 100f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText("You Win!", size.width / 2f, size.height / 2f, paint)
                }
            }
        }
    }
}
