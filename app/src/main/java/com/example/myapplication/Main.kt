package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import repositories.UserRepository

class Main : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.BottomNavigationView)

        val userLogged = UserRepository.buscarUsuarioPorNickname(
            (intent.extras?.get("USER_NICKNAME")
                ?: "") as String
        )

        if (savedInstanceState == null) {
            this.loadFragment(EventsFragment.newInstance("1", "2"))
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            bottomNavigationView.clearFocus()
            when (item.itemId) {
                R.id.Main_home_icon -> {
                    loadFragment(EventsFragment.newInstance("1", "2"))
                    true
                }

                R.id.Main_settings_icon -> {
                    loadFragment(SettingsFragment.newInstance("1", "2"))
                    true
                }

                R.id.Main_money_icon -> {
                    loadFragment(UserMoneyFragment.newInstance(userLogged?.id ?: 0))
                    true
                }

                R.id.Main_history_icon -> {
                    loadFragment(TicketsHistoryFragment.newInstance("1", "2"))
                    true
                }

                else -> false
            }
        }
    }

    fun loadFragment(fragmentToLoadOnMainActivity: Fragment) {
        val bundleToSend = bundleOf(USER_ID to "user_id")
        supportFragmentManager.beginTransaction()
            .replace(R.id.Main_FragmentContainerView, fragmentToLoadOnMainActivity).commit()
    }
}
