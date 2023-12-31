package com.example.practica_1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlin.random.Random


class Calculatron : AppCompatActivity() {
    var time = 10000
    var interval = 100
    var turn = true
    var present_answer = ""
    var present_operation = ""
    var next_answer = ""
    var next_operation = ""
    var user_answer = ""
    var rights = 0
    var wrongs = 0
    var total_rights = 0
    var total_wrongs = 0
    var max = 10
    var min = 0
    lateinit var operands : MutableList<String>
    lateinit var main : ConstraintLayout
    lateinit var rights_textView : TextView
    lateinit var wrongs_textView : TextView
    lateinit var present_operation_textView : TextView
    lateinit var present_answer_textView : TextView
    lateinit var c : TextView
    lateinit var ce : TextView
    lateinit var equals : TextView
    lateinit var next_operation_textView : TextView
    lateinit var next_result_textView: TextView
    lateinit var past_operation_textView : TextView
    lateinit var past_result_textView: TextView
    lateinit var progressbar : ProgressBar
    lateinit var restart : Button
    lateinit var rights_results : TextView
    lateinit var wrongs_results : TextView
    lateinit var total_rights_results : TextView
    lateinit var total_wrongs_results : TextView
    lateinit var shared_preferences : SharedPreferences
    lateinit var game_over_constraintLayout : ConstraintLayout
    lateinit var configuration_imageView : ImageView

    fun start_timer()
    {
        val mCountDownTimer: CountDownTimer
        var i = 0

        progressbar.progress = i
//        time = get_shared_preference("time")!!.toInt()

        mCountDownTimer = object : CountDownTimer(time.toLong(), interval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
//                Log.v("Log_tag", "time: $i$millisUntilFinished")
                i++
                progressbar.progress = i as Int * 100 / (time / interval)
            }

            override fun onFinish() {
                i++
                progressbar.progress = 100
                game_over()
            }
        }
        mCountDownTimer.start()
    }

    fun game_over()
    {
        turn = false
        game_over_menu()

    }

    fun game_over_menu()
    {

        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.calculatron_finish, null)

        main.addView(layout)

        restart = findViewById(R.id.restart_button)
        rights_results = findViewById(R.id.right_results_textView)
        wrongs_results = findViewById(R.id.wrong_results_textView)

        total_rights_results = findViewById(R.id.total_right_results_textView)
        total_wrongs_results = findViewById(R.id.total_wrong_results_textView)

        total_rights_results.text = "Total acertadas: $total_rights"
        total_wrongs_results.text = "Total falladas: $total_wrongs"

        rights_results.text = "Preguntas acertadas: $rights"
        wrongs_results.text = "Preguntas falladas: $wrongs"

        restart.setOnClickListener {
            recreate()
        }
    }


    fun insert_number(n: String)
{
        if(user_answer.length <= 3 && turn)
        {
            user_answer += n
        }
        present_answer_textView.text = user_answer

    }

    fun generate_operation(next : Boolean = false)
    {
        var answer = ""
        var n1 = Random.nextInt(min, max)
        var n2 = Random.nextInt(min,max)
        var operators = mutableListOf<String>()
        if(get_shared_preference("add", "boolean", true) as Boolean)
        {
            operators.add("+")
        }

        if(get_shared_preference("multiply", "boolean", true) as Boolean)
        {
            operators.add("*")
        }

        if(get_shared_preference("subtract", "boolean", true) as Boolean)
        {
            operators.add("-")
        }

        if(get_shared_preference("divide", "boolean", true) as Boolean)
        {
            operators.add("/")
        }

        Log.v("operators", operators.toString())

        var operator = operators[Random.nextInt(0,operators.size)]
        when(operator){
            "*" -> { answer = (n1 * n2).toString()}
            "+" -> { answer = (n1 + n2).toString()}
            "-" -> {
                if(n1 >= n2)
                {
                    answer = (n1 - n2).toString()
                }else{
                    operator = "*"
                    answer = (n1 * n2).toString()
                }
            }
            "/" ->{
                if(n1 % n2 == 0 && n2 != 0)
                {
                    Log.v("operation: ", "$n1 $operator $n2")
                    answer = (n1 / n2).toString()
                }else{
                    operator = "*"
                    answer = (n1 * n2).toString()
                }
            }
        }

        if(next)
        {
            next_answer = answer
            next_operation = "$n1 $operator $n2 ="
            next_operation_textView.text = next_operation

        }else{
            present_answer = answer
            present_operation = "$n1 $operator $n2 ="
            present_operation_textView.text = present_operation
        }
    }

    fun check_answer() : Boolean
    {
        Log.v("real_answer", present_answer)
        Log.v("user_answer", user_answer)
        if(present_answer.equals(user_answer))
        {
            rights++
            total_rights++
            edit_shared_preference("rights", total_rights, "int")
            rights_textView.text = rights.toString()
            return true
        }else{
            wrongs++
            total_wrongs ++
            edit_shared_preference("wrongs", total_wrongs, "int")
            wrongs_textView.text = wrongs.toString()
            return false
        }

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

    fun mark_past_answer(r: String)
    {
        if(r.equals("success"))
        {
            past_operation_textView.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_green))
        }else{
            past_operation_textView.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_red))
        }

    }

    fun reset_answer()
    {
        user_answer = ""
        present_answer_textView.text = "0"
    }

    fun remove_last_digit_answer()
    {
        user_answer = user_answer.dropLast(1)
        if(user_answer.equals(""))
        {
            present_answer_textView.text = "0"
        }else{
            present_answer_textView.text = user_answer
        }

    }

    fun cycle_operations()
    {
        past_operation_textView.visibility = View.VISIBLE
        past_result_textView.visibility = View.VISIBLE

        Log.v("present", present_operation.toString())
        Log.v("present_answer", present_answer.toString())

        past_operation_textView.text = present_operation
        past_result_textView.text = user_answer

        if(check_answer())
        {
            mark_past_answer("success")
        }else{
            mark_past_answer("")
        }

        reset_answer()

        present_operation = next_operation
        present_operation_textView.text = present_operation
        present_answer = next_answer


        generate_operation(true) //CREANDO LA SIGUIENTE OPERACIÓN

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calculatron)

        c = findViewById(R.id.c)
        ce = findViewById(R.id.ce)
        equals = findViewById(R.id.equals)
        present_operation_textView = findViewById(R.id.present_operation)
        present_answer_textView = findViewById(R.id.present_result)
        next_operation_textView = findViewById(R.id.next_operation)
        next_result_textView = findViewById(R.id.next_result)
        past_operation_textView = findViewById(R.id.past_operation)
        past_result_textView = findViewById(R.id.past_result)
        rights_textView = findViewById(R.id.rights)
        wrongs_textView = findViewById(R.id.wrongs)
        progressbar = findViewById(R.id.progressbar)
        main = findViewById(R.id.main)
        configuration_imageView = findViewById(R.id.configuration_imageView)

