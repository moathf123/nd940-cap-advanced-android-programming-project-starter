package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        private const val REQUEST_FOREGROUND_PERMISSION_RESULT_CODE = 10
        private const val APPLICATION_ID = "com.example.android.politicalpreparedness"
    }

    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var representativeAdapter: RepresentativeListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_representative, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)
        binding.viewModel = viewModel

        val states = resources.getStringArray(R.array.states)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, states)
        binding.state.adapter = adapter


        binding.buttonLocation.setOnClickListener {
            checkLocationPermissions()
        }

        binding.buttonSearch.setOnClickListener {
            viewModel.getRepresentatives()
            hideKeyboard()
        }

        representativeAdapter = RepresentativeListAdapter()
        binding.recyclerView.adapter = representativeAdapter


        viewModel._representatives.observe(viewLifecycleOwner, Observer {
            representativeAdapter.submitList(it)
        })

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (
                grantResults.isEmpty() ||
                (requestCode == REQUEST_FOREGROUND_PERMISSION_RESULT_CODE &&
                        grantResults[0] ==
                        PackageManager.PERMISSION_DENIED)) {
            // Permission denied.
            Snackbar.make(
                    binding.representativeMotionLayout,
                    getString(R.string.explanation_for_denied_location), Snackbar.LENGTH_INDEFINITE
            )
                    .setAction(getString(R.string.setting)) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
        } else {
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            getLocation()
            true
        } else {
            val permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(
                    permissionsArray,
                    REQUEST_FOREGROUND_PERMISSION_RESULT_CODE)
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION))
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    if (it != null) {
                        val address = geoCodeLocation(it)
                        viewModel.address.value = address

                        val states = resources.getStringArray(R.array.states)
                        val selectedStateIndex = states.indexOf(address.state)
                        binding.state.setSelection(selectedStateIndex)

                        viewModel.getRepresentatives()
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}