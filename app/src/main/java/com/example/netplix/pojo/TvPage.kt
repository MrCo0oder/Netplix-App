package com.example.netplix.pojo

data class TvPage(
    var page: Int,
    var results: List<TvModel>,
    var total_pages: Int,
    var total_results: Int
)