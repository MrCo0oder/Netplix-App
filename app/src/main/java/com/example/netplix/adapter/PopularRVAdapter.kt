package com.example.netplix.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.databinding.MovieCardBinding
import com.example.netplix.pojo.MovieModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PopularRVAdapter(var context: Context, var list: List<MovieModel>) :
    RecyclerView.Adapter<PopularRVAdapter.ViewHolder>() {
    class ViewHolder(val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:MovieModel){
            binding.listItem=item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding=MovieCardBinding.inflate(itemView,parent,false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url: String = "https://image.tmdb.org/t/p/w500" + list[position].poster_path
        Picasso.get().load(url).into(holder.binding.movieImgv, object : Callback {

            override fun onSuccess() {
                    holder.binding.progressBar2.visibility = View.GONE
            }

            override fun onError(e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        })
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override  fun  onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.post))
    }

}