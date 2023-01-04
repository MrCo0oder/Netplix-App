package com.example.netplix.ui
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.netplix.adapter.MoviesCarouselAdapter
import com.example.netplix.adapter.PopularRVAdapter
import com.example.netplix.adapter.TrendyRVAdapter
import com.example.netplix.databinding.FragmentMoviesBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.viewmodel.MovieViewModel
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MoviesFragment : Fragment() {
    lateinit var binding: FragmentMoviesBinding
    lateinit var rvAdapter: PopularRVAdapter
    private val moviesViewModel: MovieViewModel by lazy { ViewModelProvider(this).get(MovieViewModel::class.java) }
    lateinit var trendyAdapter: TrendyRVAdapter
    lateinit var popMoviesList: List<MovieModel>
    lateinit var trendyMoviesList: List<MovieModel>
    lateinit var upComingList: List<MovieModel>
    lateinit var moviesCarouselAdapter: MoviesCarouselAdapter
    lateinit var linearLayoutManager: CarouselLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)

        binding.swipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loadData()
            binding.swipe.setRefreshing(false);
        })
        initRV()
        loadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")

    fun loadData() {
        moviesViewModel.getPopMovies()
        moviesViewModel.getPopMoviesList()
            .observe(viewLifecycleOwner, object : Observer<List<MovieModel>> {
                override fun onChanged(t: List<MovieModel>) {
                    popMoviesList = t.sortedBy { movieModel -> movieModel.vote_average }.reversed()
                    rvAdapter.setData(popMoviesList)
                }
            })
        moviesViewModel.getTrendyMovies()
        moviesViewModel.getTrendyMoviesList()
            .observe(viewLifecycleOwner, object : Observer<List<MovieModel>> {
                override fun onChanged(t: List<MovieModel>) {
                    trendyMoviesList = t
                    trendyAdapter.setData(trendyMoviesList)
                }
            })
        moviesViewModel.getUpComing()
        moviesViewModel.getUpComingList()
            .observe(viewLifecycleOwner, object : Observer<List<MovieModel>> {
                override fun onChanged(t: List<MovieModel>) {
                    upComingList = t
                    binding.carouselRecyclerview.apply {
                        binding.carouselRecyclerview.adapter = MoviesCarouselAdapter(requireActivity(), upComingList)
                        set3DItem(true)
                        setAlpha(true)
                        setInfinite(true)
                    }
                }
            })
    }

    fun initRV() {
        popMoviesList = ArrayList()
        binding.popMRV.layoutManager = LinearLayoutManager(requireActivity().baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.popMRV.setHasFixedSize(true)
        rvAdapter= PopularRVAdapter(requireActivity())
        binding.popMRV.adapter = rvAdapter
        rvAdapter.setData(popMoviesList)

        trendyMoviesList = ArrayList()
        binding.trendyMRV.layoutManager = LinearLayoutManager(requireActivity().baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.trendyMRV.setHasFixedSize(true)
        trendyAdapter= TrendyRVAdapter(requireActivity())
        binding.trendyMRV.adapter = trendyAdapter
        trendyAdapter.setData(trendyMoviesList)

        upComingList = ArrayList()
        linearLayoutManager = CarouselLayoutManager(true, true, 0.5f, false, true, true, HORIZONTAL)
        binding.carouselRecyclerview.layoutManager = linearLayoutManager

    }
    override fun onResume() {
        super.onResume()
    }
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            loadData()
        }
    }


}


