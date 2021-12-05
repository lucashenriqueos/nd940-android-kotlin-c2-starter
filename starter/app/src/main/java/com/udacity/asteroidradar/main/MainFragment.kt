package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.activityMainImageOfTheDay.contentDescription = getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)

        viewModel.picOfTheDayLiveData.observe(viewLifecycleOwner, Observer {
            binding.activityMainImageOfTheDay.contentDescription = getString(R.string
                .nasa_picture_of_day_content_description_format, it.title)

            Picasso.with(requireContext())
                .load(it.url)
                .into(binding.activityMainImageOfTheDay)
        })

        binding.asteroidRecycler.adapter = AsteroidsAdapter { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
