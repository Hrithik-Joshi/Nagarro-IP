package com.example.q2_ip.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.q2_ip.R
import com.example.q2_ip.adapters.NewsAdapter
import com.example.q2_ip.databinding.FragmentBreakingNewsBinding
import com.example.q2_ip.databinding.ItemErrorMessageBinding
import com.example.q2_ip.ui.NewsActivity
import com.example.q2_ip.ui.NewsViewModel
import com.example.q2_ip.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentBreakingNewsBinding

    val TAG = "BreakingNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }


        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Error occurred $message",Toast.LENGTH_LONG).show()
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
            viewModel.getBreakingNews("us")
        }
    }
    private fun hideErrorMessage() {
        val itemErrorMessage = binding.root.findViewById<View>(R.id.itemErrorMessage)
        itemErrorMessage.visibility=View.INVISIBLE
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
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}