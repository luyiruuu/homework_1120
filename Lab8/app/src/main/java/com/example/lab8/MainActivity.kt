package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // 宣告 contacts 陣列，表示聯絡人資料
    private val contacts = ArrayList<Contact>()

    // 直接初始化 MyAdapter 物件
    private val myAdapter = MyAdapter(contacts)

    // 宣告 ActivityResultLauncher，處理 SecActivity 的回傳結果
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 取得回傳的 Intent，並從中取得聯絡人資訊
            val intent = result.data
            val name = intent?.getStringExtra("name").orEmpty()
            val phone = intent?.getStringExtra("phone").orEmpty()
            // 新增聯絡人資料並更新列表
            contacts.add(Contact(name, phone))
            myAdapter.notifyDataSetChanged()
        }
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

        // 使用 findViewById 方法取得元件並設定
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = myAdapter
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            // 使用 startForResult 前往 SecActivity
            val intent = Intent(this, SecActivity::class.java)
            startForResult.launch(intent)
        }
    }
}
