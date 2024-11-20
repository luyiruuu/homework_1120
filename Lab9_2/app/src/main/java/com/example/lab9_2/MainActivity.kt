package com.example.lab9_2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    // 延遲初始化變數

    private lateinit var edHeight: EditText
    private lateinit var btnCalculate: Button
    private lateinit var edAge: EditText
    private lateinit var tvWeightResult: TextView
    private lateinit var edWeight: EditText
    private lateinit var tvFatResult: TextView
    private lateinit var tvBmiResult: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var llProgress: LinearLayout
    private lateinit var btnBoy: RadioButton

    // 使用主執行緒的 Handler
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 綁定 UI 元件
        bindViews()

        // 計算按鈕點擊事件
        btnCalculate.setOnClickListener {
            if (validateInputs()) {
                runCalculationThread()
            }
        }
    }

    // 綁定 UI 元件的方法
    private fun bindViews() {
        btnCalculate = findViewById(R.id.btnCalculate)
        edHeight = findViewById(R.id.edHeight)
        edWeight = findViewById(R.id.edWeight)
        edAge = findViewById(R.id.edAge)
        tvWeightResult = findViewById(R.id.tvWeightResult)
        tvFatResult = findViewById(R.id.tvFatResult)
        tvBmiResult = findViewById(R.id.tvBmiResult)
        tvProgress = findViewById(R.id.tvProgress)
        progressBar = findViewById(R.id.progressBar)
        llProgress = findViewById(R.id.llProgress)
        btnBoy = findViewById(R.id.btnBoy)
    }

    // 驗證輸入是否合法的方法
    private fun validateInputs(): Boolean {
        return when {
            edHeight.text.isNullOrEmpty() -> {
                showToast("請輸入身高")
                false
            }
            edWeight.text.isNullOrEmpty() -> {
                showToast("請輸入體重")
                false
            }
            edAge.text.isNullOrEmpty() -> {
                showToast("請輸入年齡")
                false
            }
            else -> true
        }
    }

    // 顯示提示訊息
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 使用執行緒進行計算
    private fun runCalculationThread() {
        resetResults()

        Thread {
            for (progress in 1..100) {
                Thread.sleep(50)
                handler.post {
                    progressBar.progress = progress
                    tvProgress.text = "$progress%"
                }
            }

            // 計算 BMI、標準體重、體脂肪
            val height = edHeight.text.toString().toDouble()
            val weight = edWeight.text.toString().toDouble()
            val age = edAge.text.toString().toDouble()
            val bmi = weight / ((height / 100).pow(2))

            val (standardWeight, bodyFat) = if (btnBoy.isChecked) {
                (height - 80) * 0.7 to (1.39 * bmi + 0.16 * age - 19.34)
            } else {
                (height - 70) * 0.6 to (1.39 * bmi + 0.16 * age - 9)
            }

            handler.post {
                updateResults(bmi, standardWeight, bodyFat)
            }
        }.start()
    }

    // 重置結果顯示
    private fun resetResults() {
        tvWeightResult.text = "標準體重\n無"
        tvFatResult.text = "體脂肪\n無"
        tvBmiResult.text = "BMI\n無"
        progressBar.progress = 0
        tvProgress.text = "0%"
        llProgress.visibility = View.VISIBLE
    }

    // 更新計算結果
    private fun updateResults(bmi: Double, standardWeight: Double, bodyFat: Double) {
        llProgress.visibility = View.GONE
        tvWeightResult.text = "標準體重\n${String.format("%.2f", standardWeight)}"
        tvFatResult.text = "體脂肪\n${String.format("%.2f", bodyFat)}"
        tvBmiResult.text = "BMI\n${String.format("%.2f", bmi)}"
    }
}
