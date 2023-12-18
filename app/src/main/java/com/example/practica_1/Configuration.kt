package com.example.practica_1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Configuration : AppCompatActivity() {

    var min : Int = 0
    var max : Int = 10
    var time : Int = 20000
    var errors = mutableListOf(true,true,true)
    lateinit var shared_preferences : SharedPreferences
    lateinit var checkbox : LinearLayout
    lateinit var timeEdit : TextInputEditText
    lateinit var timeLayout : TextInputLayout
    lateinit var minEdit : TextInputEditText
    lateinit var minLayout : TextInputLayout
    lateinit var maxEdit : TextInputEditText
    lateinit var maxLayout : TextInputLayout
    lateinit var save : Button
    lateinit var checkboxAdd : CheckBox
    lateinit var checkboxSubtract : CheckBox
    lateinit var checkboxMultiply : CheckBox
    lateinit var checkboxDivide : CheckBox

    fun get_shared_preference(key: String, type: String, default : Any) : Any?
    {
        var res = when(type)
        {
            "int" -> shared_preferences.getInt(key, default as Int)
            "boolean" -> shared_preferences.getBoolean(key, default as Boolean)
            "double" -> shared_preferences.getFloat(key,default as Float)
            else -> shared_preferences.getString(key, default as String)

        }
        Log.v("get", "${key}: ${res.toString()}")

        return res
    }

    fun decorate_error(view: View)
    {
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.wrong_red))
    }

    fun decorate_success(view: View)
    {
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.dark_grey))
    }

    fun edit_shared_preference(key: String, value : Any, type: String)
    {
        with(shared_preferences.edit()){
            when(type)
            {
                "int" -> putInt(key, value as Int)
                "boolean" -> putBoolean(key, value as Boolean)
                "double" -> putFloat(key, value as Float)
                else -> putString(key, value as String)

            }
            commit()
        }
    }

    fun validate(regex : String, element : TextInputEditText, index : Int) : Boolean
    {
        var aux  = element?.text.toString()

        if(aux.isBlank() || aux.isEmpty())
        {
            element.error = "Este campo es necesario"
            decorate_error(element)
            errors[index] = true
            return false
        }else if (!aux.matches(Regex(regex)))
        {
            element.error = "Deber ser un número entero"
            decorate_error(element)
            errors[index] = true
            return false
        }else{
            element.error = null
            errors[index] = false
            decorate_success(element)
            return true
        }
    }

    fun checkbox_reader(element: CheckBox, key : String)
    {
        edit_shared_preference(key, element.isChecked, "boolean")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuration)

        checkbox = findViewById<LinearLayout>(R.id.checkbox)
//        shared_preferences = this.getPreferences( 0);
//        shared_preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
//        shared_preferences = this.getPreferences(getString(R.string))

        shared_preferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

        timeEdit = findViewById(R.id.timeEdit)
        minEdit = findViewById(R.id.minEdit)
        maxEdit = findViewById(R.id.maxEdit)

        maxLayout = findViewById<TextInputLayout>(R.id.maxLayout)
        minLayout = findViewById<TextInputLayout>(R.id.minLayout)
        timeLayout = findViewById<TextInputLayout>(R.id.time)

        checkboxAdd = findViewById<CheckBox>(R.id.checkbox_add)
        checkboxSubtract = findViewById<CheckBox>(R.id.checkbox_subtract)
        checkboxMultiply = findViewById<CheckBox>(R.id.checkbox_multiply)
        checkboxDivide = findViewById<CheckBox>(R.id.checkbox_divide)

        save = findViewById(R.id.save)

        min = get_shared_preference("min", "int", 0) as Int
        max = get_shared_preference("max", "int", 10) as Int
        time = get_shared_preference("time_left", "int", 20000) as Int

        checkboxAdd.isChecked = get_shared_preference("add", "boolean", true) as Boolean
        checkboxMultiply.isChecked = get_shared_preference("multiply", "boolean", true) as Boolean
        checkboxSubtract.isChecked = get_shared_preference("subtract", "boolean", true) as Boolean
        checkboxDivide.isChecked = get_shared_preference("divide", "boolean", true) as Boolean

        timeEdit.setText(time.toString())
        maxEdit.setText(max.toString())
        minEdit.setText(min.toString())

        timeEdit.addTextChangedListener {
            if(validate("^[0-9]{0,8}\$", timeEdit, 0))
            {
                time = timeEdit.text.toString().toInt()
                Log.v("time", time.toString())
            }
        }

        minEdit.addTextChangedListener {
            if(validate("^[0-9]{0,3}\$", minEdit, 1))
            {
                min = minEdit.text.toString().toInt()
                Log.v("min", min.toString())
            }
        }

        maxEdit.addTextChangedListener {
            if(validate("^[0-9]{0,3}\$", maxEdit, 2))
            {
                max = maxEdit.text.toString().toInt()
                Log.v("max", max.toString())
            }
        }

        checkboxAdd.setOnCheckedChangeListener { buttonView, isChecked ->
            checkbox_reader(checkboxAdd, "add")
        }

        checkboxSubtract.setOnCheckedChangeListener { buttonView, isChecked ->
            checkbox_reader(checkboxSubtract, "subtract")
        }

        checkboxMultiply.setOnCheckedChangeListener { buttonView, isChecked ->
            checkbox_reader(checkboxMultiply, "multiply")
        }

        checkboxDivide.setOnCheckedChangeListener { buttonView, isChecked ->
            checkbox_reader(checkboxDivide, "divide")
        }



        save.setOnClickListener {
            Log.v("errors", errors.toString())
            validate("^[0-9]{1,3}\$", minEdit, 1)
            validate("^[0-9]{1,3}\$", maxEdit, 2)
            validate("^[0-9]{1,8}\$", timeEdit, 0)

            if(true !in errors && max >= min)
            {
                edit_shared_preference("max", max, "int")
                edit_shared_preference("min", min, "int")
                edit_shared_preference("time_left", time, "int")

                Log.v("preference", "max: ${get_shared_preference("max", "int", 20)}")
                Log.v("preference", "min: ${get_shared_preference("min", "int", 0)}")
                Log.v("preference", "time: ${get_shared_preference("time_left", "int", 20000)}")
                val intent = Intent(this, Calculatron::class.java)
                startActivity(intent)
            }

        }







    }
}