package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.data.superclass.User
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import kotlin.random.Random


class SignUpNicknamePassword : AppCompatActivity(), IntSharedFunctions {

    lateinit var newEmailAddress: TextInputEditText
    lateinit var newNickname: TextInputEditText
    lateinit var newPassword: TextInputEditText
    lateinit var confirmPassword: TextInputEditText

    lateinit var createNewUserButton: MaterialButton
    lateinit var backwardArrowButton: MaterialButton
    lateinit var signUpNicknamePasswordConstraintLayout: ConstraintLayout

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
        signUpNicknamePasswordConstraintLayout =
            findViewById(R.id.SignUp_NicknamePassword_ConstraintLayout)

        createNewUserButton.setOnClickListener {
            val emailAddressText = newEmailAddress.text.toString()
            val nicknameText = newNickname.text.toString()
            val newPasswordText = newPassword.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            if (this.theUserIdReceivedIsNotNull(userId)
            ) {
                if (this.allOfTheseAttributesAreValid(
                        userId ?: -1L,
                        emailAddressText,
                        nicknameText
                    )
                ) {
                    if (passwordsMatch(newPasswordText, confirmPasswordText) && !passwordIsStrong(
                            newPasswordText
                        )
                    ) {
                        makeAndShowShortLengthSnackBar(
                            "Contraseña débil. Reintentar",
                            signUpNicknamePasswordConstraintLayout
                        )
                        newPassword.setText("")
                        confirmPassword.setText("")
                    } else if (!passwordsMatch(newPasswordText, confirmPasswordText)) {
                        makeAndShowShortLengthSnackBar(
                            "Las contraseñas no coinciden",
                            signUpNicknamePasswordConstraintLayout
                        )
                        newPassword.setText("")
                        confirmPassword.setText("")
                    } else {
                        val creationDate = LocalDateTime.now().toString()
                        val newUser = User(
                            name.toString(),
                            surname.toString(),
                            userId!!,
                            address.toString(),
                            phoneNumber.toString(),
                            nicknameText,
                            newPasswordText,
                            emailAddressText,
                            creationDate,
                            generateRandomClientNumber()
                        )
                        UserRepository.registerNewUser(newUser)
                        val intentSentToActivityMain = Intent(this, Main::class.java)
                        intentSentToActivityMain.putExtra("USER_ID", newUser.personalID)
                        startActivity(intentSentToActivityMain)
                        finish()
                    }
                } else if (!emailIsNotTaken(emailAddressText)) {
                    makeAndShowShortLengthSnackBar(
                        "El email ingresado ya ha sido registrado.",
                        signUpNicknamePasswordConstraintLayout
                    )
                    newEmailAddress.setText("")
                } else if (!emailIsValid(emailAddressText)) {
                    makeAndShowShortLengthSnackBar(
                        "Email inválido. Reintentar.",
                        signUpNicknamePasswordConstraintLayout
                    )
                    newEmailAddress.setText("")
                }
            }
        }

        backwardArrowButton.setOnClickListener {
            this.returnToSignUpUserContactDataActivity()
        }
    }

    private fun generateRandomClientNumber(): Long {

        var clientNumber: Long
        do {
            clientNumber = Random.nextLong(1, 100000)
            if (clientNumber !in UserRepository.getListOfTakenClientNumbers()) {
                return clientNumber
            }
        } while (clientNumber in UserRepository.getListOfTakenClientNumbers())
        return 0L
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
        nicknameAsText: String
    ): Boolean {
        return this.userIdIsValid(userIdAsLong) && emailIsValid(emailAddressAsText) && emailIsNotTaken(
            emailAddressAsText
        ) && nicknameIsValid(nicknameAsText)
    }

    fun userIdIsValid(userIdToValidate: Long): Boolean {
        return !UserRepository.userIdIsDuplicated(userIdToValidate) && UserRepository.userIdGreaterThanZero(
            userIdToValidate
        )
    }
}