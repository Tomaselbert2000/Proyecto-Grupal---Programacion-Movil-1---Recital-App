package com.example.myapplication

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.superclass.Ticket
import com.example.myapplication.repositories.EventRepository
import com.example.myapplication.repositories.PaymentMethodRepository
import java.time.LocalDateTime.now

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: TicketViewHolder,
        position: Int
    ) {
        val event = EventRepository.getEventById(ticketList[position].eventId)
        holder.ticketNumberPlaceHolder.text = ticketList[position].id.toString()
        holder.artistPlaceHolder.text = event?.artist
        holder.datePlaceHolder.text = event?.date
        holder.timePlaceHolder.text = event?.time
        holder.seatQuantityPlaceHolder.text = ticketList[position].quantity.toString()
        holder.pricePerSeatPlaceHolder.text = ticketList[position].price.toString()
        val ticketSubtotalWithOutFees = ticketList[position].calculateTicketSubtotal()
        holder.subTotalPlaceHolder.text = ticketSubtotalWithOutFees.toString()
        val ticketFeeBasedOnPaymentMethodUsed = this.calculateFee(
            ticketList[position].calculateTicketSubtotal(), ticketList[position].idMedioDePagoUsado
        )
        val ticketTotalIncludingFee = ticketSubtotalWithOutFees + ticketFeeBasedOnPaymentMethodUsed
        holder.paymentMethodPlaceHolder.text =
            PaymentMethodRepository.searchPaymentMethodById(ticketList[position].idMedioDePagoUsado)?.name
        holder.feePlaceHolder.text = ticketFeeBasedOnPaymentMethodUsed.toString()
        holder.ticketTotalPlaceHolder.text = ticketTotalIncludingFee.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateFee(
        ticketSubtotal: Double,
        paymentMethodId: Long
    ): Double {
        val paymentMethodUsed = PaymentMethodRepository.searchPaymentMethodById(paymentMethodId)
        if (paymentMethodUsed != null) {
            val fee = paymentMethodUsed.calculateFee(ticketSubtotal, now())
            return fee
        }
        return 0.0
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }
}