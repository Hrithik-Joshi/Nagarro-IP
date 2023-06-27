package com.example.q2_ip.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.q2_ip.R
import com.example.q2_ip.databinding.FragmentArticleBinding
import com.example.q2_ip.ui.NewsActivity
import com.example.q2_ip.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding:FragmentArticleBinding
    val args: ArticleFragmentArgs by navArgs()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentArticleBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }
        binding.fab.setOnClickListener {
            viewModel.saveArticles(article)
            Snackbar.make(view,"Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}