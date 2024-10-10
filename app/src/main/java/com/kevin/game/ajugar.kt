package com.kevin.game

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class ajugar : AppCompatActivity() {

    private var puntos = 0
    private lateinit var imagenes: IntArray
    private var imagenABuscar: Int = 0
    private lateinit var timer: CountDownTimer
    private val imagenesSeleccionadas = mutableSetOf<Int>() // Conjunto para almacenar imágenes ya seleccionadas
    private var contadorSeleccionadas = 0 // Contador para imágenes seleccionadas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajugar)

        // captura el nombre del player desde el intent (local storage) xd
        val nombreJugador = intent.getStringExtra("PLAYER_NAME")

        // Mostrar el nombre del jugador en el TextView
        val textViewJugador = findViewById<TextView>(R.id.textViewJugador)
        textViewJugador.text = nombreJugador

        // Inicializar el vector con las imágenes del drawable
        imagenes = intArrayOf(
            R.drawable.dino, R.drawable.patricio,
            R.drawable.pooh, R.drawable.nohabianadamejor,
            R.drawable.raios, R.drawable.barnie
        )

        iniciarJuego()

        // Iniciar el temporizador
        iniciarTemporizador()
    }

    private fun iniciarJuego() {
        // Elegir una imagen aleatoria como la imagen a buscar
        imagenABuscar = imagenes.random()

        // Mostrar la imagen a buscar en el ImageView correspondiente
        val imageViewBuscar = findViewById<ImageView>(R.id.imageViewBuscar)
        imageViewBuscar.setImageResource(imagenABuscar)

        // Inicializar el grid de imágenes
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

        // Limpiar el GridLayout
        gridLayout.removeAllViews()

        // Crear 16 ImageViews dinámicamente en un GridLayout
        for (i in 0 until 16) {
            // Crear un nuevo ImageView para cada celda
            val imageView = ImageView(this)
            imageView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 200 // Ajusta el tamaño de las imágenes según el diseño deseado
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }

            // Asignar la imagen por defecto (react) a cada ImageView
            imageView.setImageResource(R.drawable.react)

            // Asignar el evento de clic a cada imagen
            imageView.setOnClickListener {
                // Verificar si la imagen ya ha sido seleccionada
                if (imageView.tag == null) {
                    // Seleccionar una imagen aleatoria del array de imágenes
                    val imagenSeleccionada = imagenes.random()
                    imageView.setImageResource(imagenSeleccionada)

                    // Asignar la imagen seleccionada a la etiqueta (tag) del ImageView
                    imageView.tag = imagenSeleccionada

                    // Verificar si la imagen seleccionada es la que el jugador estaba buscando
                    if (imagenSeleccionada == imagenABuscar) {
                        actualizarPuntos(100)
                    } else {
                        actualizarPuntos(-10)
                    }

                    // Deshabilitar el clic en la imagen después de seleccionar
                    imageView.isClickable = false

                    // Incrementar el contador de imágenes seleccionadas
                    contadorSeleccionadas++

                    // Verificar si todas las imágenes han sido seleccionadas
                    if (contadorSeleccionadas == 16) {
                        finalizarJuego() // Llama a finalizar el juego si se seleccionan todas las imágenes
                    }
                }
            }

            // Añadir el ImageView al GridLayout
            gridLayout.addView(imageView)
        }
    }

    private fun iniciarTemporizador() {
        timer = object : CountDownTimer(40000, 1000) { // 40 segundos
            override fun onTick(millisUntilFinished: Long) {
                val textViewTiempo = findViewById<TextView>(R.id.textViewTiempo)
                val segundosRestantes = (millisUntilFinished / 1000).toInt()
                val minutos = segundosRestantes / 60
                val segundos = segundosRestantes % 60
                textViewTiempo.text = String.format("%02d:%02d", minutos, segundos)
            }

            override fun onFinish() {
                finalizarJuego() // Llama a finalizar el juego cuando se acabe el tiempo
            }
        }.start()
    }

    private fun finalizarJuego() {
        // Detener el temporizador
        timer.cancel()

        // Mostrar el diálogo con el puntaje final
        mostrarDialogoPuntajeFinal()
    }

    private fun mostrarDialogoPuntajeFinal() {
        // Crear un AlertDialog para mostrar el puntaje final
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Fin del juego!")
        builder.setMessage("Tu puntaje final es: $puntos")

        // Botón para volver a jugar
        builder.setPositiveButton("Volver a jugar") { dialog, _ ->
            // Reiniciar el juego
            puntos = 0
            contadorSeleccionadas = 0 // Reiniciar el contador
            iniciarJuego()
            dialog.dismiss()
            iniciarTemporizador()
        }

        // Botón para salir
        builder.setNegativeButton("Salir") { dialog, _ ->
            dialog.dismiss()
            finish() // Cierra la actividad y vuelve a main activity
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Función para actualizar y mostrar los puntos
    private fun actualizarPuntos(cambio: Int) {
        puntos += cambio
        val textViewPuntos = findViewById<TextView>(R.id.textViewPuntos)
        textViewPuntos.text = puntos.toString()
    }
}
