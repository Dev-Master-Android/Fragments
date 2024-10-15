package com.example.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val notes: List<Note>,
    private val dbHelper: NoteDatabaseHelper,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteId.text = note.id.toString()
        holder.noteText.text = note.text
        holder.noteCheckbox.setOnCheckedChangeListener(null)
        holder.noteCheckbox.isChecked = note.isCompleted
        holder.noteDate.text = note.dateCreated
        holder.noteCheckbox.tag = note.id
        holder.noteCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            val tag = buttonView.tag as Int
            val currentNote = notes.find { it.id == tag }
            currentNote?.isCompleted = isChecked
            if (currentNote != null) {
                dbHelper.updateNoteCompletion(currentNote)
            }
        }
        holder.itemView.setOnClickListener {
            onNoteClick(note)
        }
    }

    override fun getItemCount() = notes.size

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteId: TextView = view.findViewById(R.id.note_id)
        val noteText: TextView = view.findViewById(R.id.note_text)
        val noteCheckbox: CheckBox = view.findViewById(R.id.note_checkbox)
        val noteDate: TextView = view.findViewById(R.id.note_date)
    }
}



