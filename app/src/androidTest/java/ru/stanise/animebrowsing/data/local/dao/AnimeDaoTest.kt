package ru.stanise.animebrowsing.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.stanise.animebrowsing.data.local.db.AppDatabase
import ru.stanise.animebrowsing.ui.model.availableGenres


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AnimeDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: AnimeDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.animeDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertGenres_insertAndFetchCorrectly() = runTest {
        dao.insertGenres(availableGenres.toList())

        val fetchedGenres = dao.getGenres()

        assertTrue(fetchedGenres.isNotEmpty())

        assertEquals(availableGenres.size, fetchedGenres.size)
    }
}