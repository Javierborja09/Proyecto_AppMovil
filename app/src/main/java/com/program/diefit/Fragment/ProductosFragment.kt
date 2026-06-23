package com.program.diefit.Fragment

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
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.program.diefit.ProductoRepository
import com.program.diefit.R
import com.program.diefit.entities.Producto

class ProductosFragment : Fragment() {

    private lateinit var buscarInput: TextInputEditText
    private lateinit var contenedorProductos: LinearLayout
    private lateinit var btnNuevoProducto: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_productos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buscarInput = view.findViewById(R.id.buscarInput)
        contenedorProductos = view.findViewById(R.id.contenedorProductos)
        btnNuevoProducto = view.findViewById(R.id.btnNuevoProducto)

        renderProductos(ProductoRepository.obtenerTodos())

        buscarInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                renderProductos(ProductoRepository.buscar(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnNuevoProducto.setOnClickListener {
            findNavController().navigate(R.id.action_productos_to_nuevoProducto)
        }
    }

    override fun onResume() {
        super.onResume()
        renderProductos(ProductoRepository.buscar(buscarInput.text.toString()))
    }

    private fun renderProductos(lista: List<Producto>) {
        contenedorProductos.removeAllViews()

        if (lista.isEmpty()) {
            val tv = TextView(requireContext()).apply {
                text = "No hay productos"
                setPadding(0, 24, 0, 24)
                textSize = 14f
            }
            contenedorProductos.addView(tv)
            return
        }

        lista.forEach { producto ->
            val card = MaterialCardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 24 }
                radius = 12f * resources.displayMetrics.density
                cardElevation = 0f
            }

            val inner = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                val pad = (16 * resources.displayMetrics.density).toInt()
                setPadding(pad, pad, pad, pad)
            }

            val tvNombre = TextView(requireContext()).apply {
                text = producto.nombre
                textSize = 16f
                setTypeface(typeface, Typeface.BOLD)
            }

            val tvDetalle = TextView(requireContext()).apply {
                text = "${producto.cantidad} ${producto.unidad} · ${producto.calorias} kcal · Proteínas: ${producto.proteinas} g"
                textSize = 13f
                setPadding(0, 8, 0, 0)
            }

            inner.addView(tvNombre)
            inner.addView(tvDetalle)
            card.addView(inner)

            card.setOnLongClickListener {
                ProductoRepository.eliminar(producto)
                renderProductos(ProductoRepository.buscar(buscarInput.text.toString()))
                Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show()
                true
            }

            contenedorProductos.addView(card)
        }
    }
}