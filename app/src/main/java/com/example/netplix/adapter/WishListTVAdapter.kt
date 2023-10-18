package com.example.netplix.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.databinding.WishListItemBinding
import com.example.netplix.models.TvModel
import com.example.netplix.utils.Constants
import com.example.netplix.utils.loadImage

class WishListTVAdapter(
    private var fragment: Fragment,
    private var callBack: (selectedItem: TvModel) -> Unit
) :
    BaseRecyclerAdapter<TvModel, WishListItemBinding>() {

    override fun itemBinding(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding =
            WishListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishListMoviesViewHolder(binding)
    }

    override fun initItemWithBinding(holder: RecyclerView.ViewHolder, item: TvModel) {
        (holder as WishListMoviesViewHolder).bind(item)
    }

    inner class WishListMoviesViewHolder(itemBinding: WishListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {
        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: TvModel) {
            binding.image.loadImage(fragment, Constants.IMAGES_BASE + item.poster_path) { b, m ->
                if (b.not())
                    Toast.makeText(fragment.requireContext(), m, Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
            }
        }

        override fun onClick(view: View?) {
            list[bindingAdapterPosition].let { callBack(it) }
        }
    }
}