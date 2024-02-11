package tinkoff.fintech.lab.ui.details.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CategoryDetail(title: String, content: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Gray)) {
                append("$title: ")
            }
            withStyle(style = SpanStyle(color = Color.Gray)) {
                append(content)
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreview() {
    CategoryDetail(title = "Жанр", content = "комедия")
}