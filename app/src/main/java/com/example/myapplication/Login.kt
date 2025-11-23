package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity(), IntSharedFunctions {
    lateinit var nickname: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var signInButton: MaterialButton
    lateinit var signUpButton: MaterialButton
    lateinit var loginConstraintLayout: ConstraintLayout
    lateinit var destinationActivityMain: Class<Main>
    lateinit var destinationActivitySignUp: Class<SignUpUserContactData>


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInButton = findViewById(R.id.Login_SignIn_Button)
        signUpButton = findViewById(R.id.Login_SignUp_Button)
        nickname = findViewById(R.id.Login_EmailNickname_EditText)
        password = findViewById(R.id.Login_Password_EditText)
        destinationActivityMain = Main::class.java
        destinationActivitySignUp = SignUpUserContactData::class.java

        loginConstraintLayout =
            findViewById(R.id.Login_ConstraintLayout)

        signInButton.setOnClickListener {
            this.processLogin(destinationActivityMain)
        }

        signUpButton.setOnClickListener {
            this.createUser(destinationActivitySignUp)
        }
    }

    fun processLogin(destinationActivity: Class<Main>) {
        val nicknameAsText = nickname.text.toString()
        val passwordAsText = password.text.toString()
        if (UserRepository.login(
                nicknameAsText,
                passwordAsText
            ) != null
        ) {
            val intent = Intent(
                this,
                destinationActivity
            )
            intent.putExtra(
                "USER_ID",
                UserRepository.login(nicknameAsText, passwordAsText)?.personalID ?: 0
            )
            startActivity(intent)
            finish()
        } else if (nicknameAsText.isEmpty() || passwordAsText.isEmpty()) {
            makeAndShowShortLengthSnackBar("Campos de ingreso vacíos", loginConstraintLayout)
        } else {
            makeAndShowShortLengthSnackBar(
                "Usuario o contraseña incorrectos",
                loginConstraintLayout
            )
        }
    }

    private fun createUser(destinationActivity: Class<SignUpUserContactData>) {
        val intent = Intent(this, destinationActivity)
        startActivity(intent)
        finish()
    }
}