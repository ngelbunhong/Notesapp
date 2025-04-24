package com.example.notesapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id : Int =0,
    val title: String,
    val content: String,
    val date: String
): Parcelable {
    override fun toString(): String {
        return "Note(id=$id, title='$title', content='$content,date=$date')"
    }
}
