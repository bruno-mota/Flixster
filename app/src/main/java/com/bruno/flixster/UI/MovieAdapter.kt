package com.bruno.flixster.UI

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bruno.flixster.Data.Movie
import com.bruno.flixster.DetailActivity
import com.bruno.flixster.R
import com.bumptech.glide.Glide
import android.util.Pair as UtilPair

const val MOVIE_EXTRA = "MOVIE_EXTRA"


class MovieAdapter(private val context: Context, private val movies: List<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false)
        Log.i("Adapter", "onCreate")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        Log.i("Adapter", "onBindViewHolder")
    }

    override fun getItemCount()= movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val title = itemView.findViewById<TextView>(R.id.title)
        private val overview = itemView.findViewById<TextView>(R.id.overview)
        private val poster = itemView.findViewById<ImageView>(R.id.posterImg)

        init{
            itemView.setOnClickListener(this)
        }
        fun bind(movie:Movie){
            Log.i("Adapter", "Inside bind fun")
            title.text = movie.title
            overview.text=movie.overview
            val image: String
            val orientation = context.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                image = movie.backDropImageUrl
            }else{
                image = movie.posterImageUrl
            }

            Glide.with(context).load(image).placeholder(R.drawable.popcorn_time).into(poster)
        }

        override fun onClick(v: View?) {

            Log.i("Adapter", "onCLick")
            val movie = movies[adapterPosition]
            val intent = Intent(context,DetailActivity::class.java)
            /*val p1 = Pair.create<View,String>(title, "title")
            val p2 = Pair.cr<View,String>(overview, "overview")
            //intent.putExtra(DetailActivity.)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,p1,p2)*/
            intent.putExtra(MOVIE_EXTRA,movie)
            context.startActivity(intent)
        }
    }
}
