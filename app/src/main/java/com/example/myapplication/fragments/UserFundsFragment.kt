package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.PaymentMethodInfo
import com.example.myapplication.R
import com.example.myapplication.TransactionHistory
import com.example.myapplication.data.superclass.User
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class UserFundsFragment : Fragment() {
    private var userId: Long = 0L
    private var fundsVisibilityToggleStatusIsTrue = true
    lateinit var loggedUser: User
    lateinit var goToAvailablePaymentMethodsButton: MaterialButton
    lateinit var currentFundsPlaceHolder: MaterialTextView
    lateinit var toggleVisibilityIcon: MaterialButton
    lateinit var goToTransactionHistoryHistoryButton : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getLong(USER_ID, 0L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            fundsVisibilityToggleStatusIsTrue =
                savedInstanceState.getBoolean("FUNDS_TOGGLE_VISIBILITY_STATUS", true)
        }

        loggedUser = UserRepository.getUserById(userId)!!
        goToAvailablePaymentMethodsButton =
            view.findViewById(R.id.UserMoney_goToPaymentMethodsButton)
        currentFundsPlaceHolder = view.findViewById(R.id.UserMoney_CurrentMoneyTextview)
        toggleVisibilityIcon = view.findViewById(R.id.VisibilityToggle)
        goToTransactionHistoryHistoryButton = view.findViewById(R.id.UserMoney_FundsHistoryIconButton)

        toggleVisibilityIcon.setOnClickListener {
            fundsVisibilityToggleStatusIsTrue = !fundsVisibilityToggleStatusIsTrue
            this.updateUserFundsAndUI()
        }
        goToAvailablePaymentMethodsButton.setOnClickListener {
            val intentSentToPaymentMethodInfo = Intent(context, PaymentMethodInfo::class.java)
            startActivity(intentSentToPaymentMethodInfo)
        }

        goToTransactionHistoryHistoryButton.setOnClickListener {
            val intentSentToFundsMovementHistory = Intent(context, TransactionHistory::class.java)
            intentSentToFundsMovementHistory.putExtra("USER_ID", loggedUser.id)
            startActivity(intentSentToFundsMovementHistory)
        }

        this.updateUserFundsAndUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("FUNDS_TOGGLE_VISIBILITY_STATUS", fundsVisibilityToggleStatusIsTrue)
    }

    fun updateUserFundsAndUI() {
        if (fundsVisibilityToggleStatusIsTrue) {
            toggleVisibilityIcon.setIconResource(R.drawable.visibility_24dp)
            val userFundsAsString = "$" + loggedUser.money.toString()
            currentFundsPlaceHolder.text = userFundsAsString
        } else {
            toggleVisibilityIcon.setIconResource(R.drawable.visibility_off_24dp)
            val userFundsAsString = "$ *******"
            currentFundsPlaceHolder.text = userFundsAsString
        }
    }

    companion object {

        const val USER_ID = "user_id"

        @JvmStatic
        fun newInstance(id: Long?) =
            UserFundsFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, id ?: 0L)
                }
            }
    }
}