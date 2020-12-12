package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {


    //TODO: Add select single election query


    //TODO: Add clear query

    //Done: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElections(elections: List<Election>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)

    //Done: Add select all election query
    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table where Saved = 1")
    fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id=:id")
    fun getElectionById(id: Int): LiveData<Election>

    //Done: Add delete query
    @Query("DELETE FROM election_table where id=:id")
    fun deleteElectionById(id: Int)
}

