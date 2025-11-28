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

class ConfirmPasswordFragment : Fragment(), IntSharedFunctions {
    private var userId: Long? = null
    lateinit var userToUpdatePassword: User
    lateinit var newPasswordEditText: TextInputEditText
    lateinit var reEnterNewPasswordEditText: TextInputEditText
    lateinit var saveChangesAndExitButton: MaterialButton
    lateinit var confirmPasswordFragmentConstraintLayout: ConstraintLayout

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
        return inflater.inflate(R.layout.fragment_confirm_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userToUpdatePassword = UserRepository.getUserById(userId)!!
        newPasswordEditText = view.findViewById(R.id.ConfirmPassword_EnterNewPassword_EditText)
        reEnterNewPasswordEditText =
            view.findViewById(R.id.ConfirmPassword_ReEnterPassword_EditText)
        saveChangesAndExitButton = view.findViewById(R.id.ConfirmPassword_SaveChangesAndExitButton)
        confirmPasswordFragmentConstraintLayout =
            view.findViewById(R.id.ConfirmPassword_ConstraintLayout)

        saveChangesAndExitButton.setOnClickListener {

            val newPasswordEditTextAsString = newPasswordEditText.text.toString()
            val reEnterPasswordEditTextAsString = reEnterNewPasswordEditText.text.toString()

            if (newPasswordEditTextAsString.isBlank() || reEnterPasswordEditTextAsString.isBlank()) {
                makeAndShowShortLengthSnackBar(
                    "Completar todos los campos.",
                    confirmPasswordFragmentConstraintLayout
                )
                this.resetFields()
            } else if (passwordsMatch(
                    newPasswordEditTextAsString,
                    reEnterPasswordEditTextAsString
                ) && !passwordIsStrong(newPasswordEditTextAsString)
            ) {
                makeAndShowShortLengthSnackBar(
                    ("Contraseña débil. Reintentar"),
                    confirmPasswordFragmentConstraintLayout
                )
                this.resetFields()
            } else if (!passwordsMatch(
                    newPasswordEditTextAsString,
                    reEnterPasswordEditTextAsString
                )
            ) {
                makeAndShowShortLengthSnackBar(
                    "Las contraseñas no coinciden.",
                    confirmPasswordFragmentConstraintLayout
                )
                this.resetFields()
            } else {
                userToUpdatePassword.updatePassword(newPasswordEditTextAsString)
                (requireActivity() as EditPassword).goBackToSettingsAfterPasswordUpdate()
            }
        }
    }

    private fun resetFields() {
        newPasswordEditText.setText("")
        reEnterNewPasswordEditText.setText("")
    }

    companion object {

        private const val USER_ID = "userId"

        @JvmStatic
        fun newInstance(id: Long) =
            ConfirmPasswordFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, id)
                }
            }
    }
}
