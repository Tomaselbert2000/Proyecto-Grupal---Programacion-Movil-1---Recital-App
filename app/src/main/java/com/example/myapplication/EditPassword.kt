package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.data.superclass.User
import com.example.myapplication.fragments.ConfirmPasswordFragment
import com.example.myapplication.fragments.EnterPasswordFragment
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton

class EditPassword : AppCompatActivity(), IntSharedFunctions {

    lateinit var enterPasswordFragment: Fragment
    lateinit var confirmNewPasswordFragment: Fragment
    lateinit var loggedUser: User
    lateinit var goBackToEditUserAttributesButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        val userId = intent.getLongExtra("USER_ID", 0L)

        loggedUser = UserRepository.getUserById(userId)!!

        goBackToEditUserAttributesButton = findViewById(R.id.EditPassword_BackwardsButton)

        enterPasswordFragment = EnterPasswordFragment.newInstance(loggedUser.personalID)

        confirmNewPasswordFragment = ConfirmPasswordFragment.newInstance(loggedUser.personalID)

        val listOfFragmentsToLoad = mutableListOf(enterPasswordFragment, confirmNewPasswordFragment)
        addFragmentsFromList(
            listOfFragmentsToLoad,
            R.id.EditPassword_FragmentContainer,
            this.supportFragmentManager
        )

        switchFragment(enterPasswordFragment, listOfFragmentsToLoad, this.supportFragmentManager)

        goBackToEditUserAttributesButton.setOnClickListener {
            finish()
        }
    }

    fun goToConfirmPasswordFragment() {
        switchFragment(
            confirmNewPasswordFragment,
            mutableListOf(enterPasswordFragment, confirmNewPasswordFragment),
            this.supportFragmentManager
        )
    }

    fun goBackToSettingsAfterPasswordUpdate() {
        finish()
    }
}