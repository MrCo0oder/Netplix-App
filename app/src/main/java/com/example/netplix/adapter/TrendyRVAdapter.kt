package com.example.netplix.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.databinding.TrendyMovieCardBinding
import com.example.netplix.pojo.MovieModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TrendyRVAdapter(var context: Context, var trendyMovieList: List<MovieModel>) :
    RecyclerView.Adapter<TrendyRVAdapter.ViewHolder>() {
    class ViewHolder(val binding: TrendyMovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:MovieModel){
            binding.item=item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding=TrendyMovieCardBinding.inflate(itemView,parent,false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url: String = "https://image.tmdb.org/t/p/w500" + trendyMovieList[position].poster_path
        Picasso.get().load(url).into(holder.binding.movieImgv, object : Callback {
            override fun onSuccess() {
                    holder.binding.progressBar2.visibility = View.GONE
            }

            override fun onError(e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        })
        holder.bind(trendyMovieList[position])
    }

    override fun getItemCount(): Int {
        return trendyMovieList.size
    }

    override  fun  onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.post))
    }

}