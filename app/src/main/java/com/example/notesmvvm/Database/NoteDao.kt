package com.example.notesmvvm.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesmvvm.Model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Update notes_table Set title =:title, note= :note Where id= :id")
    suspend fun update(id: Int?, title: String?, note:String?)

    @Query("Select * from notes_table order by id DESC")
    fun getAllNotes(): LiveData<List<Note>>
}