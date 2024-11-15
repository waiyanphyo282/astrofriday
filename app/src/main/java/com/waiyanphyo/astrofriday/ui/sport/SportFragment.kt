package com.waiyanphyo.astrofriday.ui.sport

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.waiyanphyo.astrofriday.R
import com.waiyanphyo.astrofriday.databinding.FragmentSearchBinding
import com.waiyanphyo.astrofriday.databinding.FragmentSportBinding
import com.waiyanphyo.astrofriday.domain.model.AllSports
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
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                gravity = View.TEXT_ALIGNMENT_CENTER
            }
            noDataTextView.text = getString(R.string.no_relevant_data)
            contentLayout.addView(noDataTextView)
        } else {
            val tableLayout = TableLayout(requireContext())
            populateTable(tableLayout, sportsData)
            contentLayout.addView(tableLayout)
        }
    }

    private fun populateTable(tableLayout: TableLayout, sportsData: List<Sport>) {
        addHeaderRow(tableLayout)
        sportsData.forEach { sport ->
            val tableRow = createTableRow(
                listOf(
                    sport.stadium,
                    sport.country,
                    sport.tournament,
                    sport.date,
                    sport.match
                )
            )
            tableLayout.addView(tableRow)
        }
    }

    private fun addHeaderRow(tableLayout: TableLayout) {
        val headerRow = createTableRow(
            listOf("Stadium", "Country", "Tournament", "Date", "Match")
        )
        tableLayout.addView(headerRow)
    }

    private fun createTableRow(values: List<String>): TableRow {
        return TableRow(requireContext()).apply {
            values.forEach { value ->
                addView(
                    TextView(requireContext()).apply {
                        text = value
                        textSize = 14f
                        setPadding(8, 8, 8, 8)
                    }
                )
            }
        }
    }
}