package tinkoff.fintech.lab.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import tinkoff.fintech.lab.App
import tinkoff.fintech.lab.R
import tinkoff.fintech.lab.di.injectedViewModel
import tinkoff.fintech.lab.domain.model.FilmModel

@Composable
fun ListScreenHolder() {
    val context = LocalContext.current.applicationContext
    val viewModel = injectedViewModel {
        (context as App).appComponent.listViewModelFactory.create()
    }
    val state by viewModel.state.collectAsState()

    ListScreen(state, viewModel::obtainEvent)
}

@Composable
private fun ListScreen(state: ListState, onEvent: (ListEvent) -> Unit) {

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(state.filmType, onEvent) }
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            when (state) {
                is ListState.Data -> FilmsList(
                    data = state.data,
                    onEvent = onEvent
                )

                is ListState.Loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomBar(filmTypeState: FilmType, onEvent: (ListEvent) -> Unit) {
    TabRow(
        selectedTabIndex = filmTypeState.ordinal,
        modifier = Modifier.background(Color.Green),
        indicator = {}
    ) {
        Tab(
            selected = filmTypeState == FilmType.POPULAR,
            onClick = { onEvent.invoke(ListEvent.ChangeFilmType(FilmType.POPULAR)) },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))

        ) {
            Text(
                stringResource(id = R.string.tab_name_popular),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(width = 158.dp, height = 45.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Cyan)
            )
        }

        Tab(
            selected = filmTypeState == FilmType.FAVORITE,
            onClick = { onEvent.invoke(ListEvent.ChangeFilmType(FilmType.FAVORITE)) },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text(
                stringResource(id = R.string.tab_name_favorites),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(width = 158.dp, height = 45.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Cyan)
            )
        }
    }
}


@Composable
private fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 30.dp)
    ) {
        Text(
            "Популярные",
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Icon(
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "search_icon",
            modifier = Modifier
                .padding(end = 16.dp)
        )
    }
}

@Composable
private fun FilmsList(data: List<FilmModel>, onEvent: (ListEvent) -> Unit) {
    LazyColumn(
        modifier = Modifier
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
private fun FilmItem(film: FilmModel, onEvent: (ListEvent) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(93.dp)
            .padding(start = 15.dp, top = 15.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(15.dp)
            )
            .background(Color.White)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onEvent.invoke(ListEvent.AddFilmToFavorite(film.id))
                }
            )

    ) {
        AsyncImage(
            model = film.posterUrlPreview, contentDescription = "item icon",
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
                text = film.nameRus,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.widthIn(max = 184.dp)
            )
            Text(
                text = "${film.genres} (${film.year})",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.star), contentDescription = "",
            modifier = Modifier.weight(1f)
        )
    }
}

data class Film(
    val name: String
)

@Composable
@Preview
private fun ListScreenHolderPreview() {

    val data = listOf(
        FilmModel(
            id = 1,
            nameRus = "Test film",
            nameEng = "Eng test film",
            year = 2000,
            genres = listOf("test"),
            countries = listOf("test"),
            rating = "55",
            posterUrlPreview = "",
            posterUrl = ""
        ),
        FilmModel(
            id = 2,
            nameRus = "Test film",
            nameEng = "Eng test film",
            year = 2000,
            genres = listOf("test"),
            countries = listOf("test"),
            rating = "55",
            posterUrlPreview = "",
            posterUrl = ""
        ),
        FilmModel(
            id = 3,
            nameRus = "Test film",
            nameEng = "Eng test film",
            year = 2000,
            genres = listOf("test"),
            countries = listOf("test"),
            rating = "55",
            posterUrlPreview = "",
            posterUrl = ""
        )
    )

    ListScreen(ListState.Data(data, FilmType.POPULAR), {})
}