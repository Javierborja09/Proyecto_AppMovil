package com.program.diefit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val destino = if (UserRepository.usuarioActual != null) {
                HomeActivity::class.java
            } else {
                MainActivity::class.java
            }
            startActivity(Intent(this, destino))
            finish()
        }, 1500) // 1.5 segundos
    }
}