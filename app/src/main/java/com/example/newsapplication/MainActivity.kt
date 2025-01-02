package com.example.newsapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.newsapplication.Api.ApiManager
import com.example.newsapplication.Api.ArticleResponse
import com.example.newsapplication.Api.ArticlesItem
import com.example.newsapplication.Api.NewsResponse
import com.example.newsapplication.Api.SourcesItem
import com.example.newsapplication.Utilites.CustomToolbar
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
                Scaffold(modifier = Modifier.fillMaxSize() , topBar = { CustomToolbar()}) { innerPadding ->
                    ApiCompose(modifier = Modifier.padding(innerPadding))
                    FullBackGround(modifier = Modifier.padding(innerPadding))

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
    val selectedSourceId = remember {
        mutableStateOf("")
    }


    LaunchedEffect (Unit){
        ApiManager.getNewsService().GetSources().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.sources?.isNotEmpty() == true)
                        newsstate.addAll(responseBody.sources)
                    selectedSourceId.value = responseBody?.sources?.get(0)?.id ?: ""
                }


            }

            override fun onFailure(p0: Call<NewsResponse>, p1: Throwable) {
            }

        })

    }
    Column {
        NewsContent(newslist = newsstate, onSourceSelected = {
            selectedSourceId.value = it
        }, modifier = modifier)
        NewsArticle(sourceid = selectedSourceId.value, modifier = modifier)
    }
}

@Composable
fun NewsContent( newslist: List<SourcesItem>,onSourceSelected: (String) -> Unit,modifier: Modifier = Modifier) {
    val selectindex = remember {
        mutableIntStateOf(0)
    }
    val selected = Modifier
        .padding(4.dp)
        .background(Green, shape = CircleShape)
        .padding(
            horizontal = 4.dp, vertical = 4.dp
        )
    val unselected = Modifier
        .padding(4.dp)
        .border(2.dp, Green, shape = CircleShape)
        .padding(
            horizontal = 4.dp, vertical = 4.dp
        )
    if (newslist.isNotEmpty())
        ScrollableTabRow(selectedTabIndex = selectindex.intValue, modifier = modifier,
            divider = {}, edgePadding = 0.dp, indicator = {}) {
            newslist.forEachIndexed { index, news ->
                Tab(selected = selectindex.intValue == index, onClick = {
                    onSourceSelected(news.id ?: "")
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

@Composable
fun NewsArticle( sourceid : String, modifier: Modifier = Modifier) {
    val articlestate = remember {
        mutableStateListOf<ArticlesItem>()
    }
    LaunchedEffect(sourceid) {
        articlestate.clear()
        ApiManager.getNewsService().GetNews("238e4e26601947f6bb765fab3f064e2e",sourceid).enqueue(
            object : Callback<ArticleResponse> {
                override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.articles?.isNotEmpty() == true)
                            articlestate.addAll(responseBody.articles)
                    }

                }

                override fun onFailure(p0: Call<ArticleResponse>, p1: Throwable) {

                }

            }
        )

    }
    Newsbox(articlelist = articlestate, modifier = modifier)

}

@Composable
fun Newsbox( articlelist : List<ArticlesItem>,modifier: Modifier = Modifier) {
    LazyColumn {
        items(articlelist) { articlesItem ->
            NewsItem(articlesItem = articlesItem)

        }
    }

}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(articlesItem: ArticlesItem, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        GlideImage(
            model = articlesItem.urlToImage,
            contentDescription = "news",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Text(
            text = articlesItem.author ?: "Unknown Author",
            modifier = Modifier.padding(top = 8.dp)
            , color = Color.Gray
        )
        Text(
            text = articlesItem.title ?: "No Title",
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = articlesItem.publishedAt ?: "No Date",
            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)

        )
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}

@Composable
fun FullBackGround(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.pattern),
        contentDescription = "background",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )

}








