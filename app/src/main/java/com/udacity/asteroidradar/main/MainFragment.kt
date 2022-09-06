package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this, MainViewModel.Factory(application))[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidListener {
            viewModel.displayDetails(it)
        })



        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayDetailsComplete()
            }
        })



        viewModel.isToday.observe(viewLifecycleOwner, Observer {
            if (it){
                (binding.asteroidRecycler.adapter as AsteroidAdapter).submitList(viewModel.dailyList.value)
            }else{
                (binding.asteroidRecycler.adapter as AsteroidAdapter).submitList(viewModel.weeklyList.value)
            }
        })

        viewModel.dailyList.observe(viewLifecycleOwner, Observer {

            Log.e("MainFragment" , "dailyList: "+it?.size)
        })
        viewModel.weeklyList.observe(viewLifecycleOwner, Observer {

            Log.e("MainFragment" , "weeklyList: "+it?.size)
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when(item.itemId){
                R.id.show_today_asteroid ->true
                R.id.show_all_menu ->false

                else -> false
            }
        )
        return true
    }
}
