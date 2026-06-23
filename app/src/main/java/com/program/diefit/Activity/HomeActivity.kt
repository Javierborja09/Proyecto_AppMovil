package com.program.diefit.Activity

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.program.diefit.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navController)

        // Submenú para Rutinas
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_rutinas -> {
                    showSubMenu(listOf("Mis rutinas", "Historial")) { opcion ->
                        when (opcion) {
                            "Mis rutinas"  -> navController.navigate(R.id.rutinasFragment)
                            "Historial"    -> navController.navigate(R.id.historialRutinasFragment)
                        }
                    }
                    true
                }
                R.id.nav_nutricion -> {
                    showSubMenu(listOf("Registrar comida")) {opcion ->
                        when (opcion) {
                            "Registrar comida" -> navController.navigate(R.id.registrarComidaFragment)
                        }
                    }
                    true
                }
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_productos -> {
                    navController.navigate(R.id.productosFragment)
                    true
                }
                R.id.nav_perfil -> {
                    navController.navigate(R.id.perfilFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun showSubMenu(opciones: List<String>, onSelect: (String) -> Unit) {
        val dialog = BottomSheetDialog(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 16, 0, 32)
        }
        opciones.forEach { opcion ->
            val item = TextView(this).apply {
                text = opcion
                textSize = 16f
                setPadding(48, 32, 48, 32)
                setOnClickListener {
                    onSelect(opcion)
                    dialog.dismiss()
                }
            }
            layout.addView(item)
        }
        dialog.setContentView(layout)
        dialog.show()
    }
}