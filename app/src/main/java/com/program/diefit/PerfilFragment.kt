package com.program.diefit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PerfilFragment : Fragment() {

    private lateinit var tvNombrePerfil: TextView
    private lateinit var tvEmailPerfil: TextView

    private lateinit var nombreLayout: TextInputLayout
    private lateinit var pesoLayout: TextInputLayout
    private lateinit var tallaLayout: TextInputLayout
    private lateinit var nombreInput: TextInputEditText
    private lateinit var edadInput: TextInputEditText
    private lateinit var pesoInput: TextInputEditText
    private lateinit var tallaInput: TextInputEditText
    private lateinit var metaInput: TextInputEditText
    private lateinit var chipGenero: ChipGroup
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnLogout: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_perfil, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNombrePerfil = view.findViewById<TextView>(R.id.tvNombrePerfil)
        tvEmailPerfil = view.findViewById<TextView>(R.id.tvEmailPerfil)

        nombreLayout = view.findViewById(R.id.nombreLayout)
        pesoLayout   = view.findViewById(R.id.pesoLayout)
        tallaLayout  = view.findViewById(R.id.tallaLayout)
        nombreInput  = view.findViewById(R.id.nombreInput)
        edadInput    = view.findViewById(R.id.edadInput)
        pesoInput    = view.findViewById(R.id.pesoInput)
        tallaInput   = view.findViewById(R.id.tallaInput)
        metaInput    = view.findViewById(R.id.metaInput)
        chipGenero   = view.findViewById(R.id.chipGenero)
        btnGuardar   = view.findViewById(R.id.btnGuardarPerfil)
        btnLogout    = view.findViewById(R.id.btnLogout)

        UserRepository.usuarioActual?.let { user ->

            tvNombrePerfil.text = user.nombre
            tvEmailPerfil.text = user.email

            nombreInput.setText(user.nombre)
            edadInput.setText(if (user.edad != "—") user.edad else "")
            pesoInput.setText(user.peso)
            tallaInput.setText(user.talla)
            metaInput.setText(if (user.meta != "—") user.meta else "")
        }

        btnGuardar.setOnClickListener {
            if (validar()) guardarPerfil()
        }

        btnLogout.setOnClickListener {
            UserRepository.cerrarSesion()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun validar(): Boolean {
        var ok = true
        nombreLayout.error = null
        pesoLayout.error   = null
        tallaLayout.error  = null

        if (nombreInput.text.isNullOrEmpty()) {
            nombreLayout.error = "Ingresa tu nombre"
            ok = false
        }
        if (pesoInput.text.isNullOrEmpty()) {
            pesoLayout.error = "Ingresa tu peso"
            ok = false
        }
        if (tallaInput.text.isNullOrEmpty()) {
            tallaLayout.error = "Ingresa tu talla"
            ok = false
        }
        return ok
    }

    private fun guardarPerfil() {
        val nombre = nombreInput.text.toString()
        val edad   = edadInput.text.toString().ifEmpty { "—" }
        val peso   = pesoInput.text.toString()
        val talla  = tallaInput.text.toString()
        val meta   = metaInput.text.toString().ifEmpty { "—" }

        val genero = when (chipGenero.checkedChipId) {
            R.id.chipMasculino -> "Masculino"
            R.id.chipFemenino  -> "Femenino"
            R.id.chipOtro      -> "Otro"
            else               -> "Sin especificar"
        }

        UserRepository.usuarioActual?.let { user ->

            tvNombrePerfil.text = user.nombre

            user.nombre = nombre
            user.edad   = edad
            user.peso   = peso
            user.talla  = talla
            user.meta   = meta
            user.genero = genero
        }

        UserRepository.actualizarUsuarioActual()

        Toast.makeText(
            requireContext(),
            "$nombre · $peso kg · $talla cm · Meta: $meta kg · $genero",
            Toast.LENGTH_LONG
        ).show()
    }
}