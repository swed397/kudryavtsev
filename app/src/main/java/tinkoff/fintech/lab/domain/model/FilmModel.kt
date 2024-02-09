package tinkoff.fintech.lab.domain.model

data class FilmModel(
    val id: Long,
    val nameRus: String,
    val nameEng: String,
    val year: Int,
    val genres: List<String>,
    val countries: List<String>,
    val rating: String,
    val posterUrlPreview: String,
    val posterUrl: String,
)