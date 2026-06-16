package com.program.diefit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class HistorialRutinasFragment : Fragment() {

    private lateinit var tvRacha: TextView
    private lateinit var contenedorHistorial: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_historial_rutinas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvRacha = view.findViewById(R.id.tvRacha)
        contenedorHistorial = view.findViewById(R.id.contenedorHistorial)

        render()
    }

    override fun onResume() {
        super.onResume()
        render()
    }

    private fun render() {
        tvRacha.text = RegistroRutinaRepository.calcularRacha().toString()
        contenedorHistorial.removeAllViews()

        val fechas = RegistroRutinaRepository.fechasConRegistro()

        if (fechas.isEmpty()) {
            val tv = TextView(requireContext()).apply {
                text = "Aún no tienes rutinas completadas"
                setPadding(0, 24, 0, 24)
                textSize = 14f
            }
            contenedorHistorial.addView(tv)
            return
        }

        fechas.forEach { fecha ->
            val registrosDelDia = RegistroRutinaRepository.obtenerPorFecha(fecha)

            val card = MaterialCardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 16 }
                radius = 12f * resources.displayMetrics.density
                cardElevation = 0f
            }

            val inner = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                val pad = (16 * resources.displayMetrics.density).toInt()
                setPadding(pad, pad, pad, pad)
            }

            val tvFecha = TextView(requireContext()).apply {
                text = fecha
                textSize = 15f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }

            inner.addView(tvFecha)

            registrosDelDia.forEach { registro ->
                val tvRutina = TextView(requireContext()).apply {
                    text = "✓ ${registro.rutinaNombre}"
                    textSize = 13f
                    setPadding(0, 6, 0, 0)
                }
                inner.addView(tvRutina)
            }

            card.addView(inner)
            contenedorHistorial.addView(card)
        }
    }
}