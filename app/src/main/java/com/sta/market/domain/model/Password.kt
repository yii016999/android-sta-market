package com.sta.market.domain.model

const val PASSWORD_MIN_LENGTH = 6

@JvmInline
value class Password(val value: String) {
    init {
        // Only validate format if not empty
        // Let the business logic layer handle empty validation
        if (value.isNotBlank()) {
            require(value.length >= PASSWORD_MIN_LENGTH) { "Password must be at least $PASSWORD_MIN_LENGTH characters long." }
        }
    }
}
