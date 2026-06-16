package com.program.diefit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.program.diefit.entities.Rutina
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RutinasFragment : Fragment() {

    private lateinit var contenedorRutinas: LinearLayout
    private lateinit var btnNuevaRutina: MaterialButton
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_rutinas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contenedorRutinas = view.findViewById(R.id.contenedorRutinas)
        btnNuevaRutina = view.findViewById(R.id.btnNuevaRutina)

        renderRutinas(RutinaRepository.obtenerTodos())

        btnNuevaRutina.setOnClickListener {
            findNavController().navigate(R.id.action_rutinas_to_nuevaRutina)
        }
    }

    override fun onResume() {
        super.onResume()
        renderRutinas(RutinaRepository.obtenerTodos())
    }

    private fun renderRutinas(lista: List<Rutina>) {
        contenedorRutinas.removeAllViews()
        val hoy = formatoFecha.format(Calendar.getInstance().time)

        if (lista.isEmpty()) {
            val tv = TextView(requireContext()).apply {
                text = "No hay rutinas"
                setPadding(0, 24, 0, 24)
                textSize = 14f
            }
            contenedorRutinas.addView(tv)
            return
        }

        lista.forEach { rutina ->
            val card = MaterialCardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 24 }
                radius = 12f * resources.displayMetrics.density
                cardElevation = 0f
            }

            val fila = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                val pad = (16 * resources.displayMetrics.density).toInt()
                setPadding(pad, pad, pad, pad)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val infoLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val tvNombre = TextView(requireContext()).apply {
                text = rutina.nombre
                textSize = 16f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }

            val tvDetalle = TextView(requireContext()).apply {
                text = "${rutina.ejercicios} ejercicios · ${rutina.duracion} min"
                textSize = 13f
                setPadding(0, 8, 0, 0)
            }

            infoLayout.addView(tvNombre)
            infoLayout.addView(tvDetalle)

            val checkHoy = CheckBox(requireContext()).apply {
                isChecked = RegistroRutinaRepository.estaMarcada(hoy, rutina.nombre)
                text = "Hoy"
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        RegistroRutinaRepository.marcarCumplida(hoy, rutina.nombre)
                    } else {
                        RegistroRutinaRepository.desmarcar(hoy, rutina.nombre)
                    }
                }
            }

            fila.addView(infoLayout)
            fila.addView(checkHoy)
            card.addView(fila)

            card.setOnLongClickListener {
                RutinaRepository.eliminar(rutina)
                renderRutinas(RutinaRepository.obtenerTodos())
                true
            }

            contenedorRutinas.addView(card)
        }
    }
}