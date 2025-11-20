package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data.superclass.Ticket
import repositories.TicketCollectionRepository
import repositories.TicketsRepository

const val USER_ID = "param1"

class TicketsHistoryFragment : Fragment() {
    private var param1: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tickets_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ticketListRecyclerView =
            view.findViewById<RecyclerView>(R.id.fragment_ticket_history_recycler_view)
        ticketListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val ticketsIdList = TicketCollectionRepository.buscarComprasPorId(param1)
        ticketListRecyclerView.adapter =
            TicketAdapter(TicketsRepository.obtenerListaDeTickets())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TicketsHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, param1)
                }
            }
    }
}