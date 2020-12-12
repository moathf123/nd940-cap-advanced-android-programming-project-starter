package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentElectionBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_election, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ElectionDatabase.getInstance(application).electionDao

        val viewModelFactory = ElectionsViewModelFactory(dataSource)

        viewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(ElectionsViewModel::class.java)

        binding.lifecycleOwner = this

        binding.viewmodel = viewModel


        val savedElectionsAdapter = ElectionListAdapter(ElectionListener {
            findNavController()
                    .navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id, it.division))
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            savedElectionsAdapter.submitList(it)
        })

        binding.recyclerViewSavedElections.adapter = savedElectionsAdapter


        val upcomingElectionsAdapter = ElectionListAdapter(ElectionListener {
            findNavController()
                    .navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id, it.division))
        })

        binding.recyclerViewUpcomingElections.adapter = upcomingElectionsAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                upcomingElectionsAdapter.submitList(it)
            }
        })


        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}
