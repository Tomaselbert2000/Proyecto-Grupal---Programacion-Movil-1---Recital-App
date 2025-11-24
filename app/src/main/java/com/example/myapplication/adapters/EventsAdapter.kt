package com.example.myapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.superclass.Event
import com.example.myapplication.interfaces.IntEventFragmentFunctions
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.viewHolders.EventViewHolder

class EventsAdapter(val eventList: MutableList<Event>, val listener : IntEventFragmentFunctions) : RecyclerView.Adapter<EventViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val itemViewInflated = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_list_recycler_view_item_layout, parent, false)
        return EventViewHolder(itemViewInflated)
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {
        val event = eventList[position]

        holder.artistName.text = event.artist

        Glide.with(holder.artistPhoto.context).load(event.image).centerCrop()
            .into(holder.artistPhoto)

        val buyTicketsButton = holder.goToByTicketsActivityButton

        buyTicketsButton.setOnClickListener {
            listener.onEventClickListener(eventList[position])
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}