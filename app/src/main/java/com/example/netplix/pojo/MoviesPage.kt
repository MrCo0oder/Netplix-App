package com.example.netplix.pojo

data class MoviesPage(
    var page: Int,
    var results: List<MovieModel>,
    var total_pages: Int,
    var total_results: Int
)