package tinkoff.fintech.lab.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import tinkoff.fintech.lab.App
import tinkoff.fintech.lab.R
import tinkoff.fintech.lab.di.injectedViewModel
import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.ui.details.DetailsScreen
import tinkoff.fintech.lab.ui.details.DetailsState

@Composable
fun ListScreenHolder(navController: NavController) {
    val context = LocalContext.current.applicationContext
    val viewModel = injectedViewModel {
        (context as App).appComponent.listViewModelFactory.create(navController)
    }
    val state by viewModel.state.collectAsState()

    val config = LocalConfiguration.current

    LaunchedEffect(config) {
        viewModel.obtainEvent(
            ListEvent.OnOrientationChange(config.orientation)
        )
    }

    ListScreen(state, viewModel::obtainEvent)
}

@Composable
private fun ListScreen(
    state: ListState,
    onEvent: (ListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { TopBar(state, onEvent) },
        bottomBar = { BottomBar(state.filmType, onEvent) },
        modifier = modifier
            .systemBarsPadding()
            .background(Color.White)
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            when (state) {
                is ListState.Data -> {
                    if (state.filmDetails != null) {
                        Row(Modifier.fillMaxSize()) {
                            FilmsList(
                                data = state.visibleData,
                                onEvent = onEvent,
                                modifier = Modifier.weight(1f)
                            )

                            DetailsScreen(
                                state = DetailsState.Data(filmDetails = state.filmDetails),
                                onNavigate = {},
                                modifier = Modifier.weight(1f)
                            )
                        }

                    } else {
                        FilmsList(
                            data = state.visibleData,
                            onEvent = onEvent
                        )
                    }
                }

                is ListState.Loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                )

                is ListState.NoContent -> NoContentScreen()
                ListState.Error -> ErrorScreen(onEvent)
            }
        }
    }
}

@Composable
private fun ErrorScreen(onEvent: (ListEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_cloud_off_24),
            contentDescription = "error icon",
            tint = Color.Blue,
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = "Произошла ошибка при загрузке данных, проверьте подключение к сети",
            fontSize = 14.sp,
            color = Color.Blue,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )
        Button(
            onClick = { onEvent.invoke(ListEvent.ReloadData) },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Повторить")
        }
    }
}

@Composable
private fun NoContentScreen() {
    Text(
        "Не найдено",
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        modifier = Modifier
            .size(width = 127.dp, height = 38.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Blue)
    )
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
                    .background(if (filmTypeState == FilmType.POPULAR) Color.Cyan else Color.Blue)
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
                    .background(if (filmTypeState == FilmType.FAVORITE) Color.Cyan else Color.Blue)
            )
        }
    }
}


@Composable
private fun TopBar(state: ListState, onEvent: (ListEvent) -> Unit) {
    var isSearching by remember { mutableStateOf(false) }

    if (isSearching) {
        SearchText(onClickIcon = { isSearching = isSearching.not() }, onEvent)
    } else {
        ScreenTitle(
            screenTitle = if (state.filmType == FilmType.POPULAR) stringResource(id = R.string.tab_name_popular) else stringResource(
                id = R.string.tab_name_favorites
            ),
            onClickIcon = {
                isSearching = isSearching.not()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchText(onClickIcon: () -> Unit, onEvent: (ListEvent) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp)
            .background(Color.White)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "back arrow search",
            modifier = Modifier
                .size(24.dp)
                .clickable { onClickIcon.invoke() }
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text(
                    "Поиск",
                    color = Color.Gray,
                    maxLines = 1
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onEvent.invoke(ListEvent.FilterByText(searchText))
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ScreenTitle(screenTitle: String, onClickIcon: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 30.dp)
            .background(Color.White)
    ) {
        Text(
            screenTitle,
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Icon(
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "search_icon",
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { onClickIcon.invoke() }
        )
    }
}

@Composable
private fun FilmsList(
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
            .padding(start = 15.dp, top = 15.dp)
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
                painter = painterResource(id = R.drawable.star), contentDescription = "",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
@Preview
private fun ListScreenHolderPreview() {

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

    ListScreen(ListState.Data(data = data, visibleData = data, filmType = FilmType.POPULAR), {})
}