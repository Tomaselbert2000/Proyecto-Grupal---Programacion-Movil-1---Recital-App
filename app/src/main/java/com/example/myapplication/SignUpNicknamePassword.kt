package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.superclass.User
import com.example.myapplication.interfaces.SharedFunctions
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate.now


class SignUpNicknamePassword : AppCompatActivity(), SharedFunctions{

    lateinit var newEmailAddress: TextInputEditText
    lateinit var newNickname: TextInputEditText
    lateinit var newPassword: TextInputEditText
    lateinit var confirmPassword: TextInputEditText

    lateinit var createNewUserButton: MaterialButton
    lateinit var backwardArrowButton: MaterialButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_nickname_password)
        val name = intent.getStringExtra("NAME")
        val surname = intent.getStringExtra("SURNAME")
        val userId = intent.getStringExtra("ID")?.toLong()
        val address = intent.getStringExtra("ADDRESS")
        val phoneNumber = intent.getStringExtra("PHONE_NUMBER")

        newEmailAddress = findViewById(R.id.SignUp_NicknamePassword_EmailAddressEditText)
        newNickname = findViewById(R.id.SignUp_NicknamePassword_NewNickname_EditText)
        newPassword = findViewById(R.id.SignUp_NicknamePassword_Password_EditText)
        confirmPassword = findViewById(R.id.SignUp_NicknamePassword_ConfirmPassword_EditText)
        createNewUserButton = findViewById(R.id.SignUp_NicknamePassword_CreateAccount_Button)
        backwardArrowButton = findViewById(R.id.SignUp_NicknamePassword_BackwardsButton)

        createNewUserButton.setOnClickListener {
            val emailAddressText = newEmailAddress.text.toString()
            val nicknameText = newNickname.text.toString()
            val newPasswordText = newPassword.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            if (this.theUserIdReceivedIsNotNull(
                    userId
                )
            ) {
                if (this.allOfTheseAttributesAreValid(
                        userId ?: -1L,
                        emailAddressText,
                        nicknameText,
                        newPasswordText,
                        confirmPasswordText
                    )
                ) {
                    val creationDate = now().toString()
                    val newUser = User(
                        name.toString(),
                        surname.toString(),
                        userId!!,
                        address.toString(),
                        phoneNumber.toString(),
                        nicknameText,
                        newPasswordText,
                        emailAddressText,
                        creationDate
                    )
                    UserRepository.registerNewUser(newUser)

                    val intentSentToActivityMain = Intent(this, Main::class.java)
                    intentSentToActivityMain.putExtra("USER_ID", newUser.id)
                    startActivity(intentSentToActivityMain)
                    finish()
                }
            }
        }

        backwardArrowButton.setOnClickListener {
            this.returnToSignUpUserContactDataActivity()
        }
    }

    private fun returnToSignUpUserContactDataActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun theUserIdReceivedIsNotNull(
        userIdAsLong: Long?
    ): Boolean {
        return userIdAsLong != null
    }

    private fun allOfTheseAttributesAreValid(
        userIdAsLong: Long,
        emailAddressAsText: String,
        nicknameAsText: String,
        newPasswordAsText: String,
        confirmPasswordAsText: String
    ): Boolean {
        return this.userIdIsValid(userIdAsLong) && this.emailIsValid(emailAddressAsText) && this.emailIsNotTaken(
            emailAddressAsText
        ) && this.nicknameIsValid(nicknameAsText) && this.passwordsMatch(
            newPasswordAsText,
            confirmPasswordAsText
        )
    }


    fun userIdIsValid(userIdToValidate: Long): Boolean {
        return !UserRepository.userIdIsDuplicated(userIdToValidate) && UserRepository.userIdGreaterThanZero(
            userIdToValidate
        )
    }

    fun nicknameIsValid(nicknameToValidate: String): Boolean {
        return UserRepository.userNicknameIsNotTaken(nicknameToValidate)
    }

    fun emailIsNotTaken(emailToValidate: String): Boolean {
        return UserRepository.userEmailAddressIsNotTaken(emailToValidate)
    }

    fun emailIsValid(emailToValidate: String): Boolean {
        return emailToValidate.any { it == '@' }
    }

    fun passwordsMatch(passwordInputField: String, passwordConfirmationField: String): Boolean {
        return passwordInputField == passwordConfirmationField
    }

}