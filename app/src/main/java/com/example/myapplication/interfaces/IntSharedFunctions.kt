package com.example.myapplication.interfaces

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.snackbar.Snackbar

interface IntSharedFunctions {
    fun makeAndShowShortLengthSnackBar(
        messageToDisplay: String,
        viewToDisplayOn: ConstraintLayout
    ) {
        Snackbar.make(viewToDisplayOn, messageToDisplay, Snackbar.LENGTH_SHORT).show()
    }

    fun addFragment(fragmentToAdd: Fragment, containerId: Int, manager: FragmentManager) {
        manager.beginTransaction().add(containerId, fragmentToAdd).commit()
    }

    fun hideFragment(fragmentToHide: Fragment, manager: FragmentManager) {
        manager.beginTransaction().hide(fragmentToHide).commit()
    }

    fun showFragment(fragmentToShow: Fragment, manager: FragmentManager) {
        manager.beginTransaction().show(fragmentToShow).commit()
    }

    fun addFragmentsFromList(
        fragmentList: MutableList<Fragment>,
        containerId: Int,
        manager: FragmentManager
    ) {
        for (fragment in fragmentList) {
            this.addFragment(fragment, containerId, manager)
        }
    }

    fun switchFragment(
        fragmentToSwitch: Fragment,
        listOfFragments: MutableList<Fragment>,
        manager: FragmentManager
    ) {
        for (fragment in listOfFragments) {
            if (fragment == fragmentToSwitch) {
                showFragment(fragment, manager)
            } else {
                hideFragment(fragment, manager)
            }
        }
    }

    fun passwordIsStrong(newPassword: String): Boolean {
        var uppercase = 0
        var specialCharacter = 0
        var numbers = 0
        for (letter in newPassword) {
            when (letter.code) {
                in 33..38 -> {
                    specialCharacter++
                }

                in 48..57 -> {
                    numbers++
                }

                in 65..90 -> {
                    uppercase++
                }
            }
        }
        return newPassword.length >= 8 && specialCharacter >= 1 && uppercase >= 1 && numbers >= 0
    }

    fun passwordsMatch(password: String, passwordConfirmation: String): Boolean {
        return password == passwordConfirmation
    }

    fun nicknameIsValid(nicknameToValidate: String): Boolean {
        return UserRepository.userNicknameIsNotTaken(nicknameToValidate)
    }

    fun emailIsNotTaken(emailToValidate: String): Boolean {
        return UserRepository.userEmailAddressIsNotTaken(emailToValidate)
    }

    fun emailIsValid(emailToValidate: String): Boolean {
        return emailToValidate.any { it == '@' }
    }
}