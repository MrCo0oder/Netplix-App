package com.example.netplix.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel

abstract class BaseRecyclerAdapter<T, K : ViewBinding>() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: K
    protected var list: MutableList<T> = mutableListOf()
    protected var itemIndex = 0
    protected var defaultMaxItemCount: Int = 0
    protected var infiniteScrolling: Boolean = false


    override fun getItemCount(): Int = list.size


    fun setData(newList: MutableList<T>) {
        val diffCallback = RecyclerDiffUtilModule(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        if (this.list.isNotEmpty())
            this.list.clear()
        this.list = newList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }


    protected fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }


    abstract fun itemBinding(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        initItemWithBinding(holder, list[position])
    }

    abstract fun initItemWithBinding(holder: RecyclerView.ViewHolder, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return itemBinding(parent, viewType)
    }

    fun getItemAt(swipedTvPosition: Int): T {
        return list.get(swipedTvPosition)
    }

    fun clearAdapter() {
        if (this.list.isNotEmpty()) {
            val diffCallback = RecyclerDiffUtilModule(this.list, mutableListOf())
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            this.list.clear()
            this.itemIndex = -1
            diffResult.dispatchUpdatesTo(this)
        }
    }

}
