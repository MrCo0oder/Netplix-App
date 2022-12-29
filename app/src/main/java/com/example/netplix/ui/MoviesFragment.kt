package com.example.netplix.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.netplix.adapter.CarouselAdapter
import com.example.netplix.adapter.PopularRVAdapter
import com.example.netplix.adapter.TrendyRVAdapter
import com.example.netplix.databinding.FragmentMoviesBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.Page
import com.example.netplix.viewmodel.MovieViewModel
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager


class MoviesFragment : Fragment() {
    lateinit var binding: FragmentMoviesBinding
    lateinit var rvAdapter: PopularRVAdapter

    private val moviesViewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }
    lateinit var trendyAdapter: TrendyRVAdapter
    lateinit var popMoviesList: List<MovieModel>
    lateinit var trendyMoviesList: List<MovieModel>
    lateinit var upComingList: List<MovieModel>
    lateinit var carouselAdapter :CarouselAdapter
    lateinit var linearLayoutManager: CarouselLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            getData("First")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        initRV()
        binding.swipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getData("Refresh")
            binding.swipe.setRefreshing(false);
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData(type: String) {

        if (type.equals("Refresh")) {

            binding.carouselRecyclerview.apply {
                binding.carouselRecyclerview.adapter = CarouselAdapter(requireActivity(),upComingList)
                set3DItem(true)
                setAlpha(true)
                setInfinite(true)
            }
            binding.popMRV.apply {
                rvAdapter = PopularRVAdapter(requireActivity().baseContext, popMoviesList)
                binding.popMRV.adapter = rvAdapter
            }

            binding.trendyMRV.apply {
                trendyAdapter = TrendyRVAdapter(requireActivity().baseContext, trendyMoviesList.shuffled())
                binding.trendyMRV.adapter = trendyAdapter
            }

        } else {
            try {

                binding.carouselRecyclerview.apply {
                    binding.carouselRecyclerview.adapter = CarouselAdapter(requireActivity(),upComingList)
                    set3DItem(true)
                    setAlpha(true)
                    setInfinite(true)
                }
                binding.popMRV.apply {
                    rvAdapter = PopularRVAdapter(requireActivity().baseContext, popMoviesList)
                    binding.popMRV.adapter = rvAdapter

                }
                binding.trendyMRV.apply {
                    trendyAdapter = TrendyRVAdapter(requireActivity().baseContext, trendyMoviesList.shuffled())
                    binding.trendyMRV.adapter = trendyAdapter

                }
            } catch (e: Exception) {
                Log.d("Movie fragment 1", e.localizedMessage as String)
            }
        }

        binding.swipe.isRefreshing = false
    }

    private fun getData(s: String) {

        try {
            moviesViewModel.getMovie().first.observe(requireActivity(), object : Observer<Page> { override fun onChanged(t: Page) {
                        popMoviesList = t.results.sortedBy { movieModel -> movieModel.vote_average }.reversed()
                loadData(s)

                    } })
            moviesViewModel.getMovie().second.observe(requireActivity(), object : Observer<String> { override fun onChanged(t: String) {
                Log.d("Movie fragment 4",t)    } })
            moviesViewModel.getWeekTrendingMovies().first.observe(requireActivity(), object : Observer<Page> {
                    override fun onChanged(t: Page) {
                        trendyMoviesList = t.results
                        loadData(s)

                    }
                })
            moviesViewModel.getWeekTrendingMovies().second.observe(requireActivity(), object : Observer<String> {
                    override fun onChanged(t: String) {
                        Log.d("Movie fragment 2",t)
                        binding.swipe.isRefreshing = false
                    }
                })
            moviesViewModel.getUpcomingMovies().first.observe(requireActivity(), object : Observer<Page> { override fun onChanged(t: Page) {
                upComingList = t.results.sortedBy { movieModel -> movieModel.vote_average }.reversed()
                loadData(s)

            } })
            moviesViewModel.getUpcomingMovies().second.observe(requireActivity(), object : Observer<String> { override fun onChanged(t: String) {
                Log.d("Movie fragment 4",t)    } })
        }catch (e:Exception){
            Log.d("Movie fragment 3", e.localizedMessage as String)
            binding.swipe.isRefreshing = false
        }
    }
    fun initRV(){
        binding.popMRV.layoutManager = LinearLayoutManager(requireActivity().baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.popMRV.setHasFixedSize(true)
        popMoviesList = ArrayList()
        binding.trendyMRV.layoutManager=LinearLayoutManager(requireActivity().baseContext,LinearLayoutManager.HORIZONTAL,false)
        binding.trendyMRV.setHasFixedSize(true)
        trendyMoviesList = ArrayList()
        upComingList  = ArrayList()
        linearLayoutManager= CarouselLayoutManager(true,true,0.5f,false,true,true, HORIZONTAL)
        binding.carouselRecyclerview.layoutManager = linearLayoutManager

    }
    override fun onResume() {
        super.onResume()
        getData("")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            getData("")
        }
    }


}


