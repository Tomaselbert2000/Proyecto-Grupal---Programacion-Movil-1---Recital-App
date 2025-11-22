package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.TransactionAdapter
import com.example.myapplication.repositories.TransactionRepository

class UserTransactionsList : Fragment() {
    private var userId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getLong("USER_ID", 0L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_transactions_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transactionRecyclerView : RecyclerView = view.findViewById(R.id.UserTransactionsListFragment_RecyclerView)
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionRecyclerView.adapter = TransactionAdapter(TransactionRepository.getTransactionsListByUserId(userId))
    }

    companion object {
        @JvmStatic
        fun newInstance(userId : Long) =
            UserTransactionsList().apply {
                arguments = Bundle().apply {
                    putLong("USER_ID", userId)
                }
            }
    }
}