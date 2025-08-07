package com.sta.market.domain.model

@JvmInline
value class Email(val value: String) {
    init {
        if (value.isNotBlank()) {
            require(EMAIL_REGEX.matches(value)) { "Invalid email format." }
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    }
}
