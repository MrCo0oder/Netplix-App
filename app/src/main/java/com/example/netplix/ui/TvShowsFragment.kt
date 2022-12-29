package com.example.netplix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.netplix.R
import com.example.netplix.databinding.FragmentMoviesBinding
import com.example.netplix.databinding.FragmentTvShowsBinding


class TvShowsFragment : Fragment() {

    lateinit var binding: FragmentTvShowsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentTvShowsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}