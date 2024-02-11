package tinkoff.fintech.lab.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import tinkoff.fintech.lab.R
import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.ui.list.ListEvent

@Composable
fun BottomBar(filmTypeState: FilmType, onEvent: (ListEvent) -> Unit) {
    TabRow(
        selectedTabIndex = filmTypeState.ordinal,
        modifier = Modifier.background(Color.Green),
        containerColor = Color.White,
        indicator = {}
    ) {
        Tab(
            selected = filmTypeState == FilmType.POPULAR,
            onClick = { onEvent.invoke(ListEvent.ChangeFilmType(FilmType.POPULAR)) },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(width = 158.dp, height = 45.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (filmTypeState == FilmType.POPULAR)
                            colorResource(id = R.color.app_light_blue)
                        else
                            colorResource(id = R.color.app_blue)
                    )
            ) {
                Text(
                    stringResource(id = R.string.tab_name_popular),
                    textAlign = TextAlign.Center,
                    color = if (filmTypeState == FilmType.POPULAR)
                        colorResource(id = R.color.app_blue)
                    else
                        Color.White,
                )
            }
        }

        Tab(
            selected = filmTypeState == FilmType.FAVORITE,
            onClick = { onEvent.invoke(ListEvent.ChangeFilmType(FilmType.FAVORITE)) },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(width = 158.dp, height = 45.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (filmTypeState == FilmType.FAVORITE)
                            colorResource(id = R.color.app_light_blue)
                        else
                            colorResource(id = R.color.app_blue)
                    )
            ) {
                Text(
                    stringResource(id = R.string.tab_name_favorites),
                    textAlign = TextAlign.Center,
                    color = if (filmTypeState == FilmType.FAVORITE)
                        colorResource(id = R.color.app_blue)
                    else
                        Color.White,
                )
            }
        }
    }
}

@Composable
@Preview
private fun ComponentPreview() {
    BottomBar(filmTypeState = FilmType.FAVORITE, onEvent = {})
}