package com.example.practica_1

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.widget.ImageView

class Carta {
    var levantada : Boolean
    var img : Int
    var view : ImageView

    constructor(img : Int, view : ImageView)
    {
        this.img = img
        this.levantada = false
        this.view = view
    }

    private fun waitFor(milliseconds: Long) {
        try {
            Thread.sleep(milliseconds)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun flip(){
        if(!levantada)
        {
            view.setRotationY(0f)
            view.animate().rotationY(90f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.setImageResource(img)
                    view.setRotationY(270f)
                    view.animate().rotationY(360f).setListener(null)
                }
            })
            levantada = true
        }

    }

    fun unflip(v : ImageView)
    {
        v.setRotationY(0f)
        v.animate().rotationY(90f).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                v.setImageResource(R.drawable.c0)
                v.setRotationY(270f)
                v.animate().rotationY(360f).setListener(null)
            }
        })
        levantada = false
    }
}