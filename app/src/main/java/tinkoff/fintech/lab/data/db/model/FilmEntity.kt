package tinkoff.fintech.lab.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film")
data class FilmEntity(
    @PrimaryKey @ColumnInfo("id") val id: Long,
    @ColumnInfo("name_rus") val nameRus: String,
    @ColumnInfo("name_eng") val nameEng: String,
    @ColumnInfo("year") val year: Int,
    @ColumnInfo("genres_string") val genresString: String,
    @ColumnInfo("genre") val genre: String,
    @ColumnInfo("countries") val countriesString: String,
    @ColumnInfo("rating") val rating: String,
    @ColumnInfo("poster_url_preview") val posterUrlPreview: String,
    @ColumnInfo("poster_url") val posterUrl: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("film_length") val filmLength: Int,
)
