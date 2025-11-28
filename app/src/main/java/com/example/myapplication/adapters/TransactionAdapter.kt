package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.superclass.Transaction
import com.example.myapplication.viewHolders.TransactionViewHolder

class TransactionAdapter(val transactionList: MutableList<Transaction>) :
    RecyclerView.Adapter<TransactionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val itemInflated = LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_recycler_view_item_layout, parent, false)
        return TransactionViewHolder(itemInflated)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {
        holder.transactionIdPlaceHolder.text = transactionList[position].transactionId.toString()
        holder.transactionAmountPlaceHolder.text = transactionList[position].amount.toString()
        holder.transactionDateTimePlaceHolder.text = transactionList[position].dateAndTime.toString()
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}