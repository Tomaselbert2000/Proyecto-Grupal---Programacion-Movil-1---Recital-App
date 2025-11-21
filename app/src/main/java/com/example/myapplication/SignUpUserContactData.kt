package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R.layout
import com.example.myapplication.repositories.UserRepository
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
                ) && this.containsOnlyLetters(name) && this.containsOnlyLetters(surname) && this.phoneNumberContainsOnlyDigits(
                    phoneNumber
                ) && this.phoneNumberIsValid(phoneNumber)
            ) {
                this.continueToSignUpNicknamePasswordActivity(
                    name,
                    surname,
                    id,
                    address,
                    phoneNumber
                )
            } else if (!this.nothingIsBlank(name, surname, id, address, phoneNumber)) {
                this.showErrorMessageSnackBar("No se admiten campos en blanco")
            } else if (!this.containsOnlyLetters(name) || !this.containsOnlyLetters(surname)) {
                this.showErrorMessageSnackBar("Nombre o apellido inválidos")
            } else if (!this.phoneNumberIsValid(phoneNumber)) {
                this.showErrorMessageSnackBar("Número de teléfono ya registrado")
            }else if(!this.phoneNumberContainsOnlyDigits(phoneNumber)){
                this.showErrorMessageSnackBar("Número de teléfono inválido")
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
        return inputString.text?.all { it.isLetter() || it == ' '} ?: false
    }

    private fun phoneNumberContainsOnlyDigits(phoneNumber: TextInputEditText): Boolean {
        return phoneNumber.text?.all { it.isDigit() } ?: false
    }

    fun phoneNumberIsValid(phoneNumberToValidate: TextInputEditText): Boolean {
        return UserRepository.phoneNumberIsNotTaken(phoneNumberToValidate.text.toString())
    }

    private fun continueToSignUpNicknamePasswordActivity(
        name: TextInputEditText,
        surname: TextInputEditText,
        id: TextInputEditText,
        address: TextInputEditText,
        phoneNumber: TextInputEditText
    ) {
        val intent = Intent(this, SignUpNicknamePassword::class.java)
        intent.putExtra("NAME", name.text.toString())
        intent.putExtra("SURNAME", surname.text.toString())
        intent.putExtra("ID", id.text.toString())
        intent.putExtra("ADDRESS", address.text.toString())
        intent.putExtra("PHONE_NUMBER", phoneNumber.text.toString())
        startActivity(intent)
        finish()
    }

    private fun returnToLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun showErrorMessageSnackBar(message: String) {
        Snackbar.make(mainConstraintLayout, message, Snackbar.LENGTH_SHORT)
            .show()
    }
}