package com.example.netplix.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.databinding.MovieCardBinding
import com.example.netplix.databinding.TvCardBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.TvModel
import com.example.netplix.ui.MoviesDetailsActivity
import com.example.netplix.ui.TvDetailsActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TvSearchAdapter(var context: Context) :
    RecyclerView.Adapter<TvSearchAdapter.ViewHolder>() {
    lateinit var list: List<TvModel>
    class ViewHolder(val binding: TvCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:TvModel){
            binding.listItem=item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding=TvCardBinding.inflate(itemView,parent,false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.root.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {

                val intent = Intent(context, TvDetailsActivity::class.java)
                intent.putExtra("Tv", list.get(position))
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        })
        val url: String = "https://image.tmdb.org/t/p/w500" + list[position].poster_path
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.binding.tvImgv, object : Callback {

            override fun onSuccess() {
                    holder.binding.progressBar2.visibility = View.GONE
            }

            override fun onError(e: Exception) {
                holder.binding.progressBar2.visibility = View.GONE
                Log.d("search adapter",e.message.toString())
            }
        })
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun clearData() {
        val data: List<TvModel> = ArrayList()
        list=data
        notifyDataSetChanged()
    }
    fun setData(data:List<TvModel>) {
        list=data
        notifyDataSetChanged()
    }
//    override  fun  onViewAttachedToWindow(holder: ViewHolder) {
//        super.onViewAttachedToWindow(holder)
//        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.post))
//    }
}