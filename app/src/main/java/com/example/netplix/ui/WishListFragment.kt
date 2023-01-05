package com.example.netplix.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.adapter.MoviesRecyclerAdapter
import com.example.netplix.adapter.TvRecyclerAdapter
import com.example.netplix.databinding.FragmentWishListBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.TvModel
import com.example.netplix.viewmodel.MovieViewModel
import com.example.netplix.viewmodel.TvViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WishListFragment : Fragment() {
    val moviesViewModel: MovieViewModel by lazy { ViewModelProvider(this).get(MovieViewModel::class.java) }
    val tvViewModel:TvViewModel by lazy { ViewModelProvider(this).get(TvViewModel::class.java) }
    lateinit var moviesAdapter: MoviesRecyclerAdapter
    lateinit var tvAdapter: TvRecyclerAdapter
    lateinit var moviesList: List<MovieModel>
    lateinit var tvList: List<TvModel>
    lateinit var binding: FragmentWishListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentWishListBinding.inflate(layoutInflater, container, false)
        binding.button.setOnClickListener {
            if (binding.moviesWishList.visibility == View.VISIBLE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(
                        binding.moviesCardview,
                        AutoTransition()
                    )
                }
                binding.moviesWishList.visibility = View.GONE
                binding.button.text = getText(R.string.expand)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(
                        binding.moviesCardview,
                        AutoTransition()
                    )
                }
                binding.moviesWishList.visibility = View.VISIBLE
                binding.button.text = getText(R.string.collapse)
            }
        }
        binding.button2.setOnClickListener {
            if (binding.tvWishList.visibility == View.VISIBLE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(binding.tvCardview, AutoTransition())
                }
                binding.tvWishList.visibility = View.GONE
                binding.button2.text = getText(R.string.expand)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(binding.tvCardview, AutoTransition())
                }
                binding.tvWishList.visibility = View.VISIBLE
                binding.button2.text = getText(R.string.collapse)
            }
        }
        initRV()
        moviesViewModel.getAllMovies()
        moviesViewModel.getMoviesFromDB().observe(viewLifecycleOwner,object :Observer<List<MovieModel>>{
            override fun onChanged(t: List<MovieModel>) {
                moviesList=t
                moviesAdapter.setData(t)
            }

        })
        tvViewModel.getAllTv()
        tvViewModel.getTVFromDB().observe(viewLifecycleOwner,object :Observer<List<TvModel>>{
            override fun onChanged(t: List<TvModel>) {
                tvList=t
                tvAdapter.setData(tvList)
            }
        })
        setupMoviesSwipe()
        setupTVSwipe()
        return binding.root
    }
    fun initRV() {
        moviesList = ArrayList()
        binding.moviesWishList.layoutManager = LinearLayoutManager(requireActivity().baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.moviesWishList.setHasFixedSize(true)
        moviesAdapter= MoviesRecyclerAdapter(requireActivity())
        binding.moviesWishList.adapter = moviesAdapter
        moviesAdapter.setData(moviesList)

        tvList = ArrayList()
        binding.tvWishList.layoutManager = LinearLayoutManager(requireActivity().baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.tvWishList.setHasFixedSize(true)
        tvAdapter= TvRecyclerAdapter(requireActivity())
        binding.tvWishList.adapter = tvAdapter
        tvAdapter.setData(tvList)

    }
    private fun setupMoviesSwipe() {
        val callback: ItemTouchHelper.SimpleCallback =

            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
                override fun onMove(
                    @NonNull recyclerView: RecyclerView,
                    @NonNull viewHolder: RecyclerView.ViewHolder,
                    @NonNull target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    @NonNull viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val swipedMoviePosition = viewHolder.adapterPosition
                    val swipedMovie: MovieModel = moviesAdapter.getMovieAt(swipedMoviePosition)
                    moviesViewModel.deleteMovie(swipedMovie.id)
                    moviesAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        context,
                        "movie deleted from database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.moviesWishList)
    }
    private fun setupTVSwipe() {
        val callback: ItemTouchHelper.SimpleCallback =

            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
                override fun onMove(
                    @NonNull recyclerView: RecyclerView,
                    @NonNull viewHolder: RecyclerView.ViewHolder,
                    @NonNull target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    @NonNull viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val swipedTvPosition = viewHolder.adapterPosition
                    val swipedTv: TvModel = tvAdapter.getTvAt(swipedTvPosition)
                    tvViewModel.deleteTv(swipedTv.id)
                    tvAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        context,
                        "tv deleted from database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.tvWishList)
    }
}