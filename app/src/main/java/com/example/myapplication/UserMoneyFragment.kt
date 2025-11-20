package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class UserMoneyFragment : Fragment() {
    private var param1: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_money, container, false)
    }

    companion object {

        const val USER_ID = "user_id"
        @JvmStatic
        fun newInstance(param1: Long) =
            UserMoneyFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, param1)
                }
            }
    }
}