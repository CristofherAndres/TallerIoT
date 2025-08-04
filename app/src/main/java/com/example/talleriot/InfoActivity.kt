package com.example.talleriot

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class InfoActivity : AppCompatActivity() {

    private lateinit var txtUltimoComando: TextView
    private lateinit var txtUltimaTemp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        txtUltimoComando = findViewById(R.id.txtUltimoComando)
        txtUltimaTemp = findViewById(R.id.txtUltimaTemp)

        val database = FirebaseDatabase.getInstance()
        val ledRef = database.getReference("iot_device/led")
        val tempRef = database.getReference("iot_device/temperature")

        // Leer último estado del LED
        ledRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val estado = snapshot.getValue(Boolean::class.java)
                val comando = if (estado == true) "Encender LED" else "Apagar LED"
                txtUltimoComando.text = "Último comando: $comando"
            }

            override fun onCancelled(error: DatabaseError) {
                txtUltimoComando.text = "Último comando: Error al leer"
            }
        })

        // Leer última temperatura
        tempRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.getValue(Double::class.java)
                txtUltimaTemp.text = "Última lectura: ${temp ?: "--"} °C"
            }

            override fun onCancelled(error: DatabaseError) {
                txtUltimaTemp.text = "Última lectura: Error al leer"
            }
        })

        // Botón volver
        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
}
