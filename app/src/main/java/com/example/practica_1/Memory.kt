package com.example.practica_1

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class Memory : AppCompatActivity() {

    var turno = true
    lateinit var livesContainer: LinearLayout
    var cartas = mutableListOf<Carta>()
    var cartas_levantadas = mutableListOf<Carta>()
    var lives = 7
    var cartas_imagenes_final = mutableListOf<Int>()
    var parejas = 0
    lateinit var board: LinearLayout
    lateinit var main : ConstraintLayout
    lateinit var restart : Button
    lateinit var mediaPlayer : MediaPlayer

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }


    fun play_sound(sound : Int )
    {
        mediaPlayer = MediaPlayer.create(this, sound)

        mediaPlayer?.start()
    }

    fun menu_muerte()
    {

        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.defeat, null)

        main.addView(layout)
        restart = findViewById(R.id.defeat_button)

        restart.setOnClickListener {
            recreate()
        }
    }

    fun menu_victoria()
    {

        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.victory, null)

        main.addView(layout)
        restart = findViewById(R.id.defeat_button)

        restart.setOnClickListener {
            recreate()
        }
    }

    fun muerte()
    {
        if(livesContainer.childCount == 0)
        {
            turno = false
            menu_muerte()
        }else{
            turno = true
        }
    }

    fun victoria()
    {
        if(parejas == 6)
        {
            turno = false
            menu_victoria()
        }
    }

    fun check()
    {
        if(cartas_levantadas[0].img != cartas_levantadas[1].img)
        {
            Handler(Looper.getMainLooper()).postDelayed({
                cartas_levantadas[0].unflip(cartas_levantadas[0].view)
                cartas_levantadas[1].unflip(cartas_levantadas[1].view)
                cartas_levantadas = mutableListOf()
                removeLife()
                Log.v("vidas:", livesContainer.childCount.toString() )
                muerte()

            }, 1000)



        }else{
            cartas_levantadas = mutableListOf()
            parejas++
            turno = true
            victoria()

        }

    }

    fun addLife(container: LinearLayout) {
        val inflater = LayoutInflater.from(this)
        val imageLayout = inflater.inflate(R.layout.life, null)

        val layoutParams = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.life_width),
            resources.getDimensionPixelSize(R.dimen.life_width))

        imageLayout.layoutParams = layoutParams

        container.addView(imageLayout)
    }
    fun removeLife() {
        val childCount = livesContainer.childCount
        if (childCount > 0) {
            Handler(Looper.getMainLooper()).postDelayed({
                livesContainer.removeViewAt(childCount - 1)
            }, 1500)
            play_sound(R.raw.gunshot)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memory)

        var cartas_imagenes = resources.obtainTypedArray(R.array.cards)
        board = findViewById(R.id.board)
        main = findViewById(R.id.main)

        //INICIALIZAR CARTAS
        var k = 0

        for (i in 1 until cartas_imagenes.length()) {
            cartas_imagenes_final.add(cartas_imagenes.getResourceId(i, R.drawable.c0))
            cartas_imagenes_final.add(cartas_imagenes.getResourceId(i, R.drawable.c0))
        }

        cartas_imagenes_final.shuffle()


        for (i in 0 until board.childCount) {
            var childView : LinearLayout = board.getChildAt(i) as LinearLayout
            for (j in 0 until childView.childCount)
            {
                var c = Carta(cartas_imagenes_final[k], childView.getChildAt(j) as ImageView)

                c.view.setOnClickListener {
                    if(turno && cartas_levantadas.size < 2 && !c.levantada)
                    {
                        turno = false
                        play_sound(R.raw.flip)
                        c.flip()
                        cartas_levantadas.add(c)
                        if(cartas_levantadas.size == 2)
                        {
                            check()
                        }else{
                            turno = true
                        }
                    }
                }

                cartas.add(Carta(cartas_imagenes_final[k], childView.getChildAt(j) as ImageView))
                k++
            }
        }

        //INICIALIZAR VIDAS

        livesContainer = findViewById(R.id.lives)

        for (i in 1..lives) {
            Log.v("printing lives", i.toString())
            addLife(livesContainer)
        }


    }
}
