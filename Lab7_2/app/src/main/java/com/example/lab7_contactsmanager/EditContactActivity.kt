package com.example.lab7_contactsmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.lab7_contactsmanager.databinding.ActivityEditContactBinding


class EditContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditContactBinding
    private var contact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contact = intent.getSerializableExtra("contact") as? Contact

        binding.etName.setText(contact?.name)
        binding.etPhoneNumber.setText(contact?.phoneNumber)
        binding.etEmail.setText(contact?.email)
        binding.etAddress.setText(contact?.address)

        binding.btnSave.setOnClickListener {
            saveContact()
        }

        binding.btnDelete.setOnClickListener {
            deleteContact()
        }
    }

    private fun saveContact() {
        val name = binding.etName.text.toString()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val email = binding.etEmail.text.toString()
        val address = binding.etAddress.text.toString()

        val newContact = Contact(
            id = contact?.id ?: "",
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            address = address
        )

        val resultIntent = Intent().apply {
            putExtra("contact", newContact)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun deleteContact() {
        val resultIntent = Intent().apply {
            putExtra("contact", contact)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}