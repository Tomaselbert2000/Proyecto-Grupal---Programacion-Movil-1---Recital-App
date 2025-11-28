package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddFunds : AppCompatActivity(), IntSharedFunctions {

    lateinit var goBackToUserFundsFragment: MaterialButton
    lateinit var confirmOperationButton: MaterialButton
    lateinit var amountToAddInputEditText: TextInputEditText
    lateinit var addFundsConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_funds)

        val userId = intent.getLongExtra("USER_ID", 0L)
        goBackToUserFundsFragment = findViewById(R.id.AddFunds_BackwardsButton)
        confirmOperationButton = findViewById(R.id.AddFunds_ConfirmButton)
        amountToAddInputEditText = findViewById(R.id.AddFunds_EditText)
        addFundsConstraintLayout = findViewById(R.id.AddFunds_ConstraintLayout)

        goBackToUserFundsFragment.setOnClickListener {
            finish()
        }

        confirmOperationButton.setOnClickListener {

            val amountToAddAsDouble = amountToAddInputEditText.text.toString().toDouble()

            if (this.amountToAddIsEqualOrGreaterThan1000(amountToAddAsDouble) && this.amountToAddIsLesserOrEqualThan1000000(
                    amountToAddAsDouble
                )
            ) {
                UserRepository.addFundsToUser(userId, amountToAddAsDouble)
                finish()
            } else if (!this.amountToAddIsEqualOrGreaterThan1000(amountToAddAsDouble)) {
                makeAndShowShortLengthSnackBar(
                    "El valor mínimo de carga es $1000.0",
                    addFundsConstraintLayout
                )
            } else if (!this.amountToAddIsLesserOrEqualThan1000000(amountToAddAsDouble)) {
                makeAndShowShortLengthSnackBar(
                    "El valor máximo de carga es $1000000.0",
                    addFundsConstraintLayout
                )
            }
        }
    }

    private fun amountToAddIsLesserOrEqualThan1000000(amountToAddAsDouble: Double): Boolean {
        return amountToAddAsDouble <= 1000000.0
    }

    private fun amountToAddIsEqualOrGreaterThan1000(amountToAddAsDouble: Double): Boolean {
        return amountToAddAsDouble >= 1000.0
    }
}