package com.androidcodr.project_movie_app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//afisam lista de filme in recycleview;
//afisam titlul, data si posterul pt fiecare film
//cand se da click pe un film, trecem la pagina MovieDetailsActivity
class MovieAdapter(private var movies : List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){
    class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"  //url-ul posterelor
        //functia bindMovie, primeste obiectul "Movie" si il actualizeaza cu detaliile despre filmul respectiv
        fun bindMovie(movie: Movie){
            val movieTitle = itemView.findViewById<TextView>(R.id.title_movie)
            movieTitle.text = movie.title
            val releaseDATE = itemView.findViewById<TextView>(R.id.release_date_movie)
            releaseDATE.text = movie.releaseDate
            val moviePoster = itemView.findViewById<ImageView>(R.id.poster_movie)
            Glide.with(itemView).load(IMAGE_BASE + movie.poster).into(moviePoster)
        }
    }

    //creez un viewholder pt fiecare film care vreau sa il afisez in recycleview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder{
        val context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = movies.get(position) ///gasim pozitia unui film
        holder.bindMovie(movie)
        //listener pentru cand se da click pe un film
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MovieDetailActivity::class.java)
            //trimite datele despre film către MovieDetailActivity
            intent.putExtra("movie", movie)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

}


