package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.superclass.User
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class Account : AppCompatActivity() {

    lateinit var goBackToSettingsFragment: MaterialButton
    lateinit var editUserButton: MaterialButton
    lateinit var loggedUser: User
    lateinit var namePlaceHolder: MaterialTextView
    lateinit var surnamePlaceHolder: MaterialTextView
    lateinit var userPersonalIdPlaceHolder: MaterialTextView
    lateinit var addressPlaceHolder: MaterialTextView
    lateinit var phoneNumberPlaceHolder: MaterialTextView
    lateinit var clientNumberPlaceHolder: MaterialTextView
    lateinit var currentNicknamePlaceHolder: MaterialTextView
    lateinit var currentEmailPlaceHolder: MaterialTextView
    lateinit var creationDatePlaceHolder: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val userId = intent.getLongExtra("USER_ID", 0L)

        goBackToSettingsFragment = findViewById(R.id.Account_BackwardsButton)
        editUserButton = findViewById(R.id.Account_EditAttributesButton)
        loggedUser = UserRepository.getUserById(userId)!!

        namePlaceHolder = findViewById(R.id.Account_NamePlaceHolder)
        surnamePlaceHolder = findViewById(R.id.Account_SurnamePlaceHolder)
        userPersonalIdPlaceHolder = findViewById(R.id.Account_UserIDPlaceHolder)
        addressPlaceHolder = findViewById(R.id.Account_AddressPlaceHolder)
        phoneNumberPlaceHolder = findViewById(R.id.Account_PhoneNumberPlaceHolder)
        clientNumberPlaceHolder = findViewById(R.id.Account_ClientNumberPlaceHolder)
        currentNicknamePlaceHolder = findViewById(R.id.Account_CurrentNicknamePlaceHolder)
        currentEmailPlaceHolder = findViewById(R.id.Account_EmailPlaceHolder)
        creationDatePlaceHolder = findViewById(R.id.Account_CreationDate_PlaceHolder)

        namePlaceHolder.text = loggedUser.name
        surnamePlaceHolder.text = loggedUser.surname
        userPersonalIdPlaceHolder.text = loggedUser.personalID.toString()
        addressPlaceHolder.text = loggedUser.address
        phoneNumberPlaceHolder.text = loggedUser.phoneNumber
        clientNumberPlaceHolder.text = loggedUser.clientNumberAssigned.toString()
        currentNicknamePlaceHolder.text = loggedUser.nickname
        currentEmailPlaceHolder.text = loggedUser.email
        creationDatePlaceHolder.text = loggedUser.createdDate

        goBackToSettingsFragment.setOnClickListener {
            finish()
        }

        editUserButton.setOnClickListener {
            val intentSentToEditUserScreen = Intent(this, EditUserAtributes::class.java)
            intentSentToEditUserScreen.putExtra("USER_ID", userId)
            startActivity(intentSentToEditUserScreen)
            finish()
        }
    }
}