package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    //TODO: provide testing to the SaveReminderView and its live data objects
    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource

    var fakeReminder1 = ReminderDataItem("Get coffee.", "Grab hot cup of coffee.",
        "Starbucks", 89.98, -98.34)

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    @ExperimentalCoroutinesApi
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        //Stop the current Koin app
        stopKoin()
        fakeDataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    @Test
    fun validateData_dataThere() {
        saveReminderViewModel.onClear()
    }

    @Test
    fun saveReminder_savesReminderToDataSource() {

        //Given a SaveReminderViewModel with a FakeDataSource
        saveReminderViewModel.saveReminder(fakeReminder1)

        //Then the size of the FakeDataSource should be 1.
        assertThat(fakeDataSource.reminders?.size, equalTo(1))

           }

    @Test
    fun saveReminder_showLoadingState() {

        mainCoroutineRule.pauseDispatcher()

        //Given a Reminder is saved to the ViewModel
        saveReminderViewModel.saveReminder(fakeReminder1)

        //Initial state of showLoading is true prior to Coroutine dispatching.
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(true))

        //When the Coroutine is resumed
        mainCoroutineRule.resumeDispatcher()

        //Then the showLoading value is false.
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(false))


    }

    @Test
    fun saveReminder_validateNullLocation() {

        //Given a Reminder with an empty (null) location
        val fakeReminder2 = ReminderDataItem("Go to the store.", "Pick up milk.",
            null, 89.98, -98.34)

        //When the Reminder is validated with the ViewModel method
        val validTitle = saveReminderViewModel.validateEnteredData(fakeReminder2)
        val snackBarMessage = saveReminderViewModel.showSnackBarInt.getOrAwaitValue()

        //Then the method returns false since a location must not be empty and show snackBar "Please select location" text
        assertThat(snackBarMessage, equalTo(R.string.err_select_location))
        assertThat(validTitle, equalTo(false))


    }


}