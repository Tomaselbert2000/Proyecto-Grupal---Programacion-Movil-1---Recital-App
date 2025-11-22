package com.example.myapplication.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.textview.MaterialTextView

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val transactionIdPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TransactionListItem_TransactionIdPlaceHolder)
    val transactionAmountPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TransactionListItem_AmountPlaceHolder)
    val transactionDateTimePlaceHolder: MaterialTextView =
        view.findViewById(R.id.TransactionListItem_DateTimePlaceHolder)
}