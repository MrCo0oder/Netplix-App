package com.example.netplix.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.netplix.R
import com.example.netplix.adapter.ShowMoreMoviesPagingRecyclerAdapter
import com.example.netplix.databinding.FragmentShowMoreBinding
import com.example.netplix.di.NavigationModule
import com.example.netplix.ui.movies.MovieViewModel
import com.example.netplix.ui.tv.TvViewModel
import com.example.netplix.utils.Constants
import com.example.netplix.utils.NetworkState
import com.example.netplix.utils.gone
import com.example.netplix.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShowMoreFragment : Fragment() {
    @Inject
    lateinit var navigationModule: NavigationModule

    private lateinit var binding: FragmentShowMoreBinding
    private lateinit var tvViewModel: TvViewModel
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var moviesAdapter: ShowMoreMoviesPagingRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationModule.init(requireActivity())
        initViewModels()
        initRecyclerView()
        handleListeningFromApi()
    }

    private fun handleListeningFromApi() {
        if (arguments?.getBoolean(Constants.IS_POP) == true) {
            binding.headerTextView.text =
                buildString {
                    append(getString(R.string.popular))
//                    append(" ")
//                    append(getString(R.string.movies))
                }
            listenForPopularMoviesApi()
        } else {
            binding.headerTextView.text =
                buildString {
                    append(getString(R.string.trendy_this_week))
//                    append(" ")
//                    append(getString(R.string.movies))
                }
            listenForTrendyMoviesApi()
        }
        binding.moviesRecyclerview.adapter = moviesAdapter
    }


    private fun initViewModels() {
        movieViewModel = initMovieViewModel()
    }

    private fun initMovieViewModel() = ViewModelProvider(this)[MovieViewModel::class.java]

    private fun initRecyclerView() {
        moviesAdapter = ShowMoreMoviesPagingRecyclerAdapter(requireContext()) {
            navigationModule.navigateTo(
                R.id.detailsFragment, R.id.nav_host_fragment,
                Bundle().apply {
                    putBoolean(Constants.IS_MOVE, true)
                    putSerializable(Constants.MOVIE_ID, it)
                },
            )
        }
    }

    @SuppressLint("LongLogTag")
    private fun listenForTrendyMoviesApi() {
        lifecycleScope.launch {
            movieViewModel.trendyMoviesList.collect {
                moviesAdapter.submitData(it)
            }
        }
        movieViewModel.apply {
            getTrendyNetworkState().observe(viewLifecycleOwner) {
                Log.i("onViewCreated: ", it.msg)
                when (it) {
                    NetworkState.LOADED -> {
                        binding.progressBar.root.gone()
                        binding.moviesRecyclerview.show()
                    }

                    NetworkState.LOADING -> {
                        binding.progressBar.root.show()
                    }

                    NetworkState.ERROR -> {
                        binding.progressBar.root.gone()
                    }
                }
            }
        }
    }

    private fun listenForPopularMoviesApi() {
        lifecycleScope.launch {
            movieViewModel.popularMoviesList.collect() {
                moviesAdapter.submitData(it)
            }
        }
        movieViewModel.apply {
            getPopularNetworkState().observe(viewLifecycleOwner)
            {
                Log.i("onViewCreated: ", it.msg)
                when (it) {
                    NetworkState.LOADED -> {
                        binding.progressBar.root.gone()
                        binding.moviesRecyclerview.show()
                    }

                    NetworkState.LOADING -> {
                        binding.progressBar.root.show()
                    }

                    NetworkState.ERROR -> {
                        binding.progressBar.root.gone()
                    }
                }
            }
        }
    }
}
