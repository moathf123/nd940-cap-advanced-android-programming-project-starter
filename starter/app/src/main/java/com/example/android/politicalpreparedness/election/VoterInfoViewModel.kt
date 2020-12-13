package com.example.android.politicalpreparedness.election


import androidx.lifecycle.*
import com.example.android.politicalpreparedness.Repo
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class VoterInfoViewModel(dataSource: ElectionDao) : ViewModel() {

    private val repo = Repo(dataSource)
    val voterInfo = repo.voterInfo
    val electionSavedStat = MutableLiveData<Boolean>()
    var url = MutableLiveData<String>()
    private val electionId = MutableLiveData<Int>()
    val election = electionId.switchMap {
        liveData {
            emitSource(repo.getElectionById(it))
        }
    }

    init {
        electionSavedStat.value = false
    }

    fun getVoterInfo(address: String, electionId: Int) {
        viewModelScope.launch { repo.getVoterInfo(address, electionId) }
    }

    fun getUrl(url: String) {
        this.url.value = url
    }

    fun updateElection(election: Election) {
        viewModelScope.launch {
            election.Saved = !election.Saved
            electionSavedStat.value = !electionSavedStat.value!!
            repo.updateElection(election)
        }
    }

    fun getElectionById(id: Int) {
        electionId.value = id
    }

}