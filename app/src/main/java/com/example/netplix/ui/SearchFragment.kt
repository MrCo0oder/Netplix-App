package com.example.netplix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.netplix.R
import com.example.netplix.adapter.MoviesRecyclerAdapter
import com.example.netplix.adapter.TvRecyclerAdapter
import com.example.netplix.databinding.FragmentSearchBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.TvModel
import com.example.netplix.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

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
        return binding.root
    }

    private fun SearchSetup() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrBlank())
                {
                    searchMoviesAdapter.clearData()
                }else{

                    if (!binding.switch1.isChecked) {
                        binding.movisSearchList.visibility=VISIBLE
                        binding.tvSearchList.visibility=GONE
                        binding.searchView.queryHint=getText(R.string.movie_search)
                        viewModel.getSearchMovies(newText)
                        viewModel.getMoviesSearchList().observe(viewLifecycleOwner, object : Observer<List<MovieModel>> {
                            override fun onChanged(t: List<MovieModel>) {
                                searchMoviesList = t
                                searchMoviesAdapter.setData(searchMoviesList)
                            }
                        })
                    } else {
                        binding.movisSearchList.visibility=GONE
                        binding.tvSearchList.visibility= VISIBLE
                        binding.searchView.queryHint=getText(R.string.tv_search)
                        viewModel.getSearchTv(newText)
                        viewModel.getTvSearchList().observe(viewLifecycleOwner, object : Observer<List<TvModel>> {
                            override fun onChanged(t: List<TvModel>) {
                                searchTvList = t
                                searchTvAdapter.setData(searchTvList)
                            }
                        })
                    }
                }
                return false
            }
        })
    }
    fun initRV(){
        searchMoviesList=ArrayList<MovieModel>()
        binding.movisSearchList.layoutManager = GridLayoutManager(requireActivity().baseContext,2)
        binding.movisSearchList.setHasFixedSize(true)
        searchMoviesAdapter= MoviesRecyclerAdapter(requireActivity())
        binding.movisSearchList.adapter=searchMoviesAdapter
        searchMoviesAdapter.setData(searchMoviesList)

        searchTvList=ArrayList<TvModel>()
        binding.tvSearchList.layoutManager = GridLayoutManager(requireActivity().baseContext,2)
        binding.tvSearchList.setHasFixedSize(true)
        searchTvAdapter= TvRecyclerAdapter(requireActivity())
        binding.tvSearchList.adapter=searchTvAdapter
        searchTvAdapter.setData(searchTvList)
    }


}