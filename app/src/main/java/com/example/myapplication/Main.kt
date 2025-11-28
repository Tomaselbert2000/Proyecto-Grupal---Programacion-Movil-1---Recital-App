package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.fragments.EventsFragment
import com.example.myapplication.fragments.SettingsFragment
import com.example.myapplication.fragments.TicketsHistoryFragment
import com.example.myapplication.fragments.UserFundsFragment
import com.example.myapplication.interfaces.IntSharedFunctions
import com.google.android.material.bottomnavigation.BottomNavigationView

class Main : AppCompatActivity(), IntSharedFunctions {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var eventFragment: EventsFragment
    lateinit var userFundsFragment: UserFundsFragment
    lateinit var ticketHistoryFragment: TicketsHistoryFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getLongExtra("USER_ID", 0L)

        bottomNavigationView = findViewById(R.id.BottomNavigationView)

        val fragmentManager = supportFragmentManager

        eventFragment = (this.searchFragmentByTag(fragmentManager, "EVENTS") as? EventsFragment
            ?: fragmentManager.fragments.filterIsInstance<EventsFragment>().firstOrNull()
            ?: EventsFragment.newInstance(userId))

        userFundsFragment =
            (this.searchFragmentByTag(fragmentManager, "FUNDS") as? UserFundsFragment
                ?: fragmentManager.fragments.filterIsInstance<UserFundsFragment>().firstOrNull()
                ?: UserFundsFragment.newInstance(userId))

        ticketHistoryFragment =
            (this.searchFragmentByTag(fragmentManager, "HISTORY")) as? TicketsHistoryFragment
                ?: fragmentManager.fragments.filterIsInstance<TicketsHistoryFragment>()
                    .firstOrNull() ?: TicketsHistoryFragment.newInstance(userId)

        settingsFragment =
            (this.searchFragmentByTag(fragmentManager, "SETTINGS")) as? SettingsFragment
                ?: fragmentManager.fragments.filterIsInstance<SettingsFragment>().firstOrNull()
                        ?: SettingsFragment.newInstance(userId)

        val listOfFragmentsOfMainActivity =
            mutableListOf(eventFragment, userFundsFragment, ticketHistoryFragment, settingsFragment)

        val fragmentTransaction = fragmentManager.beginTransaction()

        if (!fragmentManager.fragments.contains(eventFragment)) {
            this.addFragmentToTransaction(
                fragmentTransaction,
                eventFragment,
                R.id.Main_FragmentContainerView,
                "EVENTS"
            )
        }

        if (!fragmentManager.fragments.contains(userFundsFragment)) {
            this.addFragmentToTransaction(
                fragmentTransaction,
                eventFragment,
                R.id.Main_FragmentContainerView,
                "FUNDS"
            )
        }

        if (!fragmentManager.fragments.contains(ticketHistoryFragment)) {
            this.addFragmentToTransaction(
                fragmentTransaction,
                eventFragment,
                R.id.Main_FragmentContainerView,
                "HISTORY"
            )
        }
        if (!fragmentManager.fragments.contains(settingsFragment)) {
            this.addFragmentToTransaction(
                fragmentTransaction,
                eventFragment,
                R.id.Main_FragmentContainerView,
                "SETTINGS"
            )
        }

        fragmentTransaction.commitAllowingStateLoss()

        switchFragment(
            eventFragment,
            listOfFragmentsOfMainActivity,
            this.supportFragmentManager
        )

        bottomNavigationView.setOnItemSelectedListener { item ->
            bottomNavigationView.clearFocus()
            when (item.itemId) {
                R.id.Main_home_icon -> {
                    switchFragment(
                        eventFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_settings_icon -> {
                    switchFragment(
                        settingsFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_money_icon -> {
                    switchFragment(
                        userFundsFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_history_icon -> {
                    switchFragment(
                        ticketHistoryFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                else -> false
            }
        }
    }

    private fun searchFragmentByTag(
        fragmentManager: FragmentManager,
        fragmentTagToSearch: String
    ): Fragment? {
        return fragmentManager.findFragmentByTag(fragmentTagToSearch)
    }

    private fun addFragmentToTransaction(
        fragmentTransaction: FragmentTransaction,
        fragmentToAdd: Fragment,
        fragmentContainerId: Int,
        fragmentTag: String
    ) {
        fragmentTransaction.add(fragmentContainerId, fragmentToAdd, fragmentTag)
    }


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getLongExtra("USER_ID", 0)

        bottomNavigationView = findViewById(R.id.BottomNavigationView)

        eventFragment = EventsFragment.newInstance(userId)

        userFundsFragment = UserFundsFragment.newInstance(userId)

        ticketHistoryFragment = TicketsHistoryFragment.newInstance(userId)

        settingsFragment = SettingsFragment.newInstance(userId)

        fragmentToShowAsDefault = eventFragment

        val listOfFragmentsOfMainActivity =
            mutableListOf(eventFragment, userFundsFragment, ticketHistoryFragment, settingsFragment)

        val existingFragment =
            supportFragmentManager.findFragmentById(R.id.Main_FragmentContainerView)

        if (existingFragment == null) {
            addFragmentsFromList(
                listOfFragmentsOfMainActivity,
                R.id.Main_FragmentContainerView,
                this.supportFragmentManager
            )
        }

        switchFragment(
            eventFragment,
            listOfFragmentsOfMainActivity,
            this.supportFragmentManager
        )

        bottomNavigationView.setOnItemSelectedListener { item ->

            bottomNavigationView.clearFocus()

            when (item.itemId) {
                R.id.Main_home_icon -> {
                    switchFragment(
                        eventFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_settings_icon -> {
                    switchFragment(
                        settingsFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_money_icon -> {
                    switchFragment(
                        userFundsFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                R.id.Main_history_icon -> {
                    switchFragment(
                        ticketHistoryFragment,
                        listOfFragmentsOfMainActivity,
                        this.supportFragmentManager
                    )
                    true
                }

                else -> false
            }
        }
    }*/
}
