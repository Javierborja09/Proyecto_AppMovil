package com.program.diefit.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.program.diefit.R
import com.program.diefit.UserRepository

class MainActivity : AppCompatActivity() {

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var googleButton: MaterialButton

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Google Sign-In cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (UserRepository.usuarioActual != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        // Configura Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        emailLayout    = findViewById(R.id.emailLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        emailInput     = findViewById(R.id.emailInput)
        passwordInput  = findViewById(R.id.passwordInput)
        loginButton    = findViewById(R.id.loginButton)
        googleButton   = findViewById(R.id.googleButton)

        findViewById<TextView>(R.id.signUpLink).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Login normal
        loginButton.setOnClickListener {
            val email    = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            emailLayout.error = null
            passwordLayout.error = null

            if (email.isEmpty()) {
                emailLayout.error = "Ingresa tu correo"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.error = "Correo no válido"
            } else if (password.isEmpty()) {
                passwordLayout.error = "Ingresa tu contraseña"
            } else if (UserRepository.login(email, password)) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                emailLayout.error = "Correo o contraseña incorrectos"
                passwordLayout.error = " "
            }
        }

        // Login con Google
        googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleLauncher.launch(signInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser!!
                    UserRepository.loginOCrearConGoogle(
                        email = firebaseUser.email ?: "",
                        nombre = firebaseUser.displayName ?: "Usuario"
                    )
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error al autenticar con Google", Toast.LENGTH_SHORT).show()
                }
            }
    }
}