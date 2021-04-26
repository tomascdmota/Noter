package com.tomascdmota.notetaker.dao

import androidx.room.*
import com.tomascdmota.notetaker.entities.Notes


@Dao
interface NoteDao {


     @get:Query("SELECT * FROM notes ORDER BY id DESC")
    val allNotes: List<Notes>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes) {}


    @Delete()
    suspend fun delete(note:Notes) {

    }
}