package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.repositories.UserRepository

class SettingsFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loggedUser = UserRepository.getUserById(userId)
    }

    companion object {

        const val USER_ID = "userId"

        @JvmStatic
        fun newInstance(id: Long?) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, id ?: 0L)
                }
            }
    }
}