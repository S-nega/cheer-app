package kz.snega.cheerapp.model

data class Athlete (
    val id: String,
    val name: String,
    val surname: String,
    val groupName: String, //groudId
    val isAttendance: Boolean = true,
    val avatarUrl: String? = null
)