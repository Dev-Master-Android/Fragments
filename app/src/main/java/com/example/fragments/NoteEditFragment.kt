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
import androidx.core.os.bundleOf

class NoteEditFragment : Fragment() {
    private lateinit var note: Note
    private lateinit var editText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var dbHelper: NoteDatabaseHelper

    companion object {
        private const val ARG_NOTE = "note"

        fun newInstance(note: Note): NoteEditFragment {
            val fragment = NoteEditFragment()
            val args = Bundle()
            args.putSerializable(ARG_NOTE, note)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getSerializable(ARG_NOTE) as Note
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_edit, container, false)

        editText = view.findViewById(R.id.editText)
        updateButton = view.findViewById(R.id.updateButton)
        deleteButton = view.findViewById(R.id.deleteButton)
        dbHelper = NoteDatabaseHelper(requireContext())

        editText.setText(note.text)

        updateButton.setOnClickListener {
            val updatedText = editText.text.toString()
            if (updatedText.isNotEmpty()) {
                note.text = updatedText
                dbHelper.updateNote(note)
                parentFragmentManager.setFragmentResult(
                    "requestKey",
                    bundleOf("updatedNote" to note)
                )
                parentFragmentManager.popBackStack()
            }
        }

        deleteButton.setOnClickListener {
            dbHelper.deleteNote(note.id)
            parentFragmentManager.setFragmentResult(
                "requestKey",
                bundleOf("deletedNoteId" to note.id)
            )
            parentFragmentManager.popBackStack()
        }
        return view
    }
}

