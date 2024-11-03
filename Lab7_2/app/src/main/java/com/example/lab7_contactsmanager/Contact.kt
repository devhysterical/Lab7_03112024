package com.example.lab7_contactsmanager

import java.io.Serializable

data class Contact(
    var id: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var address: String = ""
) : Serializable