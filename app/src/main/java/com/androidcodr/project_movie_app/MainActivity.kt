package com.androidcodr.project_movie_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movies: List<Movie>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_movies_list) //referinta pt recyclerview din activity_main.xml
        spinner = findViewById(R.id.year_spinner) //referinta pt spinner din activity_main.xml

        recyclerView.layoutManager = LinearLayoutManager(this) //afisez filmele intr-o lista verticala

        //obtin un client retrofit apeland metoda din RetrofitMovie
        val retrofit = RetrofitMovie.getClient()
        //creez o instanta a ApiInterface folosind clientul Retrofit
        val apiInterface = retrofit.create(ApiInterface::class.java)
        //solicit lista de filme folosindu-ma de metoda din interfata ApiInterface
        //aceasta metoda returneaza un obiect call care reprezinta cererea asincrona care urmeaza sa fie facuta de server
        val call = apiInterface.getMovieList()

        //aici fac cererea asincrona
        //folosesc callback pentru a primi raspunsul de la server
        call.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                val moviesResponse = response.body()
                moviesResponse?.let {
                    movies = it.movies //atribui lista de filme
                    movieAdapter = MovieAdapter(movies) //initializez movieAdapter cu lista de filme
                    recyclerView.adapter = movieAdapter //actualizez recyclerView cu lista de filme primita
                    setupSpinner() //apelez functia spinner-ului
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.e("MainActivity", "Error fetching movie data: ${t.message}", t)
            }
        })
    }

    private fun setupSpinner() {
        val years = mutableListOf<String>() // Creez o listă goală
        years.add("Toate filmele") // Adaug primul element pt spinner

        // Adaug anii din lista de filme la lista creata anterior
        movies.map { it.releaseDate }.distinct().forEach {
            if (it != null) {
                years.add(it)
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //setez primul item din spinner ca default
        spinner.setSelection(0, false)

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) { //sari peste daca primul item din spinner e selectat, daca nu filtreaza dupa data filmele
                    val selectedYear = parent?.getItemAtPosition(position).toString()
                    val filteredMovies = movies.filter { it.releaseDate == selectedYear }
                    movieAdapter.setMovies(filteredMovies) // Use movieAdapter to update the list
                } else {
                    movieAdapter.setMovies(movies) //afiseaza toate filmele daca prima optiune e selectata
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //movieAdapter.setMovies(movies)
                //nu mai trebuie, ca am facut ca prima optiune din spinner sa fie selectata automat
            }
        })
    }

}