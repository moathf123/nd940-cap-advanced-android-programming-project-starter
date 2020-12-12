package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.Repo
import com.example.android.politicalpreparedness.database.ElectionDao
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
        val database: ElectionDao
) : ViewModel() {

    private val repo = Repo(database)

    val upcomingElections = repo.elections
    //val savedElections = repo.savedElections

    init {
        viewModelScope.launch {
            repo.refreshElections()
            d { "im called lol" }
        }
    }
}