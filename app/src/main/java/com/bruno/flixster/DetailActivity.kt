package com.bruno.flixster

import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.bruno.flixster.Data.Movie
import com.bruno.flixster.UI.MOVIE_EXTRA
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers


private const val TAG = "DetailActivity"
private const val YOUTUBE_API_KEY="AIzaSyCsn91cZwPz1jTX7uKujen4Yb6UhkuoDDg"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
class DetailActivity : YouTubeBaseActivity() {
    private lateinit var tvTitle:TextView
    private lateinit var tvOverview:TextView

    private lateinit var ratingBar:RatingBar
    private lateinit var ytPlayerView:YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        tvTitle=findViewById(R.id.detail_title)
        tvOverview=findViewById(R.id.detail_overview)
        ratingBar=findViewById(R.id.detail_ratingBar)
        ytPlayerView=findViewById(R.id.yt_player)
        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating= movie.voteAverage.toFloat()
        val client = AsyncHttpClient()

        client.get(TRAILERS_URL.format(movie.movieId),object : JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG,"onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.e(TAG,"onSuccess")
                val results = json.jsonObject.getJSONArray("results")
                if (results.length()==0){
                    Log.e(TAG,"No movie trailers found.")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")
                initializeYoutube(youtubeKey)
            }
        })

    }

    private fun initializeYoutube(youtubeKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY, object: YouTubePlayer.OnInitializedListener
        {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG,"onInitializationSuccess")
                player?.cueVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                TODO("Not yet implemented")
            }

        })
    }
}