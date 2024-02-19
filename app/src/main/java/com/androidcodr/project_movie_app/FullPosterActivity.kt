package com.androidcodr.project_movie_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class FullPosterActivity : AppCompatActivity() {

    private lateinit var posterUrls: List<String>
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_poster)

        //preiau lista de URL-uri pentru postere si pozitia curenta din intent
        posterUrls =intent.getStringArrayListExtra("posterUrls") ?: emptyList()
        currentPosition = intent.getIntExtra("position", 0)

        // afișez posterul curent
        val imageView = findViewById<ImageView>(R.id.full_poster_image)
        Glide.with(this)
            .load(posterUrls[currentPosition])
            .into(imageView)

        //setez un listener pentru butonul "Next"
        val btnNext = findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            showNextPoster() //apelez functia pentru a putea trece la urmatorul poster cand apas pe buton
        }
    }

    //urmatorul poster
    private fun showNextPoster() {
        currentPosition++ //pozitia curenta creste
        if (currentPosition >= posterUrls.size) {
            currentPosition = 0 // revin la primul poster dacă am ajuns la finalul listei
        }
        //afisez urmatorul poster
        val imageView = findViewById<ImageView>(R.id.full_poster_image)
        Glide.with(this)
            .load(posterUrls[currentPosition])
            .into(imageView)
    }
}
