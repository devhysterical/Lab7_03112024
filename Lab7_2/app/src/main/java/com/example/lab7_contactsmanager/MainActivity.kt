package com.example.lab7_contactsmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab7_contactsmanager.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private val contactsList = mutableListOf<Contact>()
    private val database = FirebaseDatabase.getInstance().reference.child("contacts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        binding.btnAddContact.setOnClickListener {
            val intent = Intent(this, EditContactActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD)
        }

        fetchContacts()
    }

    private fun setupRecyclerView() {
        contactsAdapter = ContactsAdapter(this, contactsList) { contact ->
            val intent = Intent(this, EditContactActivity::class.java).apply {
                putExtra("contact", contact) // Chuyển Contact qua Intent
            }
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }
        binding.rvContacts.adapter = contactsAdapter
        binding.rvContacts.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchContacts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactsList.clear()
                snapshot.children.mapNotNullTo(contactsList) {
                    it.getValue(Contact::class.java)?.apply { id = it.key!! }
                }
                contactsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error fetching contacts", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD) {
                val newContact = data?.getSerializableExtra("contact") as? Contact
                newContact?.let { database.push().setValue(it) }
            } else if (requestCode == REQUEST_CODE_EDIT) {
                val editedContact = data?.getSerializableExtra("contact") as? Contact
                editedContact?.let { database.child(it.id).setValue(it) }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_ADD = 1
        const val REQUEST_CODE_EDIT = 2
    }
}