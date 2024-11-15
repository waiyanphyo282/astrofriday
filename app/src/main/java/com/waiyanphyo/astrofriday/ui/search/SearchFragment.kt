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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.waiyanphyo.astrofriday.R
import com.waiyanphyo.astrofriday.databinding.FragmentSearchBinding
import com.waiyanphyo.astrofriday.domain.util.Result
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
                viewModel.locations.collect { result ->
                    when (result) {
                        is Result.Error -> {
                            // show Error Message
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = result.error.message
                        }
                        Result.Loading -> {
                            // Show Loading indicator
                            binding.tvError.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            // Show Data
                            binding.tvError.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            searchAdapter.submitList(result.data)
                        }

                        null -> {
                            binding.tvError.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun searchLocation() {
        val query = binding.etSearch.text.toString()
        viewModel.searchWeather(query)
    }

}