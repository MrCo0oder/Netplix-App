package com.example.netplix.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.netplix.R
import com.example.netplix.databinding.ActivityMoviesDetailsBinding
import com.example.netplix.pojo.MovieModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class MoviesDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityMoviesDetailsBinding
    lateinit var context: Context

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        var cats = mapOf(
            28 to "Action",
            12 to "Adventure",
            16 to "Animation",
            35 to "Comedy",
            80 to "Crime",
            99 to "Documentary",
            18 to "Drama",
            10751 to "Family",
            14 to "Fantasy",
            36 to "History",
            27 to "Horror",
            10402 to "Music",
            9648 to "Mystery",
            10749 to "Romance",
            878 to "Science Fiction",
            10770 to "TV Movie",
            53 to "Thriller",
            10752 to "War",
            37 to "Western"
        )
        var myObject: MovieModel = intent.getSerializableExtra("Movie") as MovieModel
        val url: String = "https://image.tmdb.org/t/p/w500" + myObject.poster_path
        val info: String = (myObject.release_date + " | " + "Movie | " + myObject.original_language)

        Picasso.get().load(url).placeholder(R.drawable.placeholder)
            .into(binding.movieImg, object : Callback {
                override fun onSuccess() {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onError(e: Exception) {
                    Log.d("details", e.message.toString())
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            })
        var str = ""
        for (i in myObject.genre_ids) {
            str += cats.get(i) + " | "
        }
        binding.infoTV.text = str
        binding.infoTV.text = binding.infoTV.text.removeRange((binding.infoTV.text.length - 3)..(binding.infoTV.text.length - 1))
        binding.storyTV.text = myObject.overview
        binding.titleTV.text = myObject.title
        binding.ratingBar2.rating = (myObject.vote_average.toFloat()) / 2
        binding.countTV.text = myObject.vote_count.toString()
        binding.popTV.text = myObject.popularity.toString()

        binding.backBtn.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                finish()
            }
        })
        if (myObject.adult) {
            binding.adultTV.visibility = VISIBLE
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

