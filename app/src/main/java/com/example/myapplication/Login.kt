package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import repositories.UserRepository

class Login : AppCompatActivity() {
    // se declaran las variables que se usarán en el login
    lateinit var nickname: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var signInButton: MaterialButton
    lateinit var signUpButton: MaterialButton
    lateinit var loginConstraintLayout: ConstraintLayout
    lateinit var destinationActivityMain: Class<Main>
    lateinit var destinationActivitySignUp: Class<SignUpUserContactData>


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // al llamar al onCreate se infla la actividad de login
        // se busca cada componente por su id para inicializar las variables
        signInButton = findViewById(R.id.Login_SignIn_Button)
        signUpButton = findViewById(R.id.Login_SignUp_Button)
        nickname = findViewById(R.id.Login_EmailNickname_EditText)
        password = findViewById(R.id.Login_Password_EditText)
        destinationActivityMain = Main::class.java
        destinationActivitySignUp = SignUpUserContactData::class.java

        // en esta variable se guarda la vista compuesta por el constraint layout de la actividad de login
        // a fin de poder llamarlo para crear notificaciones con snackbar
        loginConstraintLayout =
            findViewById(R.id.Login_ConstraintLayout)

        // se llama al metodo onClickListener para el boton de inicio de sesion
        signInButton.setOnClickListener {
            this.processLogin(destinationActivityMain)
        }

        // y de igual manera con el botón de creación de usuario
        signUpButton.setOnClickListener {
            this.createUser(destinationActivitySignUp)
        }
    }

    fun processLogin(destinationActivity: Class<Main>) { // aca recibimos como parámetro el objeto de la clase ya inicializado
        // se obtiene el texto en limpio de los dos campos de texto de la pantalla
        val nicknameAsText = nickname.text.toString()
        val passwordAsText = password.text.toString()
        if (UserRepository.login(
                nicknameAsText,
                passwordAsText
            ) != null
        ) { // en caso de coincidir las credenciales se crea el intent
            val intent = Intent(
                this,
                destinationActivity
            ) // aca se crea el intent, llamando desde esta clase (this), hacia la actividad de destino
            intent.putExtra("USER_NICKNAME", nicknameAsText)
            intent.putExtra("USER_PASSWORD", passwordAsText)
            startActivity(intent) // se inicia la actividad de destino en esta linea
            finish() // y se finaliza esta actividad
        } else if (nicknameAsText.isEmpty() || passwordAsText.isEmpty()) { // si el usuario pulsa iniciar sesión con los campos vacios se dispara este snackbar
            this.showSnackBarAndResetFields("Campos de ingreso vacíos")
        } else { // y en caso que las credenciales no sean correctas, se dispara este 2do snackbar
            this.showSnackBarAndResetFields("Usuario o contraseña incorrectos")
        }
    }

    private fun createUser(destinationActivity: Class<SignUpUserContactData>) { // el argumento acá llama a la 1era pantalla de creación de usuario
        val intent = Intent(this, destinationActivity)
        startActivity(intent)
        finish()
    }

    private fun showSnackBarAndResetFields(message: String) {
        Snackbar.make(
            loginConstraintLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
        nickname.setText("")
        password.setText("")
    }
}