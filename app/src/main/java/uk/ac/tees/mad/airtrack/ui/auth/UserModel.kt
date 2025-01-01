package uk.ac.tees.mad.airtrack.ui.auth


data class UserModel(
    val name: String? = "",
    val email: String? = "",
    val location: String? = "",
    val profileUrl: String? = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0
)