package com.androidcodr.project_movie_app

import com.google.gson.annotations.SerializedName;
import java.io.Serializable

//am definit un model de date ca sa stochez informatiile despre filme
data class Movie (
    @SerializedName("id")
    val id : String?,

    @SerializedName("title")
    val title : String?,

    @SerializedName("poster_path")
    val poster : String?,

    @SerializedName("release_date")
    val releaseDate : String?,

    @SerializedName("vote_average")
    val rating: String?

) : Serializable