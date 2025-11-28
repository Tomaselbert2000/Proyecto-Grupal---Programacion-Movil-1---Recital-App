package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.Account
import com.example.myapplication.R
import com.example.myapplication.data.superclass.User
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton

class SettingsFragment : Fragment() {
    private var userId: Long? = null

    lateinit var goToThemesSettingsButton: MaterialButton
    lateinit var goToEditUserSettingsButton: MaterialButton
    lateinit var goToAppInfoButton: MaterialButton
    lateinit var loggedUser: User

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

        loggedUser = UserRepository.getUserById(userId)!!
        goToThemesSettingsButton = view.findViewById(R.id.SettingsList_goToThemesActivity)
        goToEditUserSettingsButton = view.findViewById(R.id.SettingsList_goToEditUserActivity)

        goToEditUserSettingsButton.setOnClickListener {
            val intentSentToAccount = Intent(this.context, Account::class.java)
            intentSentToAccount.putExtra("USER_ID", loggedUser.personalID)
            startActivity(intentSentToAccount)
        }
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