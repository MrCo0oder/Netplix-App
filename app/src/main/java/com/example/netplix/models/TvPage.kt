package com.example.netplix.models

data class TvPage(
    var page: Int,
    var results: List<TvModel>,
    var total_pages: Int,
    var total_results: Int
)