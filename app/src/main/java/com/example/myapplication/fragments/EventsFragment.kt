package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.EventsAdapter
import com.example.myapplication.R
import com.example.myapplication.repositories.EventRepository

class EventsFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventListRecyvlerView =
            view.findViewById<RecyclerView>(R.id.fragments_events_recyclerView)
        eventListRecyvlerView.layoutManager = LinearLayoutManager(requireContext())
        eventListRecyvlerView.adapter = EventsAdapter(EventRepository.obtenerListaDeEventos())
    }

    companion object {

        const val USER_ID = "userId"

        @JvmStatic
        fun newInstance(id: Long?) =
            EventsFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, id ?: 0L)
                }
            }
    }
}