package com.program.diefit.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.program.diefit.R
import com.program.diefit.RutinaRepository
import com.program.diefit.entities.Rutina

class NuevaRutinaFragment : Fragment() {

    private lateinit var nombreLayout: TextInputLayout
    private lateinit var nombreInput: TextInputEditText
    private lateinit var ejerciciosInput: TextInputEditText
    private lateinit var duracionInput: TextInputEditText
    private lateinit var chipTipo: ChipGroup
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_nueva_rutina, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nombreLayout    = view.findViewById(R.id.nombreRutinaLayout)
        nombreInput     = view.findViewById(R.id.nombreRutinaInput)
        ejerciciosInput = view.findViewById(R.id.ejerciciosInput)
        duracionInput   = view.findViewById(R.id.duracionInput)
        chipTipo        = view.findViewById(R.id.chipTipoRutina)
        btnGuardar      = view.findViewById(R.id.btnGuardarRutina)
        btnCancelar     = view.findViewById(R.id.btnCancelarRutina)

        btnGuardar.setOnClickListener {
            if (validar()) guardarRutina()
        }

        btnCancelar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validar(): Boolean {
        var ok = true
        nombreLayout.error = null

        if (nombreInput.text.isNullOrBlank()) {
            nombreLayout.error = "Ingresa el nombre de la rutina"
            ok = false
        }
        return ok
    }

    private fun guardarRutina() {
        val tipo = when (chipTipo.checkedChipId) {
            R.id.chipFuerza -> "Fuerza"
            R.id.chipCardio -> "Cardio"
            R.id.chipFlexibilidad -> "Flexibilidad"
            else -> "Fuerza"
        }

        val rutina = Rutina(
            nombre = nombreInput.text.toString().trim(),
            dias = ejerciciosInput.text.toString().ifBlank { "0" },
            duracion = duracionInput.text.toString().ifBlank { "0" },
            tipo = tipo
        )

        RutinaRepository.agregar(rutina)
        Toast.makeText(requireContext(), "Rutina guardada", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }
}