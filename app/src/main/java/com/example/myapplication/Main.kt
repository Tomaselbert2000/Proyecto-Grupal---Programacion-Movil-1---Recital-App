package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.EventsFragment
import com.example.myapplication.fragments.SettingsFragment
import com.example.myapplication.fragments.TicketsHistoryFragment
import com.example.myapplication.fragments.UserFundsFragment
import com.example.myapplication.interfaces.SharedFunctions
import com.google.android.material.bottomnavigation.BottomNavigationView

class Main : AppCompatActivity(), SharedFunctions {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var eventFragment: EventsFragment
    lateinit var userFundsFragment: UserFundsFragment
    lateinit var ticketHistoryFragment: TicketsHistoryFragment
    lateinit var settingsFragment: SettingsFragment
    lateinit var fragmentToShowAsDefault: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.BottomNavigationView)

        val userId = intent.getLongExtra("USER_ID", 0) // el intent contiene el userID del usuario logueado en la aplicacion

        eventFragment = EventsFragment.newInstance(userId)

        userFundsFragment = UserFundsFragment.newInstance(userId)

        ticketHistoryFragment = TicketsHistoryFragment.newInstance(userId)

        settingsFragment = SettingsFragment.newInstance(userId)

        fragmentToShowAsDefault = eventFragment

        val listOfFragmentsOfMainActivity = mutableListOf(eventFragment, userFundsFragment, ticketHistoryFragment, settingsFragment)

        addFragmentsFromList(listOfFragmentsOfMainActivity, R.id.Main_FragmentContainerView, this.supportFragmentManager)

        if(savedInstanceState == null){
            this.switchFragment(
                eventFragment,
                listOfFragmentsOfMainActivity,
                this.supportFragmentManager
            )
        }

        bottomNavigationView.setOnItemSelectedListener { item ->

            bottomNavigationView.clearFocus()

            when (item.itemId) {
                // aquellos fragmentos que gestionen datos de usuario como por ejemplo settings o el saldo, reciben el userID para consultar en el repositorio
                R.id.Main_home_icon -> {
                    this.switchFragment(
                        eventFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_settings_icon -> {
                    this.switchFragment(
                        settingsFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_money_icon -> {
                    this.switchFragment(
                        userFundsFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_history_icon -> {
                    this.switchFragment(ticketHistoryFragment, listOfFragmentsOfMainActivity, this.supportFragmentManager)
                    true
                }

                else -> false
            }
        }
    }
}
