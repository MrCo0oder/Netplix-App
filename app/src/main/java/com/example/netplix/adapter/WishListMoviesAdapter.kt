package com.example.netplix.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.databinding.ShowMoreItemBinding
import com.example.netplix.databinding.WishListItemBinding
import com.example.netplix.models.MovieModel
import com.example.netplix.utils.Constants
import com.example.netplix.utils.loadImage
import com.squareup.picasso.Picasso

class WishListMoviesAdapter(
    private var fragment: Fragment,
    private var callBack: (selectedItem: MovieModel) -> Unit
) :
    BaseRecyclerAdapter<MovieModel, WishListItemBinding>() {

    override fun itemBinding(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding =
            WishListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishListMoviesViewHolder(binding)
    }

    override fun initItemWithBinding(holder: RecyclerView.ViewHolder, item: MovieModel) {
        (holder as WishListMoviesViewHolder).bind(item)
    }

    inner class WishListMoviesViewHolder(itemBinding: WishListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {
        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: MovieModel) {
            binding.image.loadImage(fragment, Constants.IMAGES_BASE + item.poster_path) { _, _ ->
            }
        }

        override fun onClick(view: View?) {
            list[bindingAdapterPosition].let { callBack(it) }
        }
    }
}