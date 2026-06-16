package com.program.diefit

import android.app.Application
import com.program.diefit.data.ProductoStorage

class DiefitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        UserStorage.init(this)
        UserRepository.cargarDesdeStorage()

        ProductoStorage.init(this)
        ProductoRepository.cargarDesdeStorage()

        RutinaStorage.init(this)
        RutinaRepository.cargarDesdeStorage()

        RegistroComidaStorage.init(this)
        RegistroComidaRepository.cargarDesdeStorage()

        RegistroRutinaStorage.init(this)
        RegistroRutinaRepository.cargarDesdeStorage()
    }
}