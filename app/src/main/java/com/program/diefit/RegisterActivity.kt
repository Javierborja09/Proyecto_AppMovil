package com.program.diefit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.program.diefit.services.ReniecService
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var dniLayout: TextInputLayout
    private lateinit var dniInput: TextInputEditText
    private lateinit var btnBuscarDni: MaterialButton
    private lateinit var nombreLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmLayout: TextInputLayout
    private lateinit var nombreInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmInput: TextInputEditText
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dniLayout      = findViewById(R.id.regDniLayout)
        dniInput       = findViewById(R.id.regDniInput)
        btnBuscarDni   = findViewById(R.id.btnBuscarDni)
        nombreLayout   = findViewById(R.id.regNombreLayout)
        emailLayout    = findViewById(R.id.regEmailLayout)
        passwordLayout = findViewById(R.id.regPasswordLayout)
        confirmLayout  = findViewById(R.id.regConfirmLayout)
        nombreInput    = findViewById(R.id.regNombreInput)
        emailInput     = findViewById(R.id.regEmailInput)
        passwordInput  = findViewById(R.id.regPasswordInput)
        confirmInput   = findViewById(R.id.regConfirmInput)
        btnRegistrar   = findViewById(R.id.btnRegistrar)
        loginLink      = findViewById(R.id.loginLink)

        btnBuscarDni.setOnClickListener {
            buscarDni()
        }

        btnRegistrar.setOnClickListener {
            if (validar()) registrar()
        }

        loginLink.setOnClickListener {
            finish()
        }
    }

    private fun buscarDni() {
        val dni = dniInput.text.toString().trim()
        if (dni.length != 8) {
            dniLayout.error = "El DNI debe tener 8 dígitos"
            return
        }

        dniLayout.error = null

        lifecycleScope.launch {
            Toast.makeText(this@RegisterActivity, "Consultando RENIEC...", Toast.LENGTH_SHORT).show()
            val persona = ReniecService.consultarDni(dni)

            if (persona != null) {
                val nombreCompleto = "${persona.nombres} ${persona.apellidos}"
                nombreInput.setText(nombreCompleto)
                nombreInput.isEnabled = false
                Toast.makeText(this@RegisterActivity, "DNI verificado con éxito", Toast.LENGTH_SHORT).show()
            } else {
                dniLayout.error = "No se encontró el DNI"
                nombreInput.isEnabled = true
            }
        }
    }

    private fun validar(): Boolean {
        var ok = true
        dniLayout.error      = null
        nombreLayout.error   = null
        emailLayout.error    = null
        passwordLayout.error = null
        confirmLayout.error  = null

        val dni      = dniInput.text.toString().trim()
        val nombre   = nombreInput.text.toString().trim()
        val email    = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()
        val confirm  = confirmInput.text.toString()

        if (dni.isEmpty()) {
            dniLayout.error = "Ingresa tu DNI"
            ok = false
        } else if (dni.length != 8) {
            dniLayout.error = "El DNI debe tener 8 dígitos"
            ok = false
        }
        if (nombre.isEmpty()) {
            nombreLayout.error = "Debes validar tu DNI primero"
            ok = false
        }
        if (email.isEmpty()) {
            emailLayout.error = "Ingresa tu correo"
            ok = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Correo no válido"
            ok = false
        }
        if (password.isEmpty()) {
            passwordLayout.error = "Ingresa una contraseña"
            ok = false
        } else if (password.length < 6) {
            passwordLayout.error = "Mínimo 6 caracteres"
            ok = false
        }
        if (confirm != password) {
            confirmLayout.error = "Las contraseñas no coinciden"
            ok = false
        }
        return ok
    }

    private fun registrar() {
        val nombre   = nombreInput.text.toString().trim()
        val email    = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        when (UserRepository.registrar(nombre, email, password)) {
            UserRepository.Result.EXITO -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            UserRepository.Result.ERROR_EMAIL_EXISTE -> {
                emailLayout.error = "Este correo ya está registrado"
            }
        }
    }
}