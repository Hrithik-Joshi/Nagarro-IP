package com.example.q2_ip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.q2_ip.R
import com.example.q2_ip.databinding.ActivityNewsBinding
import com.example.q2_ip.db.ArticleDatabase
import com.example.q2_ip.repository.NewsRepository


class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = Navigation.findNavController(this, R.id.newsNavHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController);


    }
}