package com.example.talleriot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ControlActivity : AppCompatActivity() {

    private lateinit var switchLed: Switch
    private lateinit var txtLedStatus: TextView
    private lateinit var txtTemp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        // Referencias visuales
        switchLed = findViewById(R.id.switchLed)
        txtLedStatus = findViewById(R.id.txtLedStatus)
        txtTemp = findViewById(R.id.txtTemp)

        // Referencias Firebase
        val ledRef = FirebaseDatabase.getInstance().getReference("iot_device/led")
        val tempRef = FirebaseDatabase.getInstance().getReference("iot_device/temperature")

        // Leer el valor inicial del LED desde Firebase
        ledRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val estado = snapshot.getValue(Boolean::class.java) ?: false
                switchLed.isChecked = estado
                txtLedStatus.text = "LED: ${if (estado) "Encendido" else "Apagado"}"
            }

            override fun onCancelled(error: DatabaseError) {
                txtLedStatus.text = "LED: Error de lectura"
            }
        })

        // Enviar nuevo valor cuando se activa/desactiva el switch
        switchLed.setOnCheckedChangeListener { _, isChecked ->
            txtLedStatus.text = "LED: ${if (isChecked) "Encendido" else "Apagado"}"
            ledRef.setValue(isChecked)
        }

        // Leer temperatura en tiempo real
        tempRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.getValue(Double::class.java)
                txtTemp.text = "Temperatura: ${temp ?: "--"} °C"
            }

            override fun onCancelled(error: DatabaseError) {
                txtTemp.text = "Error al leer temperatura"
            }
        })

        // Botón para ir a Info
        findViewById<Button>(R.id.btnIrInfo).setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

        // Botón actualizar
        findViewById<Button>(R.id.btnActualizar).setOnClickListener {
            tempRef.get().addOnSuccessListener {
                val temp = it.getValue(Double::class.java)
                txtTemp.text = "Temperatura: ${temp ?: "--"} °C"
            }
        }
    }
}
