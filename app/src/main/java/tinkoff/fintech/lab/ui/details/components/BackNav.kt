package tinkoff.fintech.lab.ui.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tinkoff.fintech.lab.R

@Composable
fun BackNav(
    onNavigateBack: () -> Unit
) {
    Icon(
        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
        contentDescription = "back arrow search",
        tint = colorResource(id = R.color.app_blue),
        modifier = Modifier
            .padding(start = 17.dp, top = 53.dp)
            .size(24.dp)
            .clickable { onNavigateBack.invoke() }
    )
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreview() {
    BackNav(onNavigateBack = {})
}