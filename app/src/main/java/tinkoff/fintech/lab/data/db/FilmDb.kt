package tinkoff.fintech.lab.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tinkoff.fintech.lab.data.db.dao.FilmDao
import tinkoff.fintech.lab.data.db.model.FilmEntity

@Database(
    entities = [FilmEntity::class],
    version = 1
)
abstract class FilmDb : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}