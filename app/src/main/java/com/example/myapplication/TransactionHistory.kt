package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.NoMovementsFragment
import com.example.myapplication.fragments.UserTransactionsList
import com.example.myapplication.repositories.TransactionRepository
import com.google.android.material.button.MaterialButton

class TransactionHistory : AppCompatActivity() {

    lateinit var goBackToUserFundsFragment: MaterialButton
    lateinit var noTransactionsFoundFragment: Fragment
    lateinit var transactionListFragment: UserTransactionsList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)

        val userID = intent.getLongExtra("USER_ID", 0L)

        val transactionsForThisUserId = TransactionRepository.getTransactionsListByUserId(userID)

        transactionListFragment = UserTransactionsList.newInstance(userID)
        noTransactionsFoundFragment = NoMovementsFragment.newInstance("1", "2")

        goBackToUserFundsFragment = findViewById(R.id.TransactionHistory_BackwardsIconButton)

        if (transactionsForThisUserId.isEmpty()) {
            this.loadFragmentOnContainer(noTransactionsFoundFragment)
        } else {
            this.loadFragmentOnContainer(transactionListFragment)
        }

        goBackToUserFundsFragment.setOnClickListener {
            finish()
        }
    }

    private fun loadFragmentOnContainer(fragmentToLoad: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.TransactionHistory_FragmentContainerView, fragmentToLoad).show(fragmentToLoad)
            .commit()
    }
}