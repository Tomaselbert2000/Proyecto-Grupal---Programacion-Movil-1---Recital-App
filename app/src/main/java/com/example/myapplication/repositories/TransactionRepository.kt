package com.example.myapplication.repositories

import com.example.myapplication.data.superclass.Transaction

object TransactionRepository {

    private val transactions = mutableListOf<Transaction>()

    init {
        transactions.add(Transaction(1, 122500.0, 1504L))
        transactions.add(Transaction(2, 22300.0, 1504L))

        transactions.add(Transaction(3, 76500.0, 1510L))
        transactions.add(Transaction(4, 33500.0, 1510L))

        transactions.add(Transaction(5, 2500.0, 2802L))
        transactions.add(Transaction(6, 17600.0, 2802L))
    }

    fun registerTransaction(newTransaction: Transaction): Boolean {
        return this.transactions.add(newTransaction)
    }

    fun getTransactionsListByUserId(userId: Long?): MutableList<Transaction> {
        val transactionsList = mutableListOf<Transaction>()
        for(trns in this.transactions){
            if(trns.userId == userId){
                transactionsList.add(trns)
            }
        }
        return transactionsList
    }

    fun getTransactionById(transactionIdToSearch : Long): Transaction? {
        for(trns in this.transactions){
            if(trns.transactionId == transactionIdToSearch){
                return trns
            }
        }
        return null
    }
}