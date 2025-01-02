package com.example.newsapplication.Utilites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapplication.R
import com.example.newsapplication.ui.theme.Purple80

@Composable
fun CustomToolbar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                com.example.newsapplication.ui.theme.Green
                ,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                    bottomStart = 25.dp,
                    bottomEnd = 25.dp
                )
            )
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "Menu Icon",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(8.dp)
                .clickable {  }
        )

        Text(
            text = "News app",
            style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
            modifier = Modifier.align(Alignment.Center)
        )

        Image(
            painter = painterResource(id = R.drawable.search_ic),
            contentDescription = "Search Icon",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(8.dp)
                .clickable {  }
        )
    }
}

@Preview
@Composable
private fun show() {
CustomToolbar()
}
