package com.example.myapplication.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.TicketAdapter
import com.example.myapplication.data.superclass.Ticket
import com.example.myapplication.repositories.TicketsRepository
import com.google.android.material.textview.MaterialTextView
import com.example.myapplication.repositories.TicketCollectionRepository

class TicketsHistoryFragment : Fragment() {
    private var userId: Long? = null

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
        return inflater.inflate(R.layout.fragment_tickets_history, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val ticketListRecyclerView = view.findViewById<RecyclerView>(R.id.fragment_ticket_history_recycler_view)

        ticketListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val thisListContaintsTheIDsOfTicketsAsLongValues = TicketCollectionRepository.getIDsOfTicketsBoughtByTheUser(userId)

        val listOfTicketWithTheUserId = mutableListOf<Ticket>()

        for (ticketId in thisListContaintsTheIDsOfTicketsAsLongValues) {
            val ticketToAdd = TicketsRepository.getTicketById(ticketId)
            if (ticketToAdd != null) {
                listOfTicketWithTheUserId.add(ticketToAdd)
            }
        }
        ticketListRecyclerView.adapter = TicketAdapter(listOfTicketWithTheUserId)

        val totalAcumulatedByAllTheTicketsPlaceHolder : MaterialTextView = view.findViewById(R.id.TicketHistory_TotalAcumulatedPlaceHolder)

        val total = TicketsRepository.calculateTotalByList(listOfTicketWithTheUserId)

        totalAcumulatedByAllTheTicketsPlaceHolder.text = total.toString()
    }

    companion object {

        const val USER_ID = "userId"

        @JvmStatic
        fun newInstance(id: Long?) =
            TicketsHistoryFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, id ?: 0L)
                }
            }
    }
}