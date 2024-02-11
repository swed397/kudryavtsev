package tinkoff.fintech.lab.domain.model

data class DetailsFilmModel(
    val id: Long,
    val nameRus: String,
    val nameEng: String,
    val year: Int,
    val genres: String,
    val genre: String,
    val countries: String,
    val rating: String,
    val posterUrl: String,
    val description: String,
    val filmLength: String
)