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
    lateinit var cats: Map<Int, String>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
         cats = mapOf(
            28 to getString(R.string.action),
            12 to getString(R.string.adventure),
            16 to getString(R.string.animation),
            35 to getString(R.string.comedy),
            80 to getString(R.string.Crime),
            99 to getString(R.string.Documentary),
            18 to getString(R.string.Drama),
            10751 to getString(R.string.Family),
            14 to getString(R.string.Fantasy),
            36 to getString(R.string.History),
            27 to getString(R.string.Horror),
            10402 to getString(R.string.Music),
            9648 to getString(R.string.Mystery),
            10749 to getString(R.string.Romance),
            878 to getString(R.string.science_fiction),
            10770 to getString(R.string.TV_Movie),
            53 to getString(R.string.Thriller),
            10752 to getString(R.string.War),
            37 to getString(R.string.Western)
        )

        var myObject: MovieModel = intent.getSerializableExtra("Movie") as MovieModel
        val backDropUrl: String = "https://image.tmdb.org/t/p/w500" + myObject.backdrop_path
        val posterUrl: String = "https://image.tmdb.org/t/p/w500" + myObject.poster_path
        val info: String = (getString(R.string.movie)+" | "+  Locale(myObject.original_language).getDisplayName()+" | " + getString(R.string.FirstShow)+ myObject.release_date )
        binding.likeBTN.setOnClickListener {
            val viewModel: MovieViewModel by viewModels()
            viewModel.insertMovie(myObject)
            Toast.makeText(context,getString(R.string.Added),Toast.LENGTH_SHORT).show()
        }
        binding.infoTV.text=info
        when(myObject.adult)
        {
            true ->binding.adultTV.visibility = VISIBLE
            else -> { binding.adultTV.visibility = INVISIBLE }
        }
        Picasso.get().load(posterUrl).placeholder(R.drawable.placeholder)
            .into(binding.posterIV, object : Callback {
                override fun onSuccess() {
                    binding.progressBar3.visibility = View.GONE
                }
                override fun onError(e: Exception) {
                    Log.d("details", e.message.toString())
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar3.visibility = View.GONE
                }
            })
        Picasso.get().load(backDropUrl).placeholder(R.drawable.placeholder)
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
        when(myObject.overview.isNullOrEmpty()){true->{binding.storyTV.text = getString(R.string.nostory)} false ->{binding.storyTV.text =myObject.overview}}
        binding.titleTV.text = myObject.title
        binding.ratingTV.text = myObject.vote_average.toFloat().toString().plus(getString(R.string.rate))
        binding.countTV.text = myObject.vote_count.toString()
        binding.popTV.text = myObject.popularity.toString()
        binding.backBtn.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                finish()
            }
        })

    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
    }

}

