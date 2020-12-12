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

    //TODO: Declare ViewModel
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

        binding.viewmodel = viewModel

        binding.lifecycleOwner = this


        val savedElectionsAdapter = ElectionListAdapter(ElectionListener {
            findNavController()
                    .navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id, it.division))
        })

        binding.recyclerViewSavedElections.adapter = savedElectionsAdapter

        val upcomingElectionsAdapter = ElectionListAdapter(ElectionListener {
            findNavController()
                    .navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id, it.division))
        })


        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            upcomingElectionsAdapter.submitList(it)
        })

        binding.recyclerViewUpcomingElections.adapter = upcomingElectionsAdapter

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}

//Done: Add ViewModel values and create ViewModel

//Done: Add binding values

//TODO: Link elections to voter info

//Done: Initiate recycler adapters

//Done: Populate recycler adapters