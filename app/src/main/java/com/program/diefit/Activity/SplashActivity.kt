package com.program.diefit.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.program.diefit.R
import com.program.diefit.UserRepository

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        UserRepository.cargarDesdeStorage()

        Handler(Looper.getMainLooper()).postDelayed({
            val destino = if (UserRepository.usuarioActual != null) {
                HomeActivity::class.java
            } else {
                MainActivity::class.java
            }
            startActivity(Intent(this, destino))
            finish()
        }, 1500)
    }
}