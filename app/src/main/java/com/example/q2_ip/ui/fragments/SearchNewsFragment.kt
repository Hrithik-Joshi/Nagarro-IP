package com.example.q2_ip.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.q2_ip.R
import com.example.q2_ip.adapters.NewsAdapter
import com.example.q2_ip.databinding.FragmentSearchNewsBinding
import com.example.q2_ip.databinding.ItemErrorMessageBinding
import com.example.q2_ip.ui.NewsActivity
import com.example.q2_ip.ui.NewsViewModel
import com.example.q2_ip.util.Resource
import com.example.q2_ip.util.constants.Companion.SEARCH_NEWS_TIME_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel

    lateinit var newsAdapter: NewsAdapter

    lateinit var binding: FragmentSearchNewsBinding


    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }

        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        val btnRetry = binding.root.findViewById<View>(R.id.btnRetry)
        btnRetry.setOnClickListener {
            if (binding.etSearch.text.toString().isNotEmpty()) {
                viewModel.getSearchNews(binding.etSearch.text.toString())
            } else {
                hideErrorMessage()
            }
        }
    }

    private fun hideErrorMessage() {
        val itemErrorMessage = binding.root.findViewById<View>(R.id.itemErrorMessage)
        itemErrorMessage.visibility = View.INVISIBLE
    }

    private fun showErrorMessage(message:String) {
        val itemErrorMessage = binding.root.findViewById<View>(R.id.itemErrorMessage)
        itemErrorMessage.visibility=View.VISIBLE
        val tvErrorMessage = binding.root.findViewById<TextView>(R.id.tvErrorMessage)
        tvErrorMessage.text=message
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}