package com.program.diefit.Fragment

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.program.diefit.ProductoRepository
import com.program.diefit.R
import com.program.diefit.RegistroComidaRepository
import com.program.diefit.entities.Producto
import com.program.diefit.entities.RegistroComida
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrarComidaFragment : Fragment() {

    private lateinit var fechaInput: TextInputEditText
    private lateinit var buscarProductoInput: TextInputEditText
    private lateinit var contenedorProductosSeleccionables: LinearLayout
    private lateinit var cardSeleccionado: MaterialCardView
    private lateinit var tvProductoSeleccionado: TextView
    private lateinit var cantidadComidaInput: TextInputEditText
    private lateinit var btnGuardarComida: MaterialButton
    private lateinit var contenedorRegistrosDia: LinearLayout

    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var fechaSeleccionada: Calendar = Calendar.getInstance()
    private var productoSeleccionado: Producto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_registrar_comida, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fechaInput = view.findViewById(R.id.fechaInput)
        buscarProductoInput = view.findViewById(R.id.buscarProductoInput)
        contenedorProductosSeleccionables = view.findViewById(R.id.contenedorProductosSeleccionables)
        cardSeleccionado = view.findViewById(R.id.cardSeleccionado)
        tvProductoSeleccionado = view.findViewById(R.id.tvProductoSeleccionado)
        cantidadComidaInput = view.findViewById(R.id.cantidadComidaInput)
        btnGuardarComida = view.findViewById(R.id.btnGuardarComida)
        contenedorRegistrosDia = view.findViewById(R.id.contenedorRegistrosDia)

        fechaInput.setText(formatoFecha.format(fechaSeleccionada.time))

        fechaInput.setOnClickListener { mostrarDatePicker() }

        renderProductosSeleccionables(ProductoRepository.obtenerTodos())
        renderRegistrosDia()

        buscarProductoInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                renderProductosSeleccionables(ProductoRepository.buscar(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnGuardarComida.setOnClickListener {
            guardarComida()
        }
    }

    private fun mostrarDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                fechaSeleccionada.set(year, month, day)
                fechaInput.setText(formatoFecha.format(fechaSeleccionada.time))
                renderRegistrosDia()
            },
            fechaSeleccionada.get(Calendar.YEAR),
            fechaSeleccionada.get(Calendar.MONTH),
            fechaSeleccionada.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun renderProductosSeleccionables(lista: List<Producto>) {
        contenedorProductosSeleccionables.removeAllViews()

        if (lista.isEmpty()) {
            val tv = TextView(requireContext()).apply {
                text = "No hay productos"
                setPadding(0, 16, 0, 16)
                textSize = 13f
            }
            contenedorProductosSeleccionables.addView(tv)
            return
        }

        lista.forEach { producto ->
            val card = MaterialCardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 16 }
                radius = 10f * resources.displayMetrics.density
                cardElevation = 0f
            }

            val inner = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                val pad = (12 * resources.displayMetrics.density).toInt()
                setPadding(pad, pad, pad, pad)
            }

            val tvNombre = TextView(requireContext()).apply {
                text = producto.nombre
                textSize = 15f
                setTypeface(typeface, Typeface.BOLD)
            }

            val tvDetalle = TextView(requireContext()).apply {
                text = "${producto.cantidad} ${producto.unidad} · ${producto.calorias} kcal"
                textSize = 12f
                setPadding(0, 4, 0, 0)
            }

            inner.addView(tvNombre)
            inner.addView(tvDetalle)
            card.addView(inner)

            card.setOnClickListener {
                productoSeleccionado = producto
                tvProductoSeleccionado.text = producto.nombre
                cantidadComidaInput.setText(producto.cantidad)
                cardSeleccionado.visibility = View.VISIBLE
            }

            contenedorProductosSeleccionables.addView(card)
        }
    }

    private fun guardarComida() {
        val producto = productoSeleccionado
        if (producto == null) {
            Toast.makeText(requireContext(), "Selecciona un producto", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidadConsumida = cantidadComidaInput.text.toString().ifBlank { producto.cantidad }
        val factor = (cantidadConsumida.toDoubleOrNull() ?: 0.0) / (producto.cantidad.toDoubleOrNull() ?: 1.0)

        val registro = RegistroComida(
            usuarioId = 0,
            fecha = formatoFecha.format(fechaSeleccionada.time),
            nombreProducto = producto.nombre,
            cantidad = cantidadConsumida,
            unidad = producto.unidad,
            calorias = ((producto.calorias.toDoubleOrNull() ?: 0.0) * factor).toInt().toString(),
            proteinas = ((producto.proteinas.toDoubleOrNull() ?: 0.0) * factor).toInt().toString(),
            carbohidratos = ((producto.carbohidratos.toDoubleOrNull() ?: 0.0) * factor).toInt().toString(),
            grasas = ((producto.grasas.toDoubleOrNull() ?: 0.0) * factor).toInt().toString()
        )

        RegistroComidaRepository.agregar(registro)
        Toast.makeText(requireContext(), "Comida registrada", Toast.LENGTH_SHORT).show()

        cardSeleccionado.visibility = View.GONE
        productoSeleccionado = null
        cantidadComidaInput.setText("")

        renderRegistrosDia()
    }

    private fun renderRegistrosDia() {
        contenedorRegistrosDia.removeAllViews()
        val fecha = formatoFecha.format(fechaSeleccionada.time)
        val lista = RegistroComidaRepository.obtenerPorFecha(fecha)

        if (lista.isEmpty()) {
            val tv = TextView(requireContext()).apply {
                text = "Aún no registras comidas en esta fecha"
                setPadding(0, 8, 0, 8)
                textSize = 13f
            }
            contenedorRegistrosDia.addView(tv)
            return
        }

        lista.forEach { registro ->
            val card = MaterialCardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 12 }
                radius = 10f * resources.displayMetrics.density
                cardElevation = 0f
            }

            val inner = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                val pad = (12 * resources.displayMetrics.density).toInt()
                setPadding(pad, pad, pad, pad)
            }

            val tvNombre = TextView(requireContext()).apply {
                text = registro.nombreProducto
                textSize = 14f
                setTypeface(typeface, Typeface.BOLD)
            }

            val tvDetalle = TextView(requireContext()).apply {
                text = "${registro.cantidad} ${registro.unidad} · ${registro.calorias} kcal"
                textSize = 12f
                setPadding(0, 4, 0, 0)
            }

            inner.addView(tvNombre)
            inner.addView(tvDetalle)
            card.addView(inner)

            card.setOnLongClickListener {
                RegistroComidaRepository.eliminar(registro)
                renderRegistrosDia()
                true
            }

            contenedorRegistrosDia.addView(card)
        }
    }
}