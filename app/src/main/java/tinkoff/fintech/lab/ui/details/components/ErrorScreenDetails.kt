package tinkoff.fintech.lab.ui.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tinkoff.fintech.lab.R
import tinkoff.fintech.lab.ui.list.ListEvent

@Composable
fun ErrorScreenDetail() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_cloud_off_24),
            contentDescription = "error icon",
            tint = colorResource(id = R.color.app_blue),
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = stringResource(id = R.string.error_text),
            fontSize = 14.sp,
            color = colorResource(id = R.color.app_blue),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreview() {
    ErrorScreenDetail()
}