package com.example.q2_ip.model

import com.example.q2_ip.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)