package com.sta.market.domain.model

class UnauthorizedException(message: String = "Invalid credentials") : Exception(message)
class AccountLockedException(message: String = "Account is locked") : Exception(message)
