package com.example.glance

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.net.toUri
import java.net.URI

class Side {
    private lateinit var text: String
    private lateinit var image: String
    private  lateinit var audio: MediaPlayer
    private  lateinit var uri: Uri
    private var mediaType: String = "text"
    constructor(text: String) {
        this.text = text
    }

    constructor(uri: Uri){
        if (uri != null){
            this.image = uri.toString()
        }

        this.mediaType = "image"
    }

    constructor(audio: MediaPlayer){
        this.audio = audio
        this.mediaType = "audio"
    }
    fun updateSide (text: String){
        this.text = text
    }

    fun updateSide (uri: Uri) {
        if (this.text == ""){
            if (uri != null) {
                this.image = uri.toString()
                this.mediaType = "image"
            }
        }

    }
    fun getData(): String{
        if (this.mediaType == "text"){
            return this.text
        }
        return ""
    }
    fun getUri(): Uri{
        return this.image.toUri()
    }
    fun getDataType(): String{
        return this.mediaType
    }

}