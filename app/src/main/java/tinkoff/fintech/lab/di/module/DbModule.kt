package tinkoff.fintech.lab.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import tinkoff.fintech.lab.data.db.FilmDb
import tinkoff.fintech.lab.data.db.dao.FilmDao
import javax.inject.Singleton

@Module
class DbModule(private val context: Context) {

    @Provides
    @Singleton
    fun createDb(): FilmDb = Room.databaseBuilder(
        context = context,
        FilmDb::class.java, "film-db"
    ).build()

    @Provides
    @Singleton
    fun provideFilmDao(filmDb: FilmDb): FilmDao = filmDb.filmDao()
}