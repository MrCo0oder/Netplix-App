package com.example.netplix.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.netplix.adapter.SearchAdapter
import com.example.netplix.databinding.FragmentSearchBinding
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.Page
import com.example.netplix.viewmodel.MovieViewModel


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }
    lateinit var searchAdapter: SearchAdapter
    lateinit var searchingList: List<MovieModel>
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
                    searchAdapter.clearData()
                    binding.searchList.visibility= GONE
                }else{
                    binding.searchList.visibility= VISIBLE
                    if (!binding.switch1.isChecked) {
                        viewModel.getSearchMovies(newText).first.observe(requireActivity(),
                            object : Observer<Page> {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onChanged(t: Page) {
                                    searchingList=t.results
                                    binding.searchList.apply {
                                        searchAdapter =
                                            SearchAdapter(requireActivity().baseContext, searchingList)
                                        binding.searchList.adapter = searchAdapter
                                        searchAdapter.notifyDataSetChanged()
                                    }
                                }
                            })
                    } else {
                        viewModel.getSearchTv(newText).first.observe(requireActivity(),
                            object : Observer<Page> {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onChanged(t: Page) {
                                    binding.searchList.apply {
                                        searchAdapter =
                                            SearchAdapter(requireActivity().baseContext, t.results)
                                        binding.searchList.adapter = searchAdapter
                                        searchAdapter.notifyDataSetChanged()
                                    }
                                }
                            })
                    }
                }


                return false
            }
        })
    }
    fun initRV(){
        binding.searchList.layoutManager = LinearLayoutManager(requireActivity().baseContext, LinearLayoutManager.VERTICAL, false)
        binding.searchList.setHasFixedSize(true)
        searchingList=ArrayList<MovieModel>()
    }


}