package com.sta.market.domain.model

const val PASSWORD_MIN_LENGTH = 6

@JvmInline
value class Password(val value: String) {
    init {
        require(value.isNotBlank()) { "Password cannot be blank." }
        require(value.length >= PASSWORD_MIN_LENGTH) { "Password must be at least 6 characters long." }
    }
}
