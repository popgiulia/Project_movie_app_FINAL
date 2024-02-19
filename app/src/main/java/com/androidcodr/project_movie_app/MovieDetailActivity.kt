package com.androidcodr.project_movie_app

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//pagina cu detaliile despre filmul selectat
//salvare poster
class MovieDetailActivity : AppCompatActivity() {

    // Constants for SharedPreferences
    private val PREF_NAME = "MyAppsPreferences"
    private val KEY_SAVED_POSTERS = "saved_poster"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_details)

        // Obține datele despre film din Intent-ul creat in MovieAdapter
        val movie = intent.getSerializableExtra("movie") as Movie

        //actualizeaza pagina cu detaliile despre film
        val moviePoster = findViewById<ImageView>(R.id.movie_poster_details)
        val movieTitle = findViewById<TextView>(R.id.movie_title_details)
        val releaseDate = findViewById<TextView>(R.id.movie_release_date_details)
        val ratingMovie =findViewById<TextView>(R.id.rating_movie_details)
        ratingMovie.text = movie.rating

        // posterul filmului
        val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
        movieTitle.text = movie.title
        releaseDate.text = movie.releaseDate
        Glide.with(this)
            .load(IMAGE_BASE_URL + movie.poster)
            .into(moviePoster)


        // butonul de salvare a posterului
        val btnSavePoster = findViewById<Button>(R.id.btnSavePoster)
        btnSavePoster.setOnClickListener {
            val posterUrl = IMAGE_BASE_URL + movie.poster
            if (posterUrl != null) {
                savePosterURL(posterUrl) //apelam functia de salvare a url-ului posterului
                savePoster(posterUrl) //apelam functia de salvare a posterului in memoria telefonului
            }
            else {
                Log.e("MovieDetailActivity", "Poster URL is null")
                Toast.makeText(this@MovieDetailActivity, "Eroare, Url-ul e null", Toast.LENGTH_SHORT).show()
            }
        }

        // butonul pentru a vedea posterele salvate
        val btnViewSavedPosters = findViewById<Button>(R.id.btnViewSavedPosters)
        btnViewSavedPosters.setOnClickListener {
            //ia lista cu posterele salvate si le afiseaza pe pagina SavedPostersActivity
            val posterUrls = getSavedPosterUrls()
            val intent = Intent(this, SavedPostersActivity::class.java)
            intent.putStringArrayListExtra("posterUrls", ArrayList(posterUrls))
            startActivity(intent)
        }
    }

    //salvam posterul
    private fun savePoster(posterUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(posterUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveImageToInternalStorage(resource) //apelam la functia de salvare interna
                    saveImageToExternalStorage(resource) //apelam functia de salvare externa
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    //
                }
            })
    }

    // Funcție pentru a salva posterul în stocarea internă
    private fun saveImageToInternalStorage(bitmap: Bitmap) {
        val contextWrapper = ContextWrapper(applicationContext)
        val directory = contextWrapper.getDir("movie_posters", Context.MODE_PRIVATE)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "poster_$timeStamp.jpg"
        val file = File(directory, fileName)

        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            Toast.makeText(this@MovieDetailActivity, "Posterul a fost salvat intern", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@MovieDetailActivity, "Eroare la salvarea posterului", Toast.LENGTH_SHORT).show()
        }
    }

    // Funcția pentru a salva imaginea în stocarea externă
    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        if (isExternalStorageWritable()) {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "poster_$timeStamp.jpg"
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, fileName)
            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                //actualizez galeria de imagini
                MediaScannerConnection.scanFile(
                    applicationContext,
                    arrayOf(file.absolutePath),
                    arrayOf("image/jpeg"),
                    null
                )
                Toast.makeText(this@MovieDetailActivity, "Posterul a fost salvat extern", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@MovieDetailActivity, "Eroare la salvarea posterului", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@MovieDetailActivity, "Nu am putut sscrie in stocarea externa", Toast.LENGTH_SHORT).show()
        }
    }

    // Verific dacă pot scrie in stocarea externa
    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    //metoda getSharedPreferences returneaza un obiect care permite stocarea si recuperarea datelor din aplicatie
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    //functie pentru a obtine lista de url-uri ale posterelor salvate
    private fun getSavedPosterUrls(): List<String> {
        val sharedPreferences = getSharedPreferences(this) //instanta sharedPreferences
        val gson = Gson() //obtinem un obiect GSON pt serializare/deserializare
        val json = sharedPreferences.getString(KEY_SAVED_POSTERS, null) //obtinem valoare stocata in sharedPreferences sun cheia KEY_SAVED_POSTERS
        val type = object : TypeToken<List<String>>() {}.type //obeict de tip TypeToken  pentru a specifica tipul de date pe care il deserializez din json
        return gson.fromJson(json, type) ?: listOf() //obtinem lista de url-uri
    }

    //salvare url poster in lista cu url-uri
    private fun savePosterURL(posterUrl: String) {
        val posterUrls = getSavedPosterUrls().toMutableList() //lista cu url-urile posterelor
        posterUrls.add(posterUrl) // adaug noul url al posterului
        val sharedPreferences = getSharedPreferences(this) //instanta sharedPreferences
        val editor = sharedPreferences.edit() //editor pt a putea modifica SharedPreferences
        val gson = Gson()
        val json = gson.toJson(posterUrls) //convertesc la json lista de url-uri
        editor.putString(KEY_SAVED_POSTERS, json) //o salvez in SharedPreferences
        editor.apply() //aplic modificarile facute
    }

}