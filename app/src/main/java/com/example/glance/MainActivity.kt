package com.example.glance

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.tv.TvContract
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.glance.R.*
import com.example.glance.ui.theme.GlanceTheme
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList
import com.example.glance.Side
import com.example.glance.FlashCard
import com.example.glance.Set
import com.google.gson.*
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.Integer.parseInt
import java.nio.file.Files
import java.nio.file.Paths


var flashSet = ArrayList<Set>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var path = filesDir.absolutePath + "//Sets.json"
        var file = File(path)
        val isFileCreated: Boolean = file.createNewFile()
        Log.d("Begin", "Begun")
//        Log.d("bool", "$isFileCreated")
        Log.d("empty", "${file}")
        if (isFileCreated == false){

            var gson = Gson()
            var jsonString: String = file.bufferedReader().use{it.readText()}

            val jsonElement = JsonParser.parseString(jsonString).asJsonArray
            for (i in 0 until jsonElement.size()){
                val jsonObject = jsonElement[i]
                val newSet = jsonObject.asJsonObject["setName"]
                val jsonSet = Set(newSet.asString)
                val jsonArray = jsonObject.asJsonObject.getAsJsonArray("set")
                for(j in 0 until jsonArray.size()){
                    val flashObject = jsonArray[j]
                    val theObject = flashObject.asJsonObject
                    val flashArray = theObject.getAsJsonArray("flashCard")
                    val flashCard = FlashCard()
                    for (k in 0 until flashArray.size()){
                        val sideJ = flashArray[k]
                        val type = sideJ.asJsonObject["mediaType"]
                        val typeData = sideJ.asJsonObject["${type.asString}"]
                        val side = Side(typeData.asString)
                        flashCard.addSide(side)
                    }
                    jsonSet.addCard(flashCard = flashCard)
                }
                flashSet.add(jsonSet)
            }
            Log.d("element", "${flashSet[0].getCard(0).getSide(0).getData()}")

//
        }



        setContent {
            GlanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation(applicationContext)
//                    Column (
//                        horizontalAlignment = Alignment.CenterHorizontally
//                            ){
//                        SimpleImage()
//                        OptionTabs(applicationContext)
//                    }


//
                }
            }
        }
    }
}
@Composable
fun MainScreen(navController: NavController, applicationContext: Context) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SimpleImage()
        OptionTabs(applicationContext, navController = navController)
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
fun CreateEvent(context: Context, set: Set) {

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        val paddingModifier = Modifier.padding(10.dp)


        var text by remember {
            mutableStateOf("")
        }

        TextField(value = text, modifier = Modifier.fillMaxWidth(),onValueChange = { newText ->
            text = newText

        })
        set.changeName(text)
        var added by remember {
            mutableStateOf(0)
        }

        Text(text = "Title")
        CardCreate(set, 0)
        CardCreate(set, 1)
        for (i in 1..added) {
            set.addCard(flashCard = FlashCard())
            CardCreate(set, i+1)

        }

        added = CreateModifier(cards = added, context )
    }

}
@Composable
fun CreateModifier( cards: Int, context: Context):Int{
    Log.d("added", "$cards")
    var moreCards by remember {
        mutableStateOf(cards)
    }
    var createSet by remember {
        mutableStateOf(false)
    }
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(25.dp)) {

        Button(onClick = { moreCards += 1}) {
            Text(text = "Add Card")
        }
        Button(onClick = {if (moreCards>0){moreCards-=1}}){
            Text(text = "Remove Card")
        }
        Button(onClick = { createSet = true}) {
            Text(text = "Create Set")
        }
        if (createSet == true){
            var gson = Gson()
            Log.d("Create", "Create Clicked")
            val path = context.filesDir.absolutePath + "//Sets.json"
            var file = File(path)
            val fileWriter = BufferedWriter(FileWriter(file))
            var jsonString = gson.toJson(flashSet)
            fileWriter.append(jsonString)
            fileWriter.flush()
            fileWriter.close()
            Log.d("Json",jsonString)
        }
    }
    return moreCards;
}
@Composable
fun CardCreate(set: Set, num: Int){
    Card(
//        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        modifier = Modifier.padding(vertical = 15.dp)
    ) {
        var orginizeData by remember {
            mutableStateOf(ArrayList<String>())
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in 1..2) {
                set.changeCard(CreateTextField(flashCard = set.getCard(num), i = i-1 ), num)
            }
            var clicked by remember {
                mutableStateOf(0)
            }
            Log.d("clicked", "${clicked}")
            var flashCard by remember {
                mutableStateOf(set.getCard(num))
            }
            for (i in 2..clicked+1){
                if (orginizeData[i-2] ==  "text") {
                    set.changeCard(CreateTextField(flashCard = flashCard, i), num)
                }
                if (orginizeData[i-2] == "image"){
                    set.changeCard(ImageInput(flashCard = flashCard, i), num)
                }
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
                        DropdownMenuItem(onClick = {
                            orginizeData.add("text")
                            clicked +=1;
                            expanded = false;
                            flashCard.addSide(Side(""))

                        }) {
                            Text(text = "Text")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Audio")
                        }
                        DropdownMenuItem(onClick = {
                            Log.d("sizeb4",  "${set.getCard(num).getSize()}")
                            flashCard.addSide(Side(""))
                            Log.d("sizeaft",  "${set.getCard(num).getSize()}")
                            orginizeData.add("image");
                            clicked += 1;
                            expanded = false;



                        }) {
                            Text(text = "Image")
                        }
                    }
                }
                Button(onClick = {
                    clicked -= 1
                    set.getCard(num).removeLastSide()
                    orginizeData.removeAt(orginizeData.lastIndex)
                }) {
                    Text(text = "-")
                }
            }
            



        }

    }
}
@Composable
fun CreateTextField (flashCard: FlashCard, i: Int):FlashCard {
    var term by remember {
        mutableStateOf("")
    }

    TextField(value = term, onValueChange = {newTerm ->
        term = newTerm
    }, modifier = Modifier.fillMaxWidth())
    Log.d("textSize", "${flashCard.getSize()}")
    flashCard.getSide(i).updateSide(term)
    return flashCard
}
@Composable
fun ImageInput(flashCard: FlashCard, i: Int):FlashCard{

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    if(imageUri != null) {
        var side by remember {
            mutableStateOf(Side(imageUri!!))
        }
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        uri: Uri? ->  imageUri = uri
    }
    Log.d("show side", "${flashCard.getSide(0).getData()}")
    if (imageUri != null) {
        Log.d("checksize","${flashCard.getSize()}")
        flashCard.getSide(i).updateSide(uri = imageUri!!)
    }
    Log.d("imgSize", "${flashCard.getSize()}")
    Row() {
        Button(onClick = { launcher.launch("image/*")}) {
            Text(text = "+")
        }
        imageUri?.let {
            if(Build.VERSION.SDK_INT < 28){
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            }
            else{
                val  source = ImageDecoder
                    .createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            bitmap.value?.let { btm ->
                Image(bitmap = btm.asImageBitmap(), contentDescription = null, Modifier.size(200.dp))
            }

        }


    }

    return flashCard
}
@Composable
fun SetScreen(navController: NavController, num1: Int?){
    var num = num1!!
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(state = rememberScrollState())) {
        Text(text = flashSet[num].getName())
        Button(onClick = {
            var currentSet = flashSet[num]
            flashSet.removeAt(num)
            flashSet.add(0, currentSet)
            navController.navigate("flash_screen/$num")}) {
            Text(text = "FlashCards")
        }
        for(i in 0 until flashSet[num].getSize()){
            ShowCard(num, i)
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowCard(num: Int, i: Int) {
    var sideIndex by remember {
        mutableStateOf(0)
    }

    Card(
//        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        onClick = {
            if (sideIndex < flashSet[num].getCard(i).getSize() - 1) {
                sideIndex = +1
            } else {
                sideIndex = 0
            }
        },
        modifier = Modifier
            .padding(vertical = 15.dp)
            .size(300.dp, 200.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = flashSet[num].getCard(i).getSide(sideIndex).getData())
            }

        }
    }
}

