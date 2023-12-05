package com.example.p12plusplusplusplusplus

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.p12plusplusplusplusplus.ui.theme.P12PlusPlusPlusPlusPlusTheme

class MainActivity : ComponentActivity() {
    var randomSizedPhotos = (1..30).map { randomSampleImageUrl() }.toMutableStateList()//упрощенный способ задать список
    fun convertPixelsToDp(context: Context, pixels: Float) = pixels / context.resources.displayMetrics.density //конвертер
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val interactionSource = remember { MutableInteractionSource() }
            Log.d("model", randomSizedPhotos.toString())
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    itemsIndexed(randomSizedPhotos) { i, photo ->

//попросить бота создать регулярку для получения значений из строки вида val s:String="https://loremflickr.com/300/500?random=$seed"

                        val regex = """.*\/(\d+)\/(\d+)\?.*""".toRegex()
                        val matchResult = regex.find(photo)

                        val widthInPX = matchResult?.groupValues?.get(1)!!.toFloat()
                        val heightInPX = matchResult?.groupValues?.get(2)!!.toFloat()

//учитывая, что s использует значения в пикселях, их надо перевести в dp
//например, с помощью функции convertPixelsToDp
                        val widthInDP = convertPixelsToDp(this@MainActivity, widthInPX)
                        val heightInDP = convertPixelsToDp(this@MainActivity, heightInPX)
                        OutlinedCard(
                            modifier = Modifier.width(widthInDP.dp).height(heightInDP.dp),
                            elevation = CardDefaults.cardElevation(5.dp)
                        ) {
// Text(text ="$i $photo")
// var model by remember{ mutableStateOf(photo)}
                            AsyncImage(
                                model = photo,

                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clickable(
                                        onClick = {
                                            randomSizedPhotos[i] =
                                                randomSampleImageUrl(height = (200..1500).random())
//model=randomSampleImageUrl()
                                        }
                                    )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
fun randomSampleImageUrl(
    seed: Int = (0..100000).random(),
    width: Int = 300,
    height: Int = width,
): String {
    return "https://loremflickr.com/$width/$height?random=$seed"
}