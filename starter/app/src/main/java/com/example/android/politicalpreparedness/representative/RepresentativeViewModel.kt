package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {

    val _representatives = MutableLiveData<List<Representative>>()
    val address = MutableLiveData<Address>()

    fun getRepresentatives() {
        if (address.value != null) {
            viewModelScope.launch {
                try {
                    val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address.value!!.toFormattedString()).await()
                    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
