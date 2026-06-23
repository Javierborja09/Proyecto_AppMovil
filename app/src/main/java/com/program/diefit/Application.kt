package com.program.diefit

import android.app.Application
import com.program.diefit.data.SessionStorage

class DiefitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppProvider.init(this)
        SessionStorage.init(this)

        UserRepository.init(this)
        UserRepository.cargarDesdeStorage()

        ProductoRepository.init(this)
        ProductoRepository.cargarDesdeStorage()

        RutinaRepository.init(this)
        RutinaRepository.cargarDesdeStorage()

        RegistroComidaRepository.init(this)
        RegistroRutinaRepository.init(this)
    }
}