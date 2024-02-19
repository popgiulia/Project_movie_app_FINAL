package com.androidcodr.project_movie_app

import retrofit2.Call //asta am importat-o ca sa realizez apeluri asincrone catre serverul web
import retrofit2.http.GET // pentru a accesa url-ul filmelor

interface ApiInterface {

    @GET("/3/movie/popular?api_key=bbf5a3000e95f1dddf266b5e187d4b21") //folosesc o cerere get catre ruta specificata
    fun getMovieList(): Call<Movies> //metoda ca sa obtin lista de filme de la serverul web si sa o adaug in Movies
}