package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RemindersDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveReminderCheckId() = runBlocking {
        val reminder = ReminderDTO(
            "Go to the store.", "Pick up bread.",
            "Safeway", 89.98, -98.34
        )

        database.reminderDao().saveReminder(reminder)

        val savedReminder = database.reminderDao().getReminderById(reminder.id)

        assertThat<ReminderDTO>(savedReminder as ReminderDTO, notNullValue())
        assertThat(savedReminder.id, `is`(reminder.id))
        assertThat(savedReminder.title, `is`(reminder.title))
        assertThat(savedReminder.description, `is`(reminder.description))

    }

    @Test
    fun updateReminderAndGetId() = runBlocking {

        val reminder2 = ReminderDTO(
            "Go to the store.", "Pick up bread.",
            "Safeway", 89.98, -98.34
        )

        database.reminderDao().saveReminder(reminder2)

        val newReminder = ReminderDTO(
            "Go to the store.", "Pick up cheese.",
            "Safeway", 89.98, -98.34, reminder2.id
        )

        database.reminderDao().saveReminder(newReminder)

        assertThat<ReminderDTO>(newReminder as ReminderDTO, notNullValue())
        assertThat(newReminder.description, `is`("Pick up cheese."))

    }

}