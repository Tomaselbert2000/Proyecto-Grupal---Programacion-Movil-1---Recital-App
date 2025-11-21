package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class UserFundsFragment : Fragment() {
    private var userId: Long = 0L

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
        val loggedUser = UserRepository.getUserById(userId)
        val currentFunds: MaterialTextView = view.findViewById(R.id.UserMoney_CurrentMoneyTextview)
        val toggleFundsVisibilityButton = view.findViewById<MaterialButton>(R.id.VisibilityToggle)
        currentFunds.text = "$" + loggedUser?.money.toString()
        var fundsAreVisibleOnScreen = true

        toggleFundsVisibilityButton.setOnClickListener {
            fundsAreVisibleOnScreen = !fundsAreVisibleOnScreen
            if (fundsAreVisibleOnScreen) {
                currentFunds.text = "$" + loggedUser?.money.toString()
                toggleFundsVisibilityButton.setIconResource(R.drawable.visibility_24dp)
            } else {
                currentFunds.text = "$ *******"
                toggleFundsVisibilityButton.setIconResource(R.drawable.visibility_off_24dp)
            }
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