package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R.layout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SignUpUserContactData : AppCompatActivity() {

    lateinit var name: TextInputEditText
    lateinit var surname: TextInputEditText
    lateinit var id: TextInputEditText
    lateinit var address: TextInputEditText
    lateinit var phoneNumber: TextInputEditText
    lateinit var continueButton: MaterialButton
    lateinit var backwardsButton: MaterialButton
    lateinit var mainConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_signup_user_contact_data)

        name = findViewById(R.id.SignUp_UserContactData_Name_EditText)
        surname = findViewById(R.id.SignUp_UserContactData_Surname_EditText)
        id = findViewById(R.id.SignUp_UserContactData_UserID_EditText)
        address = findViewById(R.id.SignUp_UserContactData_Address_EditText)
        phoneNumber = findViewById(R.id.SignUp_UserContactData_Phone_Number_EditText)
        continueButton = findViewById(R.id.SignUp_UserContactData_ContinueButton)
        backwardsButton = findViewById(R.id.SignUp_UserContactData_BackwardsButton)
        mainConstraintLayout = findViewById(R.id.SignUp_UserContactData_ConstraintLayout)

        backwardsButton.setOnClickListener {
            this.returnToLoginActivity()
        }

        continueButton.setOnClickListener {
            if (this.nothingIsBlank(
                    name,
                    surname,
                    id,
                    address,
                    phoneNumber
                ) && this.containsOnlyLetters(name) && this.containsOnlyLetters(surname) && this.phoneNumberIsValid(
                    phoneNumber
                )
            ) {
                this.continueToSignUpNicknamePasswordActivity()
            } else if(!this.nothingIsBlank(name, surname, id, address, phoneNumber)){
                this.showBlankInputFieldsSnackBar()
            }else if(!this.containsOnlyLetters(name) || !this.containsOnlyLetters(surname)){
                this.showBlankInputFieldsSnackBar()
            }
        }

    }

    private fun nothingIsBlank(
        name: TextInputEditText,
        surname: TextInputEditText,
        id: TextInputEditText,
        address: TextInputEditText,
        phoneNumber: TextInputEditText
    ): Boolean {
        return !name.text.toString().isBlank() && !surname.text.toString()
            .isBlank() && !id.text.toString().isBlank() && !address.text.toString()
            .isBlank() && !phoneNumber.text.toString().isBlank()
    }

    private fun containsOnlyLetters(inputString: TextInputEditText): Boolean {
        return inputString.text?.all { it.isLetter() } ?: false
    }

    private fun phoneNumberIsValid(phoneNumber: TextInputEditText): Boolean {
        return phoneNumber.text?.all { it.isDigit() } ?: false
    }

    private fun continueToSignUpNicknamePasswordActivity() {
        val intent = Intent(this, SignUpNicknamePassword::class.java)
        startActivity(intent)
        finish()
    }

    private fun returnToLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun showBlankInputFieldsSnackBar() {
        Snackbar.make(mainConstraintLayout, "No se admiten campos en blanco", Snackbar.LENGTH_SHORT)
            .show()
    }
}