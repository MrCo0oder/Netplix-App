package com.example.netplix.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.netplix.R
import com.example.netplix.adapter.CompaniesRecyclerAdapter
import com.example.netplix.databinding.FragmentDetailsBinding
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.NavigationModule
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel
import com.example.netplix.models.movieDetails.MovieDetails
import com.example.netplix.models.movieDetails.ProductionCompany
import com.example.netplix.models.tvDetails.TvDetails
import com.example.netplix.ui.movies.MovieViewModel
import com.example.netplix.ui.tv.TvViewModel
import com.example.netplix.utils.Constants
import com.example.netplix.utils.Constants.Companion.IMAGES_BASE
import com.example.netplix.utils.Constants.Companion.MOVIE_ID
import com.example.netplix.utils.Constants.Companion.POSTER_URL
import com.example.netplix.utils.Constants.Companion.TV_ID
import com.example.netplix.utils.gone
import com.example.netplix.utils.hide
import com.example.netplix.utils.loadImage
import com.example.netplix.utils.show
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    @Inject
    lateinit var navigationModule: NavigationModule

    @Inject
    lateinit var firebaseModule: FirebaseModule

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var moviesViewModel: MovieViewModel
    private lateinit var tvViewModel: TvViewModel
    private lateinit var companiesRecyclerAdapter: CompaniesRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationModule.init(requireActivity())
        init()
        setFragmentType()
        binding.backBtn.setOnClickListener {
            navigationModule.popBack(navHostId = R.id.nav_host_fragment)
        }
    }

    private fun setFragmentType() {
        if (arguments?.getBoolean(Constants.IS_MOVE) == true)
            getMovieFromArgs()?.let { listeningForMoviesMainApi(it) }
        else
            getTvFromArgs()?.let { listeningForTvMainApi(it) }
    }


    private fun init() {
        initViewModels()
        initCarouselImage()
        initCompaniesAdapter()
    }

    private fun initCompaniesAdapter() {
        companiesRecyclerAdapter = CompaniesRecyclerAdapter() {
        }
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
                handleIsLikedMoviesButton(movie)
                handleMovieImagesApi(movie)
                initInfoTextView(movieDetails)
                movieDetails.adult?.let { handleContentTypeBadge(it) }
                initPosterImage(movieDetails.posterPath.toString())
//                initBackDropImage(movieDetails.backdropPath.toString())
                handleCategoriesTextView(movieDetails)
                bindingSomeViews(movieDetails)
                movieDetails.productionCompanies?.filter {
                    !it?.logoPath.isNullOrEmpty()
                }?.let {
                    if (it.isNotEmpty())
                        initCompanyLogosRecycler(it as List<ProductionCompany>)
                    else
                        binding.companiesLabel.gone()
                }
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
                initInfoTextView(show = tvShow)
                initPosterImage(tvShow.posterPath.toString())
//                initBackDropImage(tvShow.backdropPath.toString())
                handleIsLikedTvButton(tv)
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
                tvShow.productionCompanies?.filter {
                    !it?.logoPath.isNullOrEmpty()
                }?.let {
                    if (it.isNotEmpty())
                        initCompanyLogosRecycler(it as List<ProductionCompany>)
                    else
                        binding.companiesLabel.gone()
                }

                binding.homePageImageView.setOnClickListener {
                    if (!tvShow.homepage.isNullOrEmpty()) {
                        openUrlWithChromeTab(tvShow.homepage)
                    }
                }
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
            str += "${genre?.name} | "
        }
        binding.catsTV.text = str
        if (!model.genres.isEmpty()) {
            binding.catsTV.text =
                binding.catsTV.text.removeRange((binding.catsTV.text.length - 3)..(binding.catsTV.text.length - 1))
        }
    }

    private fun handleIsLikedMoviesButton(movie: MovieModel) {
        moviesViewModel.isFav(movie.id.toString()) { b, s ->
            binding.likeProgressBar.progressView.visibility = View.INVISIBLE
            binding.likeBTN.show()
            Log.d("handleIsLikedButton:", s)

            when (b) {
                true -> {
                    binding.likeBTN.setImageResource(R.drawable.filled_heart)
                }

                false -> {
                    binding.likeBTN.setImageResource(R.drawable.heart)
                }
            }
            binding.likeBTN.setOnClickListener {
                binding.likeProgressBar.progressView.show()
                binding.likeBTN.visibility = View.INVISIBLE
                moviesViewModel.isFav(movie.id.toString()) { b, s ->
                    when (b) {
                        true -> {
                            moviesViewModel.deleteMovieFromFB(movie.id.toString()) {
                                if (it.isSuccessful)
                                    binding.likeBTN.setImageResource(R.drawable.heart)
                                binding.likeProgressBar.progressView.visibility = View.INVISIBLE
                                binding.likeBTN.show()
                            }
                        }

                        false -> {
                            moviesViewModel.addMovieToFB(movie) { b, s ->

                                if (b) {
                                    binding.likeBTN.setImageResource(R.drawable.filled_heart)
                                    Log.d("handleIsLikedButton:add", s)
                                }
                                binding.likeProgressBar.progressView.visibility = View.INVISIBLE
                                binding.likeBTN.show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleIsLikedTvButton(tv: TvModel) {
        tvViewModel.isFav(tv.id.toString()) { b, s ->
            binding.likeProgressBar.progressView.visibility = View.INVISIBLE
            binding.likeBTN.show()
            Log.d("handleIsLikedButton:", s)
            when (b) {
                true -> {
                    binding.likeBTN.setImageResource(R.drawable.filled_heart)
                }

                false -> {
                    binding.likeBTN.setImageResource(R.drawable.heart)
                }
            }
            binding.likeBTN.setOnClickListener {
                binding.likeProgressBar.progressView.show()
                binding.likeBTN.visibility = View.INVISIBLE
                tvViewModel.isFav(tv.id.toString()) { b, s ->
                    when (b) {
                        true -> {
                            tvViewModel.deleteTvFromFB(tv.id.toString()) {
                                if (it.isSuccessful)
                                    binding.likeBTN.setImageResource(R.drawable.heart)
                                binding.likeProgressBar.progressView.visibility = View.INVISIBLE
                                binding.likeBTN.show()
                            }
                        }

                        false -> {
                            tvViewModel.addTvToFB(tv) { b, s ->
                                if (b) {
                                    binding.likeBTN.setImageResource(R.drawable.filled_heart)
                                    Log.d("handleIsLikedButton:add", s)
                                }
                                binding.likeProgressBar.progressView.visibility = View.INVISIBLE
                                binding.likeBTN.show()
                            }
                        }
                    }
                }
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
        binding.posterIV.loadImage(this, posterPath) { isSucceed, message ->
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

    private fun initInfoTextView(movie: MovieDetails? = null, show: TvDetails? = null) {
        var info: String =
            if (arguments?.getBoolean(Constants.IS_MOVE) == true)
                movie?.let { getMovieInfoText(it) }.toString()
            else
                show?.let { getShowInfoText(it) }.toString()
        binding.infoTV.text = info
    }

    private fun getMovieInfoText(model: MovieDetails): String =
        "${getString(R.string.movie)} | ${model.originalLanguage?.let { Locale(it).getDisplayName() }} | ${
            getString(R.string.FirstShow)
        }${model.releaseDate}"

    private fun getShowInfoText(tvShow: TvDetails): String =
        "${getString(R.string.tv_show)} | ${tvShow.originalLanguage?.let { Locale(it).getDisplayName() }} | ${
            getString(R.string.FirstShow)
        } ${tvShow.firstAirDate}"

    private fun handleMovieImagesApi(movie: MovieModel) {
        moviesViewModel.getMovieImages(movie.id)
        val list = mutableListOf<CarouselItem>()
        if (view != null) {
            moviesViewModel.getMovieImages()
                .observe(viewLifecycleOwner) {
                    if (!it.backdrops.isNullOrEmpty()) {
                        it.backdrops.forEach {
                            list.add(
                                CarouselItem(
                                    IMAGES_BASE + it?.filePath
                                )
                            )
                        }
                        binding.carouselImg.setData(list)
                    }
                }
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
            }
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
            },
        )
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
        if (model.imdbId.isNullOrEmpty().not()) {
            binding.imdbImageView.show()
        } else {
            binding.imdbImageView.gone()
        }
        binding.imdbImageView.setOnClickListener {
            openUrlWithChromeTab("https://www.imdb.com/title/${model.imdbId}/")
        }
        binding.titleTV.text = model.title
        binding.ratingTV.text = model.voteAverage?.toString().plus(getString(R.string.rate))
        binding.countTV.text = model.voteCount.toString()
        binding.popTV.text = model.popularity.toString()
        binding.homePageImageView.setOnClickListener {
            if (!model.homepage.isNullOrEmpty()) {
                openUrlWithChromeTab(model.homepage)
            }
        }
    }

    private fun initCompanyLogosRecycler(item: List<ProductionCompany>) {
        companiesRecyclerAdapter.setData(item)
        binding.companiesRecyclerView.adapter = companiesRecyclerAdapter
    }

    fun openUrlWithChromeTab(url: String?) {
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        if (url != null) {
            if (url.isNotEmpty() && url.isNotBlank()) {
                try {
                    val customTabsIntent: CustomTabsIntent = builder.build()
                    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
                } catch (e: java.lang.Exception) {
                    val customTabsIntent: CustomTabsIntent = builder.build()
                    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    customTabsIntent.launchUrl(requireContext(), Uri.parse("http://$url"))
                }
            }
        }
    }

    private fun addMovieToFirebase(movie: List<MovieModel>, callBack: (Boolean, String) -> Unit) {
//        firebaseModule.addMovieToList(movie, callBack)
    }

    private fun addTvToFirebase(tv: TvModel, callBack: (Boolean, String) -> Unit) {
        firebaseModule.addTvShowToList(tv, callBack)
    }
}