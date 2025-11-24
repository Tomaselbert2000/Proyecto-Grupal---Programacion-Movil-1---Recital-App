package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.myapplication.EditPassword
import com.example.myapplication.R
import com.example.myapplication.data.superclass.User
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EnterPasswordFragment : Fragment(), IntSharedFunctions {
    private var userId: Long? = null
    lateinit var continueToConfirmPasswordButton: MaterialButton
    lateinit var passwordEditText: TextInputEditText
    lateinit var enterPasswordFragmentConstraintLayout: ConstraintLayout
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
        return inflater.inflate(R.layout.fragment_enter_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continueToConfirmPasswordButton = view.findViewById(R.id.EnterPassword_ContinueButton)
        passwordEditText = view.findViewById(R.id.EnterPassword_EditText)
        loggedUser = UserRepository.getUserById(userId)!!
        enterPasswordFragmentConstraintLayout =
            view.findViewById(R.id.EnterPassword_InnerConstraintLayout)

        continueToConfirmPasswordButton.setOnClickListener {

            if (passwordEditText.text.toString() == loggedUser.password) {
                (requireActivity() as EditPassword).goToConfirmPasswordFragment()
            } else {
                makeAndShowShortLengthSnackBar(
                    "Contraseña incorrecta. Reintentar.",
                    enterPasswordFragmentConstraintLayout
                )
                passwordEditText.setText("")
            }
        }
    }

    companion object {

        private const val USER_ID = "userId"

        @JvmStatic
        fun newInstance(id: Long) =
            EnterPasswordFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, id)
                }
            }
    }
}
