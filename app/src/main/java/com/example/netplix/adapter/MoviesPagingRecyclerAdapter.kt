package com.example.netplix.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.databinding.MovieCardBinding
import com.example.netplix.pojo.MovieModel

class MoviesPagingRecyclerAdapter :
    PagingDataAdapter<MovieModel, MoviesPagingRecyclerAdapter.ViewHolder>(diffCallback) {


    class ViewHolder(val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel) {
            binding.listItem = item
            binding.movieName.text=item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding = MovieCardBinding.inflate(itemView, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }


    /*   override  fun  onViewAttachedToWindow(holder: ViewHolder) {
           super.onViewAttachedToWindow(holder)
           holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim))
       }*/

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem == newItem
            }

        }
    }

}