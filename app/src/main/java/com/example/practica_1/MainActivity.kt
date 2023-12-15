package com.example.practica_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val juego1 = findViewById<CardView>(R.id.memory)
        val juego2 = findViewById<CardView>(R.id.calculate)

        juego1.setOnClickListener{
            val intent = Intent(this, Memory::class.java)
            startActivity(intent)
        }

        juego2.setOnClickListener{
            val intent = Intent(this, Calculatron::class.java)
            startActivity(intent)
        }
    }


}
