package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentVoterInfoBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_voter_info, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ElectionDatabase.getInstance(application).electionDao

        val viewModelFactory = VoterInfoViewModelFactory(dataSource)

        viewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        val election = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElection
        viewModel.getElectionById(election.id)

        viewModel.getVoterInfo(election.division.id, election.id)
        binding.button.setOnClickListener {
            viewModel.updateElection(election)
        }

        viewModel.electionSavedStat.observe(viewLifecycleOwner, Observer {
            if (it == true)
                binding.button.text = getString(R.string.unfollow)
            else
                binding.button.text = getString(R.string.follow)
        })

        viewModel.url.observe(viewLifecycleOwner, Observer {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        })


        return binding.root
    }

    //TODO: Create method to load URL intents

}