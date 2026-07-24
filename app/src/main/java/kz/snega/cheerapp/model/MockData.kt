package kz.snega.cheerapp.model

object MockData {
    val athletesList = listOf(
        Athlete(
            id = "1",
            name = "Anna",
            surname = "Smirnova",
            groupName = "Rockets",
            isAttendance = true
        ),
        Athlete(
            id = "2",
            name = "Ariana",
            surname = "Sulimenko",
            groupName = "Rockets",
            isAttendance = false
        ),
        Athlete(
            id = "3",
            name = "Irina",
            surname = "Pak",
            groupName = "Comets",
            isAttendance = true
        ),
    )
}