package com.androidcodr.project_movie_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedPostersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var posterUrls: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_posters)

        // Obțin lista de URL-uri pentru postere
        posterUrls = intent.getStringArrayListExtra("posterUrls") ?: emptyList()

        // Inițializez RecyclerView
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val adapter = SavedPostersAdapter(this, posterUrls)
        recyclerView.adapter = adapter //atasez adaptorul
    }
}
