package com.example.netplix.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.netplix.R
import com.example.netplix.databinding.ActivityTvDetailsBinding
import com.example.netplix.pojo.TvModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TvDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTvDetailsBinding
    lateinit var context: Context
    var cats = mapOf(
        10759 to "Action & Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        10762 to "Kids",
        9648 to "Mystery",
        10763 to "News",
        10764 to "Reality",
        10765 to "Sci-Fi & Fantasy",
        10766 to "Soap",
        10767 to "Talk",
        10768 to "War & Politics",
        37 to "Western"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        var myObject: TvModel = intent.getSerializableExtra("Tv") as TvModel
        val url: String = "https://image.tmdb.org/t/p/w500" + myObject.poster_path
        val info: String =
            (myObject.first_air_date + " | " + "Movie | " + myObject.original_language)

        Picasso.get().load(url).placeholder(R.drawable.placeholder)
            .into(binding.tvImg, object : Callback {
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
        if (!myObject.genre_ids.isEmpty())
            binding.infoTV.text =
                binding.infoTV.text.removeRange((binding.infoTV.text.length - 3)..(binding.infoTV.text.length - 1))
        binding.storyTV.text = myObject.overview
        binding.titleTV.text = myObject.original_name
        binding.ratingBar2.rating = (myObject.vote_average.toFloat()) / 2
        binding.countTV.text = myObject.vote_count.toString()
        binding.popTV.text = myObject.popularity.toString()

        binding.backBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("curr",1)
                context.startActivity(intent)
                finish()
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("curr",1)
        startActivity(intent)
        finish()
    }
}