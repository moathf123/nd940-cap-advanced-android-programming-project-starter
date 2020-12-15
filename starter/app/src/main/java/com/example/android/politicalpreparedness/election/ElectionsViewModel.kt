package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.Repo
import com.example.android.politicalpreparedness.database.ElectionDao
import kotlinx.coroutines.launch

class ElectionsViewModel(
        dataSource: ElectionDao
) : ViewModel() {

    private val repo = Repo(dataSource)

    val upcomingElections = repo.elections
    val savedElections = repo.savedElections

    init {
        viewModelScope.launch {
            repo.refreshElections()
        }
    }
}