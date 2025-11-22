package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class PaymentMethodInfo : AppCompatActivity() {

    lateinit var goBackToUserFundsFragment: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method_info)

        goBackToUserFundsFragment = findViewById(R.id.PaymentMethodsInfo_BackwardsIconButton)

        goBackToUserFundsFragment.setOnClickListener {
            finish()
        }
    }
}