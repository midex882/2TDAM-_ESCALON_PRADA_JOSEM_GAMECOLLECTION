package com.example.practica_1

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class Configuration : AppCompatActivity() {

    var min : Int = 0
    var max : Int = 10
    var time : Int = 20000
    lateinit var shared_preferences : SharedPreferences
    lateinit var checkbox : LinearLayout
    lateinit var time_textView : TextInputEditText
    lateinit var min_textView : TextInputEditText
    lateinit var max_textView : TextInputEditText

    fun get_shared_preference(key: String) : String?
    {
        return shared_preferences.getString(key, "0")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuration)

        checkbox = findViewById<LinearLayout>(R.id.checkbox)
        shared_preferences = this.getSharedPreferences(
            "com.example.practica_1", 0);
        time_textView = findViewById(R.id.timeEdit)
        min_textView = findViewById(R.id.minEdit)
        max_textView = findViewById(R.id.maxEdit)

        min = get_shared_preference("min")?.toInt() ?: 0
        max = get_shared_preference("max")?.toInt() ?: 10
        time = get_shared_preference("time")?.toInt() ?: 20000

        time_textView.setText(time.toString())
        max_textView.setText(max.toString())
        min_textView.setText(min.toString())



    }
}