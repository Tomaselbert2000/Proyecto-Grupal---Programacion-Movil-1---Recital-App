package com.example.myapplication.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val artistName: MaterialTextView = view.findViewById(R.id.EventList_Item_ArtistName)
    val goToByTicketsActivityButton: MaterialButton =
        view.findViewById(R.id.EventList_Item_goToBuyTicketsButton)
    val artistPhoto: ShapeableImageView = view.findViewById(R.id.Event_ImagePlaceHolder)
}