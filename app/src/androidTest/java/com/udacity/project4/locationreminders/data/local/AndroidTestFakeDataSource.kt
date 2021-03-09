package com.udacity.project4.locationreminders.data.local

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class AndroidTestFakeDataSource(val reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let { return Result.Success(it) }
        return Result.Error(
            "No reminders."
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders?.let {
            for (reminder in reminders) {
                if (reminder.id == id) {
                    return Result.Success(reminder)
                }
            }
        }
        return Result.Error(
            "Reminder with that ID not found"
        )

    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}