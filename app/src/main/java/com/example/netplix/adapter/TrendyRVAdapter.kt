package com.example.netplix.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.databinding.TrendyMovieCardBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.ui.MoviesDetailsActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TrendyRVAdapter(var context: Context) :
    RecyclerView.Adapter<TrendyRVAdapter.ViewHolder>() {
    lateinit var trendyMovieList: List<MovieModel>
    class ViewHolder(val binding: TrendyMovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:MovieModel){
            binding.item=item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding=TrendyMovieCardBinding.inflate(itemView,parent,false)
        return ViewHolder(listItemBinding)
    }
    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.binding.root.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(context, MoviesDetailsActivity::class.java)
                intent.putExtra("Movie", trendyMovieList.get(position))
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)


            }
        })
        val url: String = "https://image.tmdb.org/t/p/w500" + trendyMovieList[position].poster_path
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.binding.movieImgv, object : Callback {
            override fun onSuccess() {
                    holder.binding.progressBar2.visibility = View.GONE
            }

            override fun onError(e: Exception) {
                Log.d("Trendy Adapter",e.message.toString() )

            }
        })
        holder.bind(trendyMovieList[position])
    }

    override fun getItemCount(): Int {
        return trendyMovieList.size
    }
//    override  fun  onViewAttachedToWindow(holder: ViewHolder) {
//        super.onViewAttachedToWindow(holder)
//        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.post))
//    }
    fun setData(list:List<MovieModel>){
        trendyMovieList=list
        notifyDataSetChanged()
    }
}