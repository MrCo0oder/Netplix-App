package com.example.netplix.models

data class MoviesPage(
    var page: Int,
    var results: List<MovieModel>,
    var total_pages: Int,
    var total_results: Int
)