package com.program.diefit.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.program.diefit.ProductoRepository
import com.program.diefit.R
import com.program.diefit.entities.Producto

class NuevoProductoFragment : Fragment() {

    private lateinit var nombreLayout: TextInputLayout
    private lateinit var caloriasLayout: TextInputLayout
    private lateinit var nombreInput: TextInputEditText
    private lateinit var cantidadInput: TextInputEditText
    private lateinit var unidadInput: TextInputEditText
    private lateinit var caloriasInput: TextInputEditText
    private lateinit var proteinasInput: TextInputEditText
    private lateinit var carbosInput: TextInputEditText
    private lateinit var grasasInput: TextInputEditText
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_nuevo_producto, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nombreLayout   = view.findViewById(R.id.nombreProdLayout)
        caloriasLayout = view.findViewById(R.id.caloriasLayout)
        nombreInput    = view.findViewById(R.id.nombreProdInput)
        cantidadInput  = view.findViewById(R.id.cantidadInput)
        unidadInput    = view.findViewById(R.id.unidadInput)
        caloriasInput  = view.findViewById(R.id.caloriasInput)
        proteinasInput = view.findViewById(R.id.proteinasInput)
        carbosInput    = view.findViewById(R.id.carbosInput)
        grasasInput    = view.findViewById(R.id.grasasInput)
        btnGuardar     = view.findViewById(R.id.btnGuardarProducto)
        btnCancelar    = view.findViewById(R.id.btnCancelarProducto)

        btnGuardar.setOnClickListener {
            if (validar()) guardarProducto()
        }

        btnCancelar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validar(): Boolean {
        var ok = true
        nombreLayout.error = null
        caloriasLayout.error = null

        if (nombreInput.text.isNullOrBlank()) {
            nombreLayout.error = "Ingresa el nombre"
            ok = false
        }
        if (caloriasInput.text.isNullOrBlank()) {
            caloriasLayout.error = "Ingresa las calorías"
            ok = false
        }
        return ok
    }

    private fun guardarProducto() {
        val producto = Producto(
            nombre = nombreInput.text.toString().trim(),
            cantidad = cantidadInput.text.toString().ifBlank { "100" },
            unidad = unidadInput.text.toString().ifBlank { "g" },
            calorias = caloriasInput.text.toString(),
            proteinas = proteinasInput.text.toString().ifBlank { "0" },
            carbohidratos = carbosInput.text.toString().ifBlank { "0" },
            grasas = grasasInput.text.toString().ifBlank { "0" }
        )

        ProductoRepository.agregar(producto)
        Toast.makeText(requireContext(), "Producto guardado", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }
}