package com.example.talleriot

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var txtStatus: TextView
    private lateinit var btnToggle: Button
    private lateinit var btnGoToControl: Button
    private var connected = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        txtStatus = findViewById(R.id.txtStatus)
        btnToggle = findViewById(R.id.btnToggleConnection)
        btnGoToControl = findViewById(R.id.btnGoToControl)

        // Estado inicial
        txtStatus.text = "Estado: Desconectado"
        btnToggle.text = "Conectar"
        btnGoToControl.isEnabled = false // Desactivado al inicio

        // Botón de conexión
        btnToggle.setOnClickListener {
            connected = !connected
            txtStatus.text = "Estado: ${if (connected) "Conectado" else "Desconectado"}"
            btnToggle.text = if (connected) "Desconectar" else "Conectar"
            btnGoToControl.isEnabled = connected
        }

        // Ir a ControlActivity solo si está conectado
        btnGoToControl.setOnClickListener {
            if (connected) {
                startActivity(Intent(this, ControlActivity::class.java))
            } else {
                Toast.makeText(this, "Debes estar conectado para continuar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
