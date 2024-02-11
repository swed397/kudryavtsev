package tinkoff.fintech.lab.domain.model

data class FilmModel(
    val id: Long,
    val nameRus: String,
    val nameEng: String,
    val year: Int,
    val genres: String,
    val genre: String,
    val countries: String,
    val rating: String,
    val posterUrlPreview: String,
    val posterUrl: String
)