//        shared_preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
//        shared_preferences = getSharedPreferences("shared_prefs.xml", 0)
//        shared_preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
//        shared_preferences = this.getPreferences( 0);
//        Log.v("shared", shared_preferences.toString())
//        shared_preferences = this.getSharedPreferences("shared_prefs", MODE_PRIVATE)

        shared_preferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

        total_rights = get_shared_preference("rights", "int", 0) as Int
        total_wrongs = get_shared_preference("wrongs", "int", 0) as Int
        time = get_shared_preference("time_left", "int", 30000) as Int
        max = get_shared_preference("max", "int", 10) as Int
        min = get_shared_preference("min", "int", 0) as Int

        Log.v("max", max.toString())
        Log.v("min", min.toString())
        val elementIds = arrayOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight","nine")

        for (elementId in elementIds) {
            val element = findViewById<TextView>(resources.getIdentifier(elementId, "id", packageName))

            element.setOnClickListener {
                insert_number(element.text.toString())
                Log.v("clicked:", element.text.toString())
            }
        }

        configuration_imageView.setOnClickListener {
            val intent = Intent(this, Configuration::class.java)
            startActivity(intent)
        }

        ce.setOnClickListener {
            if(turn)
            {
                reset_answer()
            }
        }

        c.setOnClickListener {
            if(turn) {
                remove_last_digit_answer()
            }
        }

        equals.setOnClickListener {
            if(turn)
            {
                cycle_operations()
            }
        }

        past_operation_textView.visibility = View.INVISIBLE
        past_result_textView.visibility = View.INVISIBLE

        generate_operation()
        generate_operation(true)

        start_timer()
    }



}