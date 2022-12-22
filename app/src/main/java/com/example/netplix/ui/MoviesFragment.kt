package com.example.netplix.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.netplix.adapter.PopularRVAdapter
import com.example.netplix.adapter.TrendyRVAdapter
import com.example.netplix.databinding.FragmentMoviesBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.Page
import com.example.netplix.viewmodel.MovieViewModel


class MoviesFragment : Fragment() {
    lateinit var binding: FragmentMoviesBinding
    lateinit var rvAdapter: PopularRVAdapter
    private val popMoviesViewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }
    lateinit var trendyAdapter: TrendyRVAdapter
    private val trendyMoviesViewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }
    lateinit var popMoviesList: List<MovieModel>
    lateinit var trendyMoviesList: List<MovieModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        initRV()
        binding.swipe.setRefreshing(true)
        binding.swipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loadData("Refresh")
            binding.swipe.setRefreshing(false);

        })
        getData()
     //   rvAdapter = PopularRVAdapter(requireActivity().baseContext, popMoviesList)
       // binding.popMRV.adapter = rvAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadData(type: String) {
        binding.swipe.isRefreshing = true
        if (type.equals("Refresh")) {
            binding.popMRV.apply {
                rvAdapter = PopularRVAdapter(requireActivity().baseContext, popMoviesList)
                binding.popMRV.adapter = rvAdapter
                binding.swipe.isRefreshing = false
                rvAdapter.notifyDataSetChanged()
            }

            binding.trendyMRV.apply {
                trendyAdapter = TrendyRVAdapter(requireActivity().baseContext, trendyMoviesList)
                binding.trendyMRV.adapter = trendyAdapter
                binding.swipe.isRefreshing = false
                trendyAdapter.notifyDataSetChanged()
            }
            Toast.makeText(requireActivity().baseContext, "Refreshing...", Toast.LENGTH_SHORT)
                .show()

        } else {
            try {
                binding.popMRV.apply {
                    rvAdapter = PopularRVAdapter(requireActivity().baseContext, popMoviesList)
                    binding.popMRV.adapter = rvAdapter
                    rvAdapter.notifyDataSetChanged()
                }
                binding.trendyMRV.apply {
                    trendyAdapter = TrendyRVAdapter(requireActivity().baseContext, trendyMoviesList)
                    binding.trendyMRV.adapter = trendyAdapter
                    trendyAdapter.notifyDataSetChanged()
                }
                binding.swipe.isRefreshing = false

            } catch (e: Exception) {
                Toast.makeText(requireActivity().baseContext, e.message, Toast.LENGTH_SHORT).show()
                binding.swipe.isRefreshing = false
            }
        }

    }

    private fun getData() {
        try {
            popMoviesViewModel.getMovie().first.observe(viewLifecycleOwner, object : Observer<Page> { override fun onChanged(t: Page) {
                        popMoviesList = t.results
                        loadData("first")
                    } })
            popMoviesViewModel.getMovie().second.observe(viewLifecycleOwner, object : Observer<String> { override fun onChanged(t: String) { Toast.makeText(requireActivity().baseContext, t, Toast.LENGTH_SHORT).show() } })
            trendyMoviesViewModel.getWeekTrendingMovies().first.observe(viewLifecycleOwner, object : Observer<Page> {
                    override fun onChanged(t: Page) {
                        trendyMoviesList = t.results
                        loadData("first")

                    }
                })
            trendyMoviesViewModel.getWeekTrendingMovies().second.observe(viewLifecycleOwner, object : Observer<String> {
                    override fun onChanged(t: String) {
                        Toast.makeText(requireActivity().baseContext, t, Toast.LENGTH_SHORT).show()

                    }

                })
        }catch (e:Exception){
            Toast.makeText(requireActivity().baseContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    fun initRV(){
        binding.popMRV.layoutManager = LinearLayoutManager(requireActivity().baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.popMRV.setHasFixedSize(true)
        popMoviesList = ArrayList()
        binding.trendyMRV.layoutManager=LinearLayoutManager(requireActivity().baseContext,LinearLayoutManager.HORIZONTAL,false)
        binding.trendyMRV.setHasFixedSize(true)
        trendyMoviesList = ArrayList()
    }

}


