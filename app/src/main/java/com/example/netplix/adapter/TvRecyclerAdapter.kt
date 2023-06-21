package com.example.netplix.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.databinding.TvCardBinding
import com.example.netplix.pojo.TvModel

class TvRecyclerAdapter(var context: Context, val clickListener: (show: TvModel) -> Unit) :
    RecyclerView.Adapter<TvRecyclerAdapter.ViewHolder>() {
    var list: List<TvModel> = ArrayList()

    class ViewHolder(val binding: TvCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TvModel) {
            binding.listItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding = TvCardBinding.inflate(itemView, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(list[position])
        holder.binding.root.setOnClickListener {
            clickListener(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun clearData() {
        val data: List<TvModel> = ArrayList()
        list = data
        notifyDataSetChanged()
    }

    fun setData(data: List<TvModel>) {
        list = data
        notifyDataSetChanged()
    }

    fun getTvAt(swipedTvPosition: Int): TvModel {
        return list.get(swipedTvPosition);
    }
}