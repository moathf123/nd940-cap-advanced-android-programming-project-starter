package com.example.android.politicalpreparedness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repo(private val database: ElectionDao) {

    val elections: LiveData<List<Election>> = database.getAllElections()
    val voterInfo = MutableLiveData<VoterInfoResponse>()
    val representatives = MutableLiveData<RepresentativeResponse>()


    fun getElection(id: Int) = database.getElectionById(id)

    suspend fun getVoterInfo(electionId: Int, address: String) {
        try {
            withContext(Dispatchers.IO) {
                val voterInfoNetworkResult = CivicsApi.retrofitService.getVoterinfo().await()
                voterInfo.postValue(voterInfoNetworkResult)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertElection(election: Election) {
        withContext(Dispatchers.IO) {
            database.insertElection(election)
        }
    }


    suspend fun refreshElections() {
        try {
            withContext(Dispatchers.IO) {
                val electionNetworkResult = CivicsApi.retrofitService.getElections().await()
                database.insertElections(electionNetworkResult.elections)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //val savedElections: LiveData<List<Election>> = database.getSavedElections()
}