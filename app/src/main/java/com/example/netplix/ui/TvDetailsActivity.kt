package com.example.netplix.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.netplix.R
import com.example.netplix.databinding.ActivityTvDetailsBinding
import com.example.netplix.pojo.TvModel
import com.example.netplix.viewmodel.MovieViewModel
import com.example.netplix.viewmodel.TvViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
@Suppress("DEPRECATION")
@AndroidEntryPoint
class TvDetailsActivity : AppCompatActivity() {
    val tvViewModel: TvViewModel by lazy { ViewModelProvider(this).get(TvViewModel::class.java) }
    lateinit var binding: ActivityTvDetailsBinding
    lateinit var context: Context
    lateinit var cats: Map<Int, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cats = mapOf(
            10759 to getString(R.string.Action_Adventure),
            16 to getString(R.string.animation),
            35 to getString(R.string.comedy),
            80 to getString(R.string.Crime),
            99 to getString(R.string.Documentary),
            18 to getString(R.string.Drama),
            10751 to getString(R.string.Family),
            10762 to getString(R.string.Kids),
            9648 to getString(R.string.Mystery),
            10763 to getString(R.string.News),
            10764 to getString(R.string.Reality),
            10765 to getString(R.string.SciFi_Fantasy),
            10766 to getString(R.string.Soap),
            10767 to getString(R.string.Talk),
            10768 to getString(R.string.War_Politics),
            37 to getString(R.string.Western)
        )
        context = this
        var myObject: TvModel = intent.getSerializableExtra("Tv") as TvModel
        binding.likeBTN.setOnClickListener {
            tvViewModel.insertTv(myObject)
            Toast.makeText(context,getString(R.string.Added),Toast.LENGTH_SHORT).show()
        }
        val posterUrl: String = "https://image.tmdb.org/t/p/w500" + myObject.poster_path
        val info: String = (getString(R.string.tv_show)+" | "+  Locale(myObject.original_language).getDisplayName()+" | " + getString(R.string.FirstShow)+ myObject.first_air_date )
        binding.infoTV.text = info
        val backDropUrl: String = "https://image.tmdb.org/t/p/w500" + myObject.backdrop_path
        Picasso.get().load(backDropUrl).placeholder(R.drawable.placeholder)
            .into(binding.tvImg, object : Callback {
                override fun onSuccess() {
                    binding.progressBar.visibility = View.GONE
                }
                override fun onError(e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            })
        Picasso.get().load(posterUrl).placeholder(R.drawable.placeholder)
            .into(binding.posterIV, object : Callback {
                override fun onSuccess() {
                    binding.progressBar3.visibility = View.GONE
                }
                override fun onError(e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar3.visibility = View.GONE
                }
            })
        var str = ""
        for (i in myObject.genre_ids) {
            str += cats.get(i) + " | "
        }
        binding.catsTV.text = str
        if (!myObject.genre_ids.isEmpty())
        { binding.catsTV.text = binding.catsTV.text.removeRange((binding.catsTV.text.length - 3)..(binding.catsTV.text.length - 1))}
        when(myObject.overview.isNullOrEmpty()){true->{binding.storyTV.text = getString(R.string.nostory)} false ->{binding.storyTV.text =myObject.overview}}
        binding.titleTV.text = myObject.name
        binding.ratingTV.text = myObject.vote_average.toFloat().toString().plus(getString(R.string.rate))
        binding.countTV.text = myObject.vote_count.toString()
        binding.popTV.text = myObject.popularity.toString()

        binding.backBtn.setOnClickListener(object : View.OnClickListener {
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