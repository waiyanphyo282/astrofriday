package com.waiyanphyo.astrofriday.ui.astronomy

import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.waiyanphyo.astrofriday.R
import com.waiyanphyo.astrofriday.databinding.FragmentAstronomyBinding
import com.waiyanphyo.astrofriday.domain.util.toFormattedString
import com.waiyanphyo.astrofriday.domain.util.toHumanReadableDistance
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AstronomyFragment : Fragment() {

    private var _binding: FragmentAstronomyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AstronomyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAstronomyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {

            etDate.setText(getFormattedDate(Date()))
            etDate.setOnClickListener {
                showDatePicker(onDateSelected = { selectedDate ->
                    run {
                        etDate.setText(selectedDate)
                        if (etLocation.text.toString().isNotEmpty()) {
                            viewModel.loadAstronomy(etLocation.text.toString(), selectedDate)
                        }
                    }
                })
            }

            etLocation.setOnEditorActionListener { _, _, _ ->
                val query = etLocation.text.toString()
                val date = etDate.text.toString()
                viewModel.loadAstronomy(query, date)
                true
            }

            viewModel.loadAstronomy(etLocation.text.toString(), etDate.text.toString())

            btnSearch.setOnClickListener {
                val query = etLocation.text.toString()
                val date = etDate.text.toString()
                viewModel.loadAstronomy(query, date)
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        if (state.isLoading) {
                            binding.tvSunrise.text = getString(R.string.loading)
                            binding.tvSunset.text = getString(R.string.loading)
                            binding.tvMoonrise.text = getString(R.string.loading)
                            binding.tvMoonset.text = getString(R.string.loading)
                            binding.tvSunriseToMoonrise.text = getString(R.string.loading)
                            binding.tvSunsetToMoonset.text = getString(R.string.loading)
                        } else {
                            if (state.astronomy != null) {
                                binding.apply {
                                    tvLocation.text = state.astronomy.locationName
                                    tvRegionCountry.text = buildString {
                                        append(state.astronomy.region)
                                        append(", ")
                                        append(state.astronomy.country)
                                    }
                                    tvDistance.text = resources.getString(R.string.distance_placeholder, state.astronomy.distance.toHumanReadableDistance())
                                    tvLocalTime.text = resources.getString(R.string.local_time_placeholder, state.astronomy.localTime)
                                    tvSunrise.text = state.astronomy.sunrise
                                    tvSunset.text = state.astronomy.sunset
                                    tvMoonrise.text = state.astronomy.moonrise
                                    tvMoonset.text = state.astronomy.moonset
                                    tvSunriseToMoonrise.text = resources.getString(R.string.sunrise_to_moonrise_placeholder, state.astronomy.sunriseToMoonrise.toFormattedString())
                                    tvSunsetToMoonset.text = resources.getString(R.string.sunset_to_moonset_placeholder, state.astronomy.sunsetToMoonset.toFormattedString())
                                }
                            }
                        }
                    }
                }
            }
        }

        return root
    }

    private fun showDatePicker(onDateSelected: (selectedDate: String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateSelected(getFormattedDate(calendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun getFormattedDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        return formattedDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}