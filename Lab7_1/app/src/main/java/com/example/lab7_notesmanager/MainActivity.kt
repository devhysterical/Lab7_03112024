package com.example.lab7_notesmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab7_notesmanager.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>()
    private val database = FirebaseDatabase.getInstance().reference.child("notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        binding.btnAddNote.setOnClickListener {
            val intent = Intent(this, EditNoteActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD)
        }

        fetchNotes()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(this, notesList) { note ->
            val intent = Intent(this, EditNoteActivity::class.java).apply {
                putExtra("note", note)
            }
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }
        binding.rvNotes.adapter = notesAdapter
        binding.rvNotes.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchNotes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notesList.clear()
                snapshot.children.mapNotNullTo(notesList) { dataSnapshot ->
                    val note = dataSnapshot.getValue(Note::class.java)
                    note?.apply { id = dataSnapshot.key }
                    note // Return the note object
                }
                notesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error fetching notes", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ADD -> {
                    val newNote = data?.getSerializableExtra("note") as Note
                    val noteId = database.push().key
                    if (noteId != null) {
                        newNote.id = noteId
                        database.child(noteId).setValue(newNote)
                    }
                }
                REQUEST_CODE_EDIT -> {
                    val editedNote = data?.getSerializableExtra("note") as Note
                    editedNote.id?.let { database.child(it).setValue(editedNote) }
                }
            }
        }
    }
    companion object {
        const val REQUEST_CODE_ADD = 1
        const val REQUEST_CODE_EDIT = 2
    }
}