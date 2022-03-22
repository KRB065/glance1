package com.example.glance
import com.example.glance.FlashCard
class Set {
    private var set = ArrayList<FlashCard>()
    private var setName = ""
    constructor(name: String){
        setName =name
    }
    fun addCard(flashCard: FlashCard){
        set.add(flashCard)
    }
    fun changeName(newName: String){
        setName = newName
    }
}