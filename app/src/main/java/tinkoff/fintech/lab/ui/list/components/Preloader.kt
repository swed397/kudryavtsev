package tinkoff.fintech.lab.ui.list.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Preloader() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(64.dp)
    )
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreview() {
    Preloader()
}