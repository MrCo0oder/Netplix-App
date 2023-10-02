package com.example.netplix.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.databinding.CompanyLogoLayoutBinding
import com.example.netplix.models.movieDetails.ProductionCompany

class CompaniesRecyclerAdapter(val clickListener: (item: ProductionCompany) -> Unit) :
    RecyclerView.Adapter<CompaniesRecyclerAdapter.ViewHolder>() {

    private  var itemsList: MutableList<ProductionCompany> = mutableListOf()

    class ViewHolder(val binding: CompanyLogoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductionCompany) {
            binding.listItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding = CompanyLogoLayoutBinding.inflate(itemView, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(itemsList[position])
        holder.itemView.setOnClickListener {
            clickListener(itemsList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun setData(list: List<ProductionCompany>) {
        list.forEach {
            if (!it.logoPath.isNullOrEmpty()) {
                itemsList.add(it)
            }
        }
        notifyDataSetChanged()
    }
}