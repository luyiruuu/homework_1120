package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sec)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 使用 findViewById 取得元件
        val edName = findViewById<EditText>(R.id.edName)
        val edPhone = findViewById<EditText>(R.id.edPhone)

        findViewById<Button>(R.id.btnSend).setOnClickListener {
            // 判斷是否輸入資料
            when {
                edName.text.isNullOrEmpty() -> showToast("請輸入姓名")
                edPhone.text.isNullOrEmpty() -> showToast("請輸入電話")
                else -> {
                    // 使用 Intent 傳遞資料
                    val intent = Intent().apply {
                        putExtra("name", edName.text.toString())
                        putExtra("phone", edPhone.text.toString())
                    }
                    // 回傳聯絡人資料
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    // 建立 showToast 方法顯示提示訊息
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
