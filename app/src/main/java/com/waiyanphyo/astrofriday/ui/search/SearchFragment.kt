package com.waiyanphyo.astrofriday.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.waiyanphyo.astrofriday.R
import com.waiyanphyo.astrofriday.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)

        binding.apply {
            rvLocation.adapter = searchAdapter
            etSearch.setOnEditorActionListener { _, _, _ ->
                searchLocation()
                true
            }
            btnSearch.setOnClickListener {
                searchLocation()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { result ->
                        if (result.isLoading) {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                            binding.rvLocation.visibility = View.GONE
                        }
                        if (result.locations != null) {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                            if (result.locations.isEmpty()) {
                                binding.rvLocation.visibility = View.GONE
                            } else {
                                binding.rvLocation.visibility = View.VISIBLE
                            }
                            searchAdapter.submitList(result.locations)
                        }
                    }
                }
                launch {
                    viewModel.events.collectLatest {
                        when (it) {

                            is SearchEvent.ErrorTextView -> {
                                binding.tvError.text = it.message
                                binding.tvError.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                binding.rvLocation.visibility = View.GONE
                            }
                            is SearchEvent.ErrorToast -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun searchLocation() {
        val query = binding.etSearch.text.toString()
        if (query.isNotEmpty()) {
            binding.etSearch.error = null
            viewModel.searchWeather(query)
        } else {
            binding.etSearch.error = "Please enter a location"
        }
    }

}