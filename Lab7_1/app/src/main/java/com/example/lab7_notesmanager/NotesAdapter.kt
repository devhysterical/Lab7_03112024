package com.example.lab7_notesmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lab7_notesmanager.databinding.ItemNoteBinding
import com.google.firebase.database.FirebaseDatabase


class NotesAdapter(
    private val context: Context,
    private val notes: MutableList<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvTitle.text = note.title
            binding.tvContent.text = note.content
            binding.root.setOnClickListener { onNoteClick(note) }
            binding.btnDelete.setOnClickListener {
                val position = adapterPosition
                notes.removeAt(position)
                notifyItemRemoved(position)
                FirebaseDatabase.getInstance().reference.child("notes").child(note.id!!).removeValue()
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size
}
