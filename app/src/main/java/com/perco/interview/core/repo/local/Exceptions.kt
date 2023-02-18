package com.perco.interview.core.repo.local

object NoData : Exception()

class TTLExpirationException(key: String) : Exception("Cache TTL expired for $key") {
    override fun toString(): String = message!!
}