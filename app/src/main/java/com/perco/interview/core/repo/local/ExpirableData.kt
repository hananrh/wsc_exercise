package com.perco.interview.core.repo.local

data class ExpirableData<T>(val data: T, val maturity: Maturity) {
    enum class Maturity {
        FRESH, STALE
    }

    companion object {
        fun <T> fresh(data: T) = ExpirableData(data, Maturity.FRESH)
        fun <T> stale(data: T) = ExpirableData(data, Maturity.STALE)
    }
}