@Composable
fun FlashScreen(navController: NavController, num1: Int?){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .verticalScroll(
            rememberScrollState()
        )) {
        var num = num1!!
        var cardIndex by remember {
            mutableStateOf(0)
        }
        Text(text = flashSet[num].getName(), fontWeight = FontWeight.Bold)
        FullCard(num = num, i = cardIndex)
        Row() {
            Button(onClick = {
               if (cardIndex> 0) {cardIndex-=1}
               else{
                   cardIndex = flashSet[num].getSize()-1
               }
            }) {
                Text(text = "Previous")
            }
            Button(onClick = {
                if (cardIndex < flashSet[num].getSize()-1){
                    cardIndex+=1
                }
                else{
                    cardIndex = 0
                }

            }) {
                Text(text = "Next")
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FullCard(num:Int, i: Int) {
    var sideIndex1 by remember {
        mutableStateOf(0)
    }

    Card(
//        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        onClick = {
            if (sideIndex1 < flashSet[num].getCard(i).getSize() - 1) {
                sideIndex1 = +1
            } else {
                sideIndex1 = 0
            }
        },
        modifier = Modifier
            .padding(vertical = 15.dp)
            .size(300.dp, 600.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = flashSet[num].getCard(i).getSide(sideIndex1).getData())
            }

        }

    }
}
@Composable
fun SearchBar(navController: NavController){
    var searchTerm by remember {
        mutableStateOf("")
    }
    TextField(value = searchTerm, onValueChange = {newTerm -> searchTerm = newTerm}, modifier = Modifier.fillMaxWidth(), placeholder = { Text(
        text = "Search.."
    )})
    SortedSets(searchTerm, navController)
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SortedSets(name: String, navController: NavController){
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for(i in flashSet){

            if(i.getName().contains(name)){
                Card(
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp,
                    onClick = {
                    navController.navigate("set_screen/${flashSet.indexOf(i)}")
                    },
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .size(300.dp, 200.dp)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(text = i.getName(), textAlign = TextAlign.Center)
                        }

                    }

                }
            }
        }
    }

}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetOptions(i:Int, navController: NavController){
    Card(
//        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        onClick = {
            navController.navigate("set_screen/$i")
        },
        modifier = Modifier
            .padding(vertical = 15.dp)
            .size(300.dp, 200.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = flashSet[i].getName(), textAlign = TextAlign.Center)
            }

        }

    }
}
@Composable
fun OptionTabs(context: Context, navController: NavController) {

    
    MaterialTheme {
        val titles = listOf<String>("Recent", "Search", "Folders", "Create")
        var state by remember {
            mutableStateOf(0)
        }
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
                ){
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

                var set = Set("")
                var flash1 = FlashCard()
                flash1.addSide(Side(""))
                flash1.addSide(Side(""))
                var flash2 = FlashCard()
                flash2.addSide(Side(""))
                flash2.addSide(Side(""))
                set.addCard(flash1)
                set.addCard(flash2)

                flashSet.add(set)
                CreateEvent(context = context, set)

            }
            if (titles[state] == "Search"){

                if (flashSet[flashSet.size-1].getName() == ""){
                    flashSet.removeAt(flashSet.size-1)
                }
                Column() {
                    SearchBar(navController = navController)
                }

            }
            if (titles[state] == "Folders"){
                if (flashSet[flashSet.size-1].getName() == ""){
                    flashSet.removeAt(flashSet.size-1)
                }
            }
            if(titles[state] == "Recent"){
                if (flashSet[flashSet.size-1].getName() == ""){
                    flashSet.removeAt(flashSet.size-1)
                }
                Log.d("size", "${flashSet.size}")
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,

                ) {
                    Text(text = "Sets")
                    for (i in 0 until flashSet.size){
                        SetOptions(i, navController = navController)
                    }
                }

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