package com.example.myapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ticketNumberPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketListItem_NumberPlaceHolder)
    val artistPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketListItem_ArtistNamePlaceHolder)
    val datePlaceHolder: MaterialTextView = view.findViewById(R.id.TicketListItem_DatePlaceHolder)
    val timePlaceHolder: MaterialTextView = view.findViewById(R.id.TicketListItem_TimePlaceHolder)
    val seatQuantityPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketListItem_SeatsPlaceHolder)
    val pricePerSeatPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketListItem_PricePerSeatPlaceHolder)
    val paymentMethodPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketListItem_PaymentMethodPlaceHolder)
    val feePlaceHolder: MaterialTextView = view.findViewById(R.id.TicketListItem_FeePlaceHolder)
    val subTotalPlaceHolder : MaterialTextView = view.findViewById(R.id.TicketListItem_SubtotalPlaceHolder)
    val ticketTotalPlaceHolder: MaterialTextView =
        view.findViewById(R.id.TicketListItem_TicketTotalPlaceHolder)
}
