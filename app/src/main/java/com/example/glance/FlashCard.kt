package com.example.glance
import com.example.glance.Side
class FlashCard {
    var flashCard = ArrayList<Side>()
    constructor(){

    }
    fun addSide(side: Side){
        flashCard.add(side)
    }
}