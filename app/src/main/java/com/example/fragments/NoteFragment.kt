package com.example.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var inputText: EditText
    private lateinit var addButton: Button
    private lateinit var dbHelper: NoteDatabaseHelper

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        inputText = view.findViewById(R.id.inputText)
        addButton = view.findViewById(R.id.addButton)
        dbHelper = NoteDatabaseHelper(requireContext())
        noteList = dbHelper.getAllNotes().toMutableList()
        adapter = NoteAdapter(noteList, dbHelper) { note ->
            val fragment = NoteEditFragment.newInstance(note)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        addButton.setOnClickListener {
            val noteText = inputText.text.toString()
            if (noteText.isNotEmpty()) {
                val newNote = Note(
                    id = 0,
                    text = noteText,
                    isCompleted = false,
                    dateCreated = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault()).format(Date())
                )
                dbHelper.addNote(newNote)
                noteList.add(newNote)
                adapter.notifyItemInserted(noteList.size - 1)
                inputText.setText("")
            }
        }
        parentFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            val updatedNote = bundle.getSerializable("updatedNote") as? Note
            val deletedNoteId = bundle.getInt("deletedNoteId", -1)

            updatedNote?.let { updateNoteInList(it) }
            if (deletedNoteId != -1) {
                removeNoteFromList(deletedNoteId)
            }
        }
        return view
    }

    fun updateNoteInList(updatedNote: Note) {
        val index = noteList.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            noteList[index] = updatedNote
            adapter.notifyItemChanged(index)
        }
    }

    private fun removeNoteFromList(noteId: Int) {
        val index = noteList.indexOfFirst { it.id == noteId }
        if (index != -1) {
            noteList.removeAt(index)
            adapter.notifyItemRemoved(index)
        }
    }
}





