package com.example.glance

import android.media.MediaPlayer
import androidx.compose.ui.graphics.ImageBitmap

class Side {
    private lateinit var text: String
    private lateinit var image: ImageBitmap
    private  lateinit var audio: MediaPlayer
    private var mediaType: String = "text"
    constructor(text: String) {
        this.text = text
    }
    constructor(image: ImageBitmap){
        this.image = image
        this.mediaType = "image"
    }
    constructor(audio: MediaPlayer){
        this.audio = audio
        this.mediaType = "audio"
    }

}