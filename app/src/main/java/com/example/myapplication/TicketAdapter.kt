package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import data.superclass.Ticket
import repositories.EventRepository

class TicketAdapter(val ticketList: MutableList<Ticket>) :
    RecyclerView.Adapter<TicketViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketViewHolder {
        val itemViewInflated = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_list_recycler_view_item_layout, parent, false)
        return TicketViewHolder(itemViewInflated)
    }

    override fun onBindViewHolder(
        holder: TicketViewHolder,
        position: Int
    ) {
        val event = EventRepository.obtenerEventoPorId(ticketList[position].eventId)
        holder.ticketNumberPlaceHolder.text = ticketList[position].id.toString()
        holder.artistPlaceHolder.text = event?.artist
        holder.datePlaceHolder.text = event?.date
        holder.timePlaceHolder.text = event?.time
        holder.seatQuantityPlaceHolder.text = ticketList[position].quantity.toString()
        holder.pricePerSeatPlaceHolder.text = ticketList[position].precio.toString()
        holder.ticketTotal.text = ticketList[position].calcularTotalPorTicket().toString()
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }
}