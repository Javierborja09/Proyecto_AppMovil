package com.program.diefit.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GroqService {

    private const val API_KEY = "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"
    private const val GROQ_URL = "https://api.groq.com/openai/v1/chat/completions"
    private const val TAG = "GroqService"

    suspend fun obtenerMensajeMotivacional(): String = withContext(Dispatchers.IO) {
        try {
            val url = URL(GROQ_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $API_KEY")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            // Construcción del JSON de manera limpia y segura
            val jsonBody = JSONObject()
            jsonBody.put("model", "llama-3.1-8b-instant")

            val messagesArray = JSONArray()

            val systemMessage = JSONObject()
            systemMessage.put("role", "system")
            systemMessage.put("content", "Eres un coach de salud y fitness. Genera una frase motivacional muy corta (máximo 15 palabras) en español para alguien que está cuidando su peso y haciendo ejercicio. Sé energético y positivo.")
            messagesArray.put(systemMessage)

            val userMessage = JSONObject()
            userMessage.put("role", "user")
            userMessage.put("content", "Dame mi frase motivacional del día.")
            messagesArray.put(userMessage)

            jsonBody.put("messages", messagesArray)
            jsonBody.put("temperature", 0.7)

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(jsonBody.toString())
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            if (responseCode == 200) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)
                val choices = jsonResponse.getJSONArray("choices")
                val messageContent = choices.getJSONObject(0).getJSONObject("message").getString("content")

                messageContent.replace("\"", "").trim()
            } else {
                val errorResponse = conn.errorStream?.bufferedReader()?.use { it.readText() }
                Log.e(TAG, "Error de Groq API (Código $responseCode): $errorResponse")

                "¡Hoy es un excelente día para superar tus metas!"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción en GroqService", e)
            "Mantente enfocado en tus objetivos, ¡tú puedes!"
        }
    }
}