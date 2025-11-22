package com.example.myapplication.interfaces

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar

interface SharedFunctions {
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
}