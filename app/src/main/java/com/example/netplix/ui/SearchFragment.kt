package com.example.netplix.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.netplix.R
import com.example.netplix.adapter.MoviesRecyclerAdapter
import com.example.netplix.adapter.TvRecyclerAdapter
import com.example.netplix.databinding.FragmentSearchBinding
import com.example.netplix.di.NavigationModule
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel
import com.example.netplix.ui.movies.MovieViewModel
import com.example.netplix.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var navigationModule: NavigationModule

    lateinit var binding: FragmentSearchBinding
    private val viewModel: MovieViewModel by lazy { ViewModelProvider(this).get(MovieViewModel::class.java) }
    lateinit var searchMoviesAdapter: MoviesRecyclerAdapter
    lateinit var searchMoviesList: List<MovieModel>
    lateinit var searchTvAdapter: TvRecyclerAdapter
    lateinit var searchTvList: List<TvModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        SearchSetup()
        initRV()
        navigationModule.init(requireActivity())
        binding.switch1.setOnClickListener {
            searchMoviesAdapter.clearData()
            searchTvAdapter.clearData()
            binding.searchView.setQuery("", false)
            if (binding.switch1.isChecked) {
                binding.searchView.queryHint = getText(R.string.tv_search)
            } else {
                binding.searchView.queryHint = getText(R.string.movie_search)
            }

        }

        return binding.root
    }

    private fun SearchSetup() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrBlank()) {
                    searchMoviesAdapter.clearData()
                } else {

                    if (!binding.switch1.isChecked) {
                        binding.movisSearchList.visibility = VISIBLE
                        binding.tvSearchList.visibility = GONE
                        binding.searchView.queryHint = getText(R.string.movie_search)
                        viewModel.getSearchMovies(newText)
                        viewModel.getMoviesSearchList().observe(viewLifecycleOwner) {
                            searchMoviesList = it
                            searchMoviesAdapter.setData(searchMoviesList)
                        }
                    } else {
                        binding.movisSearchList.visibility = GONE
                        binding.tvSearchList.visibility = VISIBLE
                        binding.searchView.queryHint = getText(R.string.tv_search)
                        viewModel.getSearchTv(newText)
                        viewModel.getTvSearchList().observe(viewLifecycleOwner) {
                            searchTvList = it
                            searchTvAdapter.setData(searchTvList)

                        }
                    }
                }
                return false
            }
        })
    }

    fun initRV() {
        searchMoviesList = ArrayList<MovieModel>()
        binding.movisSearchList.setHasFixedSize(true)
        searchMoviesAdapter = MoviesRecyclerAdapter(requireActivity()) {
            Log.d("initRV: ", it.title)
            handleNavigationToMovieDetailsFragment(it)
        }
        binding.movisSearchList.adapter = searchMoviesAdapter
        searchMoviesAdapter.setData(searchMoviesList)
        searchTvList = ArrayList<TvModel>()
        binding.tvSearchList.setHasFixedSize(true)
        searchTvAdapter = TvRecyclerAdapter(requireActivity()) {
            Log.d("initRV: ", it.name)
            handleNavigationToTVDetailsFragment(it)
        }
        binding.tvSearchList.adapter = searchTvAdapter
        searchTvAdapter.setData(searchTvList)
    }


    private fun handleNavigationToMovieDetailsFragment(tappedMovie: MovieModel) {
        navigationModule.navigateTo(
            R.id.detailsFragment,
            R.id.nav_host_fragment,
            Bundle().apply {
                putBoolean(Constants.IS_MOVE, true)
                putSerializable(Constants.MOVIE_ID, tappedMovie)
            },
        )
    }

    private fun handleNavigationToTVDetailsFragment(tappedTv: TvModel) {
        navigationModule.navigateTo(
            R.id.detailsFragment,
            R.id.nav_host_fragment,
            Bundle().apply {
                putBoolean(Constants.IS_MOVE, false)
                putSerializable(Constants.TV_ID, tappedTv)
            },
        )
    }
}