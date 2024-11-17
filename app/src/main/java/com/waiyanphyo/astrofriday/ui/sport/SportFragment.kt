package com.waiyanphyo.astrofriday.ui.sport

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waiyanphyo.astrofriday.R
import com.waiyanphyo.astrofriday.databinding.FragmentSportBinding
import com.waiyanphyo.astrofriday.domain.model.Sport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SportFragment : Fragment(R.layout.fragment_sport) {

    private lateinit var binding: FragmentSportBinding
    private val viewModel: SportViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentSportBinding.bind(view)

        binding.apply {
            ivSearch.setOnClickListener {
                if (etSearch.text.isNotEmpty()) {
                    viewModel.setQuery(etSearch.text.toString())
                }
            }
            etSearch.setOnEditorActionListener { _, _, _ ->
                if (etSearch.text.isNotEmpty()) {
                    viewModel.setQuery(etSearch.text.toString())
                }
                true
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.state.collect {
                        binding.apply {
                            if (it.isLoading) {
                                progressBar.visibility = View.VISIBLE
                                resultsLayout.visibility = View.GONE
                            } else {
                                progressBar.visibility = View.GONE
                            }
                            if (it.allSports != null) {
                                resultsLayout.visibility = View.VISIBLE
                                updateContent(binding.footballContent, it.allSports.football)
                                updateContent(binding.cricketContent, it.allSports.cricket)
                                updateContent(binding.golfContent, it.allSports.golf)
                            }
                        }
                    }
                }
                launch {
                    viewModel.events.collect {
                        when (it) {
                            is SportEvent.Error -> {
                                binding.apply {
                                    progressBar.visibility = View.GONE
                                    resultsLayout.visibility = View.GONE
                                }
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateContent(
        contentLayout: RelativeLayout,
        sportsData: List<Sport>
    ) {
        contentLayout.removeAllViews()
        if (sportsData.isEmpty()) {
            val noDataTextView = TextView(requireContext()).apply {
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 0)
                    addRule(RelativeLayout.CENTER_IN_PARENT) // Center the TextView within the parent
                }
            }
            noDataTextView.text = getString(R.string.no_relevant_data)
            contentLayout.addView(noDataTextView)
        } else {
            val recyclerView = RecyclerView(requireContext()).apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                isNestedScrollingEnabled = false
            }
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            recyclerView.layoutParams = layoutParams
            val sportAdapter = SportAdapter()
            recyclerView.adapter = sportAdapter
            sportAdapter.submitList(sportsData)
            contentLayout.addView(recyclerView)
        }
    }
}