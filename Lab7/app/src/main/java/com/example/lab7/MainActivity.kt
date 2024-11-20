package com.example.lab7

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupWindowInsets()
        setupViews()
    }

    private fun setupWindowInsets() {
        findViewById<View>(R.id.main).apply {
            ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )
                insets
            }
        }
    }

    private fun setupViews() {
        // View binding instead of findViewById
        val spinner: Spinner = findViewById(R.id.spinner)
        val listView: ListView = findViewById(R.id.listView)
        val gridView: GridView = findViewById(R.id.gridView)

        // Using immutable list where possible
        val count = buildList {
            for (i in 1..getImageCount()) {
                add("${i}個")
            }
        }

        val items = createItems()

        setupAdapters(spinner, gridView, listView, count, items)
    }

    private fun getImageCount(): Int {
        return resources.obtainTypedArray(R.array.image_list).use { array ->
            array.length()
        }
    }

    private fun createItems(): List<Item> {
        return resources.obtainTypedArray(R.array.image_list).use { array ->
            buildList {
                for (index in 0 until array.length()) {
                    add(Item(
                        photo = array.getResourceId(index, 0),
                        name = "水果${index + 1}",
                        price = (10..100).random()
                    ))
                }
            }
        }
    }

    private fun setupAdapters(
        spinner: Spinner,
        gridView: GridView,
        listView: ListView,
        count: List<String>,
        items: List<Item>
    ) {
        // Spinner adapter
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            count
        )

        // GridView setup
        gridView.apply {
            numColumns = 3
            adapter = MyAdapter(context, items, R.layout.adapter_vertical)
        }

        // ListView adapter
        listView.adapter = MyAdapter(this, items, R.layout.adapter_horizontal)
    }
}