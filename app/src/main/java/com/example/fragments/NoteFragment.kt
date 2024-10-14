package com.example.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
        adapter = NoteAdapter(noteList, dbHelper)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        addButton.setOnClickListener {
            val noteText = inputText.text.toString()
            if (noteText.isNotEmpty()) {
                val newNote = Note(
                    id = 0, // id будет автоматически увеличен в базе данных
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

        return view
    }
}



