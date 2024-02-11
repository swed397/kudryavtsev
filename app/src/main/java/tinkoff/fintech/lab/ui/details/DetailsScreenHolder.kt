package tinkoff.fintech.lab.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import tinkoff.fintech.lab.App
import tinkoff.fintech.lab.R
import tinkoff.fintech.lab.di.injectedViewModel
import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.ui.main.FilmRoutes

@Composable
fun DetailsScreenHolder(
    filmId: Long,
    filmType: FilmType,
    onNavigate: (String) -> Unit,
) {
    val context = LocalContext.current.applicationContext
    val viewModel = injectedViewModel {
        (context as App).appComponent.detailsViewModelFactory.create(
            filmId = filmId,
            filmType = filmType
        )
    }
    val state by viewModel.state.collectAsState()

    DetailsScreen(state = state, onNavigate = onNavigate)
}

@Composable
fun DetailsScreen(
    state: DetailsState,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        when (state) {
            is DetailsState.Data -> MainScreen(
                filmDetails = state.filmDetails,
                onNavigate = onNavigate
            )

            DetailsState.Loading -> CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
            )
        }
    }
}

@Composable
private fun MainScreen(filmDetails: FilmDetailsUiModel, onNavigate: (String) -> Unit) {

    Box {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            SubcomposeAsyncImage(
                model = filmDetails.posterUrl,
                contentDescription = "film poster",
                loading = { CircularProgressIndicator() },
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(height = 533.dp)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier.padding(start = 31.dp, top = 10.dp, end = 31.dp)
            ) {
                Text(
                    text = filmDetails.filmName,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,

                    )
                Text(
                    text = filmDetails.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 15.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)

                )
                CategoryDetail(title = "Жанры", content = filmDetails.filmGenresString)
                CategoryDetail(title = "Страны", content = filmDetails.filmCountriesString)
                CategoryDetail(title = "Рейтинг", content = filmDetails.filmRating)
                CategoryDetail(title = "Длительность", content = filmDetails.filmLength.toString())
            }
        }

        BackNav(onNavigate = onNavigate)
    }
}

@Composable
private fun BackNav(onNavigate: (String) -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
        contentDescription = "back arrow search",
        tint = Color.Blue,
        modifier = Modifier
            .padding(start = 17.dp, top = 53.dp)
            .size(24.dp)
            .clickable { onNavigate.invoke(FilmRoutes.LIST.name) }
    )
}

@Composable
private fun CategoryDetail(title: String, content: String) {
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
@Preview
private fun DetailsScreenPreview() {
    val data = FilmDetailsUiModel(
        filmId = 1,
        filmName = "Мастер и Маргарита",
        filmYear = 2000,
        filmGenresString = "фентези, сказка",
        filmCountriesString = "Россия",
        filmRating = "0",
        posterUrl = "",
        filmLength = "",
        description = "Москва, 1930-е годы. Известный писатель на взлёте своей карьеры внезапно " +
                "оказывается в центре литературного скандала. Спектакль по его пьесе снимают " +
                " репертуара, коллеги демонстративно избегают встречи, в считанные дни он" +
                " превращается в изгоя. Вскоре после этого, он знакомится с Маргаритой, " +
                "которая становится его возлюбленной и музой. Воодушевлённый ее любовью " +
                "и поддержкой, писатель берется за новый роман, в котором персонажи — это" +
                " люди из его окружения, а главный герой — загадочный Воланд, прообразом " +
                "которого становится недавний знакомый иностранец. Писатель уходит с головой в мир" +
                " своего романа и постепенно перестает замечать, как вымысел и реальность " +
                "сплетаются в одно целое."
    )

    DetailsScreen(DetailsState.Data(data), { })
}