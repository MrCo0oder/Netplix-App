package com.example.netplix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.netplix.R
import com.example.netplix.adapter.MoviesPagingRecyclerAdapter
import com.example.netplix.adapter.MoviesRecyclerAdapter
import com.example.netplix.adapter.RecyclerItemClickListener
import com.example.netplix.databinding.FragmentMoviesBinding
import com.example.netplix.di.NavigationModule
import com.example.netplix.pojo.MovieModel
import com.example.netplix.utils.Constants
import com.example.netplix.viewmodel.MovieViewModel
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    lateinit var binding: FragmentMoviesBinding
    private val moviesViewModel: MovieViewModel by lazy { ViewModelProvider(this)[MovieViewModel::class.java] }
    private lateinit var popularAdapter: MoviesPagingRecyclerAdapter
    private lateinit var trendyAdapter: MoviesPagingRecyclerAdapter
    private lateinit var upComingList: List<MovieModel>
    private lateinit var moviesCarouselAdapter: MoviesRecyclerAdapter
    private lateinit var linearLayoutManager: CarouselLayoutManager
    lateinit var navigationModule: NavigationModule
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loadData()
            binding.swipe.setRefreshing(false);
        })
        navigationModule = NavigationModule(requireActivity())
        initRV()
        loadData()
        onMovieClicked(moviesCarouselAdapter, binding.carouselRecyclerview)
    }

    fun loadData() {
        binding.apply {
            listenForPopularMoviesApi()
            binding.popMRV.adapter = popularAdapter
        }

        binding.apply {
            listenForTrendyMoviesApi()
            binding.trendyMRV.adapter = trendyAdapter
        }

        listenForUpcomingApi()
    }

    fun initRV() {
        initPopularRecyclerView()
        initTrendyRecyclerView()
        initUpComingRecyclerView()
    }


    private fun initPopularRecyclerView() {
        popularAdapter = MoviesPagingRecyclerAdapter(requireContext()) {
            handleNavigationToMovieDetailsFragment(it)
        }
        binding.apply {
            popMRV.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            popMRV.setHasFixedSize(true)
            listenForPopularMoviesApi()
        }
    }

    private fun initTrendyRecyclerView() {
        trendyAdapter = MoviesPagingRecyclerAdapter(requireContext()) {
            handleNavigationToMovieDetailsFragment(it)
        }
        binding.apply {
            trendyMRV.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            trendyMRV.setHasFixedSize(true)
            listenForTrendyMoviesApi()
        }
    }

    private fun handleNavigationToMovieDetailsFragment(it: MovieModel) {
        navigationModule.navigateTo(
            R.id.action_homeFragment_to_detailsFragment,
            R.id.nav_host_fragment,
            Bundle().apply {
                putBoolean(Constants.IS_MOVE, true)
                putSerializable("MOVIE_ID", it)
            })
    }

    private fun initUpComingRecyclerView() {
        upComingList = emptyList()
        moviesCarouselAdapter = MoviesRecyclerAdapter(requireActivity()){
            handleNavigationToMovieDetailsFragment(it)
        }
        binding.carouselRecyclerview.adapter = moviesCarouselAdapter
        moviesCarouselAdapter.setData(upComingList)
        linearLayoutManager =
            CarouselLayoutManager(false, false, 0.5f, false, true, true, HORIZONTAL)
        binding.carouselRecyclerview.layoutManager = linearLayoutManager
    }

    private fun listenForPopularMoviesApi() {
        lifecycleScope.launchWhenCreated {
            moviesViewModel.moviesPagingList.collect {
                popularAdapter.submitData(it)
            }
        }
    }

    private fun listenForTrendyMoviesApi() {
        lifecycleScope.launchWhenCreated {
            moviesViewModel.trendyMoviesList.collect {
                trendyAdapter.submitData(it)
            }
        }
    }

    private fun listenForUpcomingApi() {
        moviesViewModel.getUpComing()
        moviesViewModel.getUpComingList()
            .observe(viewLifecycleOwner) {
                upComingList = it
                moviesCarouselAdapter.setData(upComingList)
            }
    }


    fun onMovieClicked(adapter: MoviesRecyclerAdapter, recyclerView: RecyclerView) {

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireActivity(),
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        handleNavigationToMovieDetailsFragment(adapter.getItemAt(position))
                    }
                })
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            loadData()
        }
    }
}