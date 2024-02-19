package com.androidcodr.project_movie_app

import com.google.gson.annotations.SerializedName

//am definit o clasa ca sa stochez lista de filme
class Movies (
    @SerializedName("results")
    val movies : List<Movie> //lista de filme, reprezentand filmele din clasa "Movie"

)
