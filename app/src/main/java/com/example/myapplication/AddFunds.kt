package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddFunds : AppCompatActivity() {

    lateinit var goBackToUserFundsFragment : MaterialButton
    lateinit var confirmOperationButton : MaterialButton
    lateinit var amountToAddInputEditText : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_funds)

        goBackToUserFundsFragment = findViewById(R.id.AddFunds_BackwardsButton)
        confirmOperationButton = findViewById(R.id.AddFunds_ConfirmButton)
        amountToAddInputEditText = findViewById(R.id.AddFunds_EditText)

        goBackToUserFundsFragment.setOnClickListener {
            finish()
        }
    }
}