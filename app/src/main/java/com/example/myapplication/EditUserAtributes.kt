package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.data.superclass.User
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EditUserAtributes : AppCompatActivity(), IntSharedFunctions {

    lateinit var emailEditText: TextInputEditText
    lateinit var updateEmailButton: MaterialButton
    lateinit var nicknameEditText: TextInputEditText
    lateinit var updateNicknameButton: MaterialButton
    lateinit var phoneNumberEditText: TextInputEditText
    lateinit var updatePhoneNumberButton: MaterialButton
    lateinit var goBackToAccountButton: MaterialButton
    lateinit var goToPasswordSettingsButton: MaterialButton
    lateinit var loggedUser: User
    lateinit var editUserAttributesConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val userId = intent.getLongExtra("USER_ID", 0L)

        loggedUser = UserRepository.getUserById(userId)!!
        editUserAttributesConstraintLayout = findViewById(R.id.EditUser_OuterConstraintLayout)

        emailEditText = findViewById(R.id.EditUser_EmailEditText)
        updateEmailButton = findViewById(R.id.EditUser_UpdateEmailButton)

        nicknameEditText = findViewById(R.id.EditUser_Nickname_EditText)
        updateNicknameButton = findViewById(R.id.EditUser_UpdateNicknameButton)

        phoneNumberEditText = findViewById(R.id.EditUser_PhoneNumber_EditText)
        updatePhoneNumberButton = findViewById(R.id.EditUser_UpdatePhoneNumberButton)

        goBackToAccountButton = findViewById(R.id.EditUser_BackwardsButton)
        goToPasswordSettingsButton = findViewById(R.id.EditUser_goToPasswordSettingsButton)

        updateEmailButton.setOnClickListener {
            val emailAsText = emailEditText.text.toString()
            if (!emailIsNotTaken(emailAsText) && emailIsValid(emailAsText)) {
                makeAndShowShortLengthSnackBar(
                    "El email ingresado ya está registrado",
                    editUserAttributesConstraintLayout
                )
                emailEditText.setText("")
            } else if (!emailIsValid(emailAsText)) {
                makeAndShowShortLengthSnackBar(
                    "Email inválido. Reintentar",
                    editUserAttributesConstraintLayout
                )
                emailEditText.setText("")
            } else {
                loggedUser.updateEmail(emailAsText)
                this.showSavedChangesSnackBar()
            }
        }

        updateNicknameButton.setOnClickListener {
            val nicknameAsText = nicknameEditText.text.toString()

            if (UserRepository.userNicknameIsNotTaken(nicknameAsText)) {
                loggedUser.updateNickname(nicknameAsText)
                this.showSavedChangesSnackBar()
            } else {
                makeAndShowShortLengthSnackBar(
                    "El nickname ingresado ya está registrado",
                    editUserAttributesConstraintLayout
                )
                nicknameEditText.setText("")
            }
        }

        updatePhoneNumberButton.setOnClickListener {
            val phoneNumberAsText = phoneNumberEditText.text.toString()
            if (UserRepository.phoneNumberIsNotTaken(phoneNumberAsText)) {
                loggedUser.updatePhoneNumber(phoneNumberAsText)
                this.showSavedChangesSnackBar()
            } else {
                makeAndShowShortLengthSnackBar(
                    "El teléfono ingresado ya está registrado",
                    editUserAttributesConstraintLayout
                )
                phoneNumberEditText.setText("")
            }
        }

        goBackToAccountButton.setOnClickListener {
            val intentSentBackToAccount = Intent(this, Account::class.java)
            intentSentBackToAccount.putExtra("USER_ID", userId)
            startActivity(intentSentBackToAccount)
            finish()
        }

        goToPasswordSettingsButton.setOnClickListener {
            val intentSentToPasswordSettings = Intent(this, EditPassword::class.java)
            intentSentToPasswordSettings.putExtra("USER_ID", userId)
            startActivity(intentSentToPasswordSettings)
            finish()
        }
    }

    private fun showSavedChangesSnackBar() {
        makeAndShowShortLengthSnackBar("Cambios guardados.", editUserAttributesConstraintLayout)
    }
}