package tinkoff.fintech.lab.ui.list.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import tinkoff.fintech.lab.R
import tinkoff.fintech.lab.ui.list.FilmListUiModel
import tinkoff.fintech.lab.ui.list.ListEvent

@Composable
fun FilmsList(
    data: List<FilmListUiModel>,
    onEvent: (ListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(items = data) {
            FilmItem(film = it, onEvent = onEvent)
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FilmItem(
    film: FilmListUiModel,
    onEvent: (ListEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(93.dp)
            .padding(start = 15.dp, top = 15.dp, end = 15.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(15.dp)
            )
            .background(Color.White)
            .combinedClickable(
                onClick = {
                    onEvent.invoke(
                        ListEvent.NavigateToFilmDetails(
                            filmId = film.filmId,
                            isFavorite = film.isFavorite
                        )
                    )
                },
                onLongClick = {
                    onEvent.invoke(ListEvent.AddFilmToFavorite(film.filmId))
                }
            )

    ) {
        AsyncImage(
            model = film.filmPosterUrl, contentDescription = "item icon",
            modifier = Modifier
                .padding(start = 16.dp)
                .width(40.dp)
                .width(63.dp)
                .clip(RoundedCornerShape(5.dp))
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(4f)
        ) {
            Text(
                text = film.filmTitle,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.widthIn(max = 184.dp)
            )
            Text(
                text = "${film.filmGenre} (${film.filmYear})",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        if (film.isFavorite) {
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "",
                tint = colorResource(id = R.color.app_blue),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ComponentPreview() {
    val data = listOf(
        FilmListUiModel(
            filmId = 1,
            filmTitle = "Test film",
            filmYear = 2000,
            filmGenre = "test",
            filmPosterUrl = "",
            isFavorite = true
        ),
        FilmListUiModel(
            filmId = 2,
            filmTitle = "Test film",
            filmYear = 2000,
            filmGenre = "test",
            filmPosterUrl = "",
            isFavorite = false
        ),
        FilmListUiModel(
            filmId = 3,
            filmTitle = "Test film",
            filmYear = 2000,
            filmGenre = "test",
            filmPosterUrl = "",
            isFavorite = true
        )
    )

    FilmsList(data = data, onEvent = {})
}