package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Main : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.BottomNavigationView)

        val userId = intent.getLongExtra(
            "USER_ID",
            0
        ) // el intent contiene el userID del usuario logueado en la aplicacion

        if (savedInstanceState == null) { // en caso de no haber ninguna instancia anterior, la pantalla por default es la de eventos
            this.loadFragment(EventsFragment.newInstance(userId))
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            bottomNavigationView.clearFocus()
            when (item.itemId) {
                // aquellos fragmentos que gestionen datos de usuario como por ejemplo settings o el saldo, reciben el userID para consultar en el repositorio
                R.id.Main_home_icon -> {
                    loadFragment(EventsFragment.newInstance(userId))
                    true
                }

                R.id.Main_settings_icon -> {
                    loadFragment(SettingsFragment.newInstance(userId))
                    true
                }

                R.id.Main_money_icon -> {
                    loadFragment(UserFundsFragment.newInstance(userId))
                    true
                }

                R.id.Main_history_icon -> {
                    loadFragment(TicketsHistoryFragment.newInstance(userId))
                    true
                }


                else -> false
            }
        }
    }

    fun loadFragment(fragmentToLoadOnMainActivity: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.Main_FragmentContainerView, fragmentToLoadOnMainActivity).commit()
    }
}
