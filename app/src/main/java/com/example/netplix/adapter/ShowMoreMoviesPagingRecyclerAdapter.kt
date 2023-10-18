package com.example.netplix.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.databinding.ShowMoreItemBinding
import com.example.netplix.models.MovieModel

class ShowMoreMoviesPagingRecyclerAdapter(
    private val context: Context,
    val clickListener: (movie: MovieModel) -> Unit
) :
    PagingDataAdapter<MovieModel, ShowMoreMoviesPagingRecyclerAdapter.ViewHolder>(diffCallback) {

    inner class ViewHolder(val binding: ShowMoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel) {
            binding.listItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding = ShowMoreItemBinding.inflate(itemView, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(true)
        holder.itemView.setOnClickListener {
            clickListener(getItem(position)!!)
        }
    }

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