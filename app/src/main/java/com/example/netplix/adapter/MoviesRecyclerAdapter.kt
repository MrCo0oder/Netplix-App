package com.example.netplix.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.databinding.MovieCardBinding
import com.example.netplix.models.MovieModel

class MoviesRecyclerAdapter(var context: Context, val clickListener: (movie: MovieModel) -> Unit) :
    RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder>() {

    lateinit var moviesList: List<MovieModel>

    class ViewHolder(val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel) {
            binding.listItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding = MovieCardBinding.inflate(itemView, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(moviesList[position])
        holder.itemView.setOnClickListener {
            clickListener(moviesList[position])
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setData(list: List<MovieModel>) {
        moviesList = list
        notifyDataSetChanged()
    }

    fun clearData() {
        val data: List<MovieModel> = ArrayList()
        moviesList = data
        notifyDataSetChanged()
    }

    fun getItemAt(swipedPosition: Int): MovieModel {
        return moviesList.get(swipedPosition);
    }

}