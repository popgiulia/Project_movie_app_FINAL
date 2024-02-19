package com.androidcodr.project_movie_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SavedPostersAdapter(private val context: Context, private val posterUrls: List<String>) :
    RecyclerView.Adapter<SavedPostersAdapter.PosterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
        return PosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val posterUrl = posterUrls[position]
        // adaug imaginile
        Glide.with(context)
            .load(posterUrl)
            .into(holder.posterImageView)

        //am adaugat un setOnClickListener pt fiecare imagine din grid, astfel incat atunci cand facem click pe imagine suntem dusi la activitatea FullPosterActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FullPosterActivity::class.java)
            intent.putStringArrayListExtra("posterUrls", ArrayList(posterUrls))
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return posterUrls.size
    }

    inner class PosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImageView: ImageView = itemView.findViewById(R.id.image_poster)
    }
}
