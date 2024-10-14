package com.example.fragments

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TEXT = "text"
        private const val COLUMN_COMPLETED = "completed"
        private const val COLUMN_DATE = "dateCreated"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEXT + " TEXT,"
                + COLUMN_COMPLETED + " INTEGER,"
                + COLUMN_DATE + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addNote(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TEXT, note.text)
        values.put(COLUMN_COMPLETED, if (note.isCompleted) 1 else 0)
        values.put(COLUMN_DATE, note.dateCreated)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateNoteCompletion(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_COMPLETED, if (note.isCompleted) 1 else 0)

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val noteList: MutableList<Note> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT)),
                    isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1,
                    dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                )
                noteList.add(note)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return noteList
    }
}

