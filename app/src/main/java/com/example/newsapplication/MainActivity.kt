package com.example.newsapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapplication.Api.ApiManager
import com.example.newsapplication.Api.NewsResponse
import com.example.newsapplication.Api.SourcesItem
import com.example.newsapplication.ui.theme.Green
import com.example.newsapplication.ui.theme.NewsApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ApiCompose(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun ApiCompose(modifier: Modifier = Modifier) {
    val newsstate = remember {
        mutableStateListOf<SourcesItem>()
    }
    LaunchedEffect (Unit){
        ApiManager.getNewsService().GetSources().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.sources?.isNotEmpty() == true)
                        newsstate.addAll(responseBody.sources)

                }


            }

            override fun onFailure(p0: Call<NewsResponse>, p1: Throwable) {
            }

        })

    }
    NewsContent(newslist = newsstate , modifier = modifier)
}

@Composable
fun NewsContent( newslist: List<SourcesItem>,modifier: Modifier = Modifier) {
    val selectindex = remember {
        mutableIntStateOf(0)
    }
    val selected = Modifier.padding(4.dp).background(Green , shape = CircleShape).padding(
        horizontal = 4.dp , vertical = 4.dp
    )
    val unselected = Modifier.padding(4.dp).border(2.dp , Green , shape = CircleShape).padding(
        horizontal = 4.dp , vertical = 4.dp
    )
    if (newslist.isNotEmpty())
        ScrollableTabRow(selectedTabIndex = selectindex.intValue, modifier = modifier,
            divider = {}, edgePadding = 0.dp, indicator = {}) {
            newslist.forEachIndexed { index, news ->
                Tab(selected = selectindex.intValue == index, onClick = {
                    selectindex.intValue = index
                }, modifier = if (selectindex.intValue == index) selected else unselected, text = {
                    Text(
                        text = news.name!! ,
                        color = if (selectindex.intValue == index) Color.White else Green
                    )
                })
            }
        }
}



