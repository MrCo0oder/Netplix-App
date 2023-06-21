package com.example.netplix

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.netplix.databinding.FragmentDetailsBinding
import com.example.netplix.di.NavigationModule
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.TvModel
import com.example.netplix.pojo.movieDetails.MovieDetails
import com.example.netplix.utils.Constants
import com.example.netplix.utils.Constants.Companion.IMAGES_BASE
import com.example.netplix.utils.Constants.Companion.MOVIE_ID
import com.example.netplix.utils.Constants.Companion.POSTER_URL
import com.example.netplix.utils.Constants.Companion.TV_ID
import com.example.netplix.utils.gone
import com.example.netplix.utils.hide
import com.example.netplix.utils.loadImage
import com.example.netplix.utils.show
import com.example.netplix.viewmodel.MovieViewModel
import com.example.netplix.viewmodel.TvViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.Locale

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var navigationModule: NavigationModule
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var moviesViewModel: MovieViewModel
    private lateinit var tvViewModel: TvViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        if (arguments?.getBoolean(Constants.IS_MOVE) == true)
            getMovieFromArgs()?.let { listeningForMoviesMainApi(it) }
        else
            getTvFromArgs()?.let { listeningForTvMainApi(it) }
    }


    private fun init() {
        initViewModels()
        navigationModule = NavigationModule(requireActivity())
    }

    private fun initViewModels() {
        moviesViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        tvViewModel = ViewModelProvider(this)[TvViewModel::class.java]
    }

    private fun getMovieFromArgs() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getSerializable(MOVIE_ID, MovieModel::class.java)
    } else {
        arguments?.getSerializable(MOVIE_ID) as MovieModel

    }

    private fun getTvFromArgs() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getSerializable(TV_ID, TvModel::class.java)
    } else {
        arguments?.getSerializable(TV_ID) as TvModel
    }

    private fun listeningForMoviesMainApi(movie: MovieModel) {
        moviesViewModel.getMovie(movie.id)
        moviesViewModel.getMovieDetails().observe(requireActivity()) { movieDetails ->
            if (movieDetails != null) {
                binding.mainProgressBar.progressView.gone()
                binding.parent.show()
                initCarouselImage()
                handleIsLikedButton(movie)
                handleMovieImagesApi(movie)
                initInfoTextView(movieDetails)
                movieDetails.adult?.let { handleContentTypeBadge(it) }
                initPosterImage(movieDetails.posterPath.toString())
                initBackDropImage(movieDetails.backdropPath.toString())
                handleCategoriesTextView(movieDetails)
                bindingSomeViews(movieDetails)
//              listeningToLinksApi(movie)
            } else {
                binding.mainProgressBar.progressView.show()
                binding.parent.gone()
            }

        }
    }

    private fun listeningForTvMainApi(tv: TvModel) {
        binding.mainProgressBar.progressView.gone()
        binding.parent.show()
        handleTvImagesApi(tv)
        tvViewModel.getTv(tv.id)
        tvViewModel.getTvDetails().observe(viewLifecycleOwner) { tvShow ->
            if (tvShow != null) {
                tvShow.adult?.let { handleContentTypeBadge(it) }
                binding.likeBTN.setOnClickListener {
                    if (tvShow.id?.let { it1 -> tvViewModel.findTv(it1) } == true) {
                        binding.likeBTN.setImageResource(R.drawable.heart)
                        tvViewModel.deleteTv(tvId = tv.id)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        tvViewModel.insertTv(tv)
                        binding.likeBTN.setImageResource(R.drawable.filled_heart)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.Added),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                when (tvViewModel.findTv(tv.id)) {
                    true -> {
                        binding.likeBTN.setImageResource(R.drawable.filled_heart)
                    }

                    false -> {
                        binding.likeBTN.setImageResource(R.drawable.heart)
                    }
                }
                val info: String =
                    (getString(R.string.tv_show) + " | " + tvShow.originalLanguage?.let { Locale(it).getDisplayName() } + " | " + getString(
                        R.string.FirstShow
                    ) + tvShow.firstAirDate)
                binding.infoTV.text = info
                initPosterImage(tvShow.posterPath.toString())
                initBackDropImage(tvShow.backdropPath.toString())
                var str = ""
                for (i in tvShow.genres!!) {
                    str += i?.name + " | "
                }
                binding.catsTV.text = str
                if (!tvShow.genres.isEmpty()) {
                    binding.catsTV.text =
                        binding.catsTV.text.removeRange((binding.catsTV.text.length - 3)..(binding.catsTV.text.length - 1))
                }
                when (tvShow.overview.isNullOrEmpty()) {
                    true -> {
                        binding.storyTV.text = getString(R.string.nostory)
                    }

                    false -> {
                        binding.storyTV.text = tvShow.overview
                    }
                }
                binding.titleTV.text = tvShow.name
                binding.ratingTV.text =
                    tvShow.voteAverage?.toFloat().toString().plus(getString(R.string.rate))
                binding.countTV.text = tvShow.voteCount.toString()
                binding.popTV.text = tvShow.popularity.toString()
            } else {
                binding.mainProgressBar.progressView.show()
                binding.parent.gone()
            }
        }
    }

    private fun listeningToLinksApi(movie: MovieModel) {
        moviesViewModel.getMovieLinks(movie.id)
        moviesViewModel.getMovieLinks().observe(viewLifecycleOwner) {
        }
    }

    private fun handleCategoriesTextView(model: MovieDetails) {
        var str = ""
        for (genre in model.genres!!) {
            str += genre?.name + " | "
        }
        binding.catsTV.text = str
        if (!model.genres.isEmpty()) {
            binding.catsTV.text =
                binding.catsTV.text.removeRange((binding.catsTV.text.length - 3)..(binding.catsTV.text.length - 1))
        }
    }

    private fun handleIsLikedButton(movie: MovieModel) {
        when (moviesViewModel.findMovie(movie.id)) {
            true -> {
                binding.likeBTN.setImageResource(R.drawable.filled_heart)
            }

            false -> {
                binding.likeBTN.setImageResource(R.drawable.heart)
            }
        }
        binding.likeBTN.setOnClickListener {
            if (movie.let { movie -> moviesViewModel.findMovie(movie.id) } == true) {
                binding.likeBTN.setImageResource(R.drawable.heart)
                moviesViewModel.deleteMovie(movieId = movie.id)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.deleted),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                moviesViewModel.insertMovie(movie)
                binding.likeBTN.setImageResource(R.drawable.filled_heart)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.Added),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleContentTypeBadge(isAdult: Boolean) {
        when (isAdult) {
            true -> binding.adultTV.show()
            else -> {
                binding.adultTV.gone()
            }
        }
    }

    private fun initPosterImage(posterPath: String) {
        binding.posterIV.loadImage(posterPath) { isSucceed, message ->
            if (isSucceed) {
                binding.progressBar3.progressView.gone()
            } else {
                binding.progressBar3.progressView.gone()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.posterIV.setOnClickListener {
            if (!posterPath.isNullOrEmpty())
                handelNavigationToZoomFragment(posterPath)
        }
    }

    private fun initBackDropImage(backdropPath: String) {
        binding.movieImg.setOnClickListener {
            if (!backdropPath.isNullOrEmpty())
                handelNavigationToZoomFragment(backdropPath)
        }
        binding.movieImg.loadImage(backdropPath) { isSucceed, message ->
            if (isSucceed) {
                binding.progressBar.progressView.gone()
            } else {
                binding.progressBar.progressView.gone()
                Log.d(this.javaClass.simpleName, message)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initInfoTextView(model: MovieDetails) {
        val info: String =
            "${getString(R.string.movie)} | ${model.originalLanguage?.let { Locale(it).getDisplayName() }} | ${
                getString(R.string.FirstShow)
            }${model.releaseDate}"
        binding.infoTV.text = info
    }

    private fun handleMovieImagesApi(movie: MovieModel) {
        moviesViewModel.getMovieImages(movie.id)
        val list = mutableListOf<CarouselItem>()
        moviesViewModel.getMovieImages().observe(viewLifecycleOwner) {
            if (!it.backdrops.isNullOrEmpty()) {
                it.backdrops.forEach {
                    list.add(
                        CarouselItem(
                            IMAGES_BASE + it?.filePath
                        )
                    )
                }
                binding.carouselImg.setData(list)
                binding.carouselCard.show()
            } else
                binding.carouselCard.hide()
        }
    }

    private fun handleTvImagesApi(tv: TvModel) {
        tvViewModel.getMovieImages(tv.id)
        val list = mutableListOf<CarouselItem>()
        tvViewModel.getTvImages().observe(viewLifecycleOwner) {
            if (!it.backdrops.isNullOrEmpty()) {
                it.backdrops.forEach {
                    list.add(
                        CarouselItem(
                            IMAGES_BASE + it?.filePath
                        )
                    )
                }
                binding.carouselImg.setData(list)
                binding.carouselCard.show()
            } else
                binding.carouselCard.hide()
        }
    }

    private fun initCarouselImage() {
        binding.carouselImg.registerLifecycle(lifecycle)
        binding.carouselImg.apply {
            autoPlay = true
            infiniteCarousel = false
            showIndicator = false
            carouselListener = object : CarouselListener {
                override fun onClick(position: Int, carouselItem: CarouselItem) {
                    super.onClick(position, carouselItem)
                    carouselItem.imageUrl?.let { handelNavigationToZoomFragment(it) }
                }
            }
        }
    }

    private fun handelNavigationToZoomFragment(imageUrl: String) {
        navigationModule.navigateTo(
            R.id.zoomFragment,
            R.id.nav_host_fragment,
            Bundle().apply {
                putString(
                    POSTER_URL,
                    IMAGES_BASE + imageUrl
                )
            })
    }

    private fun bindingSomeViews(model: MovieDetails) {
        when (model.overview.isNullOrEmpty()) {
            true -> {
                binding.storyTV.text = getString(R.string.nostory)
            }

            false -> {
                binding.storyTV.text = model.overview
            }
        }
        binding.titleTV.text = model.title
        binding.ratingTV.text = model.voteAverage?.toString().plus(getString(R.string.rate))
        binding.countTV.text = model.voteCount.toString()
        binding.popTV.text = model.popularity.toString()
    }
}