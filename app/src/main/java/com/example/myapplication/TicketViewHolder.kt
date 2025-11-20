package com.example.myapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ticketNumberPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketHistory_NumberPlaceHolder)
    val artistPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketHistory_ArtistNamePlaceHolder)
    val datePlaceHolder: MaterialTextView = view.findViewById(R.id.TicketHistory_DatePlaceHolder)
    val timePlaceHolder: MaterialTextView = view.findViewById(R.id.TicketHistory_TimePlaceHolder)
    val seatQuantityPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketHistory_SeatsPlaceHolder)
    val pricePerSeatPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketHistory_PricePerSeatPlaceHolder)
    val ticketTotal: MaterialTextView = view.findViewById(R.id.TicketHistory_TicketTotalPlaceHolder)
}
