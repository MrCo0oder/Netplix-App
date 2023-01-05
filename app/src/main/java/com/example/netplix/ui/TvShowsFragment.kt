package com.example.netplix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.netplix.adapter.TvRecyclerAdapter
import com.example.netplix.databinding.FragmentTvShowsBinding
import com.example.netplix.pojo.TvModel
import com.example.netplix.viewmodel.TvViewModel
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowsFragment : Fragment() {

    lateinit var binding: FragmentTvShowsBinding
    lateinit var popTvAdapter: TvRecyclerAdapter
    private val tvViewModel: TvViewModel by lazy { ViewModelProvider(this).get(TvViewModel::class.java) }
    lateinit var topRatedAdapter: TvRecyclerAdapter
    lateinit var popTvList: List<TvModel>
    lateinit var topRatedList: List<TvModel>
    lateinit var latestList: List<TvModel>
    lateinit var carouselLatestAdapter: TvRecyclerAdapter
    lateinit var linearLayoutManager: CarouselLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentTvShowsBinding.inflate(layoutInflater, container, false)
        binding.swipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loadData()
            binding.swipe.setRefreshing(false);
        })
        initRV()
        loadData()
        return binding.root
    }

    private fun loadData() {
        tvViewModel.getPopTv()
        tvViewModel.getPopTvList()
            .observe(viewLifecycleOwner, object : Observer<List<TvModel>> {
                override fun onChanged(t: List<TvModel>) {
                    popTvList = t.sortedBy { tv -> tv.vote_average }.reversed()
                    popTvAdapter.setData(popTvList)
                }
            })
        tvViewModel.getTopRatedTv()
        tvViewModel.getTopRatedTvList()
            .observe(viewLifecycleOwner, object : Observer<List<TvModel>> {
                override fun onChanged(t: List<TvModel>) {
                    topRatedList = t
                    topRatedAdapter.setData(topRatedList)
                }
            })
        tvViewModel.getLatestTv()
        tvViewModel.getLatestList()
            .observe(viewLifecycleOwner, object : Observer<List<TvModel>> {
                override fun onChanged(t: List<TvModel>) {
                    latestList = t
                    carouselLatestAdapter.setData(latestList)
                }
            })
    }

    fun initRV() {
        popTvList = ArrayList()
        binding.popTvRV.layoutManager = LinearLayoutManager(
            requireActivity().baseContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.popTvRV.setHasFixedSize(true)
        popTvAdapter = TvRecyclerAdapter(requireActivity())
        binding.popTvRV.adapter = popTvAdapter
        popTvAdapter.setData(popTvList)

        topRatedList = ArrayList()
        binding.topRatedRV.layoutManager = LinearLayoutManager(
            requireActivity().baseContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.topRatedRV.setHasFixedSize(true)
        topRatedAdapter = TvRecyclerAdapter(requireActivity())
        binding.topRatedRV.adapter = topRatedAdapter
        topRatedAdapter.setData(topRatedList)

        latestList = ArrayList()
        linearLayoutManager = CarouselLayoutManager(true, true, 0.5f, true, true, true, RecyclerView.HORIZONTAL)
        binding.latestRecyclerview.layoutManager = linearLayoutManager
        carouselLatestAdapter = TvRecyclerAdapter(requireActivity())
        carouselLatestAdapter
        binding.latestRecyclerview.adapter = carouselLatestAdapter
        carouselLatestAdapter.setData(latestList)

    }

}