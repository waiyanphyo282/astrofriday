package com.waiyanphyo.astrofriday.ui.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.waiyanphyo.astrofriday.R
import com.waiyanphyo.astrofriday.databinding.FragmentSearchBinding
import com.waiyanphyo.astrofriday.domain.util.Result
import com.waiyanphyo.astrofriday.domain.util.asEmptyDataResult
import com.waiyanphyo.astrofriday.domain.util.onError
import com.waiyanphyo.astrofriday.domain.util.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentSearchBinding.bind(view)

        binding.apply {
            rvLocation.adapter = searchAdapter
            etSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchLocation()
                    true
                } else {
                    false
                }
            }
            btnSearch.setOnClickListener {
                searchLocation()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                            Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.rvLocation.visibility = View.VISIBLE
                        }
                        searchAdapter.submitList(result.locations)
                    }
                    if (result.errorMessage.isNotEmpty()) {
                        binding.rvLocation.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = result.errorMessage
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