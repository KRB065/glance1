package com.example.glance
import com.example.glance.Side
class FlashCard {
    var flashCard = ArrayList<Side>()
    constructor(){

    }
    fun addSide(side: Side){
        flashCard.add(side)
    }
    fun getSide(i: Int): Side{
        return flashCard[i]
    }
    fun getSize(): Int{
        return flashCard.size
    }
    fun removeLastSide(){
        flashCard.removeAt(flashCard.lastIndex)
    }
}