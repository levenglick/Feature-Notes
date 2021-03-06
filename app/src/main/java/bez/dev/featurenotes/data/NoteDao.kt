package bez.dev.featurenotes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("UPDATE note_table SET isNotification = 0 WHERE isNotification = 1 ")
    fun resetAllNotifications()

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotesByPriority(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun getNoteById(noteId: Long): LiveData<Note>
}

