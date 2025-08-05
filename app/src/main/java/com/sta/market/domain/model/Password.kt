@file:Suppress("MagicNumber")
package com.sta.market.domain.model

@JvmInline
value class Password(val value: String) {
    init {
        require(value.isNotBlank()) { "Password cannot be blank." }
        require(value.length >= 6) { "Password must be at least 6 characters long." }
    }
}
