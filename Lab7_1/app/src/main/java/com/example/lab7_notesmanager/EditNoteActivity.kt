package com.example.lab7_notesmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab7_notesmanager.databinding.ActivityEditNoteBinding

class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        note = intent.getSerializableExtra("note") as Note?

        note?.let {
            binding.etTitle.setText(it.title)
            binding.etContent.setText(it.content)
        }

        binding.btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val newNote = note ?: Note()
            newNote.title = title
            newNote.content = content

            val resultIntent = Intent().apply {
                putExtra("note", newNote)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {

        }
    }
}