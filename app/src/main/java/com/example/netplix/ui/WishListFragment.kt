package com.example.netplix.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.netplix.R
import com.example.netplix.adapter.MoviesRecyclerAdapter
import com.example.netplix.adapter.TvRecyclerAdapter
import com.example.netplix.database.NetplixDB
import com.example.netplix.databinding.FragmentWishListBinding
import com.example.netplix.di.DatabaseModule
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.NavigationModule
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel
import com.example.netplix.ui.movies.MovieViewModel
import com.example.netplix.ui.tv.TvViewModel
import com.example.netplix.utils.Constants
import com.example.netplix.utils.gone
import com.example.netplix.utils.show
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class WishListFragment : Fragment() {
    @Inject
    lateinit var navigationModule: NavigationModule

    @Inject
    lateinit var firebaseModule: FirebaseModule

    val moviesViewModel: MovieViewModel by lazy { ViewModelProvider(this)[MovieViewModel::class.java] }
    val tvViewModel: TvViewModel by lazy { ViewModelProvider(this).get(TvViewModel::class.java) }
    lateinit var moviesAdapter: MoviesRecyclerAdapter
    lateinit var tvAdapter: TvRecyclerAdapter
    lateinit var binding: FragmentWishListBinding

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireActivity())
        binding = FragmentWishListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            if (binding.moviesWishList.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    binding.moviesCardview,
                    AutoTransition()
                )
                binding.moviesWishList.gone()
                binding.button.text = getText(R.string.expand)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.moviesCardview,
                    AutoTransition()
                )
                binding.moviesWishList.show()
                binding.button.text = getText(R.string.collapse)
            }
        }
        binding.button2.setOnClickListener {
            if (binding.tvWishList.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(binding.tvCardview, AutoTransition())
                binding.tvWishList.gone()
                binding.button2.text = getText(R.string.expand)
            } else {
                TransitionManager.beginDelayedTransition(binding.tvCardview, AutoTransition())
                binding.tvWishList.show()
                binding.button2.text = getText(R.string.collapse)
            }
        }
        initRV()
        getMoviesList()
        getTvList()
        initLottie()
        binding.emptyLottie.gone()
        setupMoviesSwipe()
        setupTVSwipe()
    }

    private fun getMoviesList() {
        firebaseModule.getMoviesList {
            if (it.isEmpty()) {
                binding.moviesCardview.gone()
            } else {
                moviesAdapter.setData(it)
                binding.moviesCardview.show()
            }
            isLottieHidden()
        }
    }

    private fun getTvList() {
        firebaseModule.getShowsList {
            if (it.isEmpty()) {
                binding.tvCardview.gone()
            } else {
                tvAdapter.setData(it)
                binding.tvCardview.show()
            }
            isLottieHidden()
        }
    }

    private fun initLottie() {
        binding.apply {
            emptyLottie.setOnClickListener {
                if (emptyLottie.isAnimating) {
                    emptyLottie.cancelAnimation()
                } else {
                    emptyLottie.playAnimation()
                }
            }
        }
    }

    private fun isLottieHidden() {
        if (binding.tvCardview.visibility == View.GONE && binding.moviesCardview.visibility == View.GONE) {
            binding.emptyLottie.show()
        } else {
            binding.emptyLottie.gone()
        }
    }

    fun initRV() {
        binding.moviesWishList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.moviesWishList.setHasFixedSize(true)
        moviesAdapter = MoviesRecyclerAdapter(requireActivity()) {
            handleNavigationToMovieDetailsFragment(it)
        }
        binding.moviesWishList.adapter = moviesAdapter
        binding.tvWishList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.tvWishList.setHasFixedSize(true)
        tvAdapter = TvRecyclerAdapter(requireActivity()) {
            handleNavigationToTVDetailsFragment(it)
        }
        binding.tvWishList.adapter = tvAdapter

    }


    private fun handleNavigationToMovieDetailsFragment(tappedMovie: MovieModel) {
        navigationModule.navigateTo(
            R.id.action_homeFragment_to_detailsFragment,
            R.id.nav_host_fragment,
            Bundle().apply {
                putBoolean(Constants.IS_MOVE, true)
                putSerializable(Constants.MOVIE_ID, tappedMovie)
            },
        )
    }


    private fun handleNavigationToTVDetailsFragment(tappedTv: TvModel) {
        navigationModule.navigateTo(
            R.id.action_homeFragment_to_detailsFragment,
            R.id.nav_host_fragment,
            Bundle().apply {
                putBoolean(Constants.IS_MOVE, false)
                putSerializable(Constants.TV_ID, tappedTv)
            },
        )
    }

    private fun setupMoviesSwipe() {
        val callback: ItemTouchHelper.SimpleCallback =

            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val swipedMoviePosition = viewHolder.adapterPosition
                    val swipedMovie: MovieModel = moviesAdapter.getItemAt(swipedMoviePosition)
                    moviesViewModel.deleteMovieFromFB(swipedMovie.id.toString()) {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.deleted),
                                Toast.LENGTH_SHORT
                            ).show()
                            getMoviesList()
                            isLottieHidden()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.moviesWishList)
    }

    private fun setupTVSwipe() {
        val callback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }


                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val swipedTvPosition = viewHolder.adapterPosition
                    val swipedTv: TvModel = tvAdapter.getTvAt(swipedTvPosition)
                    tvViewModel.deleteTvFromFB(swipedTv.id.toString()) {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.deleted),
                                Toast.LENGTH_SHORT
                            ).show()
                            getTvList()
                            isLottieHidden()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.tvWishList)
    }
}