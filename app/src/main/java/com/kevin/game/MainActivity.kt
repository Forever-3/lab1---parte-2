package com.kevin.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Tomando los elementos del layour
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val buttonNext = findViewById<Button>(R.id.buttonNext)

        // Configurar botón para enviar el nombre
        buttonNext.setOnClickListener {
            val playerName = editTextName.text.toString()

            // Crear un Intent para pasar a la actividad ajugar
            val intent = Intent(this, ajugar::class.java)

            // Añadir el nombre del jugador al Intent
            intent.putExtra("PLAYER_NAME", playerName)

            // Iniciar la actividad ajugar
            startActivity(intent)
        }
    }
}
