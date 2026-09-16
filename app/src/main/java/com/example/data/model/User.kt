package com.example.data.model

enum class UserRole {
    CITIZEN,
    MUNICIPAL_AUTHORITY
}

data class User(
    val id: String = "user_1",
    val name: String = "Anand Kumar",
    val email: String = "anand@example.com",
    val phone: String = "+91 98765 43210",
    val role: UserRole = UserRole.CITIZEN,
    val district: String = "Chennai",
    val municipality: String = "Greater Chennai Corporation",
    val ward: String = "Ward 18",
    val preferredLanguage: String = "en" // "en" or "ta"
)
