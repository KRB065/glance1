package com.example.glance
import com.example.glance.FlashCard
class Set {
    private var set = ArrayList<FlashCard>()
    private var setName = ""
    private var folders = ArrayList<String>()
    constructor(name: String){
        setName =name
    }
    fun addCard(flashCard: FlashCard){
        set.add(flashCard)
    }
    fun getName(): String{
        return this.setName
    }
    fun changeName(newName: String){
        setName = newName
    }
    fun  getCard(i: Int): FlashCard{
        return set[i]
    }
    fun addToFolder(string: String){
        folders.add(string)
    }
    fun folderSize(): Int{
        return folders.size
    }
    fun getFolderName(i: Int): String{
       return folders[i]
    }
    fun changeCard(flashCard: FlashCard, int: Int){
        set[int] = flashCard
    }
    fun getSize(): Int{
        return set.size
    }
}