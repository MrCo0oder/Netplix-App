package com.example.netplix.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.netplix.R
import com.example.netplix.databinding.ActivityMoviesDetailsBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.viewmodel.MovieViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
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
        val info: String = (myObject.release_date + " | " + "Movie | " + Locale(myObject.original_language).getDisplayName())
        binding.likeBTN.setOnClickListener {
            val viewModel: MovieViewModel by viewModels()
            viewModel.insertMovie(myObject)
            Toast.makeText(context,"ADDED",Toast.LENGTH_SHORT).show()
            binding.likeBTN.visibility= INVISIBLE
        }
        binding.infoTV.text=info
        if (myObject.adult) {
            binding.adultTV.visibility = VISIBLE
        }
        else
        {
            binding.adultTV.visibility = INVISIBLE
        }
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
        binding.catsTV.text = str
        if (!myObject.genre_ids.isEmpty())
        {binding.catsTV.text = binding.catsTV.text.removeRange((binding.catsTV.text.length - 3)..(binding.catsTV.text.length - 1))}
        binding.storyTV.text = myObject.overview
        binding.titleTV.text = myObject.original_title
        binding.ratingBar2.rating = (myObject.vote_average.toFloat()) / 2
        binding.countTV.text = myObject.vote_count.toString()
        binding.popTV.text = myObject.popularity.toString()

        binding.backBtn.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("curr",0)
                context.startActivity(intent)
                finish()
            }
        })


    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("curr",0)
        startActivity(intent)
        finish()
    }

}

