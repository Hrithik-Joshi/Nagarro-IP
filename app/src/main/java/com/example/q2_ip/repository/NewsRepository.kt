package com.example.q2_ip.repository

import com.example.q2_ip.api.RetrofitInstance
import com.example.q2_ip.db.ArticleDatabase
import com.example.q2_ip.model.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQyery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQyery, pageNumber)

    suspend fun insertArticle(article: Article) = db.getArticleDao().insertArticle(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteNews(article:Article)=db.getArticleDao().deleteArticle(article)
}