package com.example.lab9_1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // 建立兩個數值，用於計算兔子與烏龜的進度
    private var progressRabbit = 0
    private var progressTurtle = 0
    // 建立變數以利後續綁定元件
    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    // 建立 Handler 變數接收訊息
    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            1 -> {
                sbRabbit.progress = progressRabbit
                if (progressRabbit >= 100 && progressTurtle < 100) {
                    showToast("兔子勝利")
                    btnStart.isEnabled = true
                }
            }
            2 -> {
                sbTurtle.progress = progressTurtle
                if (progressTurtle >= 100 && progressRabbit < 100) {
                    showToast("烏龜勝利")
                    btnStart.isEnabled = true
                }
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 綁定元件
        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)

        // 設定開始按鈕的監聽器
        btnStart.setOnClickListener {
            btnStart.isEnabled = false
            progressRabbit = 0
            progressTurtle = 0
            sbRabbit.progress = 0
            sbTurtle.progress = 0
            runRabbit()
            runTurtle()
        }
    }

    // 顯示提示訊息的方法
    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    // 用 Thread 模擬兔子移動
    private fun runRabbit() {
        Thread {
            val sleepProbability = arrayOf(true, true, false)
            while (progressRabbit < 100 && progressTurtle < 100) {
                try {
                    Thread.sleep(100)
                    if (sleepProbability.random()) {
                        Thread.sleep(300)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressRabbit += 3
                handler.sendEmptyMessage(1)
            }
        }.start()
    }

    // 用 Thread 模擬烏龜移動
    private fun runTurtle() {
        Thread {
            while (progressTurtle < 100 && progressRabbit < 100) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressTurtle += 1
                handler.sendEmptyMessage(2)
            }
        }.start()
    }
}
