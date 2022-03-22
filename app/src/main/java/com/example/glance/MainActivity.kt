package com.example.glance

import android.content.res.AssetManager
import android.media.tv.TvContract
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.glance.R.*
import com.example.glance.ui.theme.GlanceTheme
import com.google.gson.Gson
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList
import com.example.glance.Side
import com.example.glance.FlashCard
import com.example.glance.Set
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths

var flashSet = ArrayList<Set>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val file = File("C:\\Users\\kobyb\\AndroidStudioProjects\\glance\\app\\src\\main\\java\\com\\example\\glance\\assets\\Sets.json")

        setContent {
            GlanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Greeting("Android")
                    Column {
                        SimpleImage()
                        OptionTabs()
                    }


//
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    Text(text = "Hello Johannes!")
}
@Preview
@Composable
fun SimpleImage(){
    Image(painter = painterResource(id = drawable.glance),
        contentDescription = "lets go brandon",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )


}



@Composable
fun CreateEvent() {

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        val paddingModifier = Modifier.padding(10.dp)
        var rando = 5


        var text by remember {
            mutableStateOf("")
        }

        TextField(value = text, modifier = Modifier.fillMaxWidth(),onValueChange = { newText ->
            text = newText

        })
        var set = Set(text)
        var added by remember {
            mutableStateOf(0)
        }

        Text(text = "Title")
        CardCreate(set)
        CardCreate(set)
        for (i in 1..added) {
            CardCreate(set)
        }
        added = CreateModifier(cards = added )

    }

}
@Composable
fun CreateModifier( cards: Int):Int{
    var moreCards by remember {
        mutableStateOf(cards)
    }
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(25.dp)) {

        Button(onClick = { moreCards += 1}) {
            Text(text = "Add Card")
        }
        Button(onClick = {if (moreCards>0){moreCards-=1}}){
            Text(text = "Remove Card")
        }
        Button(onClick = {
            var gson = Gson()
            try{
                val file = File("C:\\Users\\kobyb\\AndroidStudioProjects\\glance\\app\\src\\main\\assets\\Sets.json")
                val fileWriter = BufferedWriter(FileWriter(file))
                var jsonString = gson.toJson(flashSet[flashSet.size])
                fileWriter.write(jsonString)
                fileWriter.flush()
                fileWriter.close()
                print(jsonString)
            }
            catch (e: Exception){
                e.printStackTrace()
            }

//            var jsonString = gson.toJson(flashSet[flashSet.size])
//            print(jsonString)



        }) {
            Text(text = "Create Set")
        }
    }
    return moreCards;
}
@Composable
fun CardCreate(set: Set){
    Card(
//        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        modifier = Modifier.padding(vertical = 15.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            var flashCard = FlashCard()
            var clicked by remember {
                mutableStateOf(0)
            }
            for (i in 1..2) {
                CreateTextField(flashCard = flashCard)
            }
            for (i in 1..clicked){
                CreateTextField(flashCard = flashCard)
            }
            var expanded by remember {
                mutableStateOf(false)
            }
            Row( modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = {expanded = true},
                   




                ) {
                    Text(text = "+")
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
                        DropdownMenuItem(onClick = { clicked +=1; expanded = false }) {
                            Text(text = "Text")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Audio")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Video")
                        }
                    }
                }
                Button(onClick = { clicked -= 1}) {
                    Text(text = "-")
                }
                set.addCard(flashCard = flashCard)
            }
            



        }

    }
}
@Composable
fun CreateTextField (flashCard: FlashCard) {
    var term by remember {
        mutableStateOf("")
    }
    TextField(value = term, onValueChange = {newTerm ->
        term = newTerm
    }, modifier = Modifier.fillMaxWidth())
    flashCard.addSide(side = Side(term))
}
@Composable
fun OptionTabs() {

    
    MaterialTheme {
        val titles = listOf<String>("Recent", "Search", "Folders", "Create")
        var state by remember {
            mutableStateOf(0)
        }
        Column {
            TabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, titles ->
                    Tab( text = { Text(titles) },
                        selected = state == index,
                        onClick = {
                            state = index;
                           
                        }

                    )
                }
            }
            if(titles[state] == "Create") {
                CreateEvent()
            }
            


        }

    }
}
//@Composable
//fun CreateSet
//        9
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GlanceTheme {
//        Greeting("Android")
        //SimpleImage()
    }
}