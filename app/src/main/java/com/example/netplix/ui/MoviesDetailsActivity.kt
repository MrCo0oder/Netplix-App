package com.example.netplix.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
    private val moviesViewModel: MovieViewModel by lazy { ViewModelProvider(this).get(MovieViewModel::class.java) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var movie: MovieModel = intent.getSerializableExtra("Movie") as MovieModel
        moviesViewModel.getMovie(movie.id)
        moviesViewModel.getMovieDetails().observe(this) { model ->
            if (model != null) {
                binding.mainProgressBar.visibility = GONE
                binding.parent.visibility = VISIBLE
                when (moviesViewModel.findMovie(movie.id)) {
                    true -> {
                        binding.likeBTN.setImageResource(R.drawable.filled_heart)
                    }

                    false -> {
                        binding.likeBTN.setImageResource(R.drawable.heart)
                    }
                }
                val backDropUrl: String = "https://image.tmdb.org/t/p/w500" + model.backdropPath
                val posterUrl: String = "https://image.tmdb.org/t/p/w500" + model.posterPath
                val info: String =
                    (getString(R.string.movie) + " | " + model.originalLanguage?.let {
                        Locale(
                            it
                        ).getDisplayName()
                    } + " | " + getString(R.string.FirstShow) + model.releaseDate)
                binding.likeBTN.setOnClickListener {
                    if (moviesViewModel.findMovie(movie.id)) {
                        binding.likeBTN.setImageResource(R.drawable.heart)
                        moviesViewModel.deleteMovie(movieId = movie.id)
                        Toast.makeText(
                            this@MoviesDetailsActivity,
                            getString(R.string.deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        moviesViewModel.insertMovie(movie)
                        binding.likeBTN.setImageResource(R.drawable.filled_heart)
                        Toast.makeText(
                            this@MoviesDetailsActivity,
                            getString(R.string.Added),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                binding.infoTV.text = info
                when (model.adult) {
                    true -> binding.adultTV.visibility = VISIBLE
                    else -> {
                        binding.adultTV.visibility = INVISIBLE
                    }
                }
                Picasso.get().load(posterUrl).placeholder(R.drawable.placeholder)
                    .into(binding.posterIV, object : Callback {
                        override fun onSuccess() {
                            binding.progressBar3.visibility = View.GONE
                        }

                        override fun onError(e: Exception) {
                            Log.d("details", e.message.toString())
                            Toast.makeText(
                                this@MoviesDetailsActivity,
                                e.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
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
                            Toast.makeText(
                                this@MoviesDetailsActivity,
                                e.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            binding.progressBar.visibility = View.GONE
                        }
                    })
                var str = ""
                for (i in model.genres!!) {
                    str += i?.name + " | "
                }
                binding.catsTV.text = str
                if (!model.genres.isEmpty()) {
                    binding.catsTV.text =
                        binding.catsTV.text.removeRange((binding.catsTV.text.length - 3)..(binding.catsTV.text.length - 1))
                }
                when (model.overview.isNullOrEmpty()) {
                    true -> {
                        binding.storyTV.text = getString(R.string.nostory)
                    }

                    false -> {
                        binding.storyTV.text = model.overview
                    }
                }
                binding.titleTV.text = model.title
                binding.ratingTV.text =
                    model.voteAverage?.toString().plus(getString(R.string.rate))
                binding.countTV.text = model.voteCount.toString()
                binding.popTV.text = model.popularity.toString()
                binding.backBtn.setOnClickListener(object : OnClickListener {
                    override fun onClick(v: View) {
                        val intent = Intent(this@MoviesDetailsActivity, MainActivity::class.java)
                        this@MoviesDetailsActivity.startActivity(intent)
                        finish()
                    }
                })
            } else {
                binding.mainProgressBar.visibility = VISIBLE
                binding.parent.visibility = GONE
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
    }

}

