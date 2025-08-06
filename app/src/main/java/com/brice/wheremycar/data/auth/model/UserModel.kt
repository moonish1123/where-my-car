package com.brice.wheremycar.data.auth.model

data class UserModel(
    val uid: String,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
)
