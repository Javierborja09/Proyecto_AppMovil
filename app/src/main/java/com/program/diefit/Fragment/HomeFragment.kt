package com.program.diefit.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.program.diefit.R
import com.program.diefit.RegistroComidaRepository
import com.program.diefit.UserRepository
import com.program.diefit.services.GroqService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName       = view.findViewById<TextView>(R.id.tvName)
        val tvPeso       = view.findViewById<TextView>(R.id.tvPeso)
        val tvMeta       = view.findViewById<TextView>(R.id.tvMeta)
        val tvCalorias   = view.findViewById<TextView>(R.id.tvCalorias)
        val tvMotivacion = view.findViewById<TextView>(R.id.tvMotivacion)

        val user = UserRepository.usuarioActual

        if (user != null) {
            tvName.text = user.nombre
            tvPeso.text = if (user.peso.isNotEmpty()) "${user.peso} kg" else "—"
            tvMeta.text = if (user.meta.isNotEmpty()) "${user.meta} kg" else "—"
        }

        cargarCaloriasHoy(tvCalorias)

        viewLifecycleOwner.lifecycleScope.launch {
            tvMotivacion.text = "Cargando tu motivación..."
            val fraseIA = GroqService.obtenerMensajeMotivacional()
            tvMotivacion.text = fraseIA
        }
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<TextView>(R.id.tvCalorias)?.let { cargarCaloriasHoy(it) }
    }

    private fun cargarCaloriasHoy(tvCalorias: TextView) {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val hoy = formatoFecha.format(Calendar.getInstance().time)

        val totalCalorias = RegistroComidaRepository.totalCaloriasPorFecha(hoy)
        tvCalorias.text = "${"%,d".format(totalCalorias)} kcal"
    }
}