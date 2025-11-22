package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.EventsFragment
import com.example.myapplication.fragments.SettingsFragment
import com.example.myapplication.fragments.TicketsHistoryFragment
import com.example.myapplication.fragments.UserFundsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Main : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var eventFragment: EventsFragment
    lateinit var userFundsFragment: UserFundsFragment
    lateinit var ticketHistoryFragment: TicketsHistoryFragment
    lateinit var settingsFragment: SettingsFragment
    lateinit var displayedFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.BottomNavigationView)

        val userId = intent.getLongExtra(
            "USER_ID",
            0
        ) // el intent contiene el userID del usuario logueado en la aplicacion

        if (savedInstanceState == null) { // en caso de no haber ninguna instancia anterior, la pantalla por default es la de eventos
            this.hideFragment(EventsFragment.newInstance(userId))
        }

        eventFragment = EventsFragment.newInstance(userId)
        userFundsFragment = UserFundsFragment.newInstance(userId)
        ticketHistoryFragment = TicketsHistoryFragment.newInstance(userId)
        settingsFragment = SettingsFragment.newInstance(userId)
        displayedFragment = eventFragment

        val listOfFragmentsToLoad = mutableListOf(
            eventFragment,
            userFundsFragment,
            ticketHistoryFragment,
            settingsFragment
        )

        this.loadHiddenFragmentsOnMemory(listOfFragmentsToLoad)
        this.showDefaultFragment()

        bottomNavigationView.setOnItemSelectedListener { item ->
            bottomNavigationView.clearFocus()
            when (item.itemId) {
                // aquellos fragmentos que gestionen datos de usuario como por ejemplo settings o el saldo, reciben el userID para consultar en el repositorio
                R.id.Main_home_icon -> {
                    this.goToThisFragmentAndHideTheOthers(eventFragment, listOfFragmentsToLoad)
                    true
                }

                R.id.Main_settings_icon -> {
                    this.goToThisFragmentAndHideTheOthers(settingsFragment, listOfFragmentsToLoad)
                    true
                }

                R.id.Main_money_icon -> {
                    this.goToThisFragmentAndHideTheOthers(userFundsFragment, listOfFragmentsToLoad)
                    true
                }

                R.id.Main_history_icon -> {
                    this.goToThisFragmentAndHideTheOthers(
                        ticketHistoryFragment,
                        listOfFragmentsToLoad
                    )
                    true
                }

                else -> false
            }
        }
    }

    private fun goToThisFragmentAndHideTheOthers(
        fragmentToGo: Fragment,
        listOfFragmentsToLoad: MutableList<Fragment>
    ) {
        for (fragment in listOfFragmentsToLoad) {
            if (fragment == fragmentToGo) {
                supportFragmentManager.beginTransaction().show(fragment).commit()
            } else {
                supportFragmentManager.beginTransaction().hide(fragment).commit()
            }
        }
    }

    private fun showDefaultFragment() {
        supportFragmentManager.beginTransaction().show(eventFragment).commit()
    }

    private fun loadHiddenFragmentsOnMemory(listOfFragmentsToLoad: MutableList<Fragment>) {
        for (fragment in listOfFragmentsToLoad) {
            this.hideFragment(fragment)
        }
    }

    fun hideFragment(fragmentToLoadOnMainActivity: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.Main_FragmentContainerView, fragmentToLoadOnMainActivity)
            .hide(fragmentToLoadOnMainActivity).commit()
    }
}
