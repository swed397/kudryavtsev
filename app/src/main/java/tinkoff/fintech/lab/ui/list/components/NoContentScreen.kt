package tinkoff.fintech.lab.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tinkoff.fintech.lab.R

@Composable
fun NoContentScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 127.dp, height = 38.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.app_blue))
    ) {
        Text(
            stringResource(id = R.string.not_found),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreview() {
    NoContentScreen()
}