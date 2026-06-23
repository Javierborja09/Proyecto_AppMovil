package com.program.diefit.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.program.diefit.R
import com.program.diefit.RegistroRutinaRepository


class HistorialRutinasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_historial_rutinas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvRacha = view.findViewById<TextView>(R.id.tvRacha)
        val lottieRacha = view.findViewById<LottieAnimationView>(R.id.lottieRacha)

        val rachaActual = RegistroRutinaRepository.calcularRacha()

        tvRacha.text = rachaActual.toString()

        if (rachaActual == 0) {
            lottieRacha.setAnimation(R.raw.racha_triste)
        } else {
            lottieRacha.setAnimation(R.raw.racha_saltarin)
        }

        lottieRacha.playAnimation()
    }
}