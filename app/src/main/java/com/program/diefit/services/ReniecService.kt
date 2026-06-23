package com.program.diefit.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ReniecService {

    private const val API_URL = "https://apiperu.dev/api/dni"
    private const val TOKEN = "a9b1cdfd20e05e0b8962177817ba6ad976781a5df3741bf4362a444bc29dc7c2"
    private const val TAG = "ReniecService"

    data class PersonaReniec(val nombres: String, val apellidos: String)

    suspend fun consultarDni(dni: String): PersonaReniec? = withContext(Dispatchers.IO) {
        try {
            val url = URL(API_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $TOKEN")
            conn.doOutput = true

            val jsonBody = JSONObject().apply {
                put("dni", dni)
            }

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(jsonBody.toString())
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            if (responseCode == 200) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)

                if (jsonResponse.optBoolean("success", false)) {
                    val data = jsonResponse.getJSONObject("data")
                    val nombres = data.getString("nombres")
                    val paterno = data.getString("apellido_paterno")
                    val materno = data.getString("apellido_materno")

                    PersonaReniec(
                        nombres = nombres,
                        apellidos = "$paterno $materno".trim()
                    )
                } else {
                    Log.e(TAG, "API Perú devolvió success=false")
                    null
                }
            } else {
                val errorResponse = conn.errorStream?.bufferedReader()?.use { it.readText() }
                Log.e(TAG, "Error HTTP $responseCode: $errorResponse")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al consultar DNI", e)
            null
        }
    }
}