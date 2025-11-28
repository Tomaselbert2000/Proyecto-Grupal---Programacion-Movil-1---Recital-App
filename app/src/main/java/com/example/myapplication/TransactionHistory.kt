package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.NoMovementsFragment
import com.example.myapplication.fragments.UserTransactionsList
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.TransactionRepository
import com.google.android.material.button.MaterialButton

class TransactionHistory : AppCompatActivity(), IntSharedFunctions {

    lateinit var goBackToUserFundsFragmentButton: MaterialButton
    lateinit var noTransactionsFoundFragment: Fragment
    lateinit var transactionListFragment: UserTransactionsList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)

        val userID = intent.getLongExtra("USER_ID", 0L)

        val transactionsForThisUserId = TransactionRepository.getTransactionsListByUserId(userID)

        transactionListFragment = UserTransactionsList.newInstance(userID)
        noTransactionsFoundFragment = NoMovementsFragment.newInstance("1", "2")

        val listOfFragments =
            mutableListOf(transactionListFragment, noTransactionsFoundFragment)

        goBackToUserFundsFragmentButton = findViewById(R.id.TransactionHistory_BackwardsIconButton)

        addFragmentsFromList(
            listOfFragments,
            R.id.TransactionHistory_FragmentContainerView,
            this.supportFragmentManager
        )

        if (transactionsForThisUserId.isEmpty()) {
            switchFragment(
                noTransactionsFoundFragment,
                listOfFragments,
                this.supportFragmentManager
            )
        } else {
            switchFragment(transactionListFragment, listOfFragments, this.supportFragmentManager)
        }

        goBackToUserFundsFragmentButton.setOnClickListener {
            finish()
        }
    }

}