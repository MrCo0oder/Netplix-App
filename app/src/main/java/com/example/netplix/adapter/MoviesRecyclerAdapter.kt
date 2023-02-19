package com.example.netplix.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.databinding.MovieCardBinding
import com.example.netplix.pojo.MovieModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MoviesRecyclerAdapter(var context: Context) :
    RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder>() {

    lateinit var moviesList: List<MovieModel>
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

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override  fun  onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim))
    }
    fun setData(list:List<MovieModel>){
        moviesList=list
        notifyDataSetChanged()
    }
    fun clearData() {
        val data: List<MovieModel> = ArrayList()
        moviesList=data
        notifyDataSetChanged()
    }

    fun getItemAt(swipedPosition: Int): MovieModel {
        return moviesList.get(swipedPosition);
    }

}