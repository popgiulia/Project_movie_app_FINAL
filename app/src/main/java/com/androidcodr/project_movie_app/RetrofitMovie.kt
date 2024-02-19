package com.androidcodr.project_movie_app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//creez un client retrofit ca sa pot face cereri catre serverul API
object RetrofitMovie {
    private const val BASE_URL = "https://api.themoviedb.org" //ur-ul serviciului web
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) { //verific daca retrofit e deja initializat sau nu
            retrofit = Retrofit.Builder() //daca nu e, creez un nou obiect Retrofit
                .baseUrl(BASE_URL) //adaug url-ul de mai sus
                .addConverterFactory(GsonConverterFactory.create()) //convertor gson
                .build()
        }
        return retrofit!! //returnez obiectul retrofit, care trebuie sa nu fie null(!!)
    }
